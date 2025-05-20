package com.smart40;

import javax.swing.SwingWorker;

//import com.gtrindade.PlcConnector;

public class PlcReaderWorker extends SwingWorker<Object[], Object[]> {
    private final PlcConnector plcConnector;
    private final int id;
    private final int db;
    private final int offset;
    private final int size;

    public PlcReaderWorker(PlcConnector plcConnector, int id, int db, int offset, int size) {
        this.plcConnector = plcConnector;
        this.id = id;
        this.db = db;
        this.offset = offset;
        this.size = size;

    }

    @Override
    protected Object[] doInBackground() throws Exception {

        while (!isCancelled()) {
            // System.out.println("Aqui em doInBackground => " + this.plcConnector + " DB =
            // " + db + " OFFSET = " + offset + " size = " + size + " ID = "+id);

            byte[] data = this.plcConnector.readBlock(this.db, this.offset, this.size); // Exemplo de leitura
            // byte[] data = this.plcConnector.readString(this.db, this.offset,
            // this.size).getBytes(); // Exemplo de leitura
            // readDataBlock(int db, int offset,
            // int size )

            // System.out.println("Resposta de conexão recebida [readBlock]: " +
            // S7ProtocolClient.bytesToHex(data, data.length));

            publish(new Object[] { id, data });
            Thread.sleep(600);

        }
        return null;

    }

    @Override
    protected void process(java.util.List<Object[]> chunks) {

        // System.out.println("Aqui em Process");
        Object[] result = chunks.get(0);
        int idWorker = (int) result[0];
        byte[] data = (byte[]) result[1];
        // System.out.println("Aqui Process");
        MainFrame.processDataBlock(idWorker, data);
    }
}
