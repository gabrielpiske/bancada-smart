package com.smart1.appsmartweb.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart1.appsmartweb.exception.BlockNotFoundException;
import com.smart1.appsmartweb.exception.ExpeditionFullException;
import com.smart1.appsmartweb.exception.StorageNotFoundException;
import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.model.Orders;
import com.smart1.appsmartweb.model.Storage;
import com.smart1.appsmartweb.model.enums.BlockColor;
import com.smart1.appsmartweb.model.enums.StationType;
import com.smart1.appsmartweb.plc.mapper.StockDb9Mapper;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.repository.OrdersRepository;
import com.smart1.appsmartweb.repository.StorageRepository;
import com.smart1.appsmartweb.service.connection.PlcConnectionManager;
import com.smart1.appsmartweb.service.station.BancadaStateStore;
import com.smart1.appsmartweb.util.DataInitializer;
import com.smart1.appsmartweb.util.PlcConnector;

/**
 * Orquestrador transacional de criação e envio de Ordens de Produção (OP) aos CLPs.
 * Corrige:
 * - BUG-03: reserva posições distintas quando o pedido possui 2 ou 3 blocos da mesma cor.
 * - BUG-11: geração atômica do número sequencial de OP via MAX(productionOrder) + 1.
 * - BUG-13: consolida o envio de pedidos usando conexões do PlcConnectionManager.
 * - BUG-14: utiliza os IPs dinâmicos das estações Estoque e Expedição.
 */
@Service
public class OrderOrchestratorService {

    private static final Logger log = LoggerFactory.getLogger(OrderOrchestratorService.class);

    private final BlockRepository blockRepository;
    private final OrdersRepository ordersRepository;
    private final StorageRepository storageRepository;
    private final InventoryService inventoryService;
    private final BancadaStateStore stateStore;

    public OrderOrchestratorService(
            BlockRepository blockRepository,
            OrdersRepository ordersRepository,
            StorageRepository storageRepository,
            InventoryService inventoryService,
            BancadaStateStore stateStore) {
        this.blockRepository = blockRepository;
        this.ordersRepository = ordersRepository;
        this.storageRepository = storageRepository;
        this.inventoryService = inventoryService;
        this.stateStore = stateStore;
    }

    @Transactional
    public byte[] processarPedidoCompleto(Map<String, String> formData) throws Exception {
        if (formData == null || formData.isEmpty()) {
            throw new IllegalArgumentException("Dados do formulário não podem ser nulos ou vazios");
        }

        int totalBlocos = contarBlocosPreenchidos(formData);
        if (totalBlocos == 0) {
            throw new IllegalArgumentException("Nenhum bloco foi especificado no pedido");
        }

        int posicaoExpedicao = inventoryService.findFirstAvailableExpeditionPosition();
        if (posicaoExpedicao == -1) {
            throw new ExpeditionFullException("Armazém de expedição está cheio (12 posições)");
        }

        Orders novaOrdem = criarNovaOrdemProducao();
        log.info("Nova Ordem de Produção criada: OP={} (blocos={}, posExpedicao={})",
                novaOrdem.getProductionOrder(), totalBlocos, posicaoExpedicao);

        // Monta e valida os blocos no estoque ANTES de enviar bytes aos CLPs (corrige BUG-03)
        byte[] dadosPedido = buildOrderBuffer(formData, totalBlocos, posicaoExpedicao, novaOrdem.getProductionOrder());

        stateStore.setPosicaoExpedicaoSolicitada(posicaoExpedicao);

        // 1. Envia os 60 bytes do pedido para o CLP Estoque (DB9, offset 2, 60B)
        String ipEstoque = PlcConnectionManager.getStationIp(StationType.ESTOQUE);
        PlcConnector plcEstoque = PlcConnectionManager.getConexao(ipEstoque);
        if (plcEstoque != null) {
            plcEstoque.writeBlock(
                    StockDb9Mapper.DB_NUMBER,
                    StockDb9Mapper.ORDER_BUFFER_OFFSET,
                    StockDb9Mapper.ORDER_BUFFER_BYTES,
                    dadosPedido);
        }

        // 2. Registra a reserva na Expedição (Banco de Dados + CLP Expedição)
        gravarPedidoNaExpedicao(novaOrdem, posicaoExpedicao);

        // 3. Dispara o pulso IniciarPedido (DB9.DBX62.0 = true) no CLP Estoque
        if (plcEstoque != null) {
            plcEstoque.writeBit(StockDb9Mapper.DB_NUMBER, 62, 0, true);
        }

        return dadosPedido;
    }

