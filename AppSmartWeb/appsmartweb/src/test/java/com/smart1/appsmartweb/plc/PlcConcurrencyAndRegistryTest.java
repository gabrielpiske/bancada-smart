package com.smart1.appsmartweb.plc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

import com.smart1.appsmartweb.model.enums.StationType;
import com.smart1.appsmartweb.service.connection.PlcConnectionManager;
import com.smart1.appsmartweb.util.PlcConnector;

class PlcConcurrencyAndRegistryTest {

    @Test
    void concurrentReadsAndWritesOnSimulatedPlcConnectorAreThreadSafe() throws Exception {
        PlcConnector connector = new PlcConnector("10.74.241.10", 102, true);
        connector.connect();

        int numThreads = 8;
        int iterationsPerThread = 100;
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int t = 0; t < numThreads; t++) {
            final int threadIndex = t;
            tasks.add(() -> {
                for (int i = 0; i < iterationsPerThread; i++) {
                    int offset = 68 + (threadIndex % 28); // DB9.DBB68..95
                    byte color = (byte) ((i % 3) + 1);
                    connector.writeByte(9, offset, color);
                    byte[] block = connector.readBlock(9, 0, 111);
                    if (block.length != 111) {
                        return false;
                    }
                }
                return true;
            });
        }

        List<Future<Boolean>> results = pool.invokeAll(tasks);
        pool.shutdown();

        for (Future<Boolean> future : results) {
            assertThat(future.get()).isTrue();
        }

        // Testa escrita e leitura de INT (Big-Endian) e BIT no mesmo DB9
        connector.writeInt(9, 66, 15);
        connector.writeBit(9, 64, 1, true);
        assertThat(connector.readInt(9, 66)).isEqualTo(15);
        assertThat(connector.readBit(9, 64, 1)).isTrue();

        connector.disconnect();
        assertThat(connector.isConnected()).isFalse();
    }

    @Test
    void plcConnectionManagerTracksDynamicStationIps() {
        PlcConnectionManager.configure("10.74.241", 102, true);
        assertThat(PlcConnectionManager.getStationIp(StationType.ESTOQUE)).isEqualTo("10.74.241.10");
        assertThat(PlcConnectionManager.getStationIp(StationType.EXPEDICAO)).isEqualTo("10.74.241.40");

        // Simula alteração de faixa de IP pelo usuário na interface web (BUG-14)
        PlcConnectionManager.registerStationIps(Map.of(
                "estoque", "192.168.0.10",
                "processo", "192.168.0.20",
                "montagem", "192.168.0.30",
                "expedicao", "192.168.0.40"));

        assertThat(PlcConnectionManager.getStationIp(StationType.ESTOQUE)).isEqualTo("192.168.0.10");
        assertThat(PlcConnectionManager.getStationIp(StationType.PROCESSO)).isEqualTo("192.168.0.20");
        assertThat(PlcConnectionManager.getStationIp(StationType.MONTAGEM)).isEqualTo("192.168.0.30");
        assertThat(PlcConnectionManager.getStationIp(StationType.EXPEDICAO)).isEqualTo("192.168.0.40");

        PlcConnectionManager.resetStationIpsToDefault();
        assertThat(PlcConnectionManager.getStationIp(StationType.ESTOQUE)).isEqualTo("10.74.241.10");
    }
}
