package com.smart1.appsmartweb.util;

import com.smart1.appsmartweb.plc.PlcClient;
import com.smart1.appsmartweb.plc.s7.S7ProtocolCodec;
import com.smart1.appsmartweb.plc.simulator.InMemorySimulatedPlcClient;

/**
 * Classe de conexão com CLP Siemens usando S7Client (ou cliente simulado em memória)
 * para leitura e escrita de variáveis nos CLPs S7-1200 e S7-1500.
 * Todas as chamadas de leitura/escrita são thread-safe (sincronizadas por instância).
 */
public class PlcConnector implements PlcClient {

    private final String ipAddress;
    private final int port;
    private final S7Client client;
    private final InMemorySimulatedPlcClient simulatedClient;
    private volatile boolean connected = false;

    public PlcConnector(String ipAddress, int port) {
        this(ipAddress, port, false);
    }

    public PlcConnector(String ipAddress, int port, boolean simulationMode) {
        this.ipAddress = ipAddress;
        this.port = port;
        if (simulationMode) {
            this.client = null;
            this.simulatedClient = new InMemorySimulatedPlcClient(ipAddress);
        } else {
            this.client = new S7Client(this.ipAddress, this.port);
            this.simulatedClient = null;
        }
    }

    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public synchronized void connect() throws Exception {
        if (simulatedClient != null) {
            simulatedClient.connect();
            connected = true;
            return;
        }
        try {
            if (client.connect()) {
                try {
                    client.sendConnectionRequest();
                    client.sendSetupCommunication();
                    connected = true;
                } catch (Exception e) {
                    client.disconnect();
                    connected = false;
                    throw new Exception("Erro ao tentar comunicação S7 com o CLP " + ipAddress + ": " + e.getMessage(), e);
                }
            } else {
                connected = false;
            }
        } catch (Exception e) {
            connected = false;
            throw new Exception("Erro ao conectar ao CLP " + ipAddress + ": " + e.getMessage(), e);
        }
    }

    @Override
    public synchronized void disconnect() throws Exception {
        try {
            if (simulatedClient != null) {
                simulatedClient.disconnect();
            } else if (client != null) {
                client.disconnect();
            }
        } catch (Exception e) {
            throw new Exception("Erro ao tentar desconectar do CLP " + ipAddress + ": " + e.getMessage(), e);
        } finally {
            connected = false;
        }
    }

    @Override
    public synchronized String readString(int db, int startAdd, int size) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.readString(db, startAdd, size);
        }
        return (String) client.sendReadRequest(db, startAdd, 0, size, "string");
    }

    @Override
    public synchronized byte[] readBlock(int db, int startAdd, int size) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.readBlock(db, startAdd, size);
        }
        return (byte[]) client.sendReadRequest(db, startAdd, 0, size, "block");
    }

    @Override
    public synchronized float readFloat(int db, int startAdd) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.readFloat(db, startAdd);
        }
        return (float) client.sendReadRequest(db, startAdd, 0, 4, "float");
    }

    @Override
    public synchronized int readInt(int db, int startAdd) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.readInt(db, startAdd);
        }
        return (int) client.sendReadRequest(db, startAdd, 0, 2, "integer");
    }

    @Override
    public synchronized byte readByte(int db, int startAdd) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.readByte(db, startAdd);
        }
        return (byte) client.sendReadRequest(db, startAdd, 0, 1, "byte");
    }

    @Override
    public synchronized boolean readBit(int db, int startAdd, int bitNumber) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.readBit(db, startAdd, bitNumber);
        }
        return (boolean) client.sendReadRequest(db, startAdd, bitNumber, 1, "boolean");
    }

    @Override
    public synchronized boolean writeString(int db, int startAdd, int size, String str) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.writeString(db, startAdd, size, str);
        }
        return client.sendWriteRequest(db, startAdd, 0, size, "string", str);
    }

    @Override
    public synchronized boolean writeBlock(int db, int startAdd, int size, byte[] block) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.writeBlock(db, startAdd, size, block);
        }
        return client.sendWriteRequest(db, startAdd, 0, size, "block", block);
    }

    @Override
    public synchronized boolean writeFloat(int db, int startAdd, float value) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.writeFloat(db, startAdd, value);
        }
        return client.sendWriteRequest(db, startAdd, 0, 4, "float", value);
    }

    @Override
    public synchronized boolean writeInt(int db, int startAdd, int value) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.writeInt(db, startAdd, value);
        }
        return client.sendWriteRequest(db, startAdd, 0, 2, "integer", value);
    }

    @Override
    public synchronized boolean writeByte(int db, int startAdd, byte value) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.writeByte(db, startAdd, value);
        }
        return client.sendWriteRequest(db, startAdd, 0, 1, "byte", value);
    }

    @Override
    public synchronized boolean writeBit(int db, int startAdd, int bitNumber, boolean logic) throws Exception {
        ensureConnected();
        if (simulatedClient != null) {
            return simulatedClient.writeBit(db, startAdd, bitNumber, logic);
        }
        return client.sendWriteRequest(db, startAdd, bitNumber, 1, "boolean", logic);
    }

    private void ensureConnected() throws Exception {
        if (!connected) {
            throw new Exception("Conexão não estabelecida com o CLP " + ipAddress + ". Chame o método connect() primeiro.");
        }
    }

    public static byte[] hexStringToByteArray(String hexString) {
        return S7ProtocolCodec.hexStringToByteArray(hexString);
    }
}
