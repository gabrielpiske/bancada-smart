package com.smart1.appsmartweb.plc.s7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Codec binário puro (stateless e thread-safe) do protocolo Siemens S7 (ISO-on-TCP / RFC 1006).
 * Preserva rigorosamente o formato de pacotes TPKT + COTP + S7 Read/Write Var em Big-Endian.
 */
public final class S7ProtocolCodec {

    public static final int PAYLOAD_OFFSET = 25;
    public static final int WRITE_RETURN_CODE_OFFSET = 21;
    public static final byte S7_RETURN_CODE_SUCCESS = (byte) 0xFF;

    private S7ProtocolCodec() {
    }

    public static byte[] createConnectionRequest() {
        return new byte[] {
                // TPKT Header (4B)
                0x03, 0x00, 0x00, 0x16,
                // ISO 8073/X.224 COTP Header (18B)
                0x11, (byte) 0xE0, 0x00, 0x00, 0x00, 0x01,
                0x00, (byte) 0xC1, 0x02, 0x01, 0x00, (byte) 0xC2, 0x02, 0x01, 0x01, (byte) 0xC0, 0x01, 0x09
        };
    }

    public static byte[] createSetupCommunication() {
        return new byte[] {
                // TPKT Header (4B)
                0x03, 0x00, 0x00, 0x19,
                // ISO 8073/X.224 COTP Header (3B)
                0x02, (byte) 0xF0, (byte) 0x80,
                // S7 Header (10B)
                0x32, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00,
                // S7 Parameters (8B - PDU size = 960 bytes)
                (byte) 0xF0, 0x00, 0x00, 0x01, 0x00, 0x01, (byte) 0x03, (byte) 0xC0
        };
    }

    public static byte[] createReadRequest(int db, int offset, int bit, int size, String type) {
        byte tpSize;
        int startAddress;

        if (size == 1 && "boolean".equalsIgnoreCase(type)) {
            tpSize = 0x01;
            startAddress = ((offset << 3) & 0xFFFFF8) | (bit & 0x07);
        } else {
            tpSize = 0x02;
            startAddress = offset << 3;
        }

        return new byte[] {
                // TPKT + ISO 8073/X.224 COTP Header
                0x03, 0x00, 0x00, (byte) 31, 0x02, (byte) 0xF0, (byte) 0x80,
                // S7 Header
                0x32, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x0E, 0x00, 0x00,
                // Parameter: Function Code (0x04 Read Var), Item Count (1)
                0x04, 0x01,
                // Item Header: Variable Specification, Length of Following, Syntax ID (S7ANY)
                0x12, 0x0A, 0x10,
                // Transport Size, Parameter length, DB Number, Area Type (0x84 = DB)
                tpSize, (byte) ((size >> 8) & 0xFF), (byte) (size & 0xFF),
                (byte) ((db >> 8) & 0xFF), (byte) (db & 0xFF), (byte) 0x84,
                // Address (24-bit Bit Address in Big-Endian — corrige BUG-10)
                (byte) ((startAddress >> 16) & 0xFF),
                (byte) ((startAddress >> 8) & 0xFF),
                (byte) (startAddress & 0xFF)
        };
    }

