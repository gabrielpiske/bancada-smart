package com.smart1.appsmartweb.plc.mapper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Mapper binário puro para o Data Block DB9 (111 bytes) do CLP Estoque (10.74.241.10)
 * e serialização do buffer de pedido (60 bytes / 30 shorts em Big-Endian).
 */
public final class StockDb9Mapper {

    public static final int DB_NUMBER = 9;
    public static final int DB_SIZE = 111;
    public static final int ORDER_BUFFER_OFFSET = 2;
    public static final int ORDER_BUFFER_BYTES = 60;
    public static final int ORDER_BUFFER_SHORTS = 30;
    public static final int STOCK_POSITIONS_OFFSET = 68;
    public static final int STOCK_CAPACITY = 28;

    private StockDb9Mapper() {
    }

    public record StockPlcSnapshot(
            boolean recebidoOpEst,
            int cor_Andar_1,
            int posicao_Estoque_Andar_1,
            int cor_Lamina_1_Andar_1,
            int cor_Lamina_2_Andar_1,
            int cor_Lamina_3_Andar_1,
            int padrao_Lamina_1_Andar_1,
            int padrao_Lamina_2_Andar_1,
            int padrao_Lamina_3_Andar_1,
            int processamento_Andar_1,
            int cor_Andar_2,
            int posicao_Estoque_Andar_2,
            int cor_Lamina_1_Andar_2,
            int cor_Lamina_2_Andar_2,
            int cor_Lamina_3_Andar_2,
            int padrao_Lamina_1_Andar_2,
            int padrao_Lamina_2_Andar_2,
            int padrao_Lamina_3_Andar_2,
            int processamento_Andar_2,
            int cor_Andar_3,
            int posicao_Estoque_Andar_3,
            int cor_Lamina_1_Andar_3,
            int cor_Lamina_2_Andar_3,
            int cor_Lamina_3_Andar_3,
            int padrao_Lamina_1_Andar_3,
            int padrao_Lamina_2_Andar_3,
            int padrao_Lamina_3_Andar_3,
            int processamento_Andar_3,
            int numeroPedidoEst,
            int andares,
            int posicaoExpedicaoEst,
            boolean iniciarPedido,
            boolean recebidoEstoque,
            boolean iniciarGuardarEst,
            int posicaoGuardarEst,
            byte[] posicoesOcupadas,
            int numeroOPEst,
            boolean cancelOPEst,
            boolean finishOPEst,
            boolean startOPEst,
            boolean ocupadoEst,
            boolean aguardandoEst,
            boolean manualEst,
            boolean emergenciaEst,
            boolean pedirPosicaoEst,
            int posicaoEstoque,
            boolean adicionarEstoque,
            boolean removerEstoque,
            boolean retornoEstoqueCheio,
            int corGuardarEstoque,
            boolean xEmergenciaAtivadaEst,
            boolean xComutadorAutomaticoEst,
            boolean xNecessitaHomeEixoVerticalEst,
            boolean xNecessitaHomeEixoGiroEst,
            boolean xServoDesligadoEixoVerticalEst,
            boolean xServoDesligadoEixoGiroEst,
            boolean xCondicaoIniciarEst) {

        public static StockPlcSnapshot empty() {
            return new StockPlcSnapshot(
                    false,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0,
                    false, false, false, 0,
                    new byte[STOCK_CAPACITY],
                    0, false, false, false,
                    false, false, false, false,
                    false, 0, false, false, false, 0,
                    false, false, false, false, false, false, false);
        }
    }

