package com.smart40;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

//import java.nio.ByteBuffer;

public class Panel1 extends JPanel {
    public static JPanel pnlPLCVar;

    // PLC
    private final JLabel lblHost;
    private final JLabel lblRack;
    private final JLabel lblSlot;
    private final JLabel lblDB;
    private final JLabel lblOffset;
    private final JLabel lblTypeTag;
    private final JLabel lblSizeTag;
    private final JLabel lblGiro;
    private final JLabel lblLinear;
    private final JLabel lblTagPLC;
    private final JLabel lblMagzine;

    public static JTextField textHostPLC;
    public JTextField textRackPLC;
    public JTextField textSlotPLC;
    public static JTextField textDBPLC;
    public static JTextField textOffsetPLC;

    static JTextField textTypeTag;
    static JTextField textSizeTag;

    static JTextField txtReadPLCTag;

    static JCheckBox chkBxVar;

    public JButton btnS7PLCStart;
    public JButton btnS7PLCStop;

    static JButton btnS7PLCRead;
    private final JButton btnS7PLCWrite;

    public static JTextField txtReadPLCGiro;
    public static JTextField txtReadPLCLinear;

    public static JPanel pnlPLCBlock;

    public static byte[] var1;

    public static PlcConnector plcWrite;

    // public static byte[] infoDB6_Est; // Supervisório Estação ESTOQUE
    // public static byte[] infoDB9_Est;

    // public static byte[] infoDB2_Pro;
    // public static byte[] infoDB6_Pro; // Supervisório Estação PROCESSO

    // public static byte[] infoDB57_Mon;
    // public static byte[] infoDB30_Mon; // Supervisório Estação ESTOQUE
    // public static byte[] infoDB60_Mon; // Supervisório Estação EXPEDIÇÃO
    // public static byte[] infoDB92_Mon; // Supervisório Estação MONTAGEM
    // public static byte[] infoDB600_Mon; // Supervisório Estação PROCESSO

    // public static byte[] infoDB9_Exp;
    // public static byte[] infoDB20_Exp; // Supervisório Estação EXPEDIÇÃO

    // private static JLabel lblStatEstoq;
    // private static JTextField txtStatEstoq;
    // private static JCheckBox chkEstoq;

    int pnlPlcVarX = 110;
    int pnlPlcVarY = 5;

    /*----- Variáveis de definição de Fontes Cores e Bordas */
    private static final Color corBackPnl = new Color(10, 50, 50);
    Color corTextLabel = new Color(250, 250, 250);
    Color corTextField = new Color(50, 90, 90);

    Color corTextFieldEst = new Color(10, 10, 10);
    public static Color lightGreen = new Color(10, 255, 50);

    static Color corJTFfont = new Color(50, 255, 255);
    // Color corJLblTitle = new Color(30, 100, 190);
    // private static Color corBackPnlOn = new Color(10, 120, 50);
    Font font14 = new Font("Arial", Font.PLAIN, 14);
    Font font14Bold = new Font("Arial", Font.BOLD, 14);
    // Font font16 = new Font("Arial", Font.PLAIN, 16);
    // Font font18 = new Font("Arial", Font.PLAIN, 18);
    // Font font22 = new Font("Arial", Font.BOLD, 22);
    // Font fontTitle = new Font("Arial", Font.BOLD, 32);

    Border border = BorderFactory.createLineBorder(Color.WHITE, 1);

    public static Border gray = BorderFactory.createLineBorder(Color.WHITE);
    public static Border red = BorderFactory.createLineBorder(Color.red, 2);
    public static Border greenOn = BorderFactory.createLineBorder(new Color(0, 255, 0), 2);
    public static Border greenOff = BorderFactory.createLineBorder(new Color(0, 128, 0), 2);
    public static Border yellow = BorderFactory.createLineBorder(Color.yellow, 1);
    public static Border lGreenBorder = BorderFactory.createLineBorder(lightGreen, 2);
    /*----------------------------------------------------------------------- */

    public static byte[] indexColor = new byte[28];

