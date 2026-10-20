package com.smart1.appsmartweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smart1.appsmartweb.plc.s7.S7ProtocolCodec;

/**
 * Cliente TCP S7 thread-safe para leitura e escrita de Data Blocks (DB) nos CLPs Siemens.
 * Todas as operações de socket são sincronizadas por instância (corrige BUG-01)
 * e não compartilham buffers de resposta mutáveis entre chamadas.
 */
public class S7Client {

    private static final Logger log = LoggerFactory.getLogger(S7Client.class);
    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 2000;
    private static final int DEFAULT_READ_TIMEOUT_MS = 2000;

    private final String plcIpAddress;
    private final int port;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;

    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public S7Client(String plcIpAddress, int port) {
        this(plcIpAddress, port, DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS);
    }

    public S7Client(String plcIpAddress, int port, int connectTimeoutMs, int readTimeoutMs) {
        this.plcIpAddress = plcIpAddress;
        this.port = port;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
    }

    public synchronized boolean connect() throws Exception {
        try {
            InetAddress address = InetAddress.getByName(plcIpAddress);
            socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), connectTimeoutMs);
            socket.setSoTimeout(readTimeoutMs);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
            log.info("Conexão TCP estabelecida com o CLP {}:{}", plcIpAddress, port);
            return true;
        } catch (IOException e) {
            throw new Exception("Falha ao conectar ao CLP " + plcIpAddress + ":" + port + " - " + e.getMessage(), e);
        }
    }

    public byte[] createReadRequest(int db, int offset, int bit, int size, String type) {
        return S7ProtocolCodec.createReadRequest(db, offset, bit, size, type);
    }

    public byte[] createWriteRequest(int db, int offset, int bit, int size, String type, Object value) {
        return S7ProtocolCodec.createWriteRequest(db, offset, bit, size, type, value);
    }

    public static byte[] hexStringToByteArray(String hexString) {
        return S7ProtocolCodec.hexStringToByteArray(hexString);
    }

    public synchronized void sendConnectionRequest() throws Exception {
        ensureConnected();
        byte[] packet = S7ProtocolCodec.createConnectionRequest();
        try {
            outputStream.flush();
            outputStream.write(packet);
            outputStream.flush();

            byte[] response = new byte[1024];
            int bytesRead = inputStream.read(response);
            if (bytesRead < 0) {
                throw new IOException("Stream encerrado pelo CLP durante handshake COTP");
            }
        } catch (Exception e) {
            throw new Exception("Erro ao enviar a solicitação de conexão COTP: " + e.getMessage(), e);
        }
    }

    public synchronized void sendSetupCommunication() throws Exception {
        ensureConnected();
        byte[] packet = S7ProtocolCodec.createSetupCommunication();
        try {
            outputStream.flush();
            outputStream.write(packet);
            outputStream.flush();

            byte[] response = new byte[1024];
            int bytesRead = inputStream.read(response);
            if (bytesRead < 0) {
                throw new IOException("Stream encerrado pelo CLP durante Setup Communication");
            }
        } catch (Exception e) {
            throw new Exception("Erro ao enviar o pacote de configuração S7: " + e.getMessage(), e);
        }
    }

    public synchronized Object sendReadRequest(int db, int offset, int bit, int size, String type) throws Exception {
        ensureConnected();
        byte[] packet = S7ProtocolCodec.createReadRequest(db, offset, bit, size, type);

        try {
            outputStream.flush();
            outputStream.write(packet);
            outputStream.flush();

            byte[] response = new byte[1024];
            int bytesRead = inputStream.read(response);
            if (bytesRead < S7ProtocolCodec.PAYLOAD_OFFSET) {
                throw new IOException("Resposta S7 truncada (" + bytesRead + " bytes) do CLP " + plcIpAddress);
            }

            Object extracted = S7ProtocolCodec.extractValueFromReadResponse(response, size, type);
            log.trace("Leitura S7 CLP {} DB{} offset={} size={} type={}", plcIpAddress, db, offset, size, type);
            return extracted;
        } catch (Exception e) {
            throw new Exception("Erro ao enviar o pacote de leitura ao CLP " + plcIpAddress + ": " + e.getMessage(), e);
        }
    }

    public synchronized boolean sendWriteRequest(int db, int offset, int bit, int size, String type, Object value)
            throws Exception {
        ensureConnected();
        byte[] packet = S7ProtocolCodec.createWriteRequest(db, offset, bit, size, type, value);

        try {
            outputStream.write(packet);
            outputStream.flush();

            byte[] response = new byte[50];
            int bytesRead = inputStream.read(response);
            boolean ok = S7ProtocolCodec.isWriteSuccess(response, bytesRead);
            log.debug("Escrita S7 CLP {} DB{} offset={} bit={} type={} value={} -> ok={}",
                    plcIpAddress, db, offset, bit, type, value, ok);
            return ok;
        } catch (IOException e) {
            throw new Exception("Erro ao enviar o pacote de escrita ao CLP " + plcIpAddress + ": " + e.getMessage(), e);
        }
    }

    public synchronized void disconnect() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            log.info("Conexão com o CLP {} encerrada.", plcIpAddress);
        } catch (IOException e) {
            log.warn("Erro ao encerrar a conexão com o CLP {}: {}", plcIpAddress, e.getMessage());
        } finally {
            outputStream = null;
            inputStream = null;
            socket = null;
        }
    }

    private void ensureConnected() throws Exception {
        if (outputStream == null || inputStream == null || socket == null || socket.isClosed()) {
            throw new Exception("Conexão não estabelecida com o CLP " + plcIpAddress + ". Chame o método connect() primeiro.");
        }
    }
}