    public static byte[] createWriteRequest(int db, int offset, int bit, int size, String type, Object value) {
        String normalizedType = type.toLowerCase();
        int lengthTag = "string".equals(normalizedType) ? size + 2 : size;

        ByteBuffer buffer = ByteBuffer.allocate(35 + lengthTag);
        buffer.order(ByteOrder.BIG_ENDIAN);

        byte tpSize;
        int startAddress;

        if (size == 1 && "boolean".equals(normalizedType)) {
            tpSize = 0x01;
            startAddress = ((offset << 3) & 0xFFFFF8) | (bit & 0x07);
        } else {
            tpSize = 0x02;
            startAddress = offset << 3;
        }

        int lengthPacket = 35 + lengthTag;
        int dataLength = 4 + size;

        // TPKT Header
        buffer.put((byte) 0x03);
        buffer.put((byte) 0x00);
        buffer.putShort((short) lengthPacket);

        // ISO 8073/X.224 COTP Header
        buffer.put((byte) 0x02);
        buffer.put((byte) 0xF0);
        buffer.put((byte) 0x80);

        // S7 Header
        buffer.put((byte) 0x32);
        buffer.put((byte) 0x01);
        buffer.putShort((short) 0x0000);
        buffer.putShort((short) 0x0000);
        buffer.putShort((short) 0x000E);
        buffer.putShort((short) dataLength);

        // Parameter: Function Code (0x05 Write Var), Item Count (1)
        buffer.put((byte) 0x05);
        buffer.put((byte) 0x01);

        // Item Header
        buffer.put((byte) 0x12);
        buffer.put((byte) 0x0A);
        buffer.put((byte) 0x10);

        // Transport Size, Parameter length, DB Number, Area Type (DB = 0x84)
        buffer.put(tpSize);
        buffer.putShort((short) size);
        buffer.putShort((short) db);
        buffer.put((byte) 0x84);

        // Address (24-bit Bit Address in Big-Endian)
        buffer.put((byte) ((startAddress >> 16) & 0xFF));
        buffer.put((byte) ((startAddress >> 8) & 0xFF));
        buffer.put((byte) (startAddress & 0xFF));

        // Data Item Header
        buffer.put((byte) 0x00);
        buffer.put((byte) (tpSize + 2));

        switch (normalizedType) {
            case "boolean" -> {
                buffer.putShort((short) lengthTag);
                buffer.put((byte) ((boolean) value ? 0x01 : 0x00));
            }
            case "integer" -> {
                buffer.putShort((short) (lengthTag << 3));
                buffer.putShort((short) ((int) value));
            }
            case "byte" -> {
                buffer.putShort((short) (lengthTag << 3));
                buffer.put((byte) value);
            }
            case "string" -> {
                String strVal = (String) value;
                buffer.putShort((short) (lengthTag << 3));
                buffer.put((byte) size);
                buffer.put((byte) strVal.trim().length());
                buffer.put(strVal.getBytes(StandardCharsets.UTF_8));
            }
            case "block" -> {
                buffer.putShort((short) (lengthTag << 3));
                buffer.put((byte[]) value);
            }
            case "float" -> {
                float valFloat = (float) value;
                ByteBuffer bufferFloat = ByteBuffer.allocate(Float.BYTES).order(ByteOrder.BIG_ENDIAN);
                bufferFloat.putFloat(valFloat);
                buffer.putShort((short) (lengthTag << 3));
                buffer.put(bufferFloat.array());
            }
            default -> throw new IllegalArgumentException("Tipo de variável S7 não suportado: " + type);
        }

        return buffer.array();
    }

    public static Object extractValueFromReadResponse(byte[] response, int size, String type) {
        return switch (type.toLowerCase()) {
            case "string" -> extractStringFromResponse(response, size);
            case "block" -> extractBlockFromResponse(response, size);
            case "integer" -> extractIntegerFromResponse(response);
            case "float" -> extractFloatFromResponse(response);
            case "byte" -> extractByteFromResponse(response);
            case "boolean" -> extractBooleanFromResponse(response);
            default -> throw new IllegalArgumentException("Tipo de variável não suportado: " + type);
        };
    }

    public static int extractIntegerFromResponse(byte[] response) {
        return ByteBuffer.wrap(response, PAYLOAD_OFFSET, 2).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public static float extractFloatFromResponse(byte[] response) {
        return ByteBuffer.wrap(response, PAYLOAD_OFFSET, 4).order(ByteOrder.BIG_ENDIAN).getFloat();
    }

    public static byte extractByteFromResponse(byte[] response) {
        return ByteBuffer.wrap(response, PAYLOAD_OFFSET, 1).order(ByteOrder.BIG_ENDIAN).get();
    }

    public static byte[] extractBlockFromResponse(byte[] response, int size) {
        return Arrays.copyOfRange(response, PAYLOAD_OFFSET, PAYLOAD_OFFSET + size);
    }

    public static boolean extractBooleanFromResponse(byte[] response) {
        return (response[PAYLOAD_OFFSET] & 0x01) == 1;
    }

    public static String extractStringFromResponse(byte[] response, int size) {
        return new String(response, PAYLOAD_OFFSET, size, StandardCharsets.UTF_8).trim();
    }

    public static boolean isWriteSuccess(byte[] response, int bytesRead) {
        return bytesRead > WRITE_RETURN_CODE_OFFSET && response[WRITE_RETURN_CODE_OFFSET] == S7_RETURN_CODE_SUCCESS;
    }

    public static byte[] hexStringToByteArray(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("A string hex deve ter um número par de caracteres.");
        }
        byte[] byteArray = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            String hexPair = hexString.substring(i, i + 2);
            byteArray[i / 2] = (byte) Integer.parseInt(hexPair, 16);
        }
        return byteArray;
    }
}
