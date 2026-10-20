package com.smart1.appsmartweb.service.connection;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.smart1.appsmartweb.plc.mapper.AssemblyDb57Mapper.AssemblyPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.ExpeditionDb9Mapper.ExpeditionPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.ProcessDb2Mapper.ProcessPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.StockDb9Mapper.StockPlcSnapshot;
import com.smart1.appsmartweb.service.InventoryService;
import com.smart1.appsmartweb.service.OrderOrchestratorService;
import com.smart1.appsmartweb.service.station.AssemblyStationHandler;
import com.smart1.appsmartweb.service.station.BancadaStateStore;
import com.smart1.appsmartweb.service.station.ExpeditionStationHandler;
import com.smart1.appsmartweb.service.station.ProcessStationHandler;
import com.smart1.appsmartweb.service.station.StockStationHandler;
import com.smart1.appsmartweb.util.PlcConnector;

import jakarta.transaction.Transactional;

/**
 * Fachada de serviço das estações da Bancada Smart.
 * Delega a criação de pedidos ao OrderOrchestratorService e o processamento binário/handshakes
 * de cada CLP aos StationHandlers especializados, sincronizando com o BancadaStateStore.
 */
@Service
public class SmartService {

    private static final Logger log = LoggerFactory.getLogger(SmartService.class);

    private final InventoryService inventoryService;
    private final OrderOrchestratorService orderOrchestratorService;
    private final BancadaStateStore stateStore;
    private final StockStationHandler stockStationHandler;
    private final ProcessStationHandler processStationHandler;
    private final AssemblyStationHandler assemblyStationHandler;
    private final ExpeditionStationHandler expeditionStationHandler;

    public SmartService(
            InventoryService inventoryService,
            OrderOrchestratorService orderOrchestratorService,
            BancadaStateStore stateStore,
            StockStationHandler stockStationHandler,
            ProcessStationHandler processStationHandler,
            AssemblyStationHandler assemblyStationHandler,
            ExpeditionStationHandler expeditionStationHandler) {
        this.inventoryService = inventoryService;
        this.orderOrchestratorService = orderOrchestratorService;
        this.stateStore = stateStore;
        this.stockStationHandler = stockStationHandler;
        this.processStationHandler = processStationHandler;
        this.assemblyStationHandler = assemblyStationHandler;
        this.expeditionStationHandler = expeditionStationHandler;
    }

    // Variáveis globais (volatile para visibilidade entre threads — corrige BUG-09)
    public static volatile boolean readOnly = false;
    public static volatile boolean aux_expedicao = false;
    public static volatile boolean pedidoEmCurso = false;
    public static volatile byte statusProducao = 0;
    public static volatile byte statusEstoque = 0;
    public static volatile byte statusProcesso = 0;
    public static volatile byte statusMontagem = 0;
    public static volatile byte statusExpedicao = 0;
    public static volatile int posicaoEstoqueSolicitada = 0;
    public static volatile int posicaoExpedicaoSolicitada = 0;
    public static volatile boolean blockFinished = false;

    // Estoque
    public volatile boolean recebidoOpEst = false;
    public volatile int cor_Andar_1 = 0;
    public volatile int posicao_Estoque_Andar_1 = 0;
    public volatile int cor_Lamina_1_Andar_1 = 0;
    public volatile int cor_Lamina_2_Andar_1 = 0;
    public volatile int cor_Lamina_3_Andar_1 = 0;
    public volatile int padrao_Lamina_1_Andar_1 = 0;
    public volatile int padrao_Lamina_2_Andar_1 = 0;
    public volatile int padrao_Lamina_3_Andar_1 = 0;
    public volatile int processamento_Andar_1 = 0;

    public volatile int cor_Andar_2 = 0;
    public volatile int posicao_Estoque_Andar_2 = 0;
    public volatile int cor_Lamina_1_Andar_2 = 0;
    public volatile int cor_Lamina_2_Andar_2 = 0;
    public volatile int cor_Lamina_3_Andar_2 = 0;
    public volatile int padrao_Lamina_1_Andar_2 = 0;
    public volatile int padrao_Lamina_2_Andar_2 = 0;
    public volatile int padrao_Lamina_3_Andar_2 = 0;
    public volatile int processamento_Andar_2 = 0;

