package com.smart1.appsmartweb.plc.simulator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.smart1.appsmartweb.plc.PlcClient;

/**
 * Simulador thread-safe de CLP Siemens S7 em memória para execução de testes automatizados
 * e desenvolvimento sem a bancada física conectada.
 * Mantém os Data Blocks em Big-Endian (ByteOrder.BIG_ENDIAN).
 */
public class InMemorySimulatedPlcClient implements PlcClient {

    private static final int DEFAULT_DB_SIZE = 256;

    private final String ipAddress;
    private final Map<Integer, byte[]> dataBlocks = new ConcurrentHashMap<>();
    private volatile boolean connected = false;

    public InMemorySimulatedPlcClient(String ipAddress) {
        this.ipAddress = ipAddress;
        // Inicializa os DBs padrão da bancada (DB9=111B/48B, DB2=9B, DB57=9B)
        dataBlocks.put(9, new byte[128]);
        dataBlocks.put(2, new byte[32]);
        dataBlocks.put(57, new byte[32]);
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public synchronized void connect() {
        this.connected = true;
    }

    @Override
    public synchronized void disconnect() {
        this.connected = false;
    }

    private byte[] getOrCreateDb(int db, int requiredLength) {
        return dataBlocks.compute(db, (key, existing) -> {
            if (existing == null) {
                return new byte[Math.max(DEFAULT_DB_SIZE, requiredLength)];
            }
            if (existing.length < requiredLength) {
                return Arrays.copyOf(existing, requiredLength);
            }
            return existing;
        });
    }

    private void ensureConnected() throws Exception {
        if (!connected) {
            throw new Exception("Conexão simulada não estabelecida para o CLP " + ipAddress);
        }
    }

    @Override
    public synchronized String readString(int db, int startAdd, int size) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + size);
        return new String(memory, startAdd, size, StandardCharsets.UTF_8).trim();
    }

    @Override
    public synchronized byte[] readBlock(int db, int startAdd, int size) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + size);
        return Arrays.copyOfRange(memory, startAdd, startAdd + size);
    }

    @Override
    public synchronized float readFloat(int db, int startAdd) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + 4);
        return ByteBuffer.wrap(memory, startAdd, 4).order(ByteOrder.BIG_ENDIAN).getFloat();
    }

    @Override
    public synchronized int readInt(int db, int startAdd) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + 2);
        return ByteBuffer.wrap(memory, startAdd, 2).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    @Override
    public synchronized byte readByte(int db, int startAdd) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + 1);
        return memory[startAdd];
    }

    @Override
    public synchronized boolean readBit(int db, int startAdd, int bitNumber) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + 1);
        return ((memory[startAdd] >> (bitNumber & 0x07)) & 0x01) == 1;
    }

    @Override
    public synchronized boolean writeString(int db, int startAdd, int size, String str) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + size);
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        Arrays.fill(memory, startAdd, startAdd + size, (byte) 0);
        System.arraycopy(bytes, 0, memory, startAdd, Math.min(bytes.length, size));
        return true;
    }

    @Override
    public synchronized boolean writeBlock(int db, int startAdd, int size, byte[] block) throws Exception {
        ensureConnected();
        int copyLength = Math.min(size, block.length);
        byte[] memory = getOrCreateDb(db, startAdd + copyLength);
        System.arraycopy(block, 0, memory, startAdd, copyLength);
        return true;
    }

    @Override
    public synchronized boolean writeFloat(int db, int startAdd, float value) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + 4);
        ByteBuffer.wrap(memory, startAdd, 4).order(ByteOrder.BIG_ENDIAN).putFloat(value);
        return true;
    }

    @Override
    public synchronized boolean writeInt(int db, int startAdd, int value) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + 2);
        ByteBuffer.wrap(memory, startAdd, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) value);
        return true;
    }

    @Override
    public synchronized boolean writeByte(int db, int startAdd, byte value) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + 1);
        memory[startAdd] = value;
        return true;
    }

    @Override
    public synchronized boolean writeBit(int db, int startAdd, int bitNumber, boolean logic) throws Exception {
        ensureConnected();
        byte[] memory = getOrCreateDb(db, startAdd + 1);
        int mask = 1 << (bitNumber & 0x07);
        if (logic) {
            memory[startAdd] = (byte) (memory[startAdd] | mask);
        } else {
            memory[startAdd] = (byte) (memory[startAdd] & ~mask);
        }
        return true;
    }
}