    public byte[] buildOrderBuffer(
            Map<String, String> formData,
            int totalBlocos,
            int posicaoExpedicao,
            Long numeroOrdem) {

        int[] dados = new int[StockDb9Mapper.ORDER_BUFFER_SHORTS];
        Set<Integer> posicoesReservadas = new HashSet<>();

        for (int blocoNum = 1; blocoNum <= 3; blocoNum++) {
            String blockColorKey = "block-color-" + blocoNum;

            if (formData.containsKey(blockColorKey) && !formData.get(blockColorKey).isEmpty()) {
                int blockColor = Integer.parseInt(formData.get(blockColorKey));
                int indexBase = (blocoNum - 1) * 9;

                int posicaoEstoque = allocateDistinctStockPosition(blockColor, posicoesReservadas);
                posicoesReservadas.add(posicaoEstoque);

                dados[indexBase] = blockColor;
                dados[indexBase + 1] = posicaoEstoque;
                dados[indexBase + 2] = getValueOrDefault(formData, "l1-color-" + blocoNum, 0);
                dados[indexBase + 3] = getValueOrDefault(formData, "l2-color-" + blocoNum, 0);
                dados[indexBase + 4] = getValueOrDefault(formData, "l3-color-" + blocoNum, 0);
                dados[indexBase + 5] = getValueOrDefault(formData, "l1-pattern-" + blocoNum, 0);
                dados[indexBase + 6] = getValueOrDefault(formData, "l2-pattern-" + blocoNum, 0);
                dados[indexBase + 7] = getValueOrDefault(formData, "l3-pattern-" + blocoNum, 0);
                dados[indexBase + 8] = 1; // processamento_Andar_X = 1 (ativo)
            }
        }

        dados[27] = numeroOrdem.intValue();
        dados[28] = totalBlocos;
        dados[29] = posicaoExpedicao;

        return StockDb9Mapper.serializeOrderBuffer(dados);
    }

    private int allocateDistinctStockPosition(int color, Set<Integer> posicoesReservadas) {
        List<Integer> posicoesLivres = blockRepository.findAvailablePositionsByColor(color);
        for (Integer pos : posicoesLivres) {
            if (!posicoesReservadas.contains(pos)) {
                return pos;
            }
        }
        String colorLabel = BlockColor.fromCode(color).getLabel();
        throw new BlockNotFoundException(
                "Não há blocos disponíveis suficientes da cor " + colorLabel + " (código " + color + ") no estoque");
    }

    private synchronized Orders criarNovaOrdemProducao() {
        Orders novaOrdem = new Orders();
        Long maxOp = ordersRepository.findMaxProductionOrder();
        novaOrdem.setProductionOrder((maxOp == null ? 0L : maxOp) + 1L);
        return ordersRepository.save(novaOrdem);
    }

    private void gravarPedidoNaExpedicao(Orders ordem, int posicaoExpedicao) throws Exception {
        Storage expeditionStorage = storageRepository.findById(DataInitializer.EXPEDITION_STORAGE_ID)
                .orElseThrow(() -> new StorageNotFoundException("Armazém de Expedição (id=2) não encontrado"));

        Block bloco = new Block();
        bloco.setStorageId(expeditionStorage);
        bloco.setPosition(posicaoExpedicao);
        bloco.setProductionOrder(ordem);
        blockRepository.save(bloco);

        String ipExp = PlcConnectionManager.getStationIp(StationType.EXPEDICAO);
        PlcConnector plcExp = PlcConnectionManager.getConexao(ipExp);
        if (plcExp != null) {
            int offset = 6 + (posicaoExpedicao - 1) * 2;
            plcExp.writeInt(9, offset, ordem.getProductionOrder().intValue());
            plcExp.writeBit(9, 2, 0, true); // RecebidoExpedicao = TRUE
        }
    }

    private int contarBlocosPreenchidos(Map<String, String> formData) {
        int total = 0;
        for (int i = 1; i <= 3; i++) {
            if (formData.containsKey("block-color-" + i) && !formData.get("block-color-" + i).isEmpty()) {
                total++;
            }
        }
        return total;
    }

    private int getValueOrDefault(Map<String, String> formData, String key, int defaultValue) {
        return formData.containsKey(key) && !formData.get(key).isEmpty()
                ? Integer.parseInt(formData.get(key))
                : defaultValue;
    }
}