    public volatile int cor_Andar_3 = 0;
    public volatile int posicao_Estoque_Andar_3 = 0;
    public volatile int cor_Lamina_1_Andar_3 = 0;
    public volatile int cor_Lamina_2_Andar_3 = 0;
    public volatile int cor_Lamina_3_Andar_3 = 0;
    public volatile int padrao_Lamina_1_Andar_3 = 0;
    public volatile int padrao_Lamina_2_Andar_3 = 0;
    public volatile int padrao_Lamina_3_Andar_3 = 0;
    public volatile int processamento_Andar_3 = 0;

    public volatile int numeroPedidoEst = 0;
    public volatile int andares = 0;
    public volatile int posicaoExpedicaoEst = 0;
    public volatile boolean iniciarPedido = false;
    public volatile boolean recebidoEstoque = false;
    public volatile boolean iniciarGuardarEst = false;
    public volatile int posicaoGuardarEst = 0;
    public volatile byte[] posicoesOcupadas = new byte[28];
    public volatile int numeroOPEst = 0;
    public volatile boolean cancelOPEst = false;
    public volatile boolean finishOPEst = false;
    public volatile boolean startOPEst = false;
    public volatile boolean ocupadoEst = false;
    public volatile boolean aguardandoEst = false;
    public volatile boolean manualEst = false;
    public volatile boolean emergenciaEst = false;
    public volatile boolean pedirPosicaoEst = false;
    public volatile int posicaoEstoque = 0;
    public volatile boolean adicionarEstoque = false;
    public volatile boolean removerEstoque = false;
    public volatile boolean retornoEstoqueCheio = false;
    public volatile int corGuardarEstoque = 0;
    public volatile boolean xEmergenciaAtivadaEst = false;
    public volatile boolean xComutadorAutomaticoEst = false;
    public volatile boolean xNecessitaHomeEixoVerticalEst = false;
    public volatile boolean xNecessitaHomeEixoGiroEst = false;
    public volatile boolean xServoDesligadoEixoVerticalEst = false;
    public volatile boolean xServoDesligadoEixoGiroEst = false;
    public volatile boolean xCondicaoIniciarEst = false;

    // Processo
    public volatile boolean recebidoOpPro = false;
    public volatile int numeroOPPro = 0;
    public volatile boolean cancelOPPro = false;
    public volatile boolean finishOPPro = false;
    public volatile boolean startOPPro = false;
    public volatile boolean ocupadoPro = false;
    public volatile boolean aguardandoPro = false;
    public volatile boolean manualPro = false;
    public volatile boolean emergenciaPro = false;
    public volatile boolean xEmergenciaAtivadaPro = false;
    public volatile boolean xComutadorAutomaticoPro = false;
    public volatile boolean xNecessitaHomeEixoXPro = false;
    public volatile boolean xNecessitaHomeEixoYPro = false;
    public volatile boolean xServoDesligadoEixoXPro = false;
    public volatile boolean xServoDesligadoEixoYPro = false;
    public volatile boolean xCondicaoIniciarPro = false;

    // Montagem
    public volatile boolean recebidoOpMon = false;
    public volatile int numeroOPMon = 0;
    public volatile boolean cancelOPMon = false;
    public volatile boolean finishOPMon = false;
    public volatile boolean startOPMon = false;
    public volatile boolean ocupadoMon = false;
    public volatile boolean aguardandoMon = false;
    public volatile boolean manualMon = false;
    public volatile boolean emergenciaMon = false;
    public volatile boolean xEmergenciaAtivadaMon = false;
    public volatile boolean xComutadorAutomaticoMon = false;
    public volatile boolean xCondicaoIniciarMon = false;

    // Expedição
    public volatile boolean recebidoOpExp = false;
    public volatile boolean recebidoExpedicao = false;
    public volatile boolean iniciarGuardarExp = false;
    public volatile int posicaoGuardarExp = 0;
    public volatile int[] orderExpedicao = new int[12];
    public volatile int numeroOPExp = 0;
    public volatile boolean cancelOPExp = false;
    public volatile boolean finishOPExp = false;
    public volatile boolean startOPExp = false;
    public volatile boolean ocupadoExp = false;
    public volatile boolean aguardandoExp = false;
    public volatile boolean manualExp = false;
    public volatile boolean emergenciaExp = false;
    public volatile boolean pedirPosicaoExp = false;
    public volatile int posicaoGuardadoExpedicao = 0;
    public volatile int posicaoRemovidoExpedicao = 0;
    public volatile boolean adicionarExpedicao = false;
    public volatile boolean removerExpedicao = false;
    public volatile int opGuardadoExpedicao = 0;
    public volatile boolean xEmergenciaAtivadaExp = false;
    public volatile boolean xComutadorAutomaticoExp = false;
    public volatile boolean xNecessitaHomeEixoVerticalExp = false;
    public volatile boolean xNecessitaHomeEixoGiroExp = false;
    public volatile boolean xNecessitaHomeEixoHorizontalExp = false;
    public volatile boolean xServoDesligadoEixoHorizontalExp = false;
    public volatile boolean xServoDesligadoEixoGiroExp = false;
    public volatile boolean xServoDesligadoEixoVerticalExp = false;
    public volatile boolean xCondicaoIniciarExp = false;

