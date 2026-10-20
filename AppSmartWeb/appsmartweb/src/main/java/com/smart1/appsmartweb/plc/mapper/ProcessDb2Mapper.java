package com.smart1.appsmartweb.plc.mapper;

/**
 * Mapper binário puro para o Data Block DB2 (9 bytes) do CLP Processo (10.74.241.20).
 */
public final class ProcessDb2Mapper {

    public static final int DB_NUMBER = 2;
    public static final int DB_SIZE = 9;

    private ProcessDb2Mapper() {
    }

    public record ProcessPlcSnapshot(
            boolean recebidoOpPro,
            int numeroOPPro,
            boolean cancelOPPro,
            boolean finishOPPro,
            boolean startOPPro,
            boolean ocupadoPro,
            boolean aguardandoPro,
            boolean manualPro,
            boolean emergenciaPro,
            boolean xEmergenciaAtivadaPro,
            boolean xComutadorAutomaticoPro,
            boolean xNecessitaHomeEixoXPro,
            boolean xNecessitaHomeEixoYPro,
            boolean xServoDesligadoEixoXPro,
            boolean xServoDesligadoEixoYPro,
            boolean xCondicaoIniciarPro) {

        public static ProcessPlcSnapshot empty() {
            return new ProcessPlcSnapshot(
                    false, 0, false, false, false,
                    false, false, false, false,
                    false, false, false, false, false, false, false);
        }
    }

    public static ProcessPlcSnapshot fromBytes(byte[] data) {
        if (data == null || data.length < DB_SIZE) {
            throw new IllegalArgumentException(
                    "Buffer do DB2 (Processo) inválido: esperado >= 9 bytes, recebido "
                            + (data == null ? "null" : data.length));
        }

        boolean recebidoOpPro = bit(data[0], 0);
        int numeroOPPro = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);

        boolean cancelOPPro = bit(data[4], 0);
        boolean finishOPPro = bit(data[4], 1);
        boolean startOPPro = bit(data[4], 2);

        boolean ocupadoPro = bit(data[6], 0);
        boolean aguardandoPro = bit(data[6], 1);
        boolean manualPro = bit(data[6], 2);
        boolean emergenciaPro = bit(data[6], 3);

        byte raByte = data[8] != 0 ? data[8] : data[7];
        boolean xEmergenciaAtivadaPro = bit(raByte, 0);
        boolean xComutadorAutomaticoPro = bit(raByte, 1);
        boolean xNecessitaHomeEixoXPro = bit(raByte, 2);
        boolean xNecessitaHomeEixoYPro = bit(raByte, 3);
        boolean xServoDesligadoEixoXPro = bit(raByte, 4);
        boolean xServoDesligadoEixoYPro = bit(raByte, 5);
        boolean xCondicaoIniciarPro = bit(raByte, 6);

        return new ProcessPlcSnapshot(
                recebidoOpPro, numeroOPPro,
                cancelOPPro, finishOPPro, startOPPro,
                ocupadoPro, aguardandoPro, manualPro, emergenciaPro,
                xEmergenciaAtivadaPro, xComutadorAutomaticoPro,
                xNecessitaHomeEixoXPro, xNecessitaHomeEixoYPro,
                xServoDesligadoEixoXPro, xServoDesligadoEixoYPro,
                xCondicaoIniciarPro);
    }

    private static boolean bit(byte b, int bitIndex) {
        return ((b >> bitIndex) & 0x01) != 0;
    }
}
