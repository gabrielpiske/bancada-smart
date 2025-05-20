
//-------------------------------------------------------------------------------------------------
/*  SENAI TIMBÓ/SC
 *  CURSO TÉCNICO EM DESENVOLVIMENTO DE SISTEMAS
 *  AUTOR: Gerson Trindade        DATA: AGO/2024
 * 
 *  Classe principal da Aplicação 
 * 
 *  Esta classe implementa a interface gráfica e
 *  funcionalidades de comunicação com a planta Smart 4.0
 */
//-------------------------------------------------------------------------------------------------
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class S7AppSwing extends JFrame {

    // ----------------------------------------------//
    // Variáveis Booleanas ESTOQUE
    // ----------------------------------------------//
    // **** NODE to PLC *****************************//
    public static boolean recebidoOpEst; // DB9 0.0
    public static boolean iniciarPedido; // DB9 62.0

    // --- Gerenciamento Estoque --------------------//
    public static boolean recebidoEstoque; // DB9 64.0
    public static boolean iniciarGuardar; // DB9 64.1

    // **** PLC to NODE *****************************/
    // --- Status Op Estoque ------------------------//
    public static boolean cancelOp; // DB9 98.0
    public static boolean finishOp; // DB9 98.1
    public static boolean startOp; // DB9 98.2

    // --- Status Estação Estoque -------------------//
    public static boolean ocupadoEst; // DB9 100.0
    public static boolean aguardandoEst; // DB9 100.1
    public static boolean manualEst; // DB9 100.2
    public static boolean emergenciaEst; // DB9 100.3

    // --- Gerenciamento Estoque --------------------//
    public static boolean pedirPosicaoEst; // DB9 102.0
    public static boolean adicionarEstoque; // DB9 106.0
    public static boolean removerEstoque; // DB9 106.1
    public static boolean retornoEstoqueCheio; // DB9 106.2
    public static int posicaoEstoque;
    public static int corGuardarEstoque;
    public static int posicaoGuardarEstoque;

    // ------ Fim variáveis booleanas ESTOQUE -----------//
    public static byte[] infoDB9_Est;

    public static boolean leitura = false;
    // Não utilizar agora
    public static PlcReaderWorker plcReaderEst;
    public static PlcReaderWorker plcReaderPro;
    public static PlcReaderWorker plcReaderMon;
    public static PlcReaderWorker plcReaderExp;

    public static byte[] indxColorBlk = new byte[28];
    public static byte[] indxPosExped = new byte[24];
    public static int[] operacao = new int[12];

    public JPanel pnlBlkEst;
    public JPanel pnlOpExp;
    public static JTextField textIp;
    public PlcConnector plcEstoque;
    public PlcConnector plcExpedicao;

    public static String opDB = "";

    public S7AppSwing() {

        setTitle("Leitura e Escrita de TAGs no CLP - Protocolo S7");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        Border borderBlack = BorderFactory.createLineBorder(new Color(20, 150, 180));

        JLabel labelIp = new JLabel("Ip Host:");
        labelIp.setBounds(50, 10, 100, 30);
        add(labelIp);

        textIp = new JTextField("10.74.241.10");
        textIp.setBounds(150, 10, 200, 30);
        add(textIp);

        JLabel labelDb = new JLabel("DB:");
        labelDb.setBounds(50, 50, 100, 30);
        add(labelDb);

        JTextField textDb = new JTextField("6");
        textDb.setBounds(150, 50, 200, 30);
        add(textDb);

        JLabel labelType = new JLabel("Tipo:");
        labelType.setBounds(50, 100, 100, 30);
        add(labelType);

        JComboBox<String> comboType = new JComboBox<>(
                new String[] { "String", "Block", "Integer", "Float", "Byte", "Boolean" });
        comboType.setBounds(150, 100, 200, 30);
        add(comboType);

        JLabel labelOffset = new JLabel("Offset:");
        labelOffset.setBounds(50, 150, 100, 30);
        add(labelOffset);

        JTextField textOffset = new JTextField("16");
        textOffset.setBounds(150, 150, 200, 30);
        add(textOffset);

        JLabel labelBitNumber = new JLabel("Bit Number:");
        labelBitNumber.setBounds(50, 200, 100, 30);
        add(labelBitNumber);

        JTextField textBitNumber = new JTextField("0");
        textBitNumber.setBounds(150, 200, 200, 30);
        add(textBitNumber);

        JLabel labelSize = new JLabel("Tamanho:");
        labelSize.setBounds(50, 250, 100, 30);
        add(labelSize);

        JTextField textSize = new JTextField("14");
        textSize.setBounds(150, 250, 200, 30);
        add(textSize);

        comboType.addActionListener((ActionEvent e) -> {

            String selectedItem = (String) comboType.getSelectedItem();
            switch (selectedItem) {
                case "Boolean" ->
                    textSize.setText("1");
                case "Byte" -> {
                    textSize.setText("1");
                    textBitNumber.setText("0");
                }
                case "Integer" -> {
                    textSize.setText("2");
                    textBitNumber.setText("0");
                }
                case "Float" -> {
                    textSize.setText("4");
                    textBitNumber.setText("0");
                }
                case "String" ->
                    textBitNumber.setText("0");
            }
        });

        JButton buttonRead = new JButton("Ler TAG");
        buttonRead.setBounds(150, 300, 200, 30);
        add(buttonRead);

        JLabel labelValueRead = new JLabel("Valor lido:");
        labelValueRead.setBounds(50, 350, 200, 30);
        add(labelValueRead);

        JTextField textValue = new JTextField();
        textValue.setBounds(150, 350, 200, 30);
        textValue.setEditable(false);
        add(textValue);

        JTextArea taValue = new JTextArea();
        taValue.setBounds(50, 390, 300, 50);
        taValue.setEditable(true);
        taValue.setLineWrap(true);
        taValue.setWrapStyleWord(true);
        taValue.setBorder(borderBlack);
        add(taValue);

        JButton buttonWrite = new JButton("Escrever TAG");
        buttonWrite.setBounds(150, 450, 200, 30);
        add(buttonWrite);

        JLabel labelValue = new JLabel("Valor Escrito:");
        labelValue.setBounds(50, 500, 100, 30);
        add(labelValue);

        JTextField textValueWrite = new JTextField();
        textValueWrite.setBounds(150, 500, 200, 30);
        add(textValueWrite);

        JButton buttonLeituras = new JButton("Iniciar Leitura Cíclica");
        buttonLeituras.setBounds(150, 550, 200, 30);
        add(buttonLeituras);

        buttonLeituras.addActionListener((ActionEvent e) -> {
            SwingUtilities.invokeLater(() -> {
                if (!leitura) {
                    leitura = true;
                    buttonLeituras.setText("Parar Leitura Cíclica");

                    String[] netIp = textIp.getText().split("\\.");

                    // Criando as conexões com os PLCs
                    PlcConnector plcEst = new PlcConnector(netIp[0] + "." + netIp[1] + "." + netIp[2] + ".10", 102);
                    try {
                        plcEst.connect();
                        System.out.println("PlcEst = " + plcEst);
                    } catch (Exception e1) {
                        // Tratar erro de conexão
                    }
                    plcReaderEst = new PlcReaderWorker(plcEst, 1, 9, 0, 111);
                    plcReaderEst.execute();

                    PlcConnector plcPro = new PlcConnector(netIp[0] + "." + netIp[1] + "." + netIp[2] + ".20", 102);
                    try {
                        plcPro.connect();
                        System.out.println("PlcPro = " + plcPro);
                    } catch (Exception e1) {
                        // Tratar erro de conexão
                    }
                    plcReaderPro = new PlcReaderWorker(plcPro, 2, 6, 20, 10);
                    plcReaderPro.execute();

                    PlcConnector plcMon = new PlcConnector(netIp[0] + "." + netIp[1] + "." + netIp[2] + ".30", 102);
                    try {
                        plcMon.connect();
                        System.out.println("PlcMon = " + plcMon);
                    } catch (Exception e1) {
                        // Tratar erro de conexão
                    }
                    plcReaderMon = new PlcReaderWorker(plcMon, 3, 6, 30, 10);
                    plcReaderMon.execute();

                    PlcConnector plcExp = new PlcConnector(netIp[0] + "." + netIp[1] + "." + netIp[2] + ".40", 102);
                    try {
                        plcExp.connect();
                        System.out.println("PlcExp = " + plcExp);
                    } catch (Exception e1) {
                        // Tratar erro de conexão
                    }
                    plcReaderExp = new PlcReaderWorker(plcExp, 4, 6, 40, 10);
                    plcReaderExp.execute();

                } else {
                    leitura = false;
                    buttonLeituras.setText("Iniciar Leitura Cíclica");
                    // Cancelar a leitura cíclica, se necessário
                    plcReaderEst.cancel(true);
                    plcReaderPro.cancel(true);
                    plcReaderMon.cancel(true);
                    plcReaderExp.cancel(true);
                }
            });
        });

        Border blkBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        pnlBlkEst = new JPanel();
        pnlBlkEst.setBounds(380, 10, 280, 245);
        pnlBlkEst.setBorder(blkBorder);
        pnlBlkEst.setLayout(null);
        add(pnlBlkEst);

        JButton btnLerEstoque = new JButton("Magazine Estoque");
        btnLerEstoque.setBounds(380, 265, 280, 30);
        add(btnLerEstoque);

        btnLerEstoque.addActionListener((ActionEvent evt) -> {
            updtPnlBlocks();
        });

        pnlOpExp = new JPanel();
        pnlOpExp.setBounds(380, 320, 380, 170);
        pnlOpExp.setBorder(blkBorder);
        pnlOpExp.setLayout(null);
        add(pnlOpExp);

        JButton btnLerExpedicao = new JButton("Magazine Expedição");
        btnLerExpedicao.setBounds(380, 500, 380, 30);
        add(btnLerExpedicao);

        btnLerExpedicao.addActionListener((ActionEvent evt) -> {
            updtPnlOper();
        });

        JButton startPedido = new JButton("Iniciar Pedido de Exemplo");
        startPedido.setBounds(380, 540, 380, 30);
        add(startPedido);

        startPedido.addActionListener((ActionEvent evt) -> {
            // Configuração de um pedido exemplo
            ByteBuffer bfPedido = ByteBuffer.allocate(60);

            bfPedido.putShort((short) 1); // Cor_Andar_1
            bfPedido.putShort((short) 1); // Posicao_Estoque_Andar_1
            bfPedido.putShort((short) 1); // Cor_Lamina_1_Andar_1
            bfPedido.putShort((short) 2); // Cor_Lamina_2_Andar_1
            bfPedido.putShort((short) 3); // Cor_Lamina_3_Andar_1
            bfPedido.putShort((short) 0); // Padrao_Lamina_1_Andar_1
            bfPedido.putShort((short) 0); // Padrao_Lamina_2_Andar_1
            bfPedido.putShort((short) 0); // Padrao_Lamina_3_Andar_1
            bfPedido.putShort((short) 1); // Processamento_Andar_1

            bfPedido.putShort((short) 0); // Cor_Andar_2
            bfPedido.putShort((short) 0); // Posicao_Estoque_Andar_2
            bfPedido.putShort((short) 0); // Cor_Lamina_1_Andar_2
            bfPedido.putShort((short) 0); // Cor_Lamina_2_Andar_2
            bfPedido.putShort((short) 0); // Cor_Lamina_3_Andar_2
            bfPedido.putShort((short) 0); // Padrao_Lamina_1_Andar_2
            bfPedido.putShort((short) 0); // Padrao_Lamina_2_Andar_2+
            bfPedido.putShort((short) 0); // Padrao_Lamina_3_Andar_2
            bfPedido.putShort((short) 0); // Processamento_Andar_2

            bfPedido.putShort((short) 0); // Cor_Andar_3
            bfPedido.putShort((short) 0); // Posicao_Estoque_Andar_3
            bfPedido.putShort((short) 0); // Cor_Lamina_1_Andar_3
            bfPedido.putShort((short) 0); // Cor_Lamina_2_Andar_3
            bfPedido.putShort((short) 0); // Cor_Lamina_3_Andar_3
            bfPedido.putShort((short) 0); // Padrao_Lamina_1_Andar_3
            bfPedido.putShort((short) 0); // Padrao_Lamina_2_Andar_3
            bfPedido.putShort((short) 0); // Padrao_Lamina_3_Andar_3
            bfPedido.putShort((short) 0); // Processamento_Andar_3

            bfPedido.putShort((short) 205); // Numero_Pedido
            bfPedido.putShort((short) 1); // Andares
            bfPedido.putShort((short) 12); // Posicao_Expedicao

            PlcConnector plc = new PlcConnector(textIp.getText().trim(), 102);
            try {
                plc.connect();

                if (plc.writeBlock(9, 2, 60, bfPedido.array())) {
                    System.out.println("Envio do pedido realizado com sucesso");

                    plc.writeBit(9, 64, 0, Boolean.parseBoolean("FALSE")); // RecebidoEstoque
                    plc.writeBit(9, 64, 1, Boolean.parseBoolean("FALSE")); // IniciarGuardar
                    plc.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE")); // IniciarPedido

                    // Inicia pedido
                    plc.writeBit(9, 62, 0, Boolean.parseBoolean("TRUE")); // IniciarPedido

                    Thread.sleep(100);

                    if (plc.readBit(9, 100, 0)) {
                        plc.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE")); // IniciarPedido
                    }

                    Thread.sleep(100);

                    if (plc.readBit(9, 98, 2)) {
                        /* StartOP */
                        plc.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // RecebidoOP
                    }

                } else {
                    System.out.println("Falha no envio do pedido.");
                }
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            try {
                plc.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

        });

        // updtPnlOper();
        buttonRead.addActionListener((ActionEvent e) -> {
            try {
                // Lógica de leitura
                int db = Integer.parseInt(textDb.getText());
                int offset = Integer.parseInt(textOffset.getText());
                int bitNumber = !textBitNumber.getText().equals("") ? Integer.parseInt(textBitNumber.getText()) : -1;
                int size = Integer.parseInt(textSize.getText());
                String type = (String) comboType.getSelectedItem();

                PlcConnector plc = new PlcConnector(textIp.getText().trim(), 102);
                plc.connect();

                switch (type.toLowerCase()) {
                    case "string" -> {
                        String str = plc.readString(db, offset, size);
                        textValue.setText(str);
                    }
                    case "block" -> {
                        String blk = bytesToHex(plc.readBlock(db, offset, size), size);
                        taValue.setText(blk);
                    }
                    case "float" -> {
                        float flt = plc.readFloat(db, offset);
                        textValue.setText(String.valueOf(flt));
                    }
                    case "integer" -> {
                        int val = plc.readInt(db, offset);
                        textValue.setText(String.valueOf(val));
                    }
                    case "byte" -> {
                        byte byt = plc.readByte(db, offset);
                        textValue.setText(String.valueOf(byt));
                    }
                    case "boolean" -> {
                        boolean bit = plc.readBit(db, offset, bitNumber);
                        textValue.setText(String.valueOf(bit));
                    }
                    default -> {
                        // Tratar erro
                    }
                }
                plc.disconnect();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonWrite.addActionListener((ActionEvent e) -> {
            try {
                // Lógica de escrita
                int db = Integer.parseInt(textDb.getText());
                int offset = Integer.parseInt(textOffset.getText());
                int bitNumber = !textBitNumber.getText().equals("") ? Integer.parseInt(textBitNumber.getText()) : -1;
                int size = Integer.parseInt(textSize.getText());
                String type = (String) comboType.getSelectedItem();

                PlcConnector plc = new PlcConnector(textIp.getText().trim(), 102);
                plc.connect();

                switch (type.toLowerCase()) {
                    case "string" -> {
                        if (plc.writeString(db, offset, size, textValueWrite.getText().trim())) {
                            System.out.println("Escrita no CLP realizada com sucesso");
                        } else {
                            System.out.println("Erro de escrita no CLP");
                        }
                    }
                    case "block" -> {
                        if (plc.writeBlock(db, offset, size,
                                PlcConnector.hexStringToByteArray(textValueWrite.getText().trim()))) {
                            System.out.println("Escrita no CLP realizada com sucesso");
                        } else {
                            System.out.println("Erro de escrita no CLP");
                        }
                    }
                    case "float" -> {
                        if (plc.writeFloat(db, offset, Float.parseFloat(textValueWrite.getText().trim()))) {
                            System.out.println("Escrita no CLP realizada com sucesso");
                        } else {
                            System.out.println("Erro de escrita no CLP");
                        }
                    }
                    case "integer" -> {
                        if (plc.writeInt(db, offset, Integer.parseInt(textValueWrite.getText().trim()))) {
                            System.out.println("Escrita no CLP realizada com sucesso");
                        } else {
                            System.out.println("Erro de escrita no CLP");
                        }
                    }
                    case "byte" -> {
                        if (plc.writeByte(db, offset, Byte.parseByte(textValueWrite.getText().trim()))) {
                            System.out.println("Escrita no CLP realizada com sucesso");
                        } else {
                            System.out.println("Erro de escrita no CLP");
                        }
                    }
                    case "boolean" -> {
                        if (plc.writeBit(db, offset, bitNumber,
                                Boolean.parseBoolean(textValueWrite.getText().trim()))) {
                            System.out.println("Escrita no CLP realizada com sucesso");
                        } else {
                            System.out.println("Erro de escrita no CLP");
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    // -----End Constructor---------------

    private void updtPnlBlocks() {

        plcEstoque = new PlcConnector(textIp.getText().trim(), 102);

        try {
            plcEstoque.connect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            indxColorBlk = plcEstoque.readBlock(9, 68, 28);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // Criar matriz de JPanels 6x5 - 2
            int largura = 35;
            int altura = 35;
            int espaco = 10;

            pnlBlkEst.removeAll();
            for (int i = 0; i < 28; i++) {

                // System.out.println("indxColorBlk[" + i + "]= " + indxColorBlk[i]);
                JLabel label = new JLabel("P" + (i + 1), SwingConstants.CENTER);
                // Define o tamanho do JPanel
                label.setSize(largura, altura);
                // Adiciona uma borda para visualização
                label.setBorder(BorderFactory.createLineBorder(Color.BLUE));
                label.setForeground(Color.white);

                label.setOpaque(true);

                switch (indxColorBlk[i]) {
                    case 1 ->
                        label.setBackground(Color.BLACK); // Define a cor de fundo do JLabel
                    case 2 ->
                        label.setBackground(Color.RED); // Define a cor de fundo do JLabel
                    case 3 ->
                        label.setBackground(Color.BLUE); // Define a cor de fundo do JLabel
                    default ->
                        label.setBackground(Color.WHITE); // Define a cor de fundo do JLabel
                }
                // Calcula a posição do Jlabel
                int x = (i % 6) * (largura + espaco);
                int y = (i / 6) * (altura + espaco);
                label.setLocation(x + 10, y + 10);
                // Adiciona o JLabel ao Jlabel
                pnlBlkEst.add(label);
                pnlBlkEst.revalidate();
                pnlBlkEst.repaint();

            }

        });
    }

    private void updtPnlOper() {

        // System.out.println("AQUI em UpdateExpedicao");
        String[] netIp = textIp.getText().split("\\.");

        plcExpedicao = new PlcConnector(netIp[0] + "." + netIp[1] + "." + netIp[2] + "." + netIp[3], 102);

        // JOptionPane.showMessageDialog(null, netIp[0] + "." + netIp[1] + "." +
        // netIp[2] + ".40");
        try {
            plcExpedicao.connect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            indxPosExped = plcExpedicao.readBlock(9, 6, 24);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            int larguraExp = 80;
            int alturaExp = 40;
            int espacoExp = 10;

            pnlOpExp.removeAll();

            for (int c = 0; c < 24; c += 2) {
                byte[] op = { indxPosExped[c], indxPosExped[c + 1] };

                operacao[c / 2] = ByteBuffer.wrap(op, 0, 2).order(ByteOrder.BIG_ENDIAN).getShort();

                // System.out.println("indxPosExped[" + (c / 2) + "]=" + operacao[c / 2]);
            }

            for (int i = 0; i < 12; i++) {
                JPanel panel = new JPanel();
                panel.setSize(larguraExp, alturaExp); // Define o tamanho do JPanel
                // panel.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Adiciona uma
                // borda para visualização
                // panel.setOpaque(true);
                panel.setBackground(Color.black); // Define a cor de fundo do JPanel

                JLabel label = new JLabel();
                label.setBorder(BorderFactory.createLineBorder(Color.blue));

                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setSize(larguraExp, alturaExp); // Define o tamanho do JLabel
                // label.setBorder(gray); // Adiciona uma borda para visualização
                label.setBackground(Color.black);
                // label.setOpaque(true); // Necessário para definir a cor de fundo

                if (operacao[i] > 0) {
                    label.setText("P" + (i + 1) + "= [ OP" + String.format("%02d", operacao[i]) + " ]");
                    label.setBorder(BorderFactory.createLineBorder(Color.red));
                } else {
                    label.setText("P" + (i + 1) + "= [ ____ ]");
                    label.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                }

                // Calcula a posição do JPanel
                int x = (i % 4) * (larguraExp + espacoExp);
                int y = (i / 4) * (alturaExp + espacoExp);

                // Define a posição do JLabel
                label.setLocation(x + 15, y + 15);

                // Adiciona o JLabel ao JPanel
                pnlOpExp.add(label);

            }
            pnlOpExp.revalidate();
            pnlOpExp.repaint();
        });

    }

    private static String bytesToHex(byte[] bytes, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString().trim();
    }

    // Os próximos dois métodos construir somente a assinatura
    // Método estático para atualizar o textField correspondente com base no ID
    public static void processDataBlock(int id, byte[] bytes) {
        SwingUtilities.invokeLater(() -> {
            switch (id) {
                case 1 -> {
                    System.out.println("Retorno de dados ID 1");

                    infoDB9_Est = bytes;
                    opDB = "db9Est";

                    plcEstoque();

                }
                case 2 -> {
                    System.out.println("Retorno de dados ID 2");
                }
                case 3 -> {
                    System.out.println("Retorno de dados ID 3");
                }
                case 4 -> {
                    System.out.println("Retorno de dados ID 4");
                }
                case 5 -> {
                    System.out.println("Retorno de dados ID 5");
                }
                default ->
                    System.out.println("ID inválido: " + id);
            }

        });

    }

    public static void plcEstoque() {
        if (leitura) {
            if (opDB.equals("db9Est")) {

                recebidoOpEst = ((infoDB9_Est[0] & 0x01) == 1);

                iniciarPedido = ((infoDB9_Est[62] & 0x01) == 1);

                recebidoEstoque = ((infoDB9_Est[64] & 0x01) == 1);
                iniciarGuardar = ((infoDB9_Est[64] & 0x02) == 2);

                cancelOp = ((infoDB9_Est[98] & 0x01) == 1);
                finishOp = ((infoDB9_Est[98] & 0x02) == 2);
                startOp = ((infoDB9_Est[98] & 0x04) == 4);

                ocupadoEst = ((infoDB9_Est[100] & 0x01) == 1);
                aguardandoEst = ((infoDB9_Est[100] & 0x02) == 2);
                manualEst = ((infoDB9_Est[100] & 0x04) == 4);
                emergenciaEst = ((infoDB9_Est[100] & 0x08) == 8);

                pedirPosicaoEst = ((infoDB9_Est[102] & 0x01) == 1);

                adicionarEstoque = ((infoDB9_Est[106] & 0x01) == 1);
                removerEstoque = ((infoDB9_Est[106] & 0x02) == 2);
                retornoEstoqueCheio = ((infoDB9_Est[106] & 0x04) == 4);

                posicaoEstoque = ((infoDB9_Est[104] & 0xFF) << 8) | (infoDB9_Est[105] & 0xFF);
                corGuardarEstoque = ((infoDB9_Est[108] & 0xFF) << 8) | (infoDB9_Est[109] & 0xFF);
                posicaoGuardarEstoque = ((infoDB9_Est[66] & 0xFF) << 8) | (infoDB9_Est[67] & 0xFF);

                PlcConnector plcWriteEst = new PlcConnector(textIp.getText(), 102);

                // Se o pedido foi iniciado e a estação ESTOQUE informou que iniciou a operação
                // ficando no estado OCUPADO
                // então a flag iniciarPedido fica em FALSE
                if (iniciarPedido == true & ocupadoEst == true) {

                    try {

                        plcWriteEst.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE")); // IniciarPedido

                    } catch (Exception e) {
                        System.out.println(
                                "ERRO [iniciarPedido == true & ocupadoEst == true]: Atualização da Flag IniciarPedido [DB9:62.0] para FALSE");
                    }

                }

                // Se as três flags (StartOP, FinishOP e CancelOP) estão em FALSE, então a flag
                // RecebidoOP fica em FALSE
                if (startOp == false & finishOp == false & cancelOp == false) {

                    System.out.println("Flag [startOp] [finishOp] [cancelOp]: RecebidoOPEstoque_FALSE");
                    try {
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 0, 1, 0, 1);
                        plcWriteEst.writeBit(9, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPEst

                    } catch (Exception e) {
                        System.out.println("ERRO: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para FALSE");
                    }

                }

                // Se a estação ESTOQUE sinalizou o início da operação e ficou OCUPADO, então a
                // flag RecebidoOP fica em TRUE
                if (startOp == true & recebidoOpEst == false) {

                    System.out.println("Flag: RecebidoOPEstoque_TRUE");
                    try {
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 0, 1, 0, 1);
                        plcWriteEst.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // RecebidoOPEst

                    } catch (Exception e) {
                        System.out.println(
                                "ERRO [startOp]: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para TRUE");
                    }

                }

                // Se a estação ESTOQUE sinalizou o témino da operação e ficou OCUPADO, então a
                // flag RecebidoOP fica em TRUE
                if (finishOp == true & recebidoOpEst == false) {

                    System.out.println("Flag: RecebidoOPEstoque_TRUE");
                    try {
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 0, 1, 0, 1);
                        plcWriteEst.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // RecebidoOPEst

                    } catch (Exception e) {
                        System.out.println(
                                "ERRO [finishOp]: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para TRUE");
                    }

                }
                if (removerEstoque == false & adicionarEstoque == false) {

                    System.out.println("Flag: RecebidoEstoque_FALSE");

                    try {
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                        plcWriteEst.writeBit(9, 64, 0, Boolean.parseBoolean("FALSE")); // RecebidoEstoque

                    } catch (Exception e) {
                        System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para FALSE");
                    }

                }
                if ((posicaoEstoque > 0) & removerEstoque == true) {

                    System.out.println("Flag: RecebidoEstoque_TRUE - REMOVER ESTOQUE");

                    // Coloca a flag RecebidoEstoque em TRUE
                    try {
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                        plcWriteEst.writeBit(9, 64, 0, Boolean.parseBoolean("TRUE")); // RecebidoEstoque

                    } catch (Exception e) {
                        System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para TRUE");
                    }

                    System.out.println("Removendo da posição: " + posicaoEstoque);

                }
                if ((posicaoEstoque > 0) & adicionarEstoque == true) {

                    System.out.println("Flag: RecebidoEstoque_TRUE - ADICIONAR ESTOQUE");

                    // Coloca a flag RecebidoEstoque em TRUE
                    try {
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                        plcWriteEst.writeBit(9, 64, 0, Boolean.parseBoolean("TRUE")); // RecebidoEstoque

                    } catch (Exception e) {
                        System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para TRUE");
                    }

                }
                if ((ocupadoEst == true | retornoEstoqueCheio == true) & iniciarGuardar == true) {

                    System.out.println("Flag: IniciarGuardar_FALSE");

                    // Coloca a flag IniciarGuardar em FALSE
                    try {

                        // Coloca a flag IniciarGuardar em TRUE
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                        plcWriteEst.writeBit(9, 64, 1, Boolean.parseBoolean("FALSE"));

                    } catch (Exception e) {
                        System.out.println(
                                "ERRO: Atualização da Flag IniciarGuardarEstoque [DB9:64.1] para FALSE");
                    }

                }
                if (pedirPosicaoEst & !ocupadoEst) { // Verifica se Estoque pede posição para guardar
                    System.out.println("ESTOU AQUI- (pedirPosicaoEst == true) & ocupadoEst == false");

                    // Rotina para verificar qual posição está disponível para guardar
                    // Solicita posição disponível para guardar (0-LIVRE 1-PRETO 2-VERMELHO 3-AZUL)
                    // Certifique-se de que posEstoqueRequest é seguro para acesso
                    System.out.println("Posição disponível no Magazine Estoque: " + "posEstoqueRequest");

                    try {

                        // Atualiza a variável PosicaoGuardar no CLP ESTOQUE
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 66, 2, 0, 1);
                    } catch (Exception e) {
                        System.out.println("ERRO: Atualização da PosicaoGuardarEstoque [DB9:66]");
                    }

                    try {

                        // Coloca a flag IniciarGuardar em TRUE
                        // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                        plcWriteEst.writeBit(9, 64, 1, Boolean.parseBoolean("TRUE"));

                    } catch (Exception e) {
                        System.out.println("ERRO: Atualização da Flag IniciarGuardarEstoque [DB9:64.1]");
                    }

                }
            }

        }

    }

    // ------------------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            S7AppSwing app = new S7AppSwing();
            app.setVisible(true);

        });
    }
}
