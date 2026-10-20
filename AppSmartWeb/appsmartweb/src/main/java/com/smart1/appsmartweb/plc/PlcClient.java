package com.smart1.appsmartweb.plc;

/**
 * Contrato de comunicação com um CLP Siemens (real via TCP S7 ou simulado em memória).
 */
public interface PlcClient {

    String getIpAddress();

    boolean isConnected();

    void connect() throws Exception;

    void disconnect() throws Exception;

    String readString(int db, int startAdd, int size) throws Exception;

    byte[] readBlock(int db, int startAdd, int size) throws Exception;

    float readFloat(int db, int startAdd) throws Exception;

    int readInt(int db, int startAdd) throws Exception;

    byte readByte(int db, int startAdd) throws Exception;

    boolean readBit(int db, int startAdd, int bitNumber) throws Exception;

    boolean writeString(int db, int startAdd, int size, String str) throws Exception;

    boolean writeBlock(int db, int startAdd, int size, byte[] block) throws Exception;

    boolean writeFloat(int db, int startAdd, float value) throws Exception;

    boolean writeInt(int db, int startAdd, int value) throws Exception;

    boolean writeByte(int db, int startAdd, byte value) throws Exception;

    boolean writeBit(int db, int startAdd, int bitNumber, boolean logic) throws Exception;
}
