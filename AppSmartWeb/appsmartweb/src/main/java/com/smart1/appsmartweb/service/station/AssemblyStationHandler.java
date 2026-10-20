package com.smart1.appsmartweb.service.station;

import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.plc.PlcClient;
import com.smart1.appsmartweb.plc.mapper.AssemblyDb57Mapper;
import com.smart1.appsmartweb.plc.mapper.AssemblyDb57Mapper.AssemblyPlcSnapshot;

/**
 * Máquina de estados e regras de negócio da Estação 3 — MONTAGEM (DB57, 9 bytes).
 */
@Component
public class AssemblyStationHandler {

    private static final int DB_NUMBER = AssemblyDb57Mapper.DB_NUMBER;

    private final BancadaStateStore stateStore;
    private final OpHandshakeHelper handshakeHelper;

    public AssemblyStationHandler(BancadaStateStore stateStore, OpHandshakeHelper handshakeHelper) {
        this.stateStore = stateStore;
        this.handshakeHelper = handshakeHelper;
    }

    public AssemblyPlcSnapshot handle(PlcClient plc, byte[] dadosClp3) {
        AssemblyPlcSnapshot snap = AssemblyDb57Mapper.fromBytes(dadosClp3);
        stateStore.setAssemblySnapshot(snap);

        if (plc == null) {
            return snap;
        }

        handshakeHelper.processHandshake(
                plc,
                DB_NUMBER,
                "MONTAGEM",
                snap.startOPMon(),
                snap.finishOPMon(),
                snap.cancelOPMon(),
                snap.recebidoOpMon(),
                stateStore.isReadOnly(),
                stateStore.isPedidoEmCurso(),
                stateStore.getStatusProducao(),
                stateStore::setStatusMontagem,
                null);

        return snap;
    }
}
