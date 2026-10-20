package com.smart1.appsmartweb.plc.mapper;

/**
 * Mapper binário puro para o Data Block DB57 (9 bytes) do CLP Montagem (10.74.241.30).
 */
public final class AssemblyDb57Mapper {

    public static final int DB_NUMBER = 57;
    public static final int DB_SIZE = 9;

    private AssemblyDb57Mapper() {
    }

    public record AssemblyPlcSnapshot(
            boolean recebidoOpMon,
            int numeroOPMon,
            boolean cancelOPMon,
            boolean finishOPMon,
            boolean startOPMon,
            boolean ocupadoMon,
            boolean aguardandoMon,
            boolean manualMon,
            boolean emergenciaMon,
            boolean xEmergenciaAtivadaMon,
            boolean xComutadorAutomaticoMon,
            boolean xCondicaoIniciarMon) {

        public static AssemblyPlcSnapshot empty() {
            return new AssemblyPlcSnapshot(
                    false, 0, false, false, false,
                    false, false, false, false,
                    false, false, false);
        }
    }

    public static AssemblyPlcSnapshot fromBytes(byte[] data) {
        if (data == null || data.length < DB_SIZE) {
            throw new IllegalArgumentException(
                    "Buffer do DB57 (Montagem) inválido: esperado >= 9 bytes, recebido "
                            + (data == null ? "null" : data.length));
        }

        boolean recebidoOpMon = bit(data[0], 0);
        int numeroOPMon = ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);

        boolean cancelOPMon = bit(data[4], 0);
        boolean finishOPMon = bit(data[4], 1);
        boolean startOPMon = bit(data[4], 2);

        boolean ocupadoMon = bit(data[6], 0);
        boolean aguardandoMon = bit(data[6], 1);
        boolean manualMon = bit(data[6], 2);
        boolean emergenciaMon = bit(data[6], 3);

        byte raByte = data[8] != 0 ? data[8] : data[7];
        boolean xEmergenciaAtivadaMon = bit(raByte, 0);
        boolean xComutadorAutomaticoMon = bit(raByte, 1);
        boolean xCondicaoIniciarMon = bit(raByte, 2);

        return new AssemblyPlcSnapshot(
                recebidoOpMon, numeroOPMon,
                cancelOPMon, finishOPMon, startOPMon,
                ocupadoMon, aguardandoMon, manualMon, emergenciaMon,
                xEmergenciaAtivadaMon, xComutadorAutomaticoMon, xCondicaoIniciarMon);
    }

    private static boolean bit(byte b, int bitIndex) {
        return ((b >> bitIndex) & 0x01) != 0;
    }
}
