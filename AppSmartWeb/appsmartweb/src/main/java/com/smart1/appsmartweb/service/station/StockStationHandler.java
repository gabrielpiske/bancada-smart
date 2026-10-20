package com.smart1.appsmartweb.service.station;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.plc.PlcClient;
import com.smart1.appsmartweb.plc.mapper.StockDb9Mapper;
import com.smart1.appsmartweb.plc.mapper.StockDb9Mapper.StockPlcSnapshot;
import com.smart1.appsmartweb.service.InventoryService;

/**
 * Máquina de estados e regras de negócio da Estação 1 — ESTOQUE (DB9, 111 bytes).
 * Elimina o uso de RestTemplate para localhost:8080 (corrige BUG-02) e operadores bitwise (corrige BUG-18).
 */
@Component
public class StockStationHandler {

    private static final Logger log = LoggerFactory.getLogger(StockStationHandler.class);
    private static final int DB_NUMBER = StockDb9Mapper.DB_NUMBER;

    private final BancadaStateStore stateStore;
    private final OpHandshakeHelper handshakeHelper;
    private final InventoryService inventoryService;

    public StockStationHandler(
            BancadaStateStore stateStore,
            OpHandshakeHelper handshakeHelper,
            InventoryService inventoryService) {
        this.stateStore = stateStore;
        this.handshakeHelper = handshakeHelper;
        this.inventoryService = inventoryService;
    }

    public StockPlcSnapshot handle(PlcClient plc, byte[] dadosClp1) {
        StockPlcSnapshot snap = StockDb9Mapper.fromBytes(dadosClp1);
        stateStore.setStockSnapshot(snap);

        if (plc == null) {
            return snap;
        }

        boolean readOnly = stateStore.isReadOnly();

        // 1. Handshake de disparo de pedido: iniciarPedido && ocupadoEst -> reseta iniciarPedido = FALSE
        if (snap.iniciarPedido() && snap.ocupadoEst()) {
            stateStore.setPedidoEmCurso(true);
            stateStore.setStatusEstoque((byte) 0);
            stateStore.setStatusProducao((byte) 0);
            if (!readOnly) {
                try {
                    plc.writeBit(DB_NUMBER, 62, 0, false);
                } catch (Exception e) {
                    log.error("[ESTOQUE] Erro ao atualizar IniciarPedido [DB9:62.0] para FALSE: {}", e.getMessage());
                }
            }
        }

        // 2. Handshake padrão de OP (StartOP / FinishOP / CancelOP <-> RecebidoOP)
        handshakeHelper.processHandshake(
                plc,
                DB_NUMBER,
                "ESTOQUE",
                snap.startOPEst(),
                snap.finishOPEst(),
                snap.cancelOPEst(),
                snap.recebidoOpEst(),
                readOnly,
                stateStore.isPedidoEmCurso(),
                stateStore.getStatusProducao(),
                stateStore::setStatusEstoque,
                null);

        // 3. Handshake de gerenciamento do magazine de Estoque: resetar RecebidoEstoque quando idle
        if (!snap.removerEstoque() && !snap.adicionarEstoque()) {
            if (!readOnly) {
                try {
                    plc.writeBit(DB_NUMBER, 64, 0, false);
                } catch (Exception e) {
                    log.error("[ESTOQUE] Erro ao atualizar RecebidoEstoque [DB9:64.0] para FALSE: {}", e.getMessage());
                }
            }
        }

        // 4. Remoção de bloco do Estoque pelo CLP
        if (snap.posicaoEstoque() > 0 && snap.removerEstoque()) {
            if (!readOnly) {
                try {
                    plc.writeBit(DB_NUMBER, 64, 0, true);
                    int offset = StockDb9Mapper.STOCK_POSITIONS_OFFSET + (snap.posicaoEstoque() - 1);
                    plc.writeByte(DB_NUMBER, offset, (byte) 0);
                    inventoryService.removeBlockFromStock(snap.posicaoEstoque());
                } catch (Exception e) {
                    log.error("[ESTOQUE] Erro ao remover bloco na posição {}: {}",
                            snap.posicaoEstoque(), e.getMessage());
                }
            }
        }

        // 5. Adição de bloco no Estoque pelo CLP (corrige BUG-02 chamando InventoryService diretamente)
        if (snap.posicaoEstoque() > 0 && snap.adicionarEstoque()) {
            if (!readOnly) {
                try {
                    plc.writeBit(DB_NUMBER, 64, 0, true);
                } catch (Exception e) {
                    log.error("[ESTOQUE] Erro ao atualizar RecebidoEstoque [DB9:64.0] para TRUE: {}", e.getMessage());
                }

                int offset = StockDb9Mapper.STOCK_POSITIONS_OFFSET + (snap.posicaoEstoque() - 1);
                try {
                    plc.writeByte(DB_NUMBER, offset, (byte) snap.corGuardarEstoque());
                    inventoryService.addBlockToStock(snap.posicaoEstoque(), snap.corGuardarEstoque());
                } catch (Exception e) {
                    log.error("[ESTOQUE] Erro ao adicionar bloco (pos={}, cor={}): {}",
                            snap.posicaoEstoque(), snap.corGuardarEstoque(), e.getMessage());
                }
            }
        }

        // 6. Reset de IniciarGuardarEst quando ocupadoEst ou retornoEstoqueCheio
        if ((snap.ocupadoEst() || snap.retornoEstoqueCheio()) && snap.iniciarGuardarEst()) {
            if (!readOnly) {
                try {
                    plc.writeBit(DB_NUMBER, 64, 1, false);
                } catch (Exception e) {
                    log.error("[ESTOQUE] Erro ao atualizar IniciarGuardarEstoque [DB9:64.1] para FALSE: {}",
                            e.getMessage());
                }
            }
        }

        // 7. Solicitação de posição livre para guardar no Estoque
        if (snap.pedirPosicaoEst() && !snap.ocupadoEst()) {
            if (!readOnly) {
                int posEstoqueLivre = inventoryService.findFirstFreeStockPosition();
                if (posEstoqueLivre > 0) {
                    stateStore.setPosicaoEstoqueSolicitada(posEstoqueLivre);
                    try {
                        plc.writeInt(DB_NUMBER, 66, posEstoqueLivre);
                    } catch (Exception e) {
                        log.error("[ESTOQUE] Erro ao atualizar PosicaoGuardarEstoque [DB9:66]: {}", e.getMessage());
                    }
                    try {
                        plc.writeBit(DB_NUMBER, 64, 1, true);
                    } catch (Exception e) {
                        log.error("[ESTOQUE] Erro ao atualizar IniciarGuardarEstoque [DB9:64.1] para TRUE: {}",
                                e.getMessage());
                    }
                } else {
                    log.warn("[ESTOQUE] CLP solicitou posição livre, mas o magazine de Estoque (28 posições) está cheio.");
                }
            }
        }

        return snap;
    }
}
