package com.smart1.appsmartweb.plc.s7;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.jupiter.api.Test;

class S7ProtocolCodecTest {

    @Test
    void connectionAndSetupPacketsMatchSpecification() {
        byte[] cotp = S7ProtocolCodec.createConnectionRequest();
        assertThat(cotp).hasSize(22);
        assertThat(cotp[0]).isEqualTo((byte) 0x03);
        assertThat(cotp[3]).isEqualTo((byte) 0x16);

        byte[] setup = S7ProtocolCodec.createSetupCommunication();
        assertThat(setup).hasSize(25);
        assertThat(setup[0]).isEqualTo((byte) 0x03);
        assertThat(setup[3]).isEqualTo((byte) 0x19);
        assertThat(setup[7]).isEqualTo((byte) 0x32); // S7 Protocol ID
    }

    @Test
    void createReadRequestEncodesStationDataBlocksAnd24BitAddressesCorrectly() {
        // Estoque: DB9, offset 0, 111 bytes
        byte[] readStock = S7ProtocolCodec.createReadRequest(9, 0, 0, 111, "block");
        assertThat(readStock).hasSize(31);
        assertThat(readStock[17]).isEqualTo((byte) 0x04); // Read Var
        assertThat(readStock[22]).isEqualTo((byte) 0x02); // tpSize BYTE
        assertThat(ByteBuffer.wrap(readStock, 23, 2).order(ByteOrder.BIG_ENDIAN).getShort()).isEqualTo((short) 111);
        assertThat(ByteBuffer.wrap(readStock, 25, 2).order(ByteOrder.BIG_ENDIAN).getShort()).isEqualTo((short) 9);
        assertThat(readStock[27]).isEqualTo((byte) 0x84); // Area DB

        // Teste do BUG-10: offset = 8192 -> startAddress = 8192 << 3 = 65536 (0x010000)
        byte[] readHighOffset = S7ProtocolCodec.createReadRequest(9, 8192, 0, 2, "integer");
        assertThat(readHighOffset[28]).isEqualTo((byte) 0x01); // MSB de 24 bits preservado!
        assertThat(readHighOffset[29]).isEqualTo((byte) 0x00);
        assertThat(readHighOffset[30]).isEqualTo((byte) 0x00);
    }

    @Test
    void createWriteRequestEncodesBigEndianValuesAndBitAddresses() {
        // Escrita de bit: DB9.DBX64.1 = true
        byte[] writeBit = S7ProtocolCodec.createWriteRequest(9, 64, 1, 1, "boolean", true);
        assertThat(writeBit).hasSize(36);
        assertThat(writeBit[17]).isEqualTo((byte) 0x05); // Write Var
        assertThat(writeBit[22]).isEqualTo((byte) 0x01); // tpSize BIT
        int bitAddress = ((writeBit[28] & 0xFF) << 16) | ((writeBit[29] & 0xFF) << 8) | (writeBit[30] & 0xFF);
        assertThat(bitAddress).isEqualTo((64 << 3) | 1);
        assertThat(writeBit[35]).isEqualTo((byte) 0x01);

        // Escrita de inteiro 16-bit Big-Endian: DB9.DBW66 = 28 (0x001C)
        byte[] writeInt = S7ProtocolCodec.createWriteRequest(9, 66, 0, 2, "integer", 28);
        assertThat(writeInt).hasSize(37);
        assertThat(writeInt[35]).isEqualTo((byte) 0x00);
        assertThat(writeInt[36]).isEqualTo((byte) 0x1C);
    }

    @Test
    void extractMethodsParsePayloadAtOffset25InBigEndian() {
        byte[] response = new byte[40];
        ByteBuffer.wrap(response, 25, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) 42);
        assertThat(S7ProtocolCodec.extractIntegerFromResponse(response)).isEqualTo(42);

        ByteBuffer.wrap(response, 25, 4).order(ByteOrder.BIG_ENDIAN).putFloat(12.5f);
        assertThat(S7ProtocolCodec.extractFloatFromResponse(response)).isEqualTo(12.5f);

        response[21] = (byte) 0xFF;
        assertThat(S7ProtocolCodec.isWriteSuccess(response, 22)).isTrue();
    }
}
