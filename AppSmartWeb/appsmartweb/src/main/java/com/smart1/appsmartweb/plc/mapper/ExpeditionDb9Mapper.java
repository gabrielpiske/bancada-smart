package com.smart1.appsmartweb.plc.mapper;

/**
 * Mapper binário puro para o Data Block DB9 (48 bytes) do CLP Expedição (10.74.241.40).
 */
public final class ExpeditionDb9Mapper {

    public static final int DB_NUMBER = 9;
    public static final int DB_SIZE = 48;
    public static final int EXPEDITION_CAPACITY = 12;

    private ExpeditionDb9Mapper() {
    }

    public record ExpeditionPlcSnapshot(
            boolean recebidoOpExp,
            boolean recebidoExpedicao,
            boolean iniciarGuardarExp,
            int posicaoGuardarExp,
            int[] orderExpedicao,
            int numeroOPExp,
            boolean cancelOPExp,
            boolean finishOPExp,
            boolean startOPExp,
            boolean ocupadoExp,
            boolean aguardandoExp,
            boolean manualExp,
            boolean emergenciaExp,
            boolean pedirPosicaoExp,
            int posicaoGuardadoExpedicao,
            int posicaoRemovidoExpedicao,
            boolean adicionarExpedicao,
            boolean removerExpedicao,
            int opGuardadoExpedicao,
            boolean xEmergenciaAtivadaExp,
            boolean xComutadorAutomaticoExp,
            boolean xNecessitaHomeEixoVerticalExp,
            boolean xNecessitaHomeEixoGiroExp,
            boolean xNecessitaHomeEixoHorizontalExp,
            boolean xServoDesligadoEixoHorizontalExp,
            boolean xServoDesligadoEixoGiroExp,
            boolean xServoDesligadoEixoVerticalExp,
            boolean xCondicaoIniciarExp) {

        public static ExpeditionPlcSnapshot empty() {
            return new ExpeditionPlcSnapshot(
                    false, false, false, 0,
                    new int[EXPEDITION_CAPACITY],
                    0, false, false, false,
                    false, false, false, false,
                    false, 0, 0, false, false, 0,
                    false, false, false, false, false, false, false, false, false);
        }
    }

    public static ExpeditionPlcSnapshot fromBytes(byte[] data) {
        if (data == null || data.length < DB_SIZE) {
            throw new IllegalArgumentException(
                    "Buffer do DB9 (Expedição) inválido: esperado >= 48 bytes, recebido "
                            + (data == null ? "null" : data.length));
        }

        boolean recebidoOpExp = bit(data[0], 0);
        boolean recebidoExpedicao = bit(data[2], 0);
        boolean iniciarGuardarExp = bit(data[2], 1);
        int posicaoGuardarExp = uint16(data, 4);

        int[] orderExpedicao = new int[EXPEDITION_CAPACITY];
        for (int i = 0; i < EXPEDITION_CAPACITY; i++) {
            orderExpedicao[i] = uint16(data, 6 + (i * 2));
        }

        int numeroOPExp = uint16(data, 30);
        boolean cancelOPExp = bit(data[32], 0);
        boolean finishOPExp = bit(data[32], 1);
        boolean startOPExp = bit(data[32], 2);

        boolean ocupadoExp = bit(data[34], 0);
        boolean aguardandoExp = bit(data[34], 1);
        boolean manualExp = bit(data[34], 2);
        boolean emergenciaExp = bit(data[34], 3);

        boolean pedirPosicaoExp = bit(data[36], 0);
        int posicaoGuardadoExpedicao = uint16(data, 38);
        int posicaoRemovidoExpedicao = uint16(data, 40);
        boolean adicionarExpedicao = bit(data[42], 0);
        boolean removerExpedicao = bit(data[42], 1);
        int opGuardadoExpedicao = uint16(data, 44);

        // Bytes 46..47: Realidade Aumentada (9 bits)
        boolean xEmergenciaAtivadaExp = bit(data[46], 0);
        boolean xComutadorAutomaticoExp = bit(data[46], 1);
        boolean xNecessitaHomeEixoVerticalExp = bit(data[46], 2);
        boolean xNecessitaHomeEixoGiroExp = bit(data[46], 3);
        boolean xNecessitaHomeEixoHorizontalExp = bit(data[46], 4);
        boolean xServoDesligadoEixoHorizontalExp = bit(data[46], 5);
        boolean xServoDesligadoEixoGiroExp = bit(data[46], 6);
        boolean xServoDesligadoEixoVerticalExp = bit(data[46], 7);
        boolean xCondicaoIniciarExp = bit(data[47], 0);

        return new ExpeditionPlcSnapshot(
                recebidoOpExp, recebidoExpedicao, iniciarGuardarExp, posicaoGuardarExp,
                orderExpedicao,
                numeroOPExp, cancelOPExp, finishOPExp, startOPExp,
                ocupadoExp, aguardandoExp, manualExp, emergenciaExp,
                pedirPosicaoExp, posicaoGuardadoExpedicao, posicaoRemovidoExpedicao,
                adicionarExpedicao, removerExpedicao, opGuardadoExpedicao,
                xEmergenciaAtivadaExp, xComutadorAutomaticoExp,
                xNecessitaHomeEixoVerticalExp, xNecessitaHomeEixoGiroExp, xNecessitaHomeEixoHorizontalExp,
                xServoDesligadoEixoHorizontalExp, xServoDesligadoEixoGiroExp, xServoDesligadoEixoVerticalExp,
                xCondicaoIniciarExp);
    }

    private static boolean bit(byte b, int bitIndex) {
        return ((b >> bitIndex) & 0x01) != 0;
    }

    private static int uint16(byte[] data, int offset) {
        return ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
    }
}
