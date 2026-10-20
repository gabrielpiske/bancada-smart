package com.smart1.appsmartweb.service.station;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.plc.PlcClient;
import com.smart1.appsmartweb.plc.mapper.ExpeditionDb9Mapper;
import com.smart1.appsmartweb.plc.mapper.ExpeditionDb9Mapper.ExpeditionPlcSnapshot;
import com.smart1.appsmartweb.service.InventoryService;

/**
 * Máquina de estados e regras de negócio da Estação 4 — EXPEDIÇÃO (DB9, 48 bytes).
 * Corrige BUG-02 (substitui RestTemplate por InventoryService),
 * BUG-05 (mantém lastProcessedOp e lastProcessedOpTimestamp como estado da instância),
 * BUG-06 (remove bloco duplicado inalcançável) e BUG-18 (operadores lógicos).
 */
@Component
public class ExpeditionStationHandler {

    private static final Logger log = LoggerFactory.getLogger(ExpeditionStationHandler.class);
    private static final int DB_NUMBER = ExpeditionDb9Mapper.DB_NUMBER;

    private final BancadaStateStore stateStore;
    private final OpHandshakeHelper handshakeHelper;
    private final InventoryService inventoryService;

    // Estado persistente entre ciclos de 800ms (corrige BUG-05)
    private volatile int lastProcessedOp = 0;
    private volatile long lastProcessedOpTimestamp = 0L;

    public ExpeditionStationHandler(
            BancadaStateStore stateStore,
            OpHandshakeHelper handshakeHelper,
            InventoryService inventoryService) {
        this.stateStore = stateStore;
        this.handshakeHelper = handshakeHelper;
        this.inventoryService = inventoryService;
    }

