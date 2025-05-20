package com.smart40;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class MainFrame extends JFrame {

    /**********************************************/

    public static int stationWorkId = 0;
    public static boolean readOnly = true;
    public static boolean blockFinished = false;

    /* --------- DATA BLOCKS ESTOQUE ----------- */
    // public static PlcConnector plcReadDB_ESTOQUE;
    private static PlcReaderWorker plcEstoqueDB6;
    private static PlcReaderWorker plcEstoqueDB9;

    /* --------- DATA BLOCKS PROCESSO ----------- */
    // public static PlcConnector plcReadDB_PROCESSO;
    private static PlcReaderWorker plcProcessoDB2;
    private static PlcReaderWorker plcProcessoDB6;

    /* --------- DATA BLOCKS MONTAGEM ----------- */
    // public static PlcConnector plcReadDB_MONTAGEM;
    private static PlcReaderWorker plcMontagemDB30; // Supervisório_Estoque
    private static PlcReaderWorker plcMontagemDB60; // Supervisório_Expedição
    private static PlcReaderWorker plcMontagemDB92; // Supervisório_Montagem
    private static PlcReaderWorker plcMontagemDB600; // Supervisório_Processo

    private static PlcReaderWorker plcMontagemDB57; // Node -> PLC

    // private static PlcReaderWorker plcMontagemDB70; // Stoppers e RFID
    // private static PlcReaderWorker plcMontagemDB76; // Esteiras
    // private static PlcReaderWorker plcMontagemDB77; // Alimentador
    // private static PlcReaderWorker plcMontagemDB78; // Inicialização
    // private static PlcReaderWorker plcMontagemDB81; // Fluxo_Robo
    // private static PlcReaderWorker plcMontagemDB90; // Parâmetros_Processo

    /* --------- DATA BLOCKS EXPEDIÇÃO ----------- */
    private static PlcReaderWorker plcExpedicaoDB9;
    private static PlcReaderWorker plcExpedicaoDB20;

    /**********************************************/

    public static boolean connectPlcs = false;
    public static boolean connectedPlcs = false;

    private static int seqSmart = 0;

    public static boolean difEstoque = false;
    public static boolean difProcesso = false;
    public static boolean difMontagem = false;
    public static boolean difExpedicao = false;

    public static boolean pedidoEmCurso = false;

    private static String oper_bool_est = "";
    private static String oper_bool_pro = "";
    private static String oper_bool_mon = "";
    private static String oper_bool_exp = "";


    public static ArrayList<String> logSmart = new ArrayList<>();

    /********************************************* */
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

    public static boolean[] boolEstoqueOld = new boolean[15];
    public static boolean[] boolEstoqueCur = new boolean[15];

    public static boolean[] boolProcessoOld = new boolean[15];
    public static boolean[] boolProcessoCur = new boolean[15];

    public static boolean[] boolMontagemOld = new boolean[11];
    public static boolean[] boolMontagemCur = new boolean[11];

    public static boolean[] boolExpedicaoOld = new boolean[22];
    public static boolean[] boolExpedicaoCur = new boolean[22];
    public static byte[] blockArray = new byte[28]; // Vetor de bytes de tamanho 30 (5x6 = 30)
    public static int[] posExpedArray = new int[13];

    // ----------------------------------------------//
    // Variáveis Booleanas PROCESSO
    // ----------------------------------------------//
    public static boolean recebidoOpPro; // DB2 0.0

    // PLC to NODE
    // --- Status Op Processo------------------------//
    public static boolean cancelOpPro; // DB2 4.0
    public static boolean finishOpPro; // DB2 4.1
    public static boolean startOpPro; // DB2 4.2

    // --- Status Estação Estoque -------------------//
    public static boolean ocupadoPro; // DB2 6.0
    public static boolean aguardandoPro; // DB2 6.1
    public static boolean manualPro; // DB2 6.2
    public static boolean emergenciaPro; // DB2 6.3

    // ----------------------------------------------//
    // Variáveis Booleanas MONTAGEM
    // ----------------------------------------------//
    public static boolean recebidoOpMon; // DB57 0.0

    // PLC to NODE
    // --- Status Op Montagem------------------------//
    public static boolean cancelOpMon; // DB57 4.0
    public static boolean finishOpMon; // DB57 4.1
    public static boolean startOpMon; // DB57 4.2

    // --- Status Estação Estoque -------------------//
    public static boolean ocupadoMon; // DB57 6.0
    public static boolean aguardandoMon; // DB57 6.1
    public static boolean manualMon; // DB57 6.2
    public static boolean emergenciaMon; // DB57 6.3

    // ----------------------------------------------//
    // Variáveis Booleanas EXPEDIÇÃO
    // ----------------------------------------------//
    public static boolean recebidoOpExp; // DB9 0.0

    // --- Gerenciamento Estoque --------------------//
    public static boolean recebidoExpedicao; // DB9 2.0
    public static boolean iniciarGuardarExp; // DB9 2.1

    public static int[] operacao = new int[12];

    public static boolean aux_expedicao = false; // Aux Expedição

    // PLC to NODE
    // --- Status Op Expedição------------------------//
    public static boolean cancelOpExp; // DB9 32.0
    public static boolean finishOpExp; // DB9 32.1
    public static boolean startOpExp; // DB9 32.2

    // --- Status Estação Expedição -------------------//
    public static boolean ocupadoExp; // DB9 34.0
    public static boolean aguardandoExp; // DB9 34.1
    public static boolean manualExp; // DB9 34.2
    public static boolean emergenciaExp; // DB9 34.3

    // --- Gerenciamento Expedição --------------------//

    public static boolean pedirPosicaoExp; // DB9 36.0
    public static boolean adicionarExpedicao; // DB9 42.0
    public static boolean removerExpedicao; // DB9 42.1
    public static int posicaoGuardarExpedicao; // DB9 4

    public static int posicaoGuardadoExpedicao; // DB9 38
    public static int posicaoRemovidoExpedicao; // DB9 40
    public static int opGuardadoExpedicao; // DB9 44

    /********************************************* */
    // Variáveis para uso com THREADS
    private static volatile int posEstoqueRequest;
    private static volatile int posExpedicaoRequest;

    /********************************************* */
    public static byte[] infoDB6_Est; // Supervisório Estação ESTOQUE
    public static byte[] infoDB9_Est;

    public static byte[] infoDB2_Pro;
    public static byte[] infoDB6_Pro; // Supervisório Estação PROCESSO

    public static byte[] infoDB57_Mon;
    public static byte[] infoDB30_Mon; // Supervisório Estação ESTOQUE
    public static byte[] infoDB60_Mon; // Supervisório Estação EXPEDIÇÃO
    public static byte[] infoDB92_Mon; // Supervisório Estação MONTAGEM
    public static byte[] infoDB600_Mon; // Supervisório Estação PROCESSO

    public static byte[] infoDB9_Exp;
    public static byte[] infoDB20_Exp; // Supervisório Estação EXPEDIÇÃO

    public static PanelBanner panelBanner;
    public static PanelStatus panelStatus;

    public static Panel1 panel1;
    public static Panel2 panel2;
    public static Panel3 panel3;
    public static ConfigPedido panel4;
    public static PanelStart startPanel;
    public static int xForm = 1;
    public static int yForm = 0;

    public static String ipEstoque;
    public static String ipProcesso;
    public static String ipMontagem;
    public static String ipExpedicao;

    public static String opDB = "";

    public static JPanel pnlConfigPanels;
    public static CardLayout cardLayout;
    public static JPanel painelPrincipal;

    // private static String[] netIp;

    /*----- Variáveis de definição de Fontes Cores e Bordas */

    private static Color greenOn = new Color(10, 255, 50);
    private static Color greenOff = new Color(10, 50, 50);

    private static Border yellow = BorderFactory.createLineBorder(Color.yellow, 1);
    private static Border onGreenBorder = BorderFactory.createLineBorder(greenOn, 2);
    private static Border offGreenBorder = BorderFactory.createLineBorder(greenOff, 2);

    private static Color corBackPnl = new Color(10, 50, 50);

    static Color corJTFfont = new Color(50, 255, 255);

    private static Color corBackPnlOn = new Color(10, 120, 50);

    static Border gray = BorderFactory.createLineBorder(Color.WHITE);

    public MainFrame() {
        // Configurações do JFrame
        setTitle("SISTEMA DE CONTROLE E SUPERVISÃO BANCADA SMART 4.0 - SENAI TIMBÓ");
        setSize(1400, 855);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Define layout para o JFrame
        setLayout(null);

        cardLayout = new CardLayout();

        painelPrincipal = new JPanel(cardLayout);
        painelPrincipal.setBounds(5, 10, 1375, 965);

        panelBanner = new PanelBanner(this, 690, 100);
        panelBanner.setLocation(0, 10);
        panelBanner.setVisible(true);

        panelStatus = new PanelStatus(this, 685, 100);
        panelStatus.setLocation(690, 10);
        panelStatus.setVisible(true);

        panel4 = new ConfigPedido(685, 685);
        panel4.setLocation(0, 115);
        panel4.setVisible(true);

        panel3 = new Panel3(this, 685, 685);
        panel3.setLocation(690, 115);
        panel3.setVisible(true);

        panel2 = new Panel2(this, 685, 685);
        panel2.setLocation(1395, 115);
        panel2.setVisible(true);

        panel1 = new Panel1(this, 685, 685);
        panel1.setLocation(1395, 115);
        panel1.setVisible(true);

        startPanel = new PanelStart(this, 1375, 965);
        startPanel.setLocation(0, 10);
        startPanel.setVisible(true);

        pnlConfigPanels = new JPanel();
        pnlConfigPanels.setBounds(0, 10, 1375, 965);
        pnlConfigPanels.setLayout(null);
        pnlConfigPanels.setVisible(true);

        pnlConfigPanels.add(panelBanner); // Adiciona o Panel1
        pnlConfigPanels.add(panelStatus); // Adiciona o Panel1
        pnlConfigPanels.add(panel1); // Adiciona o Panel1
        pnlConfigPanels.add(panel2); // Adiciona o Panel2
        pnlConfigPanels.add(panel3); // Adiciona o Panel3
        pnlConfigPanels.add(panel4); // Adiciona o Panel4

        painelPrincipal.add(pnlConfigPanels, "pnlConfigPanels");
        painelPrincipal.add(startPanel, "startPanel");

        operacao = new int[12];

        // Inicializa vetores booleanos do Estoque
        for (int c = 0; c < 15; c++) {
            boolEstoqueOld[0] = false;
            boolEstoqueCur[0] = false;
        }
        if (readOnly == true) {
            // lblStatEstoq.setBackground(corBackPnlOn);
            Panel2.chkReadOnly.setSelected(true);
        } else {
            Panel2.chkReadOnly.setSelected(false);
            loadMagazineExpedicao();
            updateMagazineExpedicao();
        }
        Panel1.btnS7PLCRead.addActionListener((ActionEvent e) -> {
            int startAddress = 0x00;
            int bitNum = 0;

            if (Panel1.textOffsetPLC.getText().indexOf(".") > 0) {

                startAddress = Integer.parseInt(Panel1.textOffsetPLC.getText().substring(0,
                        Panel1.textOffsetPLC.getText().indexOf(".")));
                bitNum = Integer.parseInt(Panel1.textOffsetPLC.getText().substring(
                        Panel1.textOffsetPLC.getText().indexOf(".") + 1,
                        Panel1.textOffsetPLC.getText().indexOf(".") + 2));
            } else {
                startAddress = Integer.parseInt(Panel1.textOffsetPLC.getText());
                bitNum = 0;
            }

            /* SUBSTITUIR - SIMPLIFICAR */
            PlcConnector plcRead = new PlcConnector(Panel1.textHostPLC.getText(), 102);

            try {
                plcRead.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            if (Panel1.textTypeTag.getText().equals("S")) {
                byte[] tagValue = new byte[20];
                // tagValue = plcRead.readBlock();
                byte[] strByte = new byte[tagValue[1] + 1];

                // readString(int db, int startAddress, int length)
                String tag = "";
                try {
                    tag = plcRead.readString(Integer.parseInt(Panel1.textDBPLC.getText()), startAddress,
                            Integer.parseInt(Panel1.textSizeTag.getText()));
                } catch (Exception ex) {
                }

                System.out.println("\n\nTAG em Bytes: " + bytesToHex(tagValue));

                // System.out.println("\n\nTAG em Bytes: " + tag);

                for (int i = 0; i < (int) tagValue[1] + 1; i++) {
                    strByte[i] = tagValue[i + 2];

                }
                strByte[tagValue[1]] = 0x00;

                for (int c = 0; c < tagValue.length; c++) {
                    System.out.print(tagValue[c]);
                }

                Panel1.txtReadPLCTag.setText(tag.trim());

            } else if (Panel1.textTypeTag.getText().equals("I")) {

                // byte[] value = new byte[6];
                // value = plcRead.readDataBlock();

                int valueTag = 0; // ((value[0] & 0xFF) << 8) | (value[1] & 0xFF);

                try {
                    valueTag = plcRead.readInt(Integer.parseInt(Panel1.textDBPLC.getText()), startAddress);
                    System.out.println("\n\nTAG em Bytes: " + valueTag);
                } catch (Exception ex) {
                }

                Panel1.txtReadPLCTag.setText(String.valueOf(valueTag));

            } else if (Panel1.textTypeTag.getText().equals("F")) {
                float valueTag = 0.0f; // ByteBuffer.wrap(plcRead.readDataBlock()).getFloat();
                try {
                    valueTag = plcRead.readFloat(Integer.parseInt(Panel1.textDBPLC.getText()), startAddress);
                    System.out.println("\n\nTAG em Bytes: " + valueTag);
                } catch (Exception ex) {
                }

                Panel1.txtReadPLCTag.setText(String.valueOf(valueTag));

            } else if (Panel1.textTypeTag.getText().equals("X")) {
                // byte valueTag = (byte) ByteBuffer.wrap(plcRead.readDataBlock()).getChar();
                // System.out.println("AQUI_1");
                if (Panel1.textOffsetPLC.getText().indexOf(".") > 0) {
                    // System.out.println("AQUI_2");
                    // int add = startAddress<<3 | bitNum;
                    // System.out.println(startAddress << 3 | bitNum);

                    boolean valueTag;
                    try {
                        valueTag = plcRead.readBit(Integer.parseInt(Panel1.textDBPLC.getText()), startAddress, bitNum);
                        // txtReadPLCTag.setText(String.valueOf(valueTag).toUpperCase());
                        if (valueTag) {
                            Panel1.chkBxVar.setSelected(true);
                            Panel1.chkBxVar.setText("TRUE");
                        } else {
                            Panel1.chkBxVar.setSelected(false);
                            Panel1.chkBxVar.setText("FALSE");
                        }
                    } catch (Exception ex) {
                    }

                } else {
                    byte valueTag;
                    try {
                        valueTag = plcRead.readByte(Integer.parseInt(Panel1.textDBPLC.getText()), startAddress);
                        Panel1.txtReadPLCTag.setText(String.valueOf(valueTag));

                    } catch (Exception ex) {
                    }

                    // for (int i = 0; i < Integer.parseInt(Panel1.textSizeTag.getText()); i++) {
                    // System.out.print(valueTag);
                    //
                    // }
                }

            } else if (Panel1.textTypeTag.getText().equals("B")) {
                // int bitOffset = 0;
                // Boolean valueTag = (plcRead.readDataBlock(/*
                // * Integer.parseInt(form.textDBPLC.getText()),
                // * Integer.parseInt(form.textOffsetPLC.getText()),
                // * Integer.parseInt(form.textSizeTag.getText())
                // */)[0] & (1 << bitOffset)) != 0;
                // Panel1.txtReadPLCTag.setText(String.valueOf(valueTag));

            }

            try {
                plcRead.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
        });

        Panel2.chkReadCiclic.addActionListener((ActionEvent e) -> {
            // System.out.println(ipProcesso);

            leituraCiclica();
        });

        // add(startPanel);
        // add(pnlConfigPanels);

        // Exibe a janela
        add(painelPrincipal);

        cardLayout.show(painelPrincipal, "startPanel");
        // cardLayout.show(painelPrincipal, "pnlConfigPanels");
        setVisible(true);
    }

    // Método para converter bytes em hexadecimal
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    public static void leituraCiclica() {

        PlcConnector plcRead = null;
        PlcConnector plcReadDB6_ESTOQUE = null;
        PlcConnector plcReadDB9_ESTOQUE = null;

        PlcConnector plcReadDB2_PROCESSO = null;
        PlcConnector plcReadDB6_PROCESSO = null;

        PlcConnector plcReadDB30_MONTAGEM = null;
        PlcConnector plcReadDB60_MONTAGEM = null;
        PlcConnector plcReadDB92_MONTAGEM = null;
        PlcConnector plcReadDB57_MONTAGEM = null;
        PlcConnector plcReadDB600_MONTAGEM = null;

        PlcConnector plcReadDB9_EXPEDICAO = null;
        PlcConnector plcReadDB20_EXPEDICAO = null;

        /*
         * PlcConnector plc_Estoque = null;
         * PlcConnector plc_Processo = null;
         * PlcConnector plc_Montagem = null;
         * PlcConnector plc_Expedicao = null;
         * plcEstoque = new PlcConnector(ipEstoque, 102);
         * plcProcesso = new PlcConnector(ipProcesso, 102);
         * plcMontagem = new PlcConnector(ipMontagem, 102);
         * plcExpedicao = new PlcConnector(ipExpedicao, 102);
         */

        plcRead = new PlcConnector(Panel1.textHostPLC.getText(), 102);

        plcReadDB6_ESTOQUE = new PlcConnector(ipEstoque, 102);
        plcReadDB9_ESTOQUE = new PlcConnector(ipEstoque, 102);

        plcReadDB2_PROCESSO = new PlcConnector(ipProcesso, 102);
        plcReadDB6_PROCESSO = new PlcConnector(ipProcesso, 102);

        plcReadDB30_MONTAGEM = new PlcConnector(ipMontagem, 102);
        plcReadDB60_MONTAGEM = new PlcConnector(ipMontagem, 102);
        plcReadDB92_MONTAGEM = new PlcConnector(ipMontagem, 102);
        plcReadDB57_MONTAGEM = new PlcConnector(ipMontagem, 102);
        plcReadDB600_MONTAGEM = new PlcConnector(ipMontagem, 102);

        plcReadDB9_EXPEDICAO = new PlcConnector(ipExpedicao, 102);
        plcReadDB20_EXPEDICAO = new PlcConnector(ipExpedicao, 102);

        if (connectPlcs & !connectedPlcs) {
            // System.out.println("OS PLCS DEVEM SER CONECTADOS.");
            /*
             * try {
             * plcEstoque.connect();
             * } catch (Exception e1) {
             * 
             * e1.printStackTrace();
             * }
             */

            try {
                plcRead.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            try {
                plcReadDB6_ESTOQUE.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            try {
                plcReadDB9_ESTOQUE.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            // */

            // ---------------------------------------------------
            /*
             * try {
             * plcProcesso.connect();
             * } catch (Exception e1) {
             * 
             * e1.printStackTrace();
             * }
             * /
             */
            try {
                plcReadDB2_PROCESSO.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            try {
                plcReadDB6_PROCESSO.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            // */
            // -------------------------------------------------------
            /*
             * try {
             * plcMontagem.connect();
             * } catch (Exception e1) {
             * e1.printStackTrace();
             * }
             * /
             */
            try {
                plcReadDB30_MONTAGEM.connect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                plcReadDB60_MONTAGEM.connect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                plcReadDB92_MONTAGEM.connect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                plcReadDB57_MONTAGEM.connect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                plcReadDB600_MONTAGEM.connect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            // */
            // -----------------------------------------------------------------
            /*
             * try {
             * plcExpedicao.connect();
             * } catch (Exception e1) {
             * 
             * e1.printStackTrace();
             * }
             */
            try {
                plcReadDB9_EXPEDICAO.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            try {
                plcReadDB20_EXPEDICAO.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            // */
            connectedPlcs = true;

        } else if (connectPlcs & connectedPlcs) {
            // System.out.println("OS PLCS DEVEM SER DESCONECTADOS.");
            connectPlcs = false;
            connectedPlcs = false;

            // --------------- ESTOQUE ------------------------------------
            /*
             * try {
             * plcEstoque.disconnect();
             * } catch (Exception e1) {
             * 
             * e1.printStackTrace();
             * }
             * /
             */
            try {
                plcReadDB6_ESTOQUE.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            try {
                plcReadDB9_ESTOQUE.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            // */
            // ---------------- PROCESSO ------------------------------------
            /*
             * try {
             * plcProcesso.disconnect();
             * } catch (Exception e1) {
             * 
             * e1.printStackTrace();
             * }
             * //
             */
            try {
                plcReadDB2_PROCESSO.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            try {
                plcReadDB6_PROCESSO.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
            // */
            // ---------------- MONTAGEM ------------------------------------

            /*
             * try {
             * plcMontagem.disconnect();
             * } catch (Exception e1) {
             * e1.printStackTrace();
             * }
             * /
             */
            try {
                plcReadDB30_MONTAGEM.disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                plcReadDB60_MONTAGEM.disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                plcReadDB92_MONTAGEM.disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                plcReadDB57_MONTAGEM.disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                plcReadDB600_MONTAGEM.disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            // */

            // ---------------- EXPEDIÇÃO ------------------------------------
            /*
             * /
             * try {
             * plcExpedicao.disconnect();
             * } catch (Exception e1) {
             * e1.printStackTrace();
             * }
             * /
             */
            try {
                plcReadDB9_EXPEDICAO.disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                plcReadDB20_EXPEDICAO.disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            // */
            connectedPlcs = false;
        }

        if (Panel2.chkReadCiclic.isSelected()) {

            Panel2.chkReadCiclic.setText("PARAR LEITURA");

            // LEITURA DE DADOS DATA BLOCKS - ESTAÇÃO ESTOQUE

            plcEstoqueDB6 = new PlcReaderWorker(plcReadDB6_ESTOQUE, 3, 6, 0, 60); //

            plcEstoqueDB9 = new PlcReaderWorker(plcReadDB9_ESTOQUE, 4, 9, 0, 111); //

            plcEstoqueDB6.execute();
            plcEstoqueDB9.execute();

            // LEITURA DE DADOS DATA BLOCKS - ESTAÇÃO PROCESSO

            plcProcessoDB2 = new PlcReaderWorker(plcReadDB2_PROCESSO, 5, 2, 0, 9); //

            plcProcessoDB6 = new PlcReaderWorker(plcReadDB6_PROCESSO, 6, 6, 0, 31);
            // //
            // plcConnector, int id)
            plcProcessoDB2.execute();
            plcProcessoDB6.execute();

            // ************************************************************* */

            // LEITURA DE DADOS DATA BLOCKS - ESTAÇÃO MONTAGEM

            plcMontagemDB57 = new PlcReaderWorker(plcReadDB57_MONTAGEM, 7, 57, 0, 9); //

            plcMontagemDB30 = new PlcReaderWorker(plcReadDB30_MONTAGEM, 8, 30, 0, 23); //

            plcMontagemDB60 = new PlcReaderWorker(plcReadDB60_MONTAGEM, 9, 60, 0, 37); //

            plcMontagemDB92 = new PlcReaderWorker(plcReadDB92_MONTAGEM, 10, 92, 0, 23); //

            plcMontagemDB600 = new PlcReaderWorker(plcReadDB600_MONTAGEM, 11, 600, 0, 31); //

            // .execute();
            plcMontagemDB30.execute();
            plcMontagemDB60.execute();
            plcMontagemDB92.execute();
            plcMontagemDB600.execute();
            plcMontagemDB57.execute();

            // ************************************************************* */

            // LEITURA DE DADOS DATA BLOCKS - ESTAÇÃO EXPEDIÇÃO

            plcExpedicaoDB9 = new PlcReaderWorker(plcReadDB9_EXPEDICAO, 12, 9, 0, 48); //

            plcExpedicaoDB20 = new PlcReaderWorker(plcReadDB20_EXPEDICAO, 13, 20, 0, 38); //

            plcExpedicaoDB9.execute();
            plcExpedicaoDB20.execute();

        } else {
            Panel2.chkReadCiclic.setText("INICIAR LEITURA");

            plcEstoqueDB6.cancel(true);
            plcEstoqueDB9.cancel(true);

            plcProcessoDB2.cancel(true);
            plcProcessoDB6.cancel(true);

            plcMontagemDB57.cancel(true);
            plcMontagemDB30.cancel(true);
            plcMontagemDB60.cancel(true);
            plcMontagemDB92.cancel(true);
            plcMontagemDB600.cancel(true);

            plcExpedicaoDB9.cancel(true);
            plcExpedicaoDB20.cancel(true);

        }
    }

    // Método para alternar a visibilidade dos painéis
    public void setPanels(boolean b1, boolean p1, boolean p2, boolean p3, boolean p4) {
        remove(startPanel); // Remove o painel inicial

        add(pnlConfigPanels);

        panelBanner.setVisible(b1);
        panelStatus.setVisible(b1);
        panel1.setVisible(p1);
        panel2.setVisible(p2);
        panel3.setVisible(p3);
        panel4.setVisible(p4);

        revalidate();
        repaint();
    }

    public static void updPanels() {
        switch (xForm) {
            case 1 -> {
                panel4.setLocation(0, 115);
                panel3.setLocation(690, 115);
                panel2.setLocation(1385, 115);
                panel1.setLocation(2075, 115);
            }
            case 2 -> {
                panel3.setLocation(0, 115);
                panel2.setLocation(690, 115);
                panel1.setLocation(1395, 115);
                panel4.setLocation(2085, 115);
            }
            case 3 -> {
                panel2.setLocation(0, 115);
                panel1.setLocation(690, 115);
                panel4.setLocation(1395, 115);
                panel3.setLocation(2085, 115);
            }
            default -> {
            }
        }
        pnlConfigPanels.revalidate();
        pnlConfigPanels.repaint();

    }

    // Método estático para atualizar o textField correspondente com base no ID
    public static void processDataBlock(int id, byte[] bytes) {
        SwingUtilities.invokeLater(() -> {

            switch (id) {
                case 1 -> { // processa a leitura do Atuador rotativo da estação Estoque
                    // textField1.setText(text);
    
                    float valueGiro = ByteBuffer.wrap(bytes).getFloat();
                    Panel1.txtReadPLCGiro.setText(String.valueOf(valueGiro));
                    // System.out.println("Valor do Atuador de Giro: " + valueGiro);
                }
                case 2 -> { // processa a leitura do Atuador linear da estação Estoque
                    // textField2.setText(text);
    
                    float valueLinear = ByteBuffer.wrap(bytes).getFloat();
                    Panel1.txtReadPLCLinear.setText(String.valueOf(valueLinear));
                    // System.out.println("Valor do Atuador Linear: " + valueLinear);
                }
    
                case 3 -> { // processa a leitura da DB6 da estação Estoque
    
                    // System.out.println("Retorno DB=6, OFFSET=16, SIZE=14");
                    infoDB6_Est = bytes;
                    opDB = "DB6_Est";
    
                    for (int c = 0; c < infoDB6_Est.length; c++) {
    
                        // System.out.println("infoDB6[" + c + "] = " + Panel1.infoDB6_Est[c]);
                    }
    
    
                    plcEstoque();
                }
                case 4 -> { // processa a leitura da DB9 da estação Estoque
                    // Informações do pedido;
                    // System.out.println("Aqui em DB9_ESTOQUE");
                    infoDB9_Est = bytes;
    
                    opDB = "DB9_Est";
                    plcEstoque();
                }
                case 5 -> { // processa a leitura da DB2 da estação Processo
                    // Informações do Processo;
                    infoDB2_Pro = bytes;
                    opDB = "DB2_Pro";
    
                    // for (int c = 0; c < infoDB2_Pro.length; c++) {
                    //
                    // System.out.println("infoDB2[" + c + "] = " + infoDB2_Pro[c]);
                    // }
                    plcProcesso();
                }
                case 6 -> { // processa a leitura da DB6 da estação Processo
                    // Informações do Processo;
                    infoDB6_Pro = bytes;
                    opDB = "DB6_Pro";
                    plcProcesso();
                }
                case 7 -> { // processa a leitura da DB57 da estação Montagem
                    // Informações da Montagem; DB57
    
                    infoDB57_Mon = bytes;
                    opDB = "DB57_Mon";
    
                    // for (int c = 0; c < infoDB2_Pro.length; c++) {
                    //
                    // System.out.println("infoDB2[" + c + "] = " + infoDB2_Pro[c]);
                    // }
                    plcMontagem();
                }
                case 8 -> { // processa a leitura da DB30 da estação Montagem
                    // Informações da Montagem - SUPERVISÓRIO ESTOQUE
                    infoDB30_Mon = bytes;
                    opDB = "DB30_Mon";
                    plcMontagem();
                }
                case 9 -> { // processa a leitura da DB60 da estação Montagem
                    // Informações da Montagem - SUPERVISÓRIO EXPEDIÇÃO
                    infoDB60_Mon = bytes;
                    opDB = "DB60_Mon";
                    plcMontagem();
                }
    
                case 10 -> { // processa a leitura da DB92 da estação Montagem
                    // Informações da Montagem - SUPERVISÓRIO MONTAGEM
                    infoDB92_Mon = bytes;
                    opDB = "DB92_Mon";
                    plcMontagem();
                }
    
                case 11 -> { // processa a leitura da DB5600 da estação Montagem
                    // Informações da Montagem - SUPERVISÓRIO PROCESSO
                    infoDB600_Mon = bytes;
                    opDB = "DB600_Mon";
                    plcMontagem();
                }
    
                case 12 -> { // processa a leitura da DB9 da estação Expedição
                    // Informações da Montagem - NODE-RED PROCESSO
                    infoDB9_Exp = bytes;
                    opDB = "DB9_Exp";
                    plcExpedicao();
                }
    
                case 13 -> { // processa a leitura da DB20 da estação Expedição
                    // Informações da Montagem - SUPERVISÓRIO PROCESSO
                    infoDB20_Exp = bytes;
                    opDB = "DB20_Exp";
                    plcExpedicao();
                }
                default -> System.out.println("ID inválido: " + id);
    
            }

        });
    }

    public static void updateVarSmart(/* byte[] cor */) {

        SwingUtilities.invokeLater(() -> {

            int largura = 36;
            int altura = 36;
            int espaco = 10;

            byte[] vlLinear = new byte[4];
            vlLinear[0] = infoDB6_Est[6];
            vlLinear[1] = infoDB6_Est[7];
            vlLinear[2] = infoDB6_Est[8];
            vlLinear[3] = infoDB6_Est[9];

            byte[] vlGiro = new byte[4];
            vlGiro[0] = infoDB6_Est[10];
            vlGiro[1] = infoDB6_Est[11];
            vlGiro[2] = infoDB6_Est[12];
            vlGiro[3] = infoDB6_Est[13];

            float valueLinear = ByteBuffer.wrap(vlLinear).getFloat();
            Panel1.txtReadPLCLinear.setText(String.valueOf(valueLinear));

            float valueGiro = ByteBuffer.wrap(vlGiro).getFloat();
            Panel1.txtReadPLCGiro.setText(String.valueOf(valueGiro));

            Panel1.pnlPLCBlock.removeAll();
            for (int c = 0; c < 28; c++) {

                Panel1.indexColor[c] = infoDB6_Est[c + 32]; // gerarValoresCores();

            }
            PanelStart.updateEstoque();
            Panel1.updateEstoque();


        });

    }

    public static void plcEstoque() {

        if (Panel2.chkReadCiclic.isSelected()) {

            if (opDB.equals("DB6_Est")) {

                /*------ Leitura do Status da Bancada------------ */

                byte[] strStatusEst = new byte[infoDB6_Est[16]];

                for (int c = 0; c < infoDB6_Est[17]; c++) {
                    strStatusEst[c] = infoDB6_Est[c + 18];

                }
                PanelStatus.txtStatEstoq.setText(new String(strStatusEst).trim());

                PanelStart.lblStatusEst.setText(new String(strStatusEst).trim());

                PanelStart.pnlPLCBlock.revalidate();
                PanelStart.pnlPLCBlock.repaint();

                PanelStart.pnlOpSmart.revalidate();
                PanelStart.pnlOpSmart.repaint();

                if ((infoDB6_Est[0] & 0x01) == 1) {
                    PanelStatus.lblStatEstoq.setBackground(corBackPnlOn);
                    PanelStatus.chkEstoq.setSelected(true);
                } else {
                    PanelStatus.lblStatEstoq.setBackground(corBackPnl);
                    PanelStatus.chkEstoq.setSelected(false);
                }

            } else if (opDB.equals("DB9_Est")) {

                updateVarSmart();

                int c = 0;
                int[] pedidos = new int[34];
                for (int i = 0; i < 68; i += 2) {
                    int result = ((infoDB9_Est[i + 2] & 0xFF) << 8) | (infoDB9_Est[i + 3] & 0xFF);
                    pedidos[c] = result; 
                    c++; 
                }

                if ((infoDB9_Est[0] & 0x01) == 1) {

                    Panel2.chkRecOpEst.setSelected(true);
                    Panel2.chkRecOpEst.setText("TRUE");
                    recebidoOpEst = true;
                    boolEstoqueCur[0] = true;
                } else {

                    Panel2.chkRecOpEst.setSelected(false);
                    Panel2.chkRecOpEst.setText("FALSE");
                    recebidoOpEst = false;
                    boolEstoqueCur[0] = false;
                }

                // --------------- BLOCO 1 ---------------------------
                Panel2.txtCorBlk1.setText(String.valueOf(pedidos[0]));
                Panel2.txtPosBlk1.setText(String.valueOf(pedidos[1]));
                Panel2.txtCorL1Blk1.setText(String.valueOf(pedidos[2]));
                Panel2.txtCorL2Blk1.setText(String.valueOf(pedidos[3]));
                Panel2.txtCorL3Blk1.setText(String.valueOf(pedidos[4]));

                Panel2.txtPadL1Blk1.setText(String.valueOf(pedidos[5]));
                Panel2.txtPadL2Blk1.setText(String.valueOf(pedidos[6]));
                Panel2.txtPadL3Blk1.setText(String.valueOf(pedidos[7]));
                Panel2.txtProBlk1.setText(String.valueOf(pedidos[8]));

                // --------------- BLOCO 2 ---------------------------
                Panel2.txtCorBlk2.setText(String.valueOf(pedidos[9]));
                Panel2.txtPosBlk2.setText(String.valueOf(pedidos[10]));
                Panel2.txtCorL1Blk2.setText(String.valueOf(pedidos[11]));
                Panel2.txtCorL2Blk2.setText(String.valueOf(pedidos[12]));
                Panel2.txtCorL3Blk2.setText(String.valueOf(pedidos[13]));

                Panel2.txtPadL1Blk2.setText(String.valueOf(pedidos[14]));
                Panel2.txtPadL2Blk2.setText(String.valueOf(pedidos[15]));
                Panel2.txtPadL3Blk2.setText(String.valueOf(pedidos[16]));
                Panel2.txtProBlk2.setText(String.valueOf(pedidos[17]));

                // --------------- BLOCO 3 ---------------------------
                Panel2.txtCorBlk3.setText(String.valueOf(pedidos[18]));
                Panel2.txtPosBlk3.setText(String.valueOf(pedidos[19]));
                Panel2.txtCorL1Blk3.setText(String.valueOf(pedidos[20]));
                Panel2.txtCorL2Blk3.setText(String.valueOf(pedidos[21]));
                Panel2.txtCorL3Blk3.setText(String.valueOf(pedidos[22]));

                Panel2.txtPadL1Blk3.setText(String.valueOf(pedidos[23]));
                Panel2.txtPadL2Blk3.setText(String.valueOf(pedidos[24]));
                Panel2.txtPadL3Blk3.setText(String.valueOf(pedidos[25]));
                Panel2.txtProBlk3.setText(String.valueOf(pedidos[26]));

                // --------------- INFO PEDIDO ---------------------------
                Panel2.txtNumPedEst.setText(String.valueOf(pedidos[27])); // Nº PEDIDO
                Panel2.txtAndarEst.setText(String.valueOf(pedidos[28]));  // QTD ANDARES
                Panel2.txtPosExpEst.setText(String.valueOf(pedidos[29])); // POSIÇÃO EXPEDIÇÃO

                if ((infoDB9_Est[62] & 0x01) == 1) {

                    Panel2.chkStartPed.setSelected(true);
                    Panel2.chkStartPed.setText("TRUE");
                    iniciarPedido = true;
                    boolEstoqueCur[1] = true;
                } else {

                    Panel2.chkStartPed.setSelected(false);
                    Panel2.chkStartPed.setText("FALSE");
                    iniciarPedido = false;
                    boolEstoqueCur[1] = false;
                }

                if ((infoDB9_Est[64] & 0x01) == 1) {

                    Panel2.chkRecEstoq.setSelected(true);
                    Panel2.chkRecEstoq.setText("TRUE");
                    recebidoEstoque = true;
                    boolEstoqueCur[2] = true;
                } else {

                    Panel2.chkRecEstoq.setSelected(false);
                    Panel2.chkRecEstoq.setText("FALSE");
                    recebidoEstoque = false;
                    boolEstoqueCur[2] = false;
                }

                if ((infoDB9_Est[64] & 0x02) == 2) {

                    Panel2.chkIniGuard.setSelected(true);
                    Panel2.chkIniGuard.setText("TRUE");
                    iniciarGuardar = true;
                    boolEstoqueCur[3] = true;
                } else {

                    Panel2.chkIniGuard.setSelected(false);
                    Panel2.chkIniGuard.setText("FALSE");
                    iniciarGuardar = false;
                    boolEstoqueCur[3] = false;
                }

                // --------------------------------------------------------------

                if ((infoDB9_Est[98] & 0x01) == 1) {
                    cancelOp = true;
                    boolEstoqueCur[4] = true;
                } else {
                    cancelOp = false;
                    boolEstoqueCur[4] = false;
                }

                if ((infoDB9_Est[98] & 0x02) == 2) {
                    finishOp = true;
                    boolEstoqueCur[5] = true;
                } else {
                    finishOp = false;
                    boolEstoqueCur[5] = false;
                }

                if ((infoDB9_Est[98] & 0x04) == 4) {
                    startOp = true;
                    boolEstoqueCur[6] = true;
                } else {
                    startOp = false;
                    boolEstoqueCur[6] = false;
                }

                if ((infoDB9_Est[100] & 0x01) == 1) {
                    ocupadoEst = true;
                    boolEstoqueCur[7] = true;
                } else {
                    ocupadoEst = false;
                    boolEstoqueCur[7] = false;
                }

                if ((infoDB9_Est[100] & 0x02) == 2) {
                    aguardandoEst = true;
                    boolEstoqueCur[8] = true;
                } else {
                    aguardandoEst = false;
                    boolEstoqueCur[8] = false;
                }

                if ((infoDB9_Est[100] & 0x04) == 4) {
                    manualEst = true;
                    boolEstoqueCur[9] = true;
                } else {
                    manualEst = false;
                    boolEstoqueCur[9] = false;
                }

                if ((infoDB9_Est[100] & 0x08) == 8) {
                    emergenciaEst = true;
                    boolEstoqueCur[10] = true;
                } else {
                    emergenciaEst = false;
                    boolEstoqueCur[10] = false;
                }

                if ((infoDB9_Est[102] & 0x01) == 1) {
                    pedirPosicaoEst = true;
                    boolEstoqueCur[11] = true;
                } else {
                    pedirPosicaoEst = false;
                    boolEstoqueCur[11] = false;
                }

                if ((infoDB9_Est[106] & 0x01) == 1) {
                    adicionarEstoque = true;
                    boolEstoqueCur[12] = true;
                } else {
                    adicionarEstoque = false;
                    boolEstoqueCur[12] = false;
                }

                if ((infoDB9_Est[106] & 0x02) == 2) {
                    removerEstoque = true;
                    boolEstoqueCur[13] = true;
                } else {
                    removerEstoque = false;
                    boolEstoqueCur[13] = false;
                }

                if ((infoDB9_Est[106] & 0x04) == 4) {
                    retornoEstoqueCheio = true;
                    boolEstoqueCur[14] = true;
                } else {
                    retornoEstoqueCheio = false;
                    boolEstoqueCur[14] = false;
                }

                posicaoEstoque = ((infoDB9_Est[104] & 0xFF) << 8) | (infoDB9_Est[105] & 0xFF);
                corGuardarEstoque = ((infoDB9_Est[108] & 0xFF) << 8) | (infoDB9_Est[109] & 0xFF);
                posicaoGuardarEstoque = ((infoDB9_Est[66] & 0xFF) << 8) | (infoDB9_Est[67] & 0xFF);

                int posGuard = ((infoDB9_Est[66] & 0xFF) << 8) | (infoDB9_Est[67] & 0xFF);

                Panel2.txtPosGuard.setText(String.valueOf(posGuard));

                // ----------- REgistro de Sequência em Arquivo TXT - Estoque
                // -------------------------------

                // Obtém o timestamp atual
                Instant now = Instant.now();
                // Se você quiser um formato mais legível
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault());

                // Formata e imprime o timestamp
                String formattedTimestamp = formatter.format(now);

                for (int i = 0; i < 15; i++) {
                    if (boolEstoqueCur[i] != boolEstoqueOld[i]) {
                        difEstoque = true; // Se encontrar algum elemento diferente
                        break; // Para de comparar
                    }
                }

                if (difEstoque) {
                    oper_bool_est = "";
                    difEstoque = false;

                    for (int i = 0; i < 15; i++) {
                        boolEstoqueOld[i] = boolEstoqueCur[i];
                    }

                    oper_bool_est += "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "NODE -> PLC" + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "PosicaoExpedicao..........:;"
                            + Panel2.txtPosExpEst.getText().trim() + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "RecebidoOP................:;"
                            + recebidoOpEst + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "IniciarPedido.............:;"
                            + iniciarPedido + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "RecebidoEstoque...........:;"
                            + recebidoEstoque + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "IniciarGuardar............:;"
                            + iniciarGuardar + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "PosicaoGuardar............:;"
                            + posicaoGuardarEstoque + "\n\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "PLC -> NODE" + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "CancelOP..................:;"
                            + cancelOp + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "FinishOP..................:;"
                            + finishOp + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "StartOP...................:;"
                            + startOp + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "Ocupado...................:;"
                            + ocupadoEst + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "Aguardando................:;"
                            + aguardandoEst + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "Manual....................:;"
                            + manualEst + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "Emergencia................:;"
                            + emergenciaEst + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "PedirPosicao..............:;"
                            + pedirPosicaoEst + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "PosicaoEstoque............:;"
                            + posicaoEstoque + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "AdicionarEstoque..........:;"
                            + adicionarEstoque + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "RemoverEstoque............:;"
                            + removerEstoque + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "RetornoEstoqueCheio.......:;"
                            + retornoEstoqueCheio + "\n" +
                            "ESTOQUE;" + seqSmart + ";" + formattedTimestamp + ";" + "CorGuardarEstoque.........:;"
                            + corGuardarEstoque + "\n";

                    logSmart.add(oper_bool_est);
                    seqSmart++;

                    PanelStart.plcWriteEst = new PlcConnector(ipEstoque, 102);

                    try {
                        PanelStart.plcWriteEst.connect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                    // Se o pedido foi iniciado e a estação ESTOQUE informou que iniciou a operação
                    // ficando no estado OCUPADO
                    // então a flag iniciarPedido fica em FALSE
                    if (iniciarPedido == true & ocupadoEst == true) {
                        pedidoEmCurso = true;

                        stationWorkId = 0;

                        updateDisplayStation();
                        if (readOnly == false) {
                            System.out.println("Flag: IniciarPedido");
                            try {

                                PanelStart.plcWriteEst.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE")); // IniciarPedido

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [iniciarPedido == true & ocupadoEst == true]: Atualização da Flag IniciarPedido [DB9:62.0] para FALSE");
                            }
                        }

                    }

                    // Se as três flags (StartOP, FinishOP e CancelOP) estão em FALSE, então a flag
                    // RecebidoOP fica em FALSE
                    if (startOp == false & finishOp == false & cancelOp == false) {
                        if (readOnly == false) {

                            System.out.println("Flag [startOp] [finishOp] [cancelOp]: RecebidoOPEstoque_FALSE");
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 0, 1, 0, 1);
                                PanelStart.plcWriteEst.writeBit(9, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPEst

                            } catch (Exception e) {
                                System.out.println("ERRO: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para FALSE");
                            }
                        }
                    }
                    // Se a estação ESTOQUE sinalizou o início da operação e ficou OCUPADO, então a
                    // flag RecebidoOP fica em TRUE
                    if (startOp == true & recebidoOpEst == false) {
                        if (pedidoEmCurso == true) {
                            stationWorkId = 1;
                        } else {
                            stationWorkId = 0;
                        }
                        updateDisplayStation();
                        if (readOnly == false) {
                            System.out.println("Flag: RecebidoOPEstoque_TRUE");
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 0, 1, 0, 1);
                                PanelStart.plcWriteEst.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // RecebidoOPEst

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [startOp]: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para TRUE");
                            }
                        }
                    }

                    // Se a estação ESTOQUE sinalizou o témino da operação e ficou OCUPADO, então a
                    // flag RecebidoOP fica em TRUE
                    if (finishOp == true & recebidoOpEst == false) {
                        if (readOnly == false) {
                            System.out.println("Flag: RecebidoOPEstoque_TRUE");
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 0, 1, 0, 1);
                                PanelStart.plcWriteEst.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // RecebidoOPEst

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [finishOp]: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para TRUE");
                            }
                        }
                    }

                    if (removerEstoque == false & adicionarEstoque == false) {
                        if (readOnly == false) {
                            System.out.println("Flag: RecebidoEstoque_FALSE");

                            try {
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                                PanelStart.plcWriteEst.writeBit(9, 64, 0, Boolean.parseBoolean("FALSE")); // RecebidoEstoque

                            } catch (Exception e) {
                                System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para FALSE");
                            }
                        }
                    }

                    if ((posicaoEstoque > 0) & removerEstoque == true) {
                        if (readOnly == false) {
                            System.out.println("Flag: RecebidoEstoque_TRUE - REMOVER ESTOQUE");

                            // Coloca a flag RecebidoEstoque em TRUE
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                                PanelStart.plcWriteEst.writeBit(9, 64, 0, Boolean.parseBoolean("TRUE")); // RecebidoEstoque

                            } catch (Exception e) {
                                System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para TRUE");
                            }

                            System.out.println("Removendo da posição: " + posicaoEstoque);
                            removeAdicionaMagazineEstoque(posicaoEstoque, (byte) 0);
                            Panel3.saveMagazineEstoque();
                        }
                    }

                    if ((posicaoEstoque > 0) & adicionarEstoque == true) {
                        if (readOnly == false) {
                            System.out.println("Flag: RecebidoEstoque_TRUE - ADICIONAR ESTOQUE");

                            // Coloca a flag RecebidoEstoque em TRUE
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                                PanelStart.plcWriteEst.writeBit(9, 64, 0, Boolean.parseBoolean("TRUE")); // RecebidoEstoque

                            } catch (Exception e) {
                                System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para TRUE");
                            }
                            removeAdicionaMagazineEstoque(posicaoGuardarEstoque, (byte) corGuardarEstoque);
                            Panel3.saveMagazineEstoque();

                        }
                    }

                    if ((ocupadoEst == true | retornoEstoqueCheio == true) & iniciarGuardar == true) {
                        if (readOnly == false) {
                            System.out.println("Flag: IniciarGuardar_FALSE");

                            // Coloca a flag IniciarGuardar em FALSE
                            try {

                                // Coloca a flag IniciarGuardar em TRUE
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                                PanelStart.plcWriteEst.writeBit(9, 64, 1, Boolean.parseBoolean("FALSE"));

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO: Atualização da Flag IniciarGuardarEstoque [DB9:64.1] para FALSE");
                            }
                        }

                    } else {

                    }
                    if (pedirPosicaoEst & !ocupadoEst) { // Verifica se Estoque pede posição para guardar
                        System.out.println("ESTOU AQUI- (pedirPosicaoEst == true) & ocupadoEst == false");

                        // Rotina para verificar qual posição está disponível para guardar
                        if (!readOnly) {

                            // Solicita posição disponível para guardar (0-LIVRE 1-PRETO 2-VERMELHO 3-AZUL)
                            // Certifique-se de que posEstoqueRequest é seguro para acesso
                            posEstoqueRequest = getPositionEstoque(0);

                            System.out.println("Posição disponível no Magazine Estoque: " + posEstoqueRequest);

                            try {

                                // Atualiza a variável PosicaoGuardar no CLP ESTOQUE
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 66, 2, 0, 1);
                                PanelStart.plcWriteEst.writeInt(9, 66, posEstoqueRequest);

                            } catch (Exception e) {
                                System.out.println("ERRO: Atualização da PosicaoGuardarEstoque [DB9:66]");
                            }

                            try {

                                // Coloca a flag IniciarGuardar em TRUE
                                // Panel3.plcWrite = new PlcConnector(ipEstoque, 9, 64, 1, 0, 1);
                                PanelStart.plcWriteEst.writeBit(9, 64, 1, Boolean.parseBoolean("TRUE"));

                            } catch (Exception e) {
                                System.out.println("ERRO: Atualização da Flag IniciarGuardarEstoque [DB9:64.1]");
                            }
                        }
                    }

                }

            }

        }
    }

    public static void plcProcesso() {
        if (Panel2.chkReadCiclic.isSelected()) {

            if (opDB.equals("DB6_Pro")) {
                // Leitura do Status da Estação PROCESSO
                byte[] strStatusPro = new byte[infoDB6_Pro[14]];

                for (int c = 0; c < infoDB6_Pro[15]; c++) {
                    strStatusPro[c] = infoDB6_Pro[c + 16];

                }
                PanelStatus.txtStatProces.setText(new String(strStatusPro).trim());
                PanelStart.lblStatusPro.setText(new String(strStatusPro).trim());

                PanelStart.pnlPLCBlock.revalidate();
                PanelStart.pnlPLCBlock.repaint();

                PanelStart.pnlOpSmart.revalidate();
                PanelStart.pnlOpSmart.repaint();

                // Leitura se a Estação PROCESSO está ligada
                if ((infoDB6_Pro[0] & 0x01) == 1) {
                    PanelStatus.lblStatProces.setBackground(corBackPnlOn);
                    PanelStatus.chkProces.setSelected(true);
                } else {
                    PanelStatus.lblStatProces.setBackground(corBackPnl);
                    PanelStatus.chkProces.setSelected(false);
                }

            } else if (opDB.equals("DB2_Pro")) {

                // System.out.println("AQUI - NO PROCESSO");

                if ((infoDB2_Pro[0] & 0x01) == 1) {
                    // lblStatEstoq.setBackground(corBackPnlOn);
                    Panel2.chkRecOpPro.setSelected(true);
                    Panel2.chkRecOpPro.setText("TRUE");
                    recebidoOpPro = true;
                    boolProcessoCur[0] = true;
                } else {
                    // lblStatEstoq.setBackground(corBackPnl);
                    Panel2.chkRecOpPro.setSelected(false);
                    Panel2.chkRecOpPro.setText("FALSE");
                    recebidoOpPro = false;
                    boolProcessoCur[0] = false;
                }

                if ((infoDB2_Pro[4] & 0x01) == 1) {
                    // lblStatEstoq.setBackground(corBackPnlOn);
                    // Panel2.chkRecOpPro.setSelected(true);
                    // Panel2.chkRecOpPro.setText("TRUE");
                    cancelOpPro = true;
                    boolProcessoCur[1] = true;
                } else {
                    // lblStatEstoq.setBackground(corBackPnl);
                    // Panel2.chkRecOpPro.setSelected(false);
                    // Panel2.chkRecOpPro.setText("FALSE");
                    cancelOpPro = false;
                    boolProcessoCur[1] = false;
                }

                if ((infoDB2_Pro[4] & 0x02) == 2) {
                    // lblStatEstoq.setBackground(corBackPnlOn);
                    // Panel2.chkRecOpPro.setSelected(true);
                    // Panel2.chkRecOpPro.setText("TRUE");
                    finishOpPro = true;
                    boolProcessoCur[2] = true;
                } else {
                    // lblStatEstoq.setBackground(corBackPnl);
                    // Panel2.chkRecOpPro.setSelected(false);
                    // Panel2.chkRecOpPro.setText("FALSE");
                    finishOpPro = false;
                    boolProcessoCur[2] = false;
                }

                if ((infoDB2_Pro[4] & 0x04) == 4) {
                    // lblStatEstoq.setBackground(corBackPnlOn);
                    // Panel2.chkRecOpPro.setSelected(true);
                    // Panel2.chkRecOpPro.setText("TRUE");
                    startOpPro = true;
                    boolProcessoCur[3] = true;
                } else {
                    // lblStatEstoq.setBackground(corBackPnl);
                    // Panel2.chkRecOpPro.setSelected(false);
                    // Panel2.chkRecOpPro.setText("FALSE");
                    startOpPro = false;
                    boolProcessoCur[3] = false;
                }

                if ((infoDB2_Pro[6] & 0x01) == 1) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    ocupadoPro = true;
                    boolProcessoCur[4] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    ocupadoPro = false;
                    boolProcessoCur[4] = false;
                }

                if ((infoDB2_Pro[6] & 0x02) == 2) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    aguardandoPro = true;
                    boolProcessoCur[5] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    aguardandoPro = false;
                    boolProcessoCur[5] = false;
                }

                if ((infoDB2_Pro[6] & 0x04) == 4) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    manualPro = true;
                    boolProcessoCur[6] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    manualPro = false;
                    boolProcessoCur[6] = false;
                }

                if ((infoDB2_Pro[6] & 0x08) == 8) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    emergenciaPro = true;
                    boolProcessoCur[7] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    emergenciaPro = false;
                    boolProcessoCur[7] = false;
                }

                // ----------- Registro de Sequência em Arquivo TXT - Processo
                // -------------------------------

                // Obtém o timestamp atual
                Instant now = Instant.now();
                // Se você quiser um formato mais legível
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault());

                // Formata e imprime o timestamp
                String formattedTimestamp = formatter.format(now);

                for (int i = 0; i < 11; i++) {
                    if (boolProcessoCur[i] != boolProcessoOld[i]) {
                        difProcesso = true; // Se encontrar algum elemento diferente
                        break; // Para de comparar
                    }
                }

                if (difProcesso) {
                    oper_bool_pro = "";
                    difProcesso = false;
                    // System.out.println("AQUI - NO PROCESSO HOUVE MUDANÇA");

                    System.arraycopy(boolProcessoCur, 0, boolProcessoOld, 0, 11);

                    oper_bool_pro += "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "NODE -> PLC" + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "RecebidoOP................:;"
                            + recebidoOpPro + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "PLC -> NODE" + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "CancelOP..................:;"
                            + cancelOpPro + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "FinishOP..................:;"
                            + finishOpPro + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "StartOP...................:;"
                            + startOpPro + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "Ocupado...................:;"
                            + ocupadoPro + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "Aguardando................:;"
                            + aguardandoPro + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "Manual....................:;"
                            + manualPro + "\n" +
                            "PROCESSO;" + seqSmart + ";" + formattedTimestamp + ";" + "Emergencia................:;"
                            + emergenciaPro + "\n";

                    logSmart.add(oper_bool_pro);
                    seqSmart++;

                    PanelStart.plcWritePro = new PlcConnector(ipProcesso, 102);

                    try {
                        PanelStart.plcWritePro.connect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                    // try (BufferedWriter writer = new BufferedWriter(new
                    // FileWriter(seqPlantaSmart, true))) {
                    // writer.write(oper_bool_pro); // Adiciona o conteúdo no final do arquivo
                    // writer.newLine(); // Adiciona uma nova linha
                    // // System.out.println("Conteúdo adicionado ao arquivo " + seqPro + " com
                    // // sucesso!");
                    // // seqProcesso++;
                    // seqSmart++;
                    // } catch (IOException e) {
                    // e.printStackTrace();
                    // }
                    // Se as três flags (StartOP, FinishOP e CancelOP) estão em FALSE, então a flag
                    // RecebidoOP fica em FALSE
                    if (startOpPro == false & finishOpPro == false & cancelOpPro == false) {
                        if (readOnly == false) {

                            try {
                                // PanelStart.plcWritePro = new PlcConnector(ipProcesso, 2, 0, 1, 0, 1);
                                PanelStart.plcWritePro.writeBit(2, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPPro
                            } catch (Exception ex) {
                            }
                        }
                    }

                    if (startOpPro == true & recebidoOpPro == false) {
                        if (pedidoEmCurso == true) {
                            stationWorkId = 2;
                        } else {
                            stationWorkId = 0;
                        }
                        updateDisplayStation();
                        if (readOnly == false) {

                            // Panel3.plcWrite = new PlcConnector(ipProcesso, 2, 0, 1, 0, 1);
                            try {
                                PanelStart.plcWritePro.writeBit(2, 0, 0, Boolean.parseBoolean("TRUE"));
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }

                    }

                    // Se a estação PROCESSO sinalizou o témino da operação e ficou OCUPADO, então a
                    // flag RecebidoOP fica em TRUE
                    if (finishOpPro == true & recebidoOpPro == false) {
                        if (readOnly == false) {

                            // Panel3.plcWrite = new PlcConnector(ipProcesso, 2, 0, 1, 0, 1);
                            try {
                                PanelStart.plcWritePro.writeBit(2, 0, 0, Boolean.parseBoolean("TRUE"));
                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                        }
                    }

                }
            }

        }

    }

    public static void plcMontagem() {
        if (Panel2.chkReadCiclic.isSelected()) {

            // Leitura do Status da Estação MONTAGEM
            switch (opDB) {
                case "DB30_Mon" -> {

                    byte[] strStatusEst = new byte[infoDB30_Mon[16]];
                    for (int c = 0; c < infoDB30_Mon[17]; c++) {
                        strStatusEst[c] = infoDB30_Mon[c + 18];

                    }

                    // for (int c = 0; c < infoDB6_Est[17]; c++) {
                    // strStatusEst[c] = infoDB6_Est[c + 18];

                    // }
                    // PanelStatus.txtStatEstoq.setText(new String(strStatusEst).trim());

                    // PanelStart.lblStatusEst.setText(new String(strStatusEst).trim());

                    // Leitura se a Estação Montagem está ligada
                    if ((infoDB30_Mon[0] & 0x01) == 1) {
                        PanelStatus.lblStatMontag.setBackground(corBackPnlOn);
                        PanelStatus.chkMontag.setSelected(true);
                    } else {
                        PanelStatus.lblStatMontag.setBackground(corBackPnl);
                        PanelStatus.chkMontag.setSelected(false);
                    }
                }
                case "DB57_Mon" -> {
                    if ((infoDB57_Mon[0] & 0x01) == 1) {
                        // lblStatEstoq.setBackground(corBackPnlOn);
                        Panel2.chkRecOpMon.setSelected(true);
                        Panel2.chkRecOpMon.setText("TRUE");
                        recebidoOpMon = true;
                        boolMontagemCur[0] = true;
                    } else {
                        // lblStatEstoq.setBackground(corBackPnl);
                        Panel2.chkRecOpMon.setSelected(false);
                        Panel2.chkRecOpMon.setText("FALSE");
                        recebidoOpMon = false;
                        boolMontagemCur[0] = false;
                    }
                    if ((infoDB57_Mon[4] & 0x01) == 1) {
                        // lblStatEstoq.setBackground(corBackPnlOn);
                        // Panel2.chkRecOpPro.setSelected(true);
                        // Panel2.chkRecOpPro.setText("TRUE");
                        cancelOpMon = true;
                        boolMontagemCur[1] = true;
                    } else {
                        // lblStatEstoq.setBackground(corBackPnl);
                        // Panel2.chkRecOpPro.setSelected(false);
                        // Panel2.chkRecOpPro.setText("FALSE");
                        cancelOpMon = false;
                        boolMontagemCur[1] = false;
                    }
                    if ((infoDB57_Mon[4] & 0x02) == 2) {
                        // lblStatEstoq.setBackground(corBackPnlOn);
                        // Panel2.chkRecOpPro.setSelected(true);
                        // Panel2.chkRecOpPro.setText("TRUE");
                        finishOpMon = true;
                        boolMontagemCur[2] = true;
                    } else {
                        // lblStatEstoq.setBackground(corBackPnl);
                        // Panel2.chkRecOpPro.setSelected(false);
                        // Panel2.chkRecOpPro.setText("FALSE");
                        finishOpMon = false;
                        boolMontagemCur[2] = false;
                    }
                    if ((infoDB57_Mon[4] & 0x04) == 4) {
                        // lblStatEstoq.setBackground(corBackPnlOn);
                        // Panel2.chkRecOpPro.setSelected(true);
                        // Panel2.chkRecOpPro.setText("TRUE");
                        startOpMon = true;
                        boolMontagemCur[3] = true;
                    } else {
                        // lblStatEstoq.setBackground(corBackPnl);
                        // Panel2.chkRecOpPro.setSelected(false);
                        // Panel2.chkRecOpPro.setText("FALSE");
                        startOpMon = false;
                        boolMontagemCur[3] = false;
                    }
                    if ((infoDB57_Mon[6] & 0x01) == 1) {
                        // Panel2.chkStartPed.setBackground(corBackPnlOn);
                        // Panel2.chkIniGuard.setSelected(true);
                        // Panel2.chkIniGuard.setText("TRUE");
                        ocupadoMon = true;
                        boolMontagemCur[4] = true;
                    } else {
                        // Panel2.chkStartPed.setBackground(corBackPnl);
                        // Panel2.chkIniGuard.setSelected(false);
                        // Panel2.chkIniGuard.setText("FALSE");
                        ocupadoMon = false;
                        boolMontagemCur[4] = false;
                    }
                    if ((infoDB57_Mon[6] & 0x02) == 2) {
                        // Panel2.chkStartPed.setBackground(corBackPnlOn);
                        // Panel2.chkIniGuard.setSelected(true);
                        // Panel2.chkIniGuard.setText("TRUE");
                        aguardandoMon = true;
                        boolMontagemCur[5] = true;
                    } else {
                        // Panel2.chkStartPed.setBackground(corBackPnl);
                        // Panel2.chkIniGuard.setSelected(false);
                        // Panel2.chkIniGuard.setText("FALSE");
                        aguardandoMon = false;
                        boolMontagemCur[5] = false;
                    }
                    if ((infoDB57_Mon[6] & 0x04) == 4) {
                        // Panel2.chkStartPed.setBackground(corBackPnlOn);
                        // Panel2.chkIniGuard.setSelected(true);
                        // Panel2.chkIniGuard.setText("TRUE");
                        manualMon = true;
                        boolMontagemCur[6] = true;
                    } else {
                        // Panel2.chkStartPed.setBackground(corBackPnl);
                        // Panel2.chkIniGuard.setSelected(false);
                        // Panel2.chkIniGuard.setText("FALSE");
                        manualMon = false;
                        boolMontagemCur[6] = false;
                    }
                    if ((infoDB57_Mon[6] & 0x08) == 8) {
                        // Panel2.chkStartPed.setBackground(corBackPnlOn);
                        // Panel2.chkIniGuard.setSelected(true);
                        // Panel2.chkIniGuard.setText("TRUE");
                        emergenciaMon = true;
                        boolMontagemCur[7] = true;
                    } else {
                        // Panel2.chkStartPed.setBackground(corBackPnl);
                        // Panel2.chkIniGuard.setSelected(false);
                        // Panel2.chkIniGuard.setText("FALSE");
                        emergenciaMon = false;
                        boolMontagemCur[7] = false;
                    }
                    // ----------- Registro de Sequência em Arquivo TXT - Montagem
                    // -------------------------------
                    // Obtém o timestamp atual
                    Instant now = Instant.now();
                    // Se você quiser um formato mais legível
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault());
                    // Formata e imprime o timestamp
                    String formattedTimestamp = formatter.format(now);
                    for (int i = 0; i < 8; i++) {
                        // System.out.println("Cur["+i +"] = "+ boolMontagemCur[i] + " <==> Old["+i +"]
                        // = "+ boolMontagemOld[i]);
                        if (boolMontagemCur[i] != boolMontagemOld[i]) {
                            difMontagem = true; // Se encontrar algum elemento diferente
                            break; // Para de comparar
                        }
                    }
                    if (difMontagem) {
                        oper_bool_mon = "";
                        difMontagem = false;

                        System.arraycopy(boolMontagemCur, 0, boolMontagemOld, 0, 8);

                        oper_bool_mon += "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "NODE -> PLC" + "\n"
                                +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "RecebidoOP................:;"
                                + recebidoOpMon + "\n" +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "PLC -> NODE" + "\n" +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "CancelOP..................:;"
                                + cancelOpMon + "\n" +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "FinishOP..................:;"
                                + finishOpMon + "\n" +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "StartOP...................:;"
                                + startOpMon + "\n" +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "Ocupado...................:;"
                                + ocupadoMon + "\n" +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "Aguardando................:;"
                                + aguardandoMon + "\n" +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "Manual....................:;"
                                + manualMon + "\n" +
                                "MONTAGEM;" + seqSmart + ";" + formattedTimestamp + ";" + "Emergencia................:;"
                                + emergenciaMon + "\n";

                        logSmart.add(oper_bool_mon);
                        seqSmart++;

                        PanelStart.plcWriteMon = new PlcConnector(ipMontagem, 102);

                        try {
                            PanelStart.plcWriteMon.connect();
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }

                        // try (BufferedWriter writer = new BufferedWriter(new
                        // FileWriter(seqPlantaSmart, true))) {
                        // writer.write(oper_bool_mon); // Adiciona o conteúdo no final do arquivo
                        // writer.newLine(); // Adiciona uma nova linha
                        // // System.out.println("Conteúdo adicionado ao arquivo " + seqMon + " com
                        // // sucesso!");
                        // // seqMontagem++;
                        // seqSmart++;
                        // } catch (IOException e) {
                        // e.printStackTrace();
                        // }
                        // Se as três flags (StartOP, FinishOP e CancelOP) estão em FALSE, então a flag
                        // RecebidoOP fica em FALSE
                        if (startOpMon == false & finishOpMon == false & cancelOpMon == false) {
                            if (readOnly == false) {

                                try {
                                    // Panel3.plcWrite = new PlcConnector(ipMontagem, 57, 0, 1, 0, 1);
                                    PanelStart.plcWriteMon.writeBit(57, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPMon
                                } catch (Exception ex) {
                                }
                            }
                        }

                        if (startOpMon == true & recebidoOpMon == false) {
                            if (pedidoEmCurso == true) {
                                stationWorkId = 3;
                            } else {
                                stationWorkId = 0;
                            }
                            updateDisplayStation();
                            if (readOnly == false) {

                                try {
                                    // Panel3.plcWrite = new PlcConnector(ipMontagem, 57, 0, 1, 0, 1);
                                    PanelStart.plcWriteMon.writeBit(57, 0, 0, Boolean.parseBoolean("TRUE")); // RecebidoOPMon
                                } catch (Exception ex) {
                                }

                            }

                        }

                        // Se a estação MONTAGEM sinalizou o témino da operação e ficou OCUPADO, então a
                        // flag RecebidoOP fica em TRUE
                        if (finishOpMon == true & recebidoOpMon == false) {
                            if (readOnly == false) {

                                // Panel3.plcWrite = new PlcConnector(ipMontagem, 57, 0, 1, 0, 1);
                                try {
                                    PanelStart.plcWriteMon.writeBit(57, 0, 0, Boolean.parseBoolean("TRUE"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } // RecebidoOPMon

                            }
                        }

                        try {
                            // PanelStart.plcWriteMon.disconnect();
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    }
                }
                case "DB60_Mon" -> {
                    byte[] strStatusExp = new byte[infoDB60_Mon[20]];
                    for (int c = 0; c < infoDB60_Mon[21]; c++) {
                        strStatusExp[c] = infoDB60_Mon[c + 22];

                    }
                    PanelStatus.txtStatExpedi.setText(new String(strStatusExp).trim());
                    PanelStart.lblStatusExp.setText(new String(strStatusExp).trim());

                    // Leitura se a Estação EXPEDIÇÃO está ligada
                    if ((infoDB60_Mon[0] & 0x01) == 1) {
                        PanelStatus.lblStatExpedi.setBackground(corBackPnlOn);
                        PanelStatus.chkExpedi.setSelected(true);
                    } else {
                        PanelStatus.lblStatExpedi.setBackground(corBackPnl);
                        PanelStatus.chkExpedi.setSelected(false);
                    }
                }
                case "DB92_Mon" -> {
                    byte[] strStatusMon = new byte[infoDB92_Mon[2]];
                    for (int c = 0; c < infoDB92_Mon[3]; c++) {
                        strStatusMon[c] = infoDB92_Mon[c + 4];

                    }
                    // PanelStatus.txtStatMontag.setText(new String(strStatusMon).trim());
                    PanelStatus.txtStatMontag.setText(new String(strStatusMon).trim());
                    PanelStart.lblStatusMon.setText(PanelStatus.txtStatMontag.getText().trim());
                    PanelStart.pnlPLCBlock.revalidate();
                    PanelStart.pnlPLCBlock.repaint();
                    PanelStart.pnlOpSmart.revalidate();
                    PanelStart.pnlOpSmart.repaint();
                    // Leitura se a Estação MONTAGEM está ligada
                    if ((infoDB92_Mon[0] & 0x01) == 1) {
                        PanelStatus.lblStatMontag.setBackground(corBackPnlOn);
                        PanelStatus.chkMontag.setSelected(true);
                    } else {
                        PanelStatus.lblStatMontag.setBackground(corBackPnl);
                        PanelStatus.chkMontag.setSelected(false);
                    }
                }
                case "DB600_Mon" -> {
                    byte[] strStatusPro = new byte[infoDB600_Mon[14]];
                    for (int c = 0; c < infoDB600_Mon[15]; c++) {
                        strStatusPro[c] = infoDB600_Mon[c + 16];

                    }
                    PanelStatus.txtStatProces.setText(new String(strStatusPro).trim());
                    PanelStart.lblStatusPro.setText(new String(strStatusPro).trim());
                    // Leitura se a Estação EXPEDIÇÃO está ligada
                    if ((infoDB600_Mon[0] & 0x01) == 1) {
                        PanelStatus.lblStatProces.setBackground(corBackPnlOn);
                        PanelStatus.chkProces.setSelected(true);
                    } else {
                        PanelStatus.lblStatProces.setBackground(corBackPnl);
                        PanelStatus.chkProces.setSelected(false);
                    }
                }
                default -> {
                }

            }

        }

    }

    public static void plcExpedicao() {
        if (Panel2.chkReadCiclic.isSelected()) {

            if (opDB.equals("DB9_Exp")) {

                if ((infoDB9_Exp[0] & 0x01) == 1) {
                    // lblStatEstoq.setBackground(corBackPnlOn);
                    // PanelStatus.chkRecOpExp.setSelected(true);
                } else {
                    // lblStatEstoq.setBackground(corBackPnl);
                    // PanelStatus.chkRecOpExp.setSelected(false);
                }

                // Leitura da Operação em cada Posição do Magazine da Expedição

                int c = 0;
                // operacao = new int[12];
                for (int i = 0; i < 24; i += 2) {
                    int result = ((infoDB9_Exp[i + 6] & 0xFF) << 8) | (infoDB9_Exp[i + 7] & 0xFF);
                    operacao[c] = (int) result; // ((byte1 & 0xFF) <<
                    // System.out.println("Operação[" + c + "] = " + operacao[c]);
                    c++; // 8) |
                         // (byte2 & 0xFF)
                }

                if ((infoDB9_Exp[0] & 0x01) == 1) {
                    // lblStatEstoq.setBackground(corBackPnlOn);
                    Panel2.chkRecOpExp.setSelected(true);
                    Panel2.chkRecOpExp.setText("TRUE");
                    recebidoOpExp = true;
                    boolExpedicaoCur[0] = true;
                } else {
                    // lblStatEstoq.setBackground(corBackPnl);
                    Panel2.chkRecOpExp.setSelected(false);
                    Panel2.chkRecOpExp.setText("FALSE");
                    recebidoOpExp = false;
                    boolExpedicaoCur[0] = false;
                }

                if ((infoDB9_Exp[2] & 0x01) == 1) {
                    // lblStatEstoq.setBackground(corBackPnlOn);
                    Panel2.chkRecExpedi.setSelected(true);
                    Panel2.chkRecExpedi.setText("TRUE");
                    recebidoExpedicao = true;
                    boolExpedicaoCur[1] = true;
                } else {
                    // lblStatEstoq.setBackground(corBackPnl);
                    Panel2.chkRecExpedi.setSelected(false);
                    Panel2.chkRecExpedi.setText("FALSE");
                    recebidoExpedicao = false;
                    boolExpedicaoCur[1] = false;
                }
                if ((infoDB9_Exp[2] & 0x02) == 2) {
                    // lblStatEstoq.setBackground(corBackPnlOn);
                    Panel2.chkIniGuardExp.setSelected(true);
                    Panel2.chkIniGuardExp.setText("TRUE");
                    iniciarGuardarExp = true;
                    boolExpedicaoCur[2] = true;
                } else {
                    // lblStatEstoq.setBackground(corBackPnl);
                    Panel2.chkIniGuardExp.setSelected(false);
                    Panel2.chkIniGuardExp.setText("FALSE");
                    iniciarGuardarExp = false;
                    boolExpedicaoCur[2] = false;
                }
                //
                int posGuard = ((infoDB9_Exp[4] & 0xFF) << 8) | (infoDB9_Exp[5] & 0xFF);
                Panel2.txtPosGuardExp.setText(String.valueOf(posGuard));

                // if (operacao[0] > 0) {
                // Panel2.txtPos1Exped.setText("OP" + String.valueOf(operacao[0]));
                // } else {
                // Panel2.txtPos1Exped.setText("");
                // }

                if (readOnly == false) {
                    loadMagazineExpedicao();
                } else {
                    updateMagazineExpedicao();

                    PanelStart.updateExpedicao();
                }

                // --------------------------------------------------------------

                if ((infoDB9_Exp[32] & 0x01) == 1) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    cancelOpExp = true;
                    boolExpedicaoCur[3] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    cancelOpExp = false;
                    boolExpedicaoCur[3] = false;
                }

                if ((infoDB9_Exp[32] & 0x02) == 2) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    finishOpExp = true;
                    boolExpedicaoCur[4] = true;
                    blockFinished = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    finishOpExp = false;
                    boolExpedicaoCur[4] = false;
                }

                if ((infoDB9_Exp[32] & 0x04) == 4) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    startOpExp = true;
                    boolExpedicaoCur[5] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    startOpExp = false;
                    boolExpedicaoCur[5] = false;
                }

                if ((infoDB9_Exp[34] & 0x01) == 1) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    ocupadoExp = true;
                    boolExpedicaoCur[6] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    ocupadoExp = false;
                    boolExpedicaoCur[6] = false;
                }

                if ((infoDB9_Exp[34] & 0x02) == 2) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    aguardandoExp = true;
                    boolExpedicaoCur[7] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    aguardandoExp = false;
                    boolExpedicaoCur[7] = false;
                }

                if ((infoDB9_Exp[34] & 0x04) == 4) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    manualExp = true;
                    boolExpedicaoCur[8] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    manualExp = false;
                    boolExpedicaoCur[8] = false;
                }

                if ((infoDB9_Exp[34] & 0x08) == 8) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    emergenciaExp = true;
                    boolExpedicaoCur[9] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    emergenciaExp = false;
                    boolExpedicaoCur[9] = false;
                }

                if ((infoDB9_Exp[36] & 0x01) == 1) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    pedirPosicaoExp = true;
                    boolExpedicaoCur[10] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    pedirPosicaoExp = false;
                    boolExpedicaoCur[10] = false;
                }

                if ((infoDB9_Exp[42] & 0x01) == 1) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    adicionarExpedicao = true;
                    boolExpedicaoCur[11] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    adicionarExpedicao = false;
                    boolExpedicaoCur[11] = false;
                }

                if ((infoDB9_Exp[42] & 0x02) == 2) {
                    // Panel2.chkStartPed.setBackground(corBackPnlOn);
                    // Panel2.chkIniGuard.setSelected(true);
                    // Panel2.chkIniGuard.setText("TRUE");
                    removerExpedicao = true;
                    boolExpedicaoCur[12] = true;
                } else {
                    // Panel2.chkStartPed.setBackground(corBackPnl);
                    // Panel2.chkIniGuard.setSelected(false);
                    // Panel2.chkIniGuard.setText("FALSE");
                    removerExpedicao = false;
                    boolExpedicaoCur[12] = false;
                }

                posicaoGuardarExpedicao = ((infoDB9_Exp[4] & 0xFF) << 8) | (infoDB9_Exp[5] & 0xFF);
                posicaoGuardadoExpedicao = ((infoDB9_Exp[38] & 0xFF) << 8) | (infoDB9_Exp[39] & 0xFF);
                posicaoRemovidoExpedicao = ((infoDB9_Exp[40] & 0xFF) << 8) | (infoDB9_Exp[41] & 0xFF);
                opGuardadoExpedicao = ((infoDB9_Exp[44] & 0xFF) << 8) | (infoDB9_Exp[45] & 0xFF);

                // posicaoRemovidoExpedicao = ((infoDB9_Est[40] & 0xFF) << 8) | (infoDB9_Est[41]
                // & 0xFF);

                // ----------- Registro de Sequência em Arquivo TXT - Expedição
                // -------------------------------

                // Obtém o timestamp atual
                Instant now = Instant.now();
                // Se você quiser um formato mais legível
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        .withZone(ZoneId.systemDefault());

                // Formata e imprime o timestamp
                String formattedTimestamp = formatter.format(now);

                for (int i = 0; i < 22; i++) {
                    if (boolExpedicaoCur[i] != boolExpedicaoOld[i]) {
                        difExpedicao = true; // Se encontrar algum elemento diferente
                        break; // Para de comparar
                    }
                }

                if (difExpedicao) {
                    oper_bool_exp = "";
                    difExpedicao = false;

                    System.arraycopy(boolExpedicaoCur, 0, boolExpedicaoOld, 0, 22);

                    oper_bool_exp += "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "NODE -> PLC" + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "RecebidoOP................:;"
                            + recebidoOpExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "RecebidoExpedicao.........:;"
                            + recebidoExpedicao + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "IniciarGuardar............:;"
                            + iniciarGuardarExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "PosicaoGuardarExpedicao...:;"
                            + posicaoGuardarExpedicao + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "PLC -> NODE" + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "CancelOP..................:;"
                            + cancelOpExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "FinishOP..................:;"
                            + finishOpExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "StartOP...................:;"
                            + startOpExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "Ocupado...................:;"
                            + ocupadoExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "Aguardando................:;"
                            + aguardandoExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "Manual....................:;"
                            + manualExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "Emergencia................:;"
                            + emergenciaExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "PedirPosicao..............:;"
                            + pedirPosicaoExp + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "PosicaoGuardadoExpedicao..:;"
                            + posicaoGuardadoExpedicao + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "PosicaoRemovidoExpedicao..:;"
                            + posicaoRemovidoExpedicao + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "AdicionarExpedicao........:;"
                            + adicionarExpedicao + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "RemoverExpedicao..........:;"
                            + removerExpedicao + "\n" +
                            "EXPEDIÇÃO;" + seqSmart + ";" + formattedTimestamp + ";" + "OpGuardadoExpedicao.......:;"
                            + opGuardadoExpedicao + "\n";

                    logSmart.add(oper_bool_exp);

                    PanelStart.plcWriteExp = new PlcConnector(ipExpedicao, 102);

                    try {
                        PanelStart.plcWriteExp.connect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                    // Se as três flags (StartOP, FinishOP e CancelOP) estão em FALSE, então a flag
                    // RecebidoOP fica em FALSE
                    if (startOpExp == false & finishOpExp == false & cancelOpExp == false) {
                        if (readOnly == false) {
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 0, 1, 0, 1);
                                PanelStart.plcWriteExp.writeBit(9, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPExp

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [startOp][finishOp]: Atualização da Flag RecebidoOPExp [DB9:0.0] para FALSE");
                            }

                        }
                    }

                    // Se o pedido foi finalizado pela estação de MONTAGEM e a estação EXPEDIÇÃO
                    // informou que iniciou a operação
                    // então a flag recebidoOpExp fica em TRUE
                    if (startOpExp == true & recebidoOpExp == false) {
                        if (pedidoEmCurso == true) {
                            stationWorkId = 4;
                        } else {
                            stationWorkId = 0;
                        }
                        blockFinished = true;
                        updateDisplayStation();
                        if (readOnly == false) {
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 0, 1, 0, 1);
                                PanelStart.plcWriteExp.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // RecebidoOPExp

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [startOp]: Atualização da Flag RecebidoOPExp [DB9:0.0] para TRUE");
                            }

                        }
                    }

                    // Se a estação EXPEDIÇÃO sinalizou o término da operação e ficou OCUPADO, então
                    // a flag RecebidoOP fica em TRUE
                    if (finishOpExp == true & recebidoOpExp == false) {
                        if (readOnly == false) {
                            // JOptionPane.showMessageDialog(null, "1 - Vou iniciar a guarda do BLOCO!!!");

                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 0, 1, 0, 1);
                                PanelStart.plcWriteExp.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // RecebidoOPExp

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [finishOp]: Atualização da Flag RecebidoOPExp [DB9:0.0] para TRUE");
                            }
                        }
                    }

                    // ------------------ ^ OK ----------------------------------------------

                    // if (pedirPosicaoExp == false) {
                    // aux_expedicao = false;
                    // if (readOnly == false) {
                    //
                    // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 2, 1, 0, 1);
                    // Panel3.plcWrite.writeBit(9, 2, 1, Boolean.parseBoolean("FALSE")); //
                    // IniciarGuardar
                    //
                    // }
                    // }

                    if (pedirPosicaoExp == false) {
                        if (!readOnly) {
                            aux_expedicao = false;
                            // Coloca a flag IniciarGuardar em FALSE
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 2, 1, 0, 1);
                                PanelStart.plcWriteExp.writeBit(9, 2, 1, Boolean.parseBoolean("FALSE"));

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [Pedir Posição]: Atualização da Flag IniciarGuardar [DB9:2.1] para FALSE");
                            }
                        }
                    }

                    // verifica se Expedição pede posição para guardar
                    if ((pedirPosicaoExp == true) & aux_expedicao == false) {

                        System.out.println(
                                "\n\nEstou aqui -  if ((pedirPosicaoExp == true) & aux_expedicao == false)\n\n");

                        // Rotina para verificar qual posição está disponível para guardar

                        aux_expedicao = true;

                        // ROTINA PARA LOCALIZAR POSIÇÃO DISPONÍVEL NO MAGAZINE DA EXPEDIÇÃO PARA
                        // ADICIONAR BLOCO CONCLUÍDO

                        // Rotina para verificar qual posição está disponível para guardar
                        if (!readOnly) {

                            // Solicita posição disponível para guardar (0-LIVRE 1-PRETO 2-VERMELHO 3-AZUL)
                            // Certifique-se de que posEstoqueRequest é seguro para acesso
                            posExpedicaoRequest = getPositionExpedicao();

                            System.out.println("Posição disponível no Magazine Expedição: " + posExpedicaoRequest);

                            // Atualiza a variável PosicaoGuardarExpedicao no CLP EXPEDIÇÂO
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 4, 2, 0, 1);
                                PanelStart.plcWriteExp.writeInt(9, 4, posExpedicaoRequest);

                            } catch (Exception e) {
                                System.out.println("ERRO: Atualização da PosicaoGuardarExpedicao [DB9:4]");
                            }

                            // Coloca a flag IniciarGuardar em TRUE
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 2, 1, 0, 1);
                                PanelStart.plcWriteExp.writeBit(9, 2, 1, Boolean.parseBoolean("TRUE"));

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [Pedir Posição]: Atualização da Flag IniciarGuardar [DB9:2.1] para TRUE");
                            }

                        }

                    }

                    if (adicionarExpedicao == false & removerExpedicao == false & ocupadoExp == false) {
                        if (readOnly == false) {
                            System.out.println("Flag: RecebidoExpedicao_TRUE");

                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 2, 1, 0, 1);
                                PanelStart.plcWriteExp.writeBit(9, 2, 0, Boolean.parseBoolean("FALSE")); // RecebidoExpedicao

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [Adicionar Expedição]: Atualização da Flag RecebidoExpedicao [DB9:2.0] para FALSE");
                            }

                        }
                    }

                    if ((adicionarExpedicao == true) & aux_expedicao == false) {
                        aux_expedicao = true;

                        // Ler a variável PosicaoGuardadoExpedicao
                        posicaoGuardadoExpedicao = ((infoDB9_Exp[38] & 0xFF) << 8) | (infoDB9_Exp[39] & 0xFF);
                        opGuardadoExpedicao = ((infoDB9_Exp[44] & 0xFF) << 8) | (infoDB9_Exp[45] & 0xFF);

                        if (readOnly == false) {
                            System.out.println("Flag: RecebidoExpediçcao_TRUE");

                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 2, 1, 0, 1);
                                PanelStart.plcWriteExp.writeBit(9, 2, 0, Boolean.parseBoolean("TRUE")); // RecebidoExpedicao

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [Adicionar Expedição]: Atualização da Flag RecebidoExpedicao [DB9:2.0] para TRUE");
                            }
                            // System.out.println("Adicionando OpGuardadoExpedicao em
                            // posicaoGuardarExpedicao");
                            // adicionaRemoveMagazineExpedicao(posicaoGuardadoExpedicao,
                            // opGuardadoExpedicao);

                        }

                    }

                    if (removerExpedicao == true & aux_expedicao == false) { // verifica se Expedição pede posição
                        // para remover
                        aux_expedicao = true;
                        System.out.println("Estou Aqui em => (removerExpedicao == true) & aux_expedicao == false)");

                        // Ler a variável PosicaoRemovidoExpedicao
                        posicaoRemovidoExpedicao = ((infoDB9_Exp[40] & 0xFF) << 8) | (infoDB9_Exp[41] & 0xFF);

                        if (readOnly == false) {
                            System.out.println("Flag: RecebidoExpediçcao_TRUE");
                            try {
                                // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 2, 1, 0, 1);
                                PanelStart.plcWriteExp.writeBit(9, 2, 0, Boolean.parseBoolean("TRUE")); // RecebidoExpedicao

                            } catch (Exception e) {
                                System.out.println(
                                        "ERRO [Adicionar Expedição]: Atualização da Flag RecebidoExpedicao [DB9:2.0] para TRUE");
                            }
                            System.out.println("Removendo Operacao de posicaoREmovidoExpedicao");
                            adicionaRemoveMagazineExpedicao(posicaoRemovidoExpedicao, 0);
                            updatePlcExpedicao();

                        }

                    }

                    if ((posicaoGuardadoExpedicao == posicaoGuardarExpedicao) & (ocupadoExp == false)
                            & (blockFinished == true)
                            & (stationWorkId == 4)) {

                        System.out.println("!!! posicaoGuardadoExpedicao == posicaoGuardarExpedicao !!!");
                        logSmart.add("!!! posicaoGuardadoExpedicao[" + posicaoGuardadoExpedicao
                                + "] == posicaoGuardarExpedicao[" + posicaoGuardarExpedicao + "] !!!");

                        if (pedidoEmCurso == true) {
                            stationWorkId = 5;
                        } else {
                            stationWorkId = 0;
                        }

                        posExpedArray[12] = Integer.parseInt(Panel3.txtNumPedido.getText());
                        updateDisplayStation();
                        System.out.println("Adicionando OpGuardadoExpedicao em posicaoGuardarExpedicao");
                        adicionaRemoveMagazineExpedicao(posicaoGuardadoExpedicao, opGuardadoExpedicao);

                        if (readOnly == false) {
                            /* */
                            for (int x = 0; x < 12; x++) {
                                System.out.print(MainFrame.posExpedArray[x] + " - ");
                            }
                            System.out.println("");
                            updatePlcExpedicao();
                        }
                    }
                    seqSmart++;
                    try {
                        // PanelStart.plcWriteExp.disconnect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }
                }
                //
            }

            else if (opDB.equals("DB20_Exp")) {

                // Leitura do Status da Estação EXPEDIÇÃO
                byte[] strStatusExp = new byte[infoDB20_Exp[20]];

                for (int c = 0; c < infoDB20_Exp[21]; c++) {
                    strStatusExp[c] = infoDB20_Exp[c + 22];

                }
                PanelStatus.txtStatExpedi.setText(new String(strStatusExp).trim());
                PanelStart.lblStatusExp.setText(new String(strStatusExp).trim());

                PanelStart.pnlPLCBlock.revalidate();
                PanelStart.pnlPLCBlock.repaint();

                PanelStart.pnlOpSmart.revalidate();
                PanelStart.pnlOpSmart.repaint();

                // Leitura se a Estação EXPEDIÇÂO está ligada
                if ((infoDB20_Exp[0] & 0x01) == 1) {
                    PanelStatus.lblStatExpedi.setBackground(corBackPnlOn);
                    PanelStatus.chkExpedi.setSelected(true);
                } else {
                    PanelStatus.lblStatExpedi.setBackground(corBackPnl);
                    PanelStatus.chkExpedi.setSelected(false);
                }

            }

        }

    }

    public static void removeAdicionaMagazineEstoque(int posicao, byte cor) {

        blockArray[posicao - 1] = cor;

        Panel3.createMagazineEstoque();
        PanelStart.updateEstoque();
        PanelStart.pnlPLCBlock.revalidate();
        PanelStart.pnlPLCBlock.repaint();

    }

    public static void adicionaRemoveMagazineExpedicao(int posicao, int op) {

        // Atualiza vetor de posições da expedição
        int[] posExped = { 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28 };

        posExpedArray[posicao - 1] = op;

        System.out.println("Posição: " + posicao + "  - Operação:" + op);

        if (readOnly == false) {

            PanelStart.plcWriteExp = new PlcConnector(ipExpedicao, 102);

            try {
                PanelStart.plcWriteExp.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            try {

                // PanelStart.plcWriteExp = new PlcConnector(ipExpedicao, 9, posExped[posicao -
                // 1], 1, 0, 1);
                PanelStart.plcWriteExp.writeInt(9, posExped[posicao - 1], 0);
                Thread.sleep(50);

            } catch (InterruptedException e) {
                System.out.println(
                        "ERRO [ATUALIZAR PLC Expedição]: Atualização das Posições do Magazine EXPEDIÇÃO [DB9:6-28]");
            } catch (Exception e) {

                e.printStackTrace();
            }

            try {
                PanelStart.plcWriteExp.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }
        }

        saveMagazineEpedicao();
        PanelStart.updateExpedicao();
        PanelStart.pnlPLCBlock.revalidate();
        PanelStart.pnlPLCBlock.repaint();

    }

    public static void updateMagazineExpedicao() {
        if (posExpedArray[0] > 0) {
            Panel2.txtPos1Exped.setText("OP" + String.valueOf(posExpedArray[0]));
        } else {
            Panel2.txtPos1Exped.setText("");
        }
        if (posExpedArray[1] > 0) {
            Panel2.txtPos2Exped.setText("OP" + String.valueOf(posExpedArray[1]));
        } else {
            Panel2.txtPos2Exped.setText("");
        }
        if (posExpedArray[2] > 0) {
            Panel2.txtPos3Exped.setText("OP" + String.valueOf(posExpedArray[2]));
        } else {
            Panel2.txtPos3Exped.setText("");
        }
        if (posExpedArray[3] > 0) {
            Panel2.txtPos4Exped.setText("OP" + String.valueOf(posExpedArray[3]));
        } else {
            Panel2.txtPos4Exped.setText("");
        }
        if (posExpedArray[4] > 0) {
            Panel2.txtPos5Exped.setText("OP" + String.valueOf(posExpedArray[4]));
        } else {
            Panel2.txtPos5Exped.setText("");
        }
        if (posExpedArray[5] > 0) {
            Panel2.txtPos6Exped.setText("OP" + String.valueOf(posExpedArray[5]));
        } else {
            Panel2.txtPos6Exped.setText("");
        }
        if (posExpedArray[6] > 0) {
            Panel2.txtPos7Exped.setText("OP" + String.valueOf(posExpedArray[6]));
        } else {
            Panel2.txtPos7Exped.setText("");
        }
        if (posExpedArray[7] > 0) {
            Panel2.txtPos8Exped.setText("OP" + String.valueOf(posExpedArray[7]));
        } else {
            Panel2.txtPos8Exped.setText("");
        }
        if (posExpedArray[8] > 0) {
            Panel2.txtPos9Exped.setText("OP" + String.valueOf(posExpedArray[8]));
        } else {
            Panel2.txtPos9Exped.setText("");
        }
        if (posExpedArray[9] > 0) {
            Panel2.txtPos10Exped.setText("OP" + String.valueOf(posExpedArray[9]));
        } else {
            Panel2.txtPos10Exped.setText("");
        }
        if (posExpedArray[10] > 0) {
            Panel2.txtPos11Exped.setText("OP" + String.valueOf(posExpedArray[10]));
        } else {
            Panel2.txtPos11Exped.setText("");
        }
        if (posExpedArray[11] > 0) {
            Panel2.txtPos12Exped.setText("OP" + String.valueOf(posExpedArray[11]));
        } else {
            Panel2.txtPos12Exped.setText("");
        }

    }

    public static void saveMagazineEpedicao() {

        String positionOps = "";

        for (int i = 0; i < 13; i++) {
            positionOps += "P" + (i + 1) + "=" + String.valueOf(posExpedArray[i]) + "\n";

        }

        // Escreve o timestamp no arquivo
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/main/java/com/smart40/MagazineExp.txt", false))) {
            writer.write(positionOps); // Adiciona o timestamp e uma nova linha
            System.out.println("Magazine Expedição atualizado com sucesso!");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void loadMagazineExpedicao() {
        int index = 0;
        // Lê o arquivo linha por linha
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/smart40/magazineexp.txt"))) {
            String linha;

            // Enquanto houver linhas no arquivo, continue lendo
            while ((linha = reader.readLine()) != null) {

                posExpedArray[index] = Integer.parseInt(linha.substring(linha.indexOf("=") + 1, linha.length()));

                index++;
            }
            // posExpedArray[12] = posExpedArray[12] + 1;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void loadMagazineEstoque() {
        // String filePath = "magazineest.txt"; // Caminho do arquivo
        int index = 0;
        // Lê o arquivo linha por linha
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/smart40/magazineest.txt"))) {
            String linha;

            // Enquanto houver linhas no arquivo, continue lendo
            while ((linha = reader.readLine()) != null) {

                blockArray[index] = Byte.parseByte(linha.substring(linha.indexOf("=") + 1, linha.length()));

                index++;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void updateDisplayStation() {

        System.out.println("stationWorkId = " + stationWorkId);
        System.out.println("pedidoEMCurso = " + pedidoEmCurso);

        switch (stationWorkId) {
            case 1 -> {
                Panel3.pnlEst.setBorder(yellow);
                PanelStart.pnlOpEst.setBackground(new Color(255, 255, 10, 95));
            }
            case 2 -> {
                Panel3.pnlEst.setBorder(onGreenBorder);
                Panel3.pnlPro.setBorder(yellow);
                PanelStart.pnlOpEst.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpPro.setBackground(new Color(255, 255, 10, 95));
            }
            case 3 -> {
                Panel3.pnlEst.setBorder(onGreenBorder);
                Panel3.pnlPro.setBorder(onGreenBorder);
                Panel3.pnlMon.setBorder(yellow);
                PanelStart.pnlOpEst.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpPro.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpMon.setBackground(new Color(255, 255, 10, 95));
            }
            case 4 -> {
                Panel3.pnlEst.setBorder(onGreenBorder);
                Panel3.pnlPro.setBorder(onGreenBorder);
                Panel3.pnlMon.setBorder(onGreenBorder);
                Panel3.pnlExp.setBorder(yellow);
                PanelStart.pnlOpEst.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpPro.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpMon.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpExp.setBackground(new Color(255, 255, 10, 95));
            }
            case 5 -> {
                Panel3.pnlEst.setBorder(onGreenBorder);
                Panel3.pnlPro.setBorder(onGreenBorder);
                Panel3.pnlMon.setBorder(onGreenBorder);
                Panel3.pnlExp.setBorder(onGreenBorder);
                PanelStart.pnlOpEst.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpPro.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpMon.setBackground(new Color(120, 255, 120, 95));
                PanelStart.pnlOpExp.setBackground(new Color(120, 255, 120, 95));
                Panel3.orderFinishedButton.setVisible(true);
                PanelStart.orderFinishedButton.setVisible(true);
            }
            default -> {
                Panel3.pnlEst.setBorder(offGreenBorder);
                Panel3.pnlPro.setBorder(offGreenBorder);
                Panel3.pnlMon.setBorder(offGreenBorder);
                Panel3.pnlExp.setBorder(offGreenBorder);
                PanelStart.pnlOpEst.setBackground(new Color(120, 255, 120, 0));
                PanelStart.pnlOpPro.setBackground(new Color(120, 255, 120, 0));
                PanelStart.pnlOpMon.setBackground(new Color(120, 255, 120, 0));
                PanelStart.pnlOpExp.setBackground(new Color(120, 255, 120, 0));
            }
        }
        PanelStart.pnlPLCBlock.revalidate();
        PanelStart.pnlPLCBlock.repaint();

        PanelStart.pnlOpSmart.revalidate();
        PanelStart.pnlOpSmart.repaint();
    }

    public static int getPositionExpedicao() {

        loadMagazineExpedicao();

        int pos = 0;

        for (int c = 0; c < 12; c++) {
            if (posExpedArray[c] == 0) {
                pos = c + 1;
                break;
            }
        }

        for (int c = 0; c < 12; c++) {
            System.out.println("posExpedArray[" + c + "]=" + posExpedArray[c]);
        }

        return pos;

    }

    public static int getPositionEstoque(int tipo) {

        loadMagazineEstoque();

        int pos = 0;

        for (int c = 0; c < 28; c++) {
            if (blockArray[c] == tipo) {
                pos = c + 1;
                break;
            }
        }

        for (int c = 0; c < 28; c++) {
            System.out.println("blockArray[" + c + "]=" + blockArray[c]);
        }

        return pos;

    }

    public static void updatePlcExpedicao() {

        byte[] posExpedArrayBytes = converterIntParaByte(posExpedArray);

        if (MainFrame.readOnly == false) {
            //
            PanelStart.plcWriteExp = new PlcConnector(MainFrame.ipExpedicao, 102);
            try {
                PanelStart.plcWriteExp.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            try {

                if (PanelStart.plcWriteExp.writeBlock(9, 6, 24, Arrays.copyOfRange(posExpedArrayBytes, 0, 24))) {
                    // System.out.println("Atualizado com Sucesso");
                }

            } catch (Exception e) {
                System.out.println(
                        "ERRO [ATUALIZAR PLC Expedição]: Atualização das Posições do Magazine EXPEDIÇÃO [DB9:6-28]");
            }

            try {
                PanelStart.plcWriteExp.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

        }
    }

    public static byte[] converterIntParaByte(int[] posExpedInt) {
        // O tamanho do array de bytes será o dobro do tamanho do array de shorts
        byte[] posExpedBytes = new byte[posExpedInt.length * 2];

        for (int i = 0; i < posExpedInt.length; i++) {
            // Cada short ocupa 2 bytes
            posExpedBytes[i * 2] = (byte) (posExpedInt[i] >> 8); // Byte mais significativo
            posExpedBytes[i * 2 + 1] = (byte) (posExpedInt[i] & 0xFF); // Byte menos significativo
        }

        return posExpedBytes;
    }

    public static void updatePLCEstoque() {

        if (MainFrame.readOnly == false) {

            PanelStart.plcWriteEst = new PlcConnector(MainFrame.ipEstoque, 102);

            try {
                PanelStart.plcWriteEst.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            try {
                // Panel3.plcWrite = new PlcConnector(MainFrame.ipEstoque, 9, 68, 28, 0, 1);
                PanelStart.plcWriteEst.writeBlock(9, 68, 28, MainFrame.blockArray);

            } catch (Exception e) {
                System.out
                        .println("ERRO [Atualização Magazine Estoque]: Atualização das posições de Blocos [DB9:68.0]");
            }

            try {
                PanelStart.plcWriteEst.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        // Cria a interface gráfica na thread do Swing
        SwingUtilities.invokeLater(() -> new MainFrame());

    }

}