    public BancadaStateStore getStateStore() {
        return stateStore;
    }

    @Transactional
    public void processarPedidoCompleto(Map<String, String> formData) throws Exception {
        orderOrchestratorService.processarPedidoCompleto(formData);
        syncGlobalFields();
    }

    public void enviarBlocoBytesAoClp(String ipClp, int db, int offset, byte[] dados, int size) throws Exception {
        PlcConnector plcConnector = PlcConnectionManager.getConexao(ipClp);
        if (plcConnector == null) {
            return;
        }
        plcConnector.writeBlock(db, offset, size, dados);
    }

    public void iniciarExecucaoPedido(String ipClp) {
        PlcConnector plcConnector = PlcConnectionManager.getConexao(ipClp);
        if (plcConnector == null) {
            return;
        }

        posicaoExpedicaoSolicitada = inventoryService.findFirstAvailableExpeditionPosition();
        stateStore.setPosicaoExpedicaoSolicitada(posicaoExpedicaoSolicitada);

        try {
            plcConnector.writeBit(9, 0, 0, false);
            plcConnector.writeBit(9, 64, 0, false);
            plcConnector.writeBit(9, 64, 1, false);
            plcConnector.writeBit(9, 62, 0, false);
            plcConnector.writeBit(9, 62, 0, true);
        } catch (Exception ex) {
            log.warn("Erro ao iniciar execução de pedido no CLP {}: {}", ipClp, ex.getMessage());
        }
    }

    public void clpEstoque(String ip, byte[] dadosClp1) {
        PlcConnector plcConnectorEst = PlcConnectionManager.getConexao(ip);
        StockPlcSnapshot snap = stockStationHandler.handle(plcConnectorEst, dadosClp1);
        syncStockFields(snap);
        syncGlobalFields();
    }

    public void clpProcesso(String ip, byte[] dadosClp2) {
        PlcConnector plcConnectorPro = PlcConnectionManager.getConexao(ip);
        ProcessPlcSnapshot snap = processStationHandler.handle(plcConnectorPro, dadosClp2);
        syncProcessFields(snap);
        syncGlobalFields();
    }

    public void clpMontagem(String ip, byte[] dadosClp3) {
        PlcConnector plcConnectorMon = PlcConnectionManager.getConexao(ip);
        AssemblyPlcSnapshot snap = assemblyStationHandler.handle(plcConnectorMon, dadosClp3);
        syncAssemblyFields(snap);
        syncGlobalFields();
    }

    public void clpExpedicao(String ip, byte[] dadosClp4) throws Exception {
        PlcConnector plcConnectorExp = PlcConnectionManager.getConexao(ip);
        ExpeditionPlcSnapshot snap = expeditionStationHandler.handle(plcConnectorExp, dadosClp4);
        syncExpeditionFields(snap);
        syncGlobalFields();
    }

    private void syncGlobalFields() {
        readOnly = stateStore.isReadOnly();
        aux_expedicao = stateStore.isAuxExpedicao();
        pedidoEmCurso = stateStore.isPedidoEmCurso();
        statusProducao = stateStore.getStatusProducao();
        statusEstoque = stateStore.getStatusEstoque();
        statusProcesso = stateStore.getStatusProcesso();
        statusMontagem = stateStore.getStatusMontagem();
        statusExpedicao = stateStore.getStatusExpedicao();
        posicaoEstoqueSolicitada = stateStore.getPosicaoEstoqueSolicitada();
        posicaoExpedicaoSolicitada = stateStore.getPosicaoExpedicaoSolicitada();
        blockFinished = stateStore.isBlockFinished();
    }