    public static StockPlcSnapshot fromBytes(byte[] data) {
        if (data == null || data.length < DB_SIZE) {
            throw new IllegalArgumentException(
                    "Buffer do DB9 (Estoque) inválido: esperado >= 111 bytes, recebido "
                            + (data == null ? "null" : data.length));
        }

        boolean recebidoOpEst = bit(data[0], 0);

        // Decodifica os 30 inteiros de 16 bits (60 bytes) do buffer de pedido (DBB2..61)
        int cor_Andar_1 = uint16(data, 2);
        int posicao_Estoque_Andar_1 = uint16(data, 4);
        int cor_Lamina_1_Andar_1 = uint16(data, 6);
        int cor_Lamina_2_Andar_1 = uint16(data, 8);
        int cor_Lamina_3_Andar_1 = uint16(data, 10);
        int padrao_Lamina_1_Andar_1 = uint16(data, 12);
        int padrao_Lamina_2_Andar_1 = uint16(data, 14);
        int padrao_Lamina_3_Andar_1 = uint16(data, 16);
        int processamento_Andar_1 = uint16(data, 18);

        int cor_Andar_2 = uint16(data, 20);
        int posicao_Estoque_Andar_2 = uint16(data, 22);
        int cor_Lamina_1_Andar_2 = uint16(data, 24);
        int cor_Lamina_2_Andar_2 = uint16(data, 26);
        int cor_Lamina_3_Andar_2 = uint16(data, 28);
        int padrao_Lamina_1_Andar_2 = uint16(data, 30);
        int padrao_Lamina_2_Andar_2 = uint16(data, 32);
        int padrao_Lamina_3_Andar_2 = uint16(data, 34);
        int processamento_Andar_2 = uint16(data, 36);

        int cor_Andar_3 = uint16(data, 38);
        int posicao_Estoque_Andar_3 = uint16(data, 40);
        int cor_Lamina_1_Andar_3 = uint16(data, 42);
        int cor_Lamina_2_Andar_3 = uint16(data, 44);
        int cor_Lamina_3_Andar_3 = uint16(data, 46);
        int padrao_Lamina_1_Andar_3 = uint16(data, 48);
        int padrao_Lamina_2_Andar_3 = uint16(data, 50);
        int padrao_Lamina_3_Andar_3 = uint16(data, 52);
        int processamento_Andar_3 = uint16(data, 54);

        int numeroPedidoEst = uint16(data, 56);
        int andares = uint16(data, 58);
        int posicaoExpedicaoEst = uint16(data, 60);

        boolean iniciarPedido = bit(data[62], 0);
        boolean recebidoEstoque = bit(data[64], 0);
        boolean iniciarGuardarEst = bit(data[64], 1);
        int posicaoGuardarEst = uint16(data, 66);

        byte[] posicoesOcupadas = Arrays.copyOfRange(data, STOCK_POSITIONS_OFFSET, STOCK_POSITIONS_OFFSET + STOCK_CAPACITY);

        int numeroOPEst = uint16(data, 96);
        boolean cancelOPEst = bit(data[98], 0);
        boolean finishOPEst = bit(data[98], 1);
        boolean startOPEst = bit(data[98], 2);

        boolean ocupadoEst = bit(data[100], 0);
        boolean aguardandoEst = bit(data[100], 1);
        boolean manualEst = bit(data[100], 2);
        boolean emergenciaEst = bit(data[100], 3);

        boolean pedirPosicaoEst = bit(data[102], 0);
        int posicaoEstoque = uint16(data, 104);
        boolean adicionarEstoque = bit(data[106], 0);
        boolean removerEstoque = bit(data[106], 1);
        boolean retornoEstoqueCheio = bit(data[106], 2);
        int corGuardarEstoque = uint16(data, 108);

        // Byte 110: Realidade Aumentada
        boolean xEmergenciaAtivadaEst = bit(data[110], 0);
        boolean xComutadorAutomaticoEst = bit(data[110], 1);
        boolean xNecessitaHomeEixoVerticalEst = bit(data[110], 2);
        boolean xNecessitaHomeEixoGiroEst = bit(data[110], 3);
        boolean xServoDesligadoEixoVerticalEst = bit(data[110], 4);
        boolean xServoDesligadoEixoGiroEst = bit(data[110], 5);
        boolean xCondicaoIniciarEst = bit(data[110], 6);

        return new StockPlcSnapshot(
                recebidoOpEst,
                cor_Andar_1, posicao_Estoque_Andar_1,
                cor_Lamina_1_Andar_1, cor_Lamina_2_Andar_1, cor_Lamina_3_Andar_1,
                padrao_Lamina_1_Andar_1, padrao_Lamina_2_Andar_1, padrao_Lamina_3_Andar_1,
                processamento_Andar_1,
                cor_Andar_2, posicao_Estoque_Andar_2,
                cor_Lamina_1_Andar_2, cor_Lamina_2_Andar_2, cor_Lamina_3_Andar_2,
                padrao_Lamina_1_Andar_2, padrao_Lamina_2_Andar_2, padrao_Lamina_3_Andar_2,
                processamento_Andar_2,
                cor_Andar_3, posicao_Estoque_Andar_3,
                cor_Lamina_1_Andar_3, cor_Lamina_2_Andar_3, cor_Lamina_3_Andar_3,
                padrao_Lamina_1_Andar_3, padrao_Lamina_2_Andar_3, padrao_Lamina_3_Andar_3,
                processamento_Andar_3,
                numeroPedidoEst, andares, posicaoExpedicaoEst,
                iniciarPedido, recebidoEstoque, iniciarGuardarEst, posicaoGuardarEst,
                posicoesOcupadas,
                numeroOPEst, cancelOPEst, finishOPEst, startOPEst,
                ocupadoEst, aguardandoEst, manualEst, emergenciaEst,
                pedirPosicaoEst, posicaoEstoque, adicionarEstoque, removerEstoque, retornoEstoqueCheio, corGuardarEstoque,
                xEmergenciaAtivadaEst, xComutadorAutomaticoEst,
                xNecessitaHomeEixoVerticalEst, xNecessitaHomeEixoGiroEst,
                xServoDesligadoEixoVerticalEst, xServoDesligadoEixoGiroEst,
                xCondicaoIniciarEst);
    }

    /**
     * Converte os 30 inteiros do pedido em um array de 60 bytes em Big-Endian
     * para escrita em DB9 offset 2.
     */
    public static byte[] serializeOrderBuffer(int[] dados) {
        if (dados == null || dados.length != ORDER_BUFFER_SHORTS) {
            throw new IllegalArgumentException("O buffer de pedido deve conter exatamente 30 inteiros (recebido: "
                    + (dados == null ? "null" : dados.length) + ")");
        }
        ByteBuffer buffer = ByteBuffer.allocate(ORDER_BUFFER_BYTES).order(ByteOrder.BIG_ENDIAN);
        for (int valor : dados) {
            buffer.putShort((short) valor);
        }
        return buffer.array();
    }

    private static boolean bit(byte b, int bitIndex) {
        return ((b >> bitIndex) & 0x01) != 0;
    }

    private static int uint16(byte[] data, int offset) {
        return ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
    }
}
