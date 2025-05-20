//-------------------------------------------------------------------------------------------------
/*  SENAI TIMBÓ/SC
 *  CURSO TÉCNICO EM DESENVOLVIMENTO DE SISTEMAS
 *  AUTOR: Gerson Trindade        DATA: AGO/2024
 * 
 *  Classe para leitura cíclica de blocos de bytes (Leitura completa de uma DB) do CLP Siemens 
 *  usando PlcConnector.
 * 
 *  Esta classe permite a leitura de blocos de Bytes a cada 500ms.
 */
//-------------------------------------------------------------------------------------------------

import javax.swing.SwingWorker;

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

            byte[] data = plcConnector.readBlock(this.db, this.offset, this.size); // Exemplo de leitura
                                                                                   // readDataBlock(int db, int offset,
                                                                                   // int size )

            publish(new Object[] { id, data });
            Thread.sleep(500);

        }
        return null;

    }

    @Override
    protected void process(java.util.List<Object[]> chunks) {

        Object[] result = chunks.get(0);
        int id = (int) result[0];
        byte[] data = (byte[]) result[1];

        S7AppSwing.processDataBlock(id, data);
    }
}