    public Panel1(MainFrame mainframe, int w, int h) {
        // Configura o layout do painel
        setSize(w, h);
        setLayout(null);
        setVisible(true);
        // setBackground(corBackPnl);

        setBackground(Color.black);

        // this.setBackground(new Color(100, 200, 250));

        // Cria os componentes

        // ----------- Estação Estoque ---------------------------------
        pnlPLCVar = new JPanel();
        pnlPLCVar.setBounds(15, 10, 655, 660);
        pnlPLCVar.setLayout(null);
        pnlPLCVar.setBorder(greenOff);
        pnlPLCVar.setBackground(corBackPnl);
        pnlPLCVar.setOpaque(false);

        ImagePanel estoque = new ImagePanel("src/main/java/com/smart40/smart/estoque1.png", 10, 30, 0.8);
        estoque.setBounds(10, 60, 350, 590);
        estoque.setBackground(corBackPnl);
        estoque.setOpaque(false);
        estoque.setBorder(gray);
        pnlPLCVar.add(estoque);

        lblHost = new JLabel("ESTAÇÃO ESTOQUE IP");
        lblHost.setBounds(10, pnlPlcVarY + 5, 210, 30);
        lblHost.setBorder(gray);
        lblHost.setForeground(corTextLabel);
        lblHost.setHorizontalAlignment(SwingConstants.CENTER);
        lblHost.setFont(font14);
        pnlPLCVar.add(lblHost);

        textHostPLC = new JTextField();
        textHostPLC.setText(MainFrame.ipEstoque);
        textHostPLC.setBounds(pnlPlcVarX + 115, pnlPlcVarY + 5, 135, 30);
        textHostPLC.setBackground(corTextFieldEst);
        textHostPLC.setBorder(border);
        textHostPLC.setFont(font14Bold);
        textHostPLC.setForeground(corJTFfont);
        textHostPLC.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(textHostPLC);

        lblRack = new JLabel("RACK");
        lblRack.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 5, 80, 30);
        lblRack.setBorder(gray);
        lblRack.setForeground(corTextLabel);
        lblRack.setHorizontalAlignment(SwingConstants.CENTER);
        lblRack.setFont(font14);
        pnlPLCVar.add(lblRack);

        textRackPLC = new JTextField("0");
        textRackPLC.setBounds(pnlPlcVarX + 345, pnlPlcVarY + 5, 50, 30);
        textRackPLC.setBackground(corTextFieldEst);
        textRackPLC.setBorder(border);
        textRackPLC.setFont(font14Bold);
        textRackPLC.setForeground(corJTFfont);
        textRackPLC.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(textRackPLC);