    public ExpeditionPlcSnapshot handle(PlcClient plc, byte[] dadosClp4) {
        ExpeditionPlcSnapshot snap = ExpeditionDb9Mapper.fromBytes(dadosClp4);
        stateStore.setExpeditionSnapshot(snap);

        if (plc == null) {
            return snap;
        }

        boolean readOnly = stateStore.isReadOnly();

        // 1. Handshake padrão de OP (StartOP / FinishOP / CancelOP <-> RecebidoOP)
        handshakeHelper.processHandshake(
                plc,
                DB_NUMBER,
                "EXPEDICAO",
                snap.startOPExp(),
                snap.finishOPExp(),
                snap.cancelOPExp(),
                snap.recebidoOpExp(),
                readOnly,
                stateStore.isPedidoEmCurso(),
                stateStore.getStatusProducao(),
                stateStore::setStatusExpedicao,
                () -> stateStore.setBlockFinished(true));

        // 2. Quando pedirPosicaoExp está FALSE, reseta aux_expedicao e IniciarGuardarExp
        if (!snap.pedirPosicaoExp()) {
            if (!readOnly) {
                stateStore.setAuxExpedicao(false);
                try {
                    plc.writeBit(DB_NUMBER, 2, 1, false);
                } catch (Exception e) {
                    log.error("[EXPEDICAO] Erro ao atualizar IniciarGuardar [DB9:2.1] para FALSE: {}", e.getMessage());
                }
            }
        }

        // 3. Solicitação de posição para guardar na Expedição
        if (snap.pedirPosicaoExp() && !stateStore.isAuxExpedicao()) {
            stateStore.setAuxExpedicao(true);
            if (!readOnly) {
                int posAlvo = stateStore.getPosicaoExpedicaoSolicitada();
                if (posAlvo <= 0) {
                    posAlvo = inventoryService.findFirstAvailableExpeditionPosition();
                    if (posAlvo > 0) {
                        stateStore.setPosicaoExpedicaoSolicitada(posAlvo);
                    }
                }
                try {
                    plc.writeInt(DB_NUMBER, 4, posAlvo);
                } catch (Exception e) {
                    log.error("[EXPEDICAO] Erro ao atualizar PosicaoGuardarExpedicao [DB9:4]: {}", e.getMessage());
                }
                try {
                    plc.writeBit(DB_NUMBER, 2, 1, true);
                } catch (Exception e) {
                    log.error("[EXPEDICAO] Erro ao atualizar IniciarGuardar [DB9:2.1] para TRUE: {}", e.getMessage());
                }
            }
        }

        // 4. Reset de RecebidoExpedicao quando não está adicionando nem removendo
        if (!readOnly && (!snap.adicionarExpedicao() && !snap.removerExpedicao())) {
            try {
                plc.writeBit(DB_NUMBER, 2, 0, false);
            } catch (Exception e) {
                log.error("[EXPEDICAO] Erro ao atualizar RecebidoExpedicao [DB9:2.0] para FALSE: {}", e.getMessage());
            }
        }

        // 5. Adição na Expedição (corrige BUG-02 e remove duplicação do BUG-06)
        if (snap.adicionarExpedicao() && !stateStore.isAuxExpedicao()) {
            stateStore.setAuxExpedicao(true);
            if (!readOnly) {
                try {
                    plc.writeBit(DB_NUMBER, 2, 0, true);
                } catch (Exception e) {
                    log.error("[EXPEDICAO] Erro ao atualizar RecebidoExpedicao [DB9:2.0] para TRUE: {}",
                            e.getMessage());
                }

                if (snap.posicaoGuardarExp() > 0) {
                    int offset = 6 + (snap.posicaoGuardarExp() - 1) * 2;
                    int opToStore = snap.opGuardadoExpedicao() > 0 ? snap.opGuardadoExpedicao() : snap.numeroOPExp();
                    try {
                        plc.writeInt(DB_NUMBER, offset, opToStore);
                        inventoryService.confirmOrderInExpedition(snap.posicaoGuardarExp(), opToStore);
                    } catch (Exception e) {
                        log.error("[EXPEDICAO] Erro ao confirmar pedido OP={} na posição {}: {}",
                                opToStore, snap.posicaoGuardarExp(), e.getMessage());
                    }
                }
            }
        }

        // 6. Remoção na Expedição (corrige BUG-02 chamando InventoryService diretamente)
        if (snap.removerExpedicao() && !stateStore.isAuxExpedicao()) {
            stateStore.setAuxExpedicao(true);
            if (!readOnly) {
                try {
                    plc.writeBit(DB_NUMBER, 2, 0, true);
                } catch (Exception e) {
                    log.error("[EXPEDICAO] Erro ao atualizar RecebidoExpedicao [DB9:2.0] para TRUE na remoção: {}",
                            e.getMessage());
                }

                if (snap.posicaoRemovidoExpedicao() > 0) {
                    int offset = 6 + (snap.posicaoRemovidoExpedicao() - 1) * 2;
                    try {
                        plc.writeInt(DB_NUMBER, offset, 0);
                        inventoryService.removeOrderFromExpedition(snap.posicaoRemovidoExpedicao());
                    } catch (Exception e) {
                        log.error("[EXPEDICAO] Erro ao remover pedido da posição {}: {}",
                                snap.posicaoRemovidoExpedicao(), e.getMessage());
                    }
                }
            }
        }

        // 7. Conclusão do ciclo global de produção quando o pedido é guardado na Expedição
        if (snap.posicaoGuardadoExpedicao() == snap.posicaoGuardarExp()
                && !snap.ocupadoExp()
                && snap.finishOPExp()) {
            if (!readOnly && stateStore.getStatusProducao() == 0 && stateStore.isPedidoEmCurso()) {
                log.info("[EXPEDICAO] Operação OP={} finalizada com sucesso na posição {}",
                        snap.opGuardadoExpedicao(), snap.posicaoGuardadoExpedicao());
                stateStore.setPedidoEmCurso(false);
                stateStore.setStatusProducao((byte) 1);
            }
        }

        // 8. Controle de novo OP guardado na Expedição (corrige BUG-05 mantendo lastProcessedOp na instância)
        if (snap.opGuardadoExpedicao() > 0 && snap.opGuardadoExpedicao() != lastProcessedOp) {
            long currentTime = System.currentTimeMillis();
            lastProcessedOp = snap.opGuardadoExpedicao();
            lastProcessedOpTimestamp = currentTime;
            log.info("[EXPEDICAO] Novo OP guardado detectado: OP={} (timestamp={})", lastProcessedOp, currentTime);

            Integer ultimaPosicao = inventoryService.getLastPositionByStorageId(2L);
            int novaPosLivre = inventoryService.findFirstAvailableExpeditionPosition();
            stateStore.setPosicaoExpedicaoSolicitada(Math.max(novaPosLivre, 0));
            stateStore.setAuxExpedicao(false);

            if (!readOnly && ultimaPosicao != null && ultimaPosicao > 0) {
                try {
                    int offset = 6 + (ultimaPosicao - 1) * 2;
                    plc.writeInt(DB_NUMBER, offset, snap.opGuardadoExpedicao());
                    plc.writeBit(DB_NUMBER, 2, 1, false);
                } catch (Exception e) {
                    log.error("[EXPEDICAO] Erro ao registrar OP={} na posição {}: {}",
                            snap.opGuardadoExpedicao(), ultimaPosicao, e.getMessage());
                }
            }
        } else if (snap.opGuardadoExpedicao() == 0) {
            lastProcessedOp = 0;
        }

        return snap;
    }

    public int getLastProcessedOp() {
        return lastProcessedOp;
    }

    public long getLastProcessedOpTimestamp() {
        return lastProcessedOpTimestamp;
    }

    public void resetLastProcessedOp() {
        this.lastProcessedOp = 0;
        this.lastProcessedOpTimestamp = 0L;
    }
}
