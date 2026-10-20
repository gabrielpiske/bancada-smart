package com.smart1.appsmartweb.service.station;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.plc.PlcClient;

/**
 * Componente reutilizável para o handshake padrão de Ordem de Produção (OP)
 * presente nas 4 estações (StartOP / FinishOP / CancelOP <-> RecebidoOP em DBX0.0).
 */
@Component
public class OpHandshakeHelper {

    private static final Logger log = LoggerFactory.getLogger(OpHandshakeHelper.class);

    public void processHandshake(
            PlcClient plc,
            int dbNumber,
            String stationName,
            boolean startOP,
            boolean finishOP,
            boolean cancelOP,
            boolean recebidoOP,
            boolean readOnly,
            boolean pedidoEmCurso,
            byte statusProducao,
            Consumer<Byte> stationStatusConsumer,
            Runnable onFinishCallback) {

        // 1. Se StartOP, FinishOP e CancelOP estão em FALSE -> reseta RecebidoOP para FALSE
        if (!startOP && !finishOP && !cancelOP) {
            if (!readOnly) {
                try {
                    plc.writeBit(dbNumber, 0, 0, false);
                } catch (Exception e) {
                    log.error("[{}] Erro ao atualizar RecebidoOP [DB{}:0.0] para FALSE: {}",
                            stationName, dbNumber, e.getMessage());
                }
            }
        }

        // 2. Se StartOP subiu e RecebidoOP está FALSE -> confirma RecebidoOP = TRUE e status = 1
        if (startOP && !recebidoOP) {
            if (statusProducao == 0 && pedidoEmCurso) {
                stationStatusConsumer.accept((byte) 1);
            }
            if (!readOnly) {
                try {
                    plc.writeBit(dbNumber, 0, 0, true);
                } catch (Exception e) {
                    log.error("[{}] Erro [startOP] ao atualizar RecebidoOP [DB{}:0.0] para TRUE: {}",
                            stationName, dbNumber, e.getMessage());
                }
            }
        }

        // 3. Se FinishOP subiu e RecebidoOP está FALSE -> confirma RecebidoOP = TRUE e status = 2
        if (finishOP && !recebidoOP) {
            if (!readOnly) {
                try {
                    plc.writeBit(dbNumber, 0, 0, true);
                    if (onFinishCallback != null) {
                        onFinishCallback.run();
                    }
                } catch (Exception e) {
                    log.error("[{}] Erro [finishOP] ao atualizar RecebidoOP [DB{}:0.0] para TRUE: {}",
                            stationName, dbNumber, e.getMessage());
                }
                if (statusProducao == 0 && pedidoEmCurso) {
                    stationStatusConsumer.accept((byte) 2);
                }
            }
        }
    }
}
