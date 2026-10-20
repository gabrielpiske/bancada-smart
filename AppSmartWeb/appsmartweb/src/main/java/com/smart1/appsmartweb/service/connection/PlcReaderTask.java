package com.smart1.appsmartweb.service.connection;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smart1.appsmartweb.plc.PlcClient;

public class PlcReaderTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PlcReaderTask.class);

    private final PlcClient plcConnector;
    private final String nomeClp;
    private final int db;
    private final int offset;
    private final int size;
    private final Consumer<byte[]> onDataRead;

    public PlcReaderTask(PlcClient plcConnector, String nomeClp, int db, int offset, int size,
            Consumer<byte[]> onDataRead) {
        this.plcConnector = plcConnector;
        this.nomeClp = nomeClp;
        this.db = db;
        this.offset = offset;
        this.size = size;
        this.onDataRead = onDataRead;
    }

    @Override
    public void run() {
        try {
            byte[] data = plcConnector.readBlock(db, offset, size);
            onDataRead.accept(data);
        } catch (Exception e) {
            log.warn("Erro ao ler CLP {} (DB{} offset={} size={}): {}", nomeClp, db, offset, size, e.getMessage());
        }
    }
}