    private void syncStockFields(StockPlcSnapshot snap) {
        this.recebidoOpEst = snap.recebidoOpEst();
        this.cor_Andar_1 = snap.cor_Andar_1();
        this.posicao_Estoque_Andar_1 = snap.posicao_Estoque_Andar_1();
        this.cor_Lamina_1_Andar_1 = snap.cor_Lamina_1_Andar_1();
        this.cor_Lamina_2_Andar_1 = snap.cor_Lamina_2_Andar_1();
        this.cor_Lamina_3_Andar_1 = snap.cor_Lamina_3_Andar_1();
        this.padrao_Lamina_1_Andar_1 = snap.padrao_Lamina_1_Andar_1();
        this.padrao_Lamina_2_Andar_1 = snap.padrao_Lamina_2_Andar_1();
        this.padrao_Lamina_3_Andar_1 = snap.padrao_Lamina_3_Andar_1();
        this.processamento_Andar_1 = snap.processamento_Andar_1();

        this.cor_Andar_2 = snap.cor_Andar_2();
        this.posicao_Estoque_Andar_2 = snap.posicao_Estoque_Andar_2();
        this.cor_Lamina_1_Andar_2 = snap.cor_Lamina_1_Andar_2();
        this.cor_Lamina_2_Andar_2 = snap.cor_Lamina_2_Andar_2();
        this.cor_Lamina_3_Andar_2 = snap.cor_Lamina_3_Andar_2();
        this.padrao_Lamina_1_Andar_2 = snap.padrao_Lamina_1_Andar_2();
        this.padrao_Lamina_2_Andar_2 = snap.padrao_Lamina_2_Andar_2();
        this.padrao_Lamina_3_Andar_2 = snap.padrao_Lamina_3_Andar_2();
        this.processamento_Andar_2 = snap.processamento_Andar_2();

        this.cor_Andar_3 = snap.cor_Andar_3();
        this.posicao_Estoque_Andar_3 = snap.posicao_Estoque_Andar_3();
        this.cor_Lamina_1_Andar_3 = snap.cor_Lamina_1_Andar_3();
        this.cor_Lamina_2_Andar_3 = snap.cor_Lamina_2_Andar_3();
        this.cor_Lamina_3_Andar_3 = snap.cor_Lamina_3_Andar_3();
        this.padrao_Lamina_1_Andar_3 = snap.padrao_Lamina_1_Andar_3();
        this.padrao_Lamina_2_Andar_3 = snap.padrao_Lamina_2_Andar_3();
        this.padrao_Lamina_3_Andar_3 = snap.padrao_Lamina_3_Andar_3();
        this.processamento_Andar_3 = snap.processamento_Andar_3();

        this.numeroPedidoEst = snap.numeroPedidoEst();
        this.andares = snap.andares();
        this.posicaoExpedicaoEst = snap.posicaoExpedicaoEst();
        this.iniciarPedido = snap.iniciarPedido();
        this.recebidoEstoque = snap.recebidoEstoque();
        this.iniciarGuardarEst = snap.iniciarGuardarEst();
        this.posicaoGuardarEst = snap.posicaoGuardarEst();
        this.posicoesOcupadas = snap.posicoesOcupadas();
        this.numeroOPEst = snap.numeroOPEst();
        this.cancelOPEst = snap.cancelOPEst();
        this.finishOPEst = snap.finishOPEst();
        this.startOPEst = snap.startOPEst();
        this.ocupadoEst = snap.ocupadoEst();
        this.aguardandoEst = snap.aguardandoEst();
        this.manualEst = snap.manualEst();
        this.emergenciaEst = snap.emergenciaEst();
        this.pedirPosicaoEst = snap.pedirPosicaoEst();
        this.posicaoEstoque = snap.posicaoEstoque();
        this.adicionarEstoque = snap.adicionarEstoque();
        this.removerEstoque = snap.removerEstoque();
        this.retornoEstoqueCheio = snap.retornoEstoqueCheio();
        this.corGuardarEstoque = snap.corGuardarEstoque();
        this.xEmergenciaAtivadaEst = snap.xEmergenciaAtivadaEst();
        this.xComutadorAutomaticoEst = snap.xComutadorAutomaticoEst();
        this.xNecessitaHomeEixoVerticalEst = snap.xNecessitaHomeEixoVerticalEst();
        this.xNecessitaHomeEixoGiroEst = snap.xNecessitaHomeEixoGiroEst();
        this.xServoDesligadoEixoVerticalEst = snap.xServoDesligadoEixoVerticalEst();
        this.xServoDesligadoEixoGiroEst = snap.xServoDesligadoEixoGiroEst();
        this.xCondicaoIniciarEst = snap.xCondicaoIniciarEst();
    }