        lblSlot = new JLabel("SLOT");
        lblSlot.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 5, 80, 30);
        lblSlot.setBorder(gray);
        lblSlot.setForeground(corTextLabel);
        lblSlot.setHorizontalAlignment(SwingConstants.CENTER);
        lblSlot.setFont(font14);
        pnlPLCVar.add(lblSlot);

        textSlotPLC = new JTextField("1");
        textSlotPLC.setBounds(pnlPlcVarX + 485, pnlPlcVarY + 5, 50, 30);
        textSlotPLC.setBackground(corTextFieldEst);
        textSlotPLC.setBorder(border);
        textSlotPLC.setFont(font14Bold);
        textSlotPLC.setForeground(corJTFfont);
        textSlotPLC.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(textSlotPLC);

        lblDB = new JLabel("DB");
        lblDB.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 55, 80, 30);
        lblDB.setBorder(gray);
        lblDB.setForeground(corTextLabel);
        lblDB.setHorizontalAlignment(SwingConstants.CENTER);
        lblDB.setFont(font14);
        pnlPLCVar.add(lblDB);

        textDBPLC = new JTextField();
        textDBPLC.setBounds(pnlPlcVarX + 345, pnlPlcVarY + 55, 50, 30);
        textDBPLC.setBackground(corTextFieldEst);
        textDBPLC.setBorder(border);
        textDBPLC.setFont(font14Bold);
        textDBPLC.setForeground(corJTFfont);
        textDBPLC.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(textDBPLC);

        lblOffset = new JLabel("OFFSET");
        lblOffset.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 55, 80, 30);
        lblOffset.setBorder(gray);
        lblOffset.setForeground(corTextLabel);
        lblOffset.setHorizontalAlignment(SwingConstants.CENTER);
        lblOffset.setFont(font14);
        pnlPLCVar.add(lblOffset);

        textOffsetPLC = new JTextField();
        textOffsetPLC.setBounds(pnlPlcVarX + 485, pnlPlcVarY + 55, 50, 30);
        textOffsetPLC.setBackground(corTextFieldEst);
        textOffsetPLC.setBorder(border);
        textOffsetPLC.setFont(font14Bold);
        textOffsetPLC.setForeground(corJTFfont);
        textOffsetPLC.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(textOffsetPLC);

        lblTypeTag = new JLabel("TIPO");
        lblTypeTag.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 95, 80, 30);
        lblTypeTag.setBorder(gray);
        lblTypeTag.setForeground(corTextLabel);
        lblTypeTag.setHorizontalAlignment(SwingConstants.CENTER);
        lblTypeTag.setFont(font14);
        pnlPLCVar.add(lblTypeTag);

        textTypeTag = new JTextField();
        textTypeTag.setBounds(pnlPlcVarX + 345, pnlPlcVarY + 95, 50, 30);
        textTypeTag.setBackground(corTextFieldEst);
        textTypeTag.setBorder(border);
        textTypeTag.setFont(font14Bold);
        textTypeTag.setForeground(corJTFfont);
        textTypeTag.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(textTypeTag);

        lblSizeTag = new JLabel("TAMANHO");
        lblSizeTag.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 95, 80, 30);
        lblSizeTag.setBorder(gray);
        lblSizeTag.setForeground(corTextLabel);
        lblSizeTag.setHorizontalAlignment(SwingConstants.CENTER);
        lblSizeTag.setFont(font14);
        pnlPLCVar.add(lblSizeTag);

        textSizeTag = new JTextField();
        textSizeTag.setBounds(pnlPlcVarX + 485, pnlPlcVarY + 95, 50, 30);
        textSizeTag.setBackground(corTextFieldEst);
        textSizeTag.setBorder(border);
        textSizeTag.setFont(font14Bold);
        textSizeTag.setForeground(corJTFfont);
        textSizeTag.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(textSizeTag);

        lblTagPLC = new JLabel("VALOR TAG");
        lblTagPLC.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 135, 135, 30);
        lblTagPLC.setBorder(gray);
        lblTagPLC.setForeground(corTextLabel);
        lblTagPLC.setHorizontalAlignment(SwingConstants.CENTER);
        lblTagPLC.setFont(font14);
        pnlPLCVar.add(lblTagPLC);

        txtReadPLCTag = new JTextField();
        txtReadPLCTag.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 135, 135, 30);
        txtReadPLCTag.setBackground(corTextFieldEst);
        txtReadPLCTag.setBorder(border);
        txtReadPLCTag.setFont(font14Bold);
        txtReadPLCTag.setForeground(corJTFfont);
        txtReadPLCTag.setHorizontalAlignment(SwingConstants.CENTER);
        txtReadPLCTag.setVisible(true);
        pnlPLCVar.add(txtReadPLCTag);

        chkBxVar = new JCheckBox();
        chkBxVar.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 135, 135, 30);
        chkBxVar.setBackground(corTextField);
        chkBxVar.setText("FALSE");
        chkBxVar.setBorder(border);
        chkBxVar.setFont(font14Bold);
        chkBxVar.setForeground(corJTFfont);
        chkBxVar.setHorizontalAlignment(SwingConstants.CENTER);
        chkBxVar.setVisible(false);
        pnlPLCVar.add(chkBxVar);

        chkBxVar.addActionListener((ActionEvent e) -> {
            if (chkBxVar.isSelected()) {
                chkBxVar.setText("TRUE");
            } else {
                chkBxVar.setText("FALSE");
            }
        });

        btnS7PLCRead = new JButton("LEITURA");
        btnS7PLCRead.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 175, 135, 30);
        btnS7PLCRead.setFont(font14);
        btnS7PLCRead.setForeground(corTextLabel);
        btnS7PLCRead.setBackground(corTextField);
        // btnS7PLCLinear.setBorder(border);
        pnlPLCVar.add(btnS7PLCRead);

        btnS7PLCWrite = new JButton("ESCRITA");
        btnS7PLCWrite.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 175, 135, 30);
        btnS7PLCWrite.setFont(font14);
        btnS7PLCWrite.setForeground(corTextLabel);
        btnS7PLCWrite.setBackground(corTextField);

        btnS7PLCWrite.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int startAddress = 0x00;
                int bitNum = 0;
                boolean tagBool = Boolean.parseBoolean(
                        txtReadPLCTag.getText().equals("1") ? "true".toUpperCase() : "false".toUpperCase());

                if (textOffsetPLC.getText().indexOf(".") > 0) {

                    startAddress = Integer.parseInt(textOffsetPLC.getText().substring(0,
                            textOffsetPLC.getText().indexOf(".")));
                    bitNum = Integer.parseInt(textOffsetPLC.getText().substring(
                            textOffsetPLC.getText().indexOf(".") + 1,
                            textOffsetPLC.getText().indexOf(".") + 2));
                } else {
                    startAddress = Integer.parseInt(textOffsetPLC.getText());
                    bitNum = 0;
                }

                plcWrite = new PlcConnector(textHostPLC.getText(), 102);

                try {
                    plcWrite.connect();
                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                // PanelStart.plcWriteEst.writeInt(Integer.parseInt(textDBPLC.getText()),
                // startAddress, Integer.parseInt(textSizeTag.getText()), 0, 1);

                if (textTypeTag.getText().equals("X")) {
                    // byte valueTag = (byte) ByteBuffer.wrap(plcRead.readDataBlock()).getChar();
                    // System.out.println("AQUI_1");
                    if (textOffsetPLC.getText().indexOf(".") > 0) {
                        // System.out.println("AQUI_1 - BOOLEAN");

                        try {

                            plcWrite.writeBit(Integer.parseInt(textDBPLC.getText()), startAddress, bitNum,
                                    Boolean.parseBoolean(chkBxVar.getText()));
                        } catch (Exception ex) {

                        }

                        try {
                            plcWrite.disconnect();
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }

                    } else {

                        // System.out.println("AQUI_2 - BYTE");
                        try {
                            plcWrite.writeByte(Integer.parseInt(textDBPLC.getText()),
                                    Integer.parseInt(textOffsetPLC.getText()), Byte.parseByte(txtReadPLCTag.getText()));
                        } catch (Exception ex) {
                        }

                        try {
                            plcWrite.disconnect();
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }

                    }

                } else if (textTypeTag.getText().equals("S")) {

                    System.out.println("string = " + txtReadPLCTag.getText().trim());

                    try {
                        plcWrite.writeString(Integer.parseInt(textDBPLC.getText()),
                                Integer.parseInt(textOffsetPLC.getText()), Integer.parseInt(textSizeTag.getText()),
                                txtReadPLCTag.getText().trim());
                    } catch (Exception ex) {
                    }

                    try {
                        plcWrite.disconnect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                } else if (textTypeTag.getText().equals("F")) {

                    try {
                        plcWrite.writeFloat(Integer.parseInt(textDBPLC.getText()),
                                Integer.parseInt(textOffsetPLC.getText()), Float.parseFloat(txtReadPLCTag.getText()));
                    } catch (Exception ex) {
                    }

                    try {
                        plcWrite.disconnect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                } else if (textTypeTag.getText().equals("I")) {
                    try {
                        plcWrite.writeInt(Integer.parseInt(textDBPLC.getText()),
                                Integer.parseInt(textOffsetPLC.getText()), Integer.parseInt(txtReadPLCTag.getText()));
                    } catch (NumberFormatException e1) {

                        e1.printStackTrace();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                    try {
                        plcWrite.disconnect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                }

                /*
                 * plcRead.writeDataBlock(Integer.parseInt(form.textDBPLC.getText()),
                 * Integer.parseInt(form.textOffsetPLC.getText()),
                 * txtReadPLCTag.getText().getBytes());
                 */
                /*
                 * System.out.println(mywriteString(Integer.parseInt(form.textDBPLC.getText()),
                 * Integer.parseInt(form.textOffsetPLC.getText()),
                 * txtReadPLCTag.getText().trim(),
                 * Integer.parseInt(form.textSizeTag.getText())));
                 * 
                 * form.updatePosBlock();
                 */
            }

        });

        // btnS7PLCGiro.setBorder(border);
        pnlPLCVar.add(btnS7PLCWrite);

        lblLinear = new JLabel("POS. LINEAR");
        lblLinear.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 225, 135, 30);
        lblLinear.setBorder(gray);
        lblLinear.setForeground(corTextLabel);
        lblLinear.setHorizontalAlignment(SwingConstants.CENTER);
        lblLinear.setFont(font14);
        pnlPLCVar.add(lblLinear);

        txtReadPLCLinear = new JTextField();
        txtReadPLCLinear.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 225, 135, 30);
        txtReadPLCLinear.setBackground(corTextFieldEst);
        txtReadPLCLinear.setBorder(border);
        txtReadPLCLinear.setFont(font14Bold);
        txtReadPLCLinear.setForeground(corJTFfont);
        txtReadPLCLinear.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(txtReadPLCLinear);

        lblGiro = new JLabel("POS. GIRO");
        lblGiro.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 265, 135, 30);
        lblGiro.setBorder(gray);
        lblGiro.setForeground(corTextLabel);
        lblGiro.setHorizontalAlignment(SwingConstants.CENTER);
        lblGiro.setFont(font14);
        pnlPLCVar.add(lblGiro);

        txtReadPLCGiro = new JTextField();
        txtReadPLCGiro.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 265, 135, 30);
        txtReadPLCGiro.setBackground(corTextFieldEst);
        txtReadPLCGiro.setBorder(border);
        txtReadPLCGiro.setFont(font14Bold);
        txtReadPLCGiro.setForeground(corJTFfont);
        txtReadPLCGiro.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPLCVar.add(txtReadPLCGiro);

        btnS7PLCStart = new JButton("START PLC");
        btnS7PLCStart.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 305, 135, 30);
        btnS7PLCStart.setFont(font14);
        btnS7PLCStart.setEnabled(true);
        btnS7PLCStart.setForeground(corTextLabel);
        btnS7PLCStart.setBackground(corTextField);
        // btnS7PLCLinear.setBorder(border);
        pnlPLCVar.add(btnS7PLCStart);

        btnS7PLCStop = new JButton("STOP PLC");
        btnS7PLCStop.setBounds(pnlPlcVarX + 400, pnlPlcVarY + 305, 135, 30);
        btnS7PLCStop.setFont(font14);
        btnS7PLCStop.setEnabled(false);
        btnS7PLCStop.setForeground(corTextLabel);
        btnS7PLCStop.setBackground(corTextField);
        // btnS7PLCGiro.setBorder(border);
        pnlPLCVar.add(btnS7PLCStop);

        // Posições do Magzine do Estoque

        lblMagzine = new JLabel("MAGAZINE ESTAÇÃO ESTOQUE");
        lblMagzine.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 365, 275, 30);
        lblMagzine.setBorder(gray);
        lblMagzine.setForeground(corTextLabel);
        lblMagzine.setHorizontalAlignment(SwingConstants.CENTER);
        lblMagzine.setFont(font14);
        pnlPLCVar.add(lblMagzine);

        pnlPLCBlock = new JPanel();
        pnlPLCBlock.setBounds(pnlPlcVarX + 260, pnlPlcVarY + 400, 275, 245);
        pnlPLCBlock.setLayout(null);
        pnlPLCBlock.setBorder(gray);
        pnlPLCBlock.setBackground(corBackPnl);
        pnlPLCBlock.setOpaque(false);
        pnlPLCVar.add(pnlPLCBlock);

        textOffsetPLC.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Método chamado quando uma tecla é pressionada e solta
                char keyChar = e.getKeyChar();
                // System.out.println("Tecla digitada: " + keyChar);
                if (keyChar == '.') {
                    // System.out.println("PONTO: " + keyChar);

                    // textTypeTag.removeAll();
                    textTypeTag.setText("X");
                    // textTypeTag.revalidate();
                    textTypeTag.repaint();
                    // textSizeTag.removeAll();
                    textSizeTag.setText("1");
                    // textSizeTag.revalidate();
                    textSizeTag.repaint();

                    txtReadPLCTag.setVisible(false);
                    txtReadPLCTag.repaint();

                    chkBxVar.setVisible(true);
                    chkBxVar.repaint();

                } else {
                    if (!textOffsetPLC.getText().contains(".")) {
                        textTypeTag.setText("");
                        textTypeTag.repaint();
                        textSizeTag.setText("");
                        textSizeTag.repaint();

                        txtReadPLCTag.setVisible(true);
                        txtReadPLCTag.repaint();

                        chkBxVar.setVisible(false);
                        chkBxVar.repaint();
                    }

                }

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Método chamado quando uma tecla é pressionada
                char keyChar = e.getKeyChar();
                if (keyChar == '.') {
                    // System.out.println("PONTO: " + keyChar);

                    // textTypeTag.removeAll();
                    textTypeTag.setText("X");
                    // textTypeTag.revalidate();
                    textTypeTag.repaint();
                    // textSizeTag.removeAll();
                    textSizeTag.setText("1");
                    // textSizeTag.revalidate();
                    textSizeTag.repaint();

                } else {
                    if (!textOffsetPLC.getText().contains(".")) {
                        textTypeTag.setText("");
                        textTypeTag.repaint();
                        textSizeTag.setText("");
                        textSizeTag.repaint();
                    }

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Método chamado quando uma tecla é solta
            }

        });

        textTypeTag.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

                char keyChar = e.getKeyChar();
                // System.out.println("Tecla digitada: " + keyChar);
                if (keyChar == 'X') {

                    textSizeTag.setText("1");
                    // textSizeTag.revalidate();
                    textSizeTag.repaint();

                } else {
                    if (!textOffsetPLC.getText().contains(".")) {
                        textSizeTag.setText("");
                        textSizeTag.repaint();
                    }

                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

                char keyChar = e.getKeyChar();
                if (keyChar == 'X') {

                    // textSizeTag.removeAll();
                    textSizeTag.setText("1");
                    // textSizeTag.revalidate();
                    textSizeTag.repaint();

                } else {
                    if (!textOffsetPLC.getText().contains(".")) {
                        textSizeTag.setText("");
                        textSizeTag.repaint();
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });

        // Adiciona os componentes ao painel
        add(pnlPLCVar);

    }

    public static void updateEstoque() {
        SwingUtilities.invokeLater(() -> {
            // System.out.println("Atualização das Posições de Blocos...");
            int largura = 36;
            int altura = 36;
            int espaco = 10;
            for (int i = 0; i < 28; i++) {
                JPanel panel = new JPanel();
                panel.setSize(largura, altura); // Define o tamanho do JPanel
                panel.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Adiciona uma
                // borda para visualização
                panel.setBackground(Color.LIGHT_GRAY); // Define a cor de fundo do JPanel

                JLabel label = new JLabel("P" + (i + 1), SwingConstants.CENTER);
                label.setSize(largura, altura); // Define o tamanho do JLabel
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Adiciona uma borda para visualização
                label.setOpaque(true); // Necessário para definir a cor de fundo
                label.setBorder(gray);
                label.setForeground(corJTFfont); // Define a cor de fundo do JLabel
                switch (Panel1.indexColor[i]) {
                    case 1 -> label.setBackground(Color.BLACK); // Define a cor de fundo do JLabel
                    case 2 -> label.setBackground(Color.RED); // Define a cor de fundo do JLabel
                    case 3 -> label.setBackground(Color.BLUE); // Define a cor de fundo do JLabel
                    default -> label.setBackground(Color.WHITE); // Define a cor de fundo do JLabel
                }

                // Calcula a posição do JPanel
                int x = (i % 6) * (largura + espaco);
                int y = (i / 6) * (altura + espaco);

                // Define a posição do JLabel
                label.setLocation(x + 5, y + 10);

                // Adiciona o JLabel ao JPanel
                Panel1.pnlPLCBlock.add(label);

                Panel1.pnlPLCBlock.revalidate();
                Panel1.pnlPLCBlock.repaint();

            }
        });
    }
}
