package com.smart1.appsmartweb.service.station;

import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.plc.PlcClient;
import com.smart1.appsmartweb.plc.mapper.ProcessDb2Mapper;
import com.smart1.appsmartweb.plc.mapper.ProcessDb2Mapper.ProcessPlcSnapshot;

/**
 * Máquina de estados e regras de negócio da Estação 2 — PROCESSO (DB2, 9 bytes).
 */
@Component
public class ProcessStationHandler {

    private static final int DB_NUMBER = ProcessDb2Mapper.DB_NUMBER;

    private final BancadaStateStore stateStore;
    private final OpHandshakeHelper handshakeHelper;

    public ProcessStationHandler(BancadaStateStore stateStore, OpHandshakeHelper handshakeHelper) {
        this.stateStore = stateStore;
        this.handshakeHelper = handshakeHelper;
    }

    public ProcessPlcSnapshot handle(PlcClient plc, byte[] dadosClp2) {
        ProcessPlcSnapshot snap = ProcessDb2Mapper.fromBytes(dadosClp2);
        stateStore.setProcessSnapshot(snap);

        if (plc == null) {
            return snap;
        }

        handshakeHelper.processHandshake(
                plc,
                DB_NUMBER,
                "PROCESSO",
                snap.startOPPro(),
                snap.finishOPPro(),
                snap.cancelOPPro(),
                snap.recebidoOpPro(),
                stateStore.isReadOnly(),
                stateStore.isPedidoEmCurso(),
                stateStore.getStatusProducao(),
                stateStore::setStatusProcesso,
                null);

        return snap;
    }
}