    private void syncProcessFields(ProcessPlcSnapshot snap) {
        this.recebidoOpPro = snap.recebidoOpPro();
        this.numeroOPPro = snap.numeroOPPro();
        this.cancelOPPro = snap.cancelOPPro();
        this.finishOPPro = snap.finishOPPro();
        this.startOPPro = snap.startOPPro();
        this.ocupadoPro = snap.ocupadoPro();
        this.aguardandoPro = snap.aguardandoPro();
        this.manualPro = snap.manualPro();
        this.emergenciaPro = snap.emergenciaPro();
        this.xEmergenciaAtivadaPro = snap.xEmergenciaAtivadaPro();
        this.xComutadorAutomaticoPro = snap.xComutadorAutomaticoPro();
        this.xNecessitaHomeEixoXPro = snap.xNecessitaHomeEixoXPro();
        this.xNecessitaHomeEixoYPro = snap.xNecessitaHomeEixoYPro();
        this.xServoDesligadoEixoXPro = snap.xServoDesligadoEixoXPro();
        this.xServoDesligadoEixoYPro = snap.xServoDesligadoEixoYPro();
        this.xCondicaoIniciarPro = snap.xCondicaoIniciarPro();
    }

    private void syncAssemblyFields(AssemblyPlcSnapshot snap) {
        this.recebidoOpMon = snap.recebidoOpMon();
        this.numeroOPMon = snap.numeroOPMon();
        this.cancelOPMon = snap.cancelOPMon();
        this.finishOPMon = snap.finishOPMon();
        this.startOPMon = snap.startOPMon();
        this.ocupadoMon = snap.ocupadoMon();
        this.aguardandoMon = snap.aguardandoMon();
        this.manualMon = snap.manualMon();
        this.emergenciaMon = snap.emergenciaMon();
        this.xEmergenciaAtivadaMon = snap.xEmergenciaAtivadaMon();
        this.xComutadorAutomaticoMon = snap.xComutadorAutomaticoMon();
        this.xCondicaoIniciarMon = snap.xCondicaoIniciarMon();
    }

    private void syncExpeditionFields(ExpeditionPlcSnapshot snap) {
        this.recebidoOpExp = snap.recebidoOpExp();
        this.recebidoExpedicao = snap.recebidoExpedicao();
        this.iniciarGuardarExp = snap.iniciarGuardarExp();
        this.posicaoGuardarExp = snap.posicaoGuardarExp();
        this.orderExpedicao = snap.orderExpedicao();
        this.numeroOPExp = snap.numeroOPExp();
        this.cancelOPExp = snap.cancelOPExp();
        this.finishOPExp = snap.finishOPExp();
        this.startOPExp = snap.startOPExp();
        this.ocupadoExp = snap.ocupadoExp();
        this.aguardandoExp = snap.aguardandoExp();
        this.manualExp = snap.manualExp();
        this.emergenciaExp = snap.emergenciaExp();
        this.pedirPosicaoExp = snap.pedirPosicaoExp();
        this.posicaoGuardadoExpedicao = snap.posicaoGuardadoExpedicao();
        this.posicaoRemovidoExpedicao = snap.posicaoRemovidoExpedicao();
        this.adicionarExpedicao = snap.adicionarExpedicao();
        this.removerExpedicao = snap.removerExpedicao();
        this.opGuardadoExpedicao = snap.opGuardadoExpedicao();
        this.xEmergenciaAtivadaExp = snap.xEmergenciaAtivadaExp();
        this.xComutadorAutomaticoExp = snap.xComutadorAutomaticoExp();
        this.xNecessitaHomeEixoVerticalExp = snap.xNecessitaHomeEixoVerticalExp();
        this.xNecessitaHomeEixoGiroExp = snap.xNecessitaHomeEixoGiroExp();
        this.xNecessitaHomeEixoHorizontalExp = snap.xNecessitaHomeEixoHorizontalExp();
        this.xServoDesligadoEixoHorizontalExp = snap.xServoDesligadoEixoHorizontalExp();
        this.xServoDesligadoEixoGiroExp = snap.xServoDesligadoEixoGiroExp();
        this.xServoDesligadoEixoVerticalExp = snap.xServoDesligadoEixoVerticalExp();
        this.xCondicaoIniciarExp = snap.xCondicaoIniciarExp();
    }

    public Map<String, Boolean> pingPlcs(Map<String, String> ips) {
        Map<String, Boolean> status = new HashMap<>();
        for (Map.Entry<String, String> entry : ips.entrySet()) {
            String nome = entry.getKey();
            String ip = entry.getValue();
            try {
                boolean conectado = PlcConnectionManager.pingIp(ip);
                status.put(nome, conectado);
            } catch (Exception e) {
                status.put(nome, false);
            }
        }
        return status;
    }
}
