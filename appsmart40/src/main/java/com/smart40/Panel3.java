package com.smart40;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
//import java.util.ArrayList;

public class Panel3 extends JPanel {

    public static JTextField[][] textFields = new JTextField[5][6]; // Matriz 5x6 de JTextFields

    // public static PlcConnector plcWrite; => Movido para o PanelStart

    public static int[] intPedidoArray = new int[30]; // Vetor de bytes de tamanho 30 (5x6 = 30)

    public static JPanel pnlPedido;

    private static JPanel pnlArrayBlocks;

    public static JTextField txtCorB1;
    public static JTextField txtCorB2;
    public static JTextField txtCorB3;

    public static JTextField txtPosB1;
    public static JTextField txtPosB2;
    public static JTextField txtPosB3;

    public static JTextField txtCorL1B1;
    public static JTextField txtCorL1B2;
    public static JTextField txtCorL1B3;

    public static JTextField txtCorL2B1;
    public static JTextField txtCorL2B2;
    public static JTextField txtCorL2B3;

    public static JTextField txtCorL3B1;
    public static JTextField txtCorL3B2;
    public static JTextField txtCorL3B3;

    public static JTextField txtPadL1B1;
    public static JTextField txtPadL1B2;
    public static JTextField txtPadL1B3;

    public static JTextField txtPadL2B1;
    public static JTextField txtPadL2B2;
    public static JTextField txtPadL2B3;

    public static JTextField txtPadL3B1;
    public static JTextField txtPadL3B2;
    public static JTextField txtPadL3B3;

    public static JTextField txtProB1;
    public static JTextField txtProB2;
    public static JTextField txtProB3;

    public static JTextField txtNumPedido;
    public static JTextField txtAndarEst;
    public static JTextField txtPosExped;

    public static JButton enviaPedidoButton;
    public static JButton startPedidoButton;
    public static JButton updateMagazineButton;

    public static JButton orderFinishedButton;

    public static JPanel pnlEst;
    public static JPanel pnlPro;
    public static JPanel pnlMon;
    public static JPanel pnlExp;

    public static int index = 0;

    // public static ArrayList<String> magazineExpedicaoList = new ArrayList<>();

    private static final Color corBackPnl = new Color(10, 50, 50);
    private static final Color lightGreen = new Color(10, 255, 50);
    public static Border gray = BorderFactory.createLineBorder(Color.WHITE);
    public static Border red = BorderFactory.createLineBorder(Color.red, 2);
    public static Border greenOn = BorderFactory.createLineBorder(new Color(0, 255, 0), 2);
    public static Border greenOff = BorderFactory.createLineBorder(new Color(0, 128, 0), 2);
    public static Border yellow = BorderFactory.createLineBorder(Color.yellow, 1);
    public static Border lGreenBorder = BorderFactory.createLineBorder(lightGreen, 2);

    Border border = BorderFactory.createLineBorder(Color.WHITE, 1);

    Color corTextLabel = new Color(250, 250, 250);
    static Color corTextField = new Color(50, 90, 90);
    Color corJLblTitleBg = new Color(30, 100, 130);

    Color corTextFieldEst = new Color(10, 10, 10);
    // Color corTextFieldExp = new Color(10, 10, 10);

    static Color corJTFfont = new Color(50, 255, 255);
    // Color corJLblTitle = new Color(30, 100, 190);

    // private static Color corBackPnlOn = new Color(10, 120, 50);

    Font font14 = new Font("Arial", Font.PLAIN, 14);
    static Font font14Bold = new Font("Arial", Font.BOLD, 14);
    // Font font16 = new Font("Arial", Font.PLAIN, 16);
    // Font font18 = new Font("Arial", Font.PLAIN, 18);
    // Font font22 = new Font("Arial", Font.BOLD, 22);

    // Font fontTitle = new Font("Arial", Font.BOLD, 32);

    public Panel3(MainFrame mainframe, int w, int h) {
        // Configura o layout do painel
        setSize(w, h);
        setLayout(null);
        setVisible(true);

        int resposta = JOptionPane.showConfirmDialog(null, "Você deseja iniciar no modo controlador?", "Confirmação",
                JOptionPane.YES_NO_OPTION);
        MainFrame.readOnly = resposta != JOptionPane.YES_OPTION;
        // setLayout(new FlowLayout());
        // this.setBackground(new Color(250, 200, 100));
        // setBackground(corBackPnl);
        setBackground(Color.black);

        // Cria os componentes
        pnlPedido = new JPanel();
        pnlPedido.setBounds(15, 10, 655, 660);
        pnlPedido.setLayout(null);
        pnlPedido.setBorder(greenOff);
        // pnlPedido.setBackground(corBackPnl);
        pnlPedido.setBackground(Color.black);
        pnlPedido.setOpaque(false);
        pnlPedido.setEnabled(false);

        pnlArrayBlocks = new JPanel(new GridLayout(5, 6)); // Criando um layout de grid 5x6
        pnlArrayBlocks.setBounds(10, 20, 250, 200);
        pnlArrayBlocks.setBackground(corBackPnl);
        pnlArrayBlocks.setBorder(gray);
        pnlPedido.add(pnlArrayBlocks);

        JLabel lblConfPedido = new JLabel("DADOS DE CONFIGURAÇÃO DO PEDIDO");
        lblConfPedido.setBounds(10, 270, 635, 30);
        lblConfPedido.setBorder(gray);
        lblConfPedido.setForeground(corTextLabel);
        lblConfPedido.setBackground(corJLblTitleBg);
        lblConfPedido.setHorizontalAlignment(SwingConstants.CENTER);
        lblConfPedido.setFont(font14Bold);
        lblConfPedido.setOpaque(true);
        pnlPedido.add(lblConfPedido);

        if (MainFrame.readOnly == false) {
            MainFrame.loadMagazineEstoque();

        }
        // index = 0;
        createMagazineEstoque();

        updateMagazineButton = new JButton("ATUALIZAR MAGAZINE");
        updateMagazineButton.setBounds(10, 230, 205, 30);
        updateMagazineButton.setForeground(corTextLabel);
        updateMagazineButton.setBackground(corTextField);
        // System.out.println("AQUI-2 "+ !MainFrame.readOnly);
        ConfigPedido.registrarPedidoBtn.setEnabled(!MainFrame.readOnly);
        updateMagazineButton.setEnabled(!MainFrame.readOnly);

        updateMagazineButton.addActionListener(e -> {

            saveMagazineEstoque();

            // PlcConnector plcWrite = new PlcConnector(Panel1.textHostPLC.getText(), 9, 68,
            // 28, 0, 1);
            // plcWrite.writeDataBlock(9, 68, blockArray);

        });
        JPanel pnlPedidoInfo = new JPanel();
        pnlPedidoInfo.setBounds(10, 310, 635, 330);
        // pnlPedidoInfo.setBorder(gray);
        pnlPedidoInfo.setLayout(null);
        pnlPedidoInfo.setOpaque(false);
        pnlPedido.add(pnlPedidoInfo);

        // Configuração dos itens do pedido
        // ----- Info Pedido ------------------------------------------------
        JLabel lblCorBlk1 = new JLabel("COR BLOCO 1");
        lblCorBlk1.setBounds(0, 0, 140, 30);
        lblCorBlk1.setBorder(gray);
        lblCorBlk1.setForeground(corTextLabel);
        lblCorBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk1.setFont(font14);
        pnlPedidoInfo.add(lblCorBlk1);

        txtCorB1 = new JTextField();
        txtCorB1.setBounds(145, 0, 60, 30);
        txtCorB1.setBackground(corTextFieldEst);
        txtCorB1.setBorder(border);
        txtCorB1.setFont(font14Bold);
        txtCorB1.setForeground(corJTFfont);
        txtCorB1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorB1.setVisible(true);
        txtCorB1.setText("0");
        pnlPedidoInfo.add(txtCorB1);

        txtCorB1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Não precisa de ação quando o campo ganha foco

            }

            @Override
            public void focusLost(FocusEvent e) {
                // Quando o campo perde o foco, altera a cor de fundo de acordo com o valor
                // digitado
                MainFrame.stationWorkId = Integer.parseInt(txtCorB1.getText());
                MainFrame.updateDisplayStation();
            }

        });

        JLabel lblCorBlk2 = new JLabel("COR BLOCO 2");
        lblCorBlk2.setBounds(215, 0, 140, 30);
        lblCorBlk2.setBorder(gray);
        lblCorBlk2.setForeground(corTextLabel);
        lblCorBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk2.setFont(font14);
        pnlPedidoInfo.add(lblCorBlk2);

        txtCorB2 = new JTextField();
        txtCorB2.setBounds(360, 0, 60, 30);
        txtCorB2.setBackground(corTextFieldEst);
        txtCorB2.setBorder(border);
        txtCorB2.setFont(font14Bold);
        txtCorB2.setForeground(corJTFfont);
        txtCorB2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorB2.setVisible(true);
        txtCorB2.setText("0");
        pnlPedidoInfo.add(txtCorB2);

        JLabel lblCorBlk3 = new JLabel("COR BLOCO 3");
        lblCorBlk3.setBounds(430, 0, 140, 30);
        lblCorBlk3.setBorder(gray);
        lblCorBlk3.setForeground(corTextLabel);
        lblCorBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk3.setFont(font14);
        pnlPedidoInfo.add(lblCorBlk3);

        txtCorB3 = new JTextField();
        txtCorB3.setBounds(575, 0, 60, 30);
        txtCorB3.setBackground(corTextFieldEst);
        txtCorB3.setBorder(border);
        txtCorB3.setFont(font14Bold);
        txtCorB3.setForeground(corJTFfont);
        txtCorB3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorB3.setVisible(true);
        txtCorB3.setText("0");
        pnlPedidoInfo.add(txtCorB3);
        // ------------------------------------------------------------------------

        JLabel lblPosBlk1 = new JLabel("POS BLOCO 1");
        lblPosBlk1.setBounds(0, 35, 140, 30);
        lblPosBlk1.setBorder(gray);
        lblPosBlk1.setForeground(corTextLabel);
        lblPosBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosBlk1.setFont(font14);
        pnlPedidoInfo.add(lblPosBlk1);

        txtPosB1 = new JTextField();
        txtPosB1.setBounds(145, 35, 60, 30);
        txtPosB1.setBackground(corTextFieldEst);
        txtPosB1.setBorder(border);
        txtPosB1.setFont(font14Bold);
        txtPosB1.setForeground(corJTFfont);
        txtPosB1.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosB1.setVisible(true);
        txtPosB1.setText("0");
        pnlPedidoInfo.add(txtPosB1);

        JLabel lblPosBlk2 = new JLabel("POS BLOCO 2");
        lblPosBlk2.setBounds(215, 35, 140, 30);
        lblPosBlk2.setBorder(gray);
        lblPosBlk2.setForeground(corTextLabel);
        lblPosBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosBlk2.setFont(font14);
        pnlPedidoInfo.add(lblPosBlk2);

        txtPosB2 = new JTextField();
        txtPosB2.setBounds(360, 35, 60, 30);
        txtPosB2.setBackground(corTextFieldEst);
        txtPosB2.setBorder(border);
        txtPosB2.setFont(font14Bold);
        txtPosB2.setForeground(corJTFfont);
        txtPosB2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosB2.setVisible(true);
        txtPosB2.setText("0");
        pnlPedidoInfo.add(txtPosB2);

        JLabel lblPosBlk3 = new JLabel("POS BLOCO 3");
        lblPosBlk3.setBounds(430, 35, 140, 30);
        lblPosBlk3.setBorder(gray);
        lblPosBlk3.setForeground(corTextLabel);
        lblPosBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosBlk3.setFont(font14);
        pnlPedidoInfo.add(lblPosBlk3);

        txtPosB3 = new JTextField();
        txtPosB3.setBounds(575, 35, 60, 30);
        txtPosB3.setBackground(corTextFieldEst);
        txtPosB3.setBorder(border);
        txtPosB3.setFont(font14Bold);
        txtPosB3.setForeground(corJTFfont);
        txtPosB3.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosB3.setVisible(true);
        txtPosB3.setText("0");
        pnlPedidoInfo.add(txtPosB3);

        // ------------------------------------------------------------------------

        JLabel lblBladesB1 = new JLabel("LÂMINAS");
        lblBladesB1.setBounds(0, 85, 75, 30);
        lblBladesB1.setBorder(gray);
        lblBladesB1.setForeground(corTextLabel);
        lblBladesB1.setHorizontalAlignment(SwingConstants.CENTER);
        lblBladesB1.setFont(font14);
        pnlPedidoInfo.add(lblBladesB1);

        JLabel lblColorB1 = new JLabel("COR");
        lblColorB1.setBounds(80, 85, 60, 30);
        lblColorB1.setBorder(gray);
        lblColorB1.setForeground(corTextLabel);
        lblColorB1.setHorizontalAlignment(SwingConstants.CENTER);
        lblColorB1.setFont(font14);
        pnlPedidoInfo.add(lblColorB1);

        JLabel lblPadB1 = new JLabel("PAD");
        lblPadB1.setBounds(145, 85, 60, 30);
        lblPadB1.setBorder(gray);
        lblPadB1.setForeground(corTextLabel);
        lblPadB1.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadB1.setFont(font14);
        pnlPedidoInfo.add(lblPadB1);

        JLabel lblBladesB2 = new JLabel("LÂMINAS");
        lblBladesB2.setBounds(215, 85, 75, 30);
        lblBladesB2.setBorder(gray);
        lblBladesB2.setForeground(corTextLabel);
        lblBladesB2.setHorizontalAlignment(SwingConstants.CENTER);
        lblBladesB2.setFont(font14);
        pnlPedidoInfo.add(lblBladesB2);

        JLabel lblColorB2 = new JLabel("COR");
        lblColorB2.setBounds(295, 85, 60, 30);
        lblColorB2.setBorder(gray);
        lblColorB2.setForeground(corTextLabel);
        lblColorB2.setHorizontalAlignment(SwingConstants.CENTER);
        lblColorB2.setFont(font14);
        pnlPedidoInfo.add(lblColorB2);

        JLabel lblPadB2 = new JLabel("PAD");
        lblPadB2.setBounds(360, 85, 60, 30);
        lblPadB2.setBorder(gray);
        lblPadB2.setForeground(corTextLabel);
        lblPadB2.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadB2.setFont(font14);
        pnlPedidoInfo.add(lblPadB2);

        JLabel lblBladesB3 = new JLabel("LÂMINAS");
        lblBladesB3.setBounds(430, 85, 75, 30);
        lblBladesB3.setBorder(gray);
        lblBladesB3.setForeground(corTextLabel);
        lblBladesB3.setHorizontalAlignment(SwingConstants.CENTER);
        lblBladesB3.setFont(font14);
        pnlPedidoInfo.add(lblBladesB3);

        JLabel lblColorB3 = new JLabel("COR");
        lblColorB3.setBounds(510, 85, 60, 30);
        lblColorB3.setBorder(gray);
        lblColorB3.setForeground(corTextLabel);
        lblColorB3.setHorizontalAlignment(SwingConstants.CENTER);
        lblColorB3.setFont(font14);
        pnlPedidoInfo.add(lblColorB3);

        JLabel lblPadB3 = new JLabel("PAD");
        lblPadB3.setBounds(575, 85, 60, 30);
        lblPadB3.setBorder(gray);
        lblPadB3.setForeground(corTextLabel);
        lblPadB3.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadB3.setFont(font14);
        pnlPedidoInfo.add(lblPadB3);
        // ------------------------------------------------------------------------

        JLabel lblBlade1B1 = new JLabel("L1");
        lblBlade1B1.setBounds(0, 120, 75, 30);
        lblBlade1B1.setBorder(gray);
        lblBlade1B1.setForeground(corTextLabel);
        lblBlade1B1.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade1B1.setFont(font14);
        pnlPedidoInfo.add(lblBlade1B1);

        txtCorL1B1 = new JTextField();
        txtCorL1B1.setBounds(80, 120, 60, 30);
        txtCorL1B1.setBackground(corTextFieldEst);
        txtCorL1B1.setBorder(border);
        txtCorL1B1.setFont(font14Bold);
        txtCorL1B1.setForeground(corJTFfont);
        txtCorL1B1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL1B1.setVisible(true);
        txtCorL1B1.setText("0");
        pnlPedidoInfo.add(txtCorL1B1);

        txtPadL1B1 = new JTextField();
        txtPadL1B1.setBounds(145, 120, 60, 30);
        txtPadL1B1.setBackground(corTextFieldEst);
        txtPadL1B1.setBorder(border);
        txtPadL1B1.setFont(font14Bold);
        txtPadL1B1.setForeground(corJTFfont);
        txtPadL1B1.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL1B1.setVisible(true);
        txtPadL1B1.setText("0");
        pnlPedidoInfo.add(txtPadL1B1);

        JLabel lblBlade1B2 = new JLabel("L1");
        lblBlade1B2.setBounds(215, 120, 75, 30);
        lblBlade1B2.setBorder(gray);
        lblBlade1B2.setForeground(corTextLabel);
        lblBlade1B2.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade1B2.setFont(font14);
        pnlPedidoInfo.add(lblBlade1B2);

        txtCorL1B2 = new JTextField();
        txtCorL1B2.setBounds(295, 120, 60, 30);
        txtCorL1B2.setBackground(corTextFieldEst);
        txtCorL1B2.setBorder(border);
        txtCorL1B2.setFont(font14Bold);
        txtCorL1B2.setForeground(corJTFfont);
        txtCorL1B2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL1B2.setVisible(true);
        txtCorL1B2.setText("0");
        pnlPedidoInfo.add(txtCorL1B2);

        txtPadL1B2 = new JTextField();
        txtPadL1B2.setBounds(360, 120, 60, 30);
        txtPadL1B2.setBackground(corTextFieldEst);
        txtPadL1B2.setBorder(border);
        txtPadL1B2.setFont(font14Bold);
        txtPadL1B2.setForeground(corJTFfont);
        txtPadL1B2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL1B2.setVisible(true);
        txtPadL1B2.setText("0");
        pnlPedidoInfo.add(txtPadL1B2);

        JLabel lblBlade1B3 = new JLabel("L1");
        lblBlade1B3.setBounds(430, 120, 75, 30);
        lblBlade1B3.setBorder(gray);
        lblBlade1B3.setForeground(corTextLabel);
        lblBlade1B3.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade1B3.setFont(font14);
        pnlPedidoInfo.add(lblBlade1B3);

        txtCorL1B3 = new JTextField();
        txtCorL1B3.setBounds(510, 120, 60, 30);
        txtCorL1B3.setBackground(corTextFieldEst);
        txtCorL1B3.setBorder(border);
        txtCorL1B3.setFont(font14Bold);
        txtCorL1B3.setForeground(corJTFfont);
        txtCorL1B3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL1B3.setVisible(true);
        txtCorL1B3.setText("0");
        pnlPedidoInfo.add(txtCorL1B3);

        txtPadL1B3 = new JTextField();
        txtPadL1B3.setBounds(575, 120, 60, 30);
        txtPadL1B3.setBackground(corTextFieldEst);
        txtPadL1B3.setBorder(border);
        txtPadL1B3.setFont(font14Bold);
        txtPadL1B3.setForeground(corJTFfont);
        txtPadL1B3.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL1B3.setVisible(true);
        txtPadL1B3.setText("0");
        pnlPedidoInfo.add(txtPadL1B3);

        // ------------------------------------------------------------------------

        JLabel lblBlade2B1 = new JLabel("L2");
        lblBlade2B1.setBounds(0, 155, 75, 30);
        lblBlade2B1.setBorder(gray);
        lblBlade2B1.setForeground(corTextLabel);
        lblBlade2B1.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade2B1.setFont(font14);
        pnlPedidoInfo.add(lblBlade2B1);

        txtCorL2B1 = new JTextField();
        txtCorL2B1.setBounds(80, 155, 60, 30);
        txtCorL2B1.setBackground(corTextFieldEst);
        txtCorL2B1.setBorder(border);
        txtCorL2B1.setFont(font14Bold);
        txtCorL2B1.setForeground(corJTFfont);
        txtCorL2B1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL2B1.setVisible(true);
        txtCorL2B1.setText("0");
        pnlPedidoInfo.add(txtCorL2B1);

        txtPadL2B1 = new JTextField();
        txtPadL2B1.setBounds(145, 155, 60, 30);
        txtPadL2B1.setBackground(corTextFieldEst);
        txtPadL2B1.setBorder(border);
        txtPadL2B1.setFont(font14Bold);
        txtPadL2B1.setForeground(corJTFfont);
        txtPadL2B1.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL2B1.setVisible(true);
        txtPadL2B1.setText("0");
        pnlPedidoInfo.add(txtPadL2B1);

        JLabel lblBlade2B2 = new JLabel("L2");
        lblBlade2B2.setBounds(215, 155, 75, 30);
        lblBlade2B2.setBorder(gray);
        lblBlade2B2.setForeground(corTextLabel);
        lblBlade2B2.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade2B2.setFont(font14);
        pnlPedidoInfo.add(lblBlade2B2);

        txtCorL2B2 = new JTextField();
        txtCorL2B2.setBounds(295, 155, 60, 30);
        txtCorL2B2.setBackground(corTextFieldEst);
        txtCorL2B2.setBorder(border);
        txtCorL2B2.setFont(font14Bold);
        txtCorL2B2.setForeground(corJTFfont);
        txtCorL2B2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL2B2.setVisible(true);
        txtCorL2B2.setText("0");
        pnlPedidoInfo.add(txtCorL2B2);

        txtPadL2B2 = new JTextField();
        txtPadL2B2.setBounds(360, 155, 60, 30);
        txtPadL2B2.setBackground(corTextFieldEst);
        txtPadL2B2.setBorder(border);
        txtPadL2B2.setFont(font14Bold);
        txtPadL2B2.setForeground(corJTFfont);
        txtPadL2B2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL2B2.setVisible(true);
        txtPadL2B2.setText("0");
        pnlPedidoInfo.add(txtPadL2B2);

        JLabel lblBlade2B3 = new JLabel("L2");
        lblBlade2B3.setBounds(430, 155, 75, 30);
        lblBlade2B3.setBorder(gray);
        lblBlade2B3.setForeground(corTextLabel);
        lblBlade2B3.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade2B3.setFont(font14);
        pnlPedidoInfo.add(lblBlade2B3);

        txtCorL2B3 = new JTextField();
        txtCorL2B3.setBounds(510, 155, 60, 30);
        txtCorL2B3.setBackground(corTextFieldEst);
        txtCorL2B3.setBorder(border);
        txtCorL2B3.setFont(font14Bold);
        txtCorL2B3.setForeground(corJTFfont);
        txtCorL2B3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL2B3.setVisible(true);
        txtCorL2B3.setText("0");
        pnlPedidoInfo.add(txtCorL2B3);

        txtPadL2B3 = new JTextField();
        txtPadL2B3.setBounds(575, 155, 60, 30);
        txtPadL2B3.setBackground(corTextFieldEst);
        txtPadL2B3.setBorder(border);
        txtPadL2B3.setFont(font14Bold);
        txtPadL2B3.setForeground(corJTFfont);
        txtPadL2B3.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL2B3.setVisible(true);
        txtPadL2B3.setText("0");
        pnlPedidoInfo.add(txtPadL2B3);

        // ------------------------------------------------------------------------

        JLabel lblBlade3B1 = new JLabel("L3");
        lblBlade3B1.setBounds(00, 190, 75, 30);
        lblBlade3B1.setBorder(gray);
        lblBlade3B1.setForeground(corTextLabel);
        lblBlade3B1.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade3B1.setFont(font14);
        pnlPedidoInfo.add(lblBlade3B1);

        txtCorL3B1 = new JTextField();
        txtCorL3B1.setBounds(80, 190, 60, 30);
        txtCorL3B1.setBackground(corTextFieldEst);
        txtCorL3B1.setBorder(border);
        txtCorL3B1.setFont(font14Bold);
        txtCorL3B1.setForeground(corJTFfont);
        txtCorL3B1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL3B1.setVisible(true);
        txtCorL3B1.setText("0");
        pnlPedidoInfo.add(txtCorL3B1);

        txtPadL3B1 = new JTextField();
        txtPadL3B1.setBounds(145, 190, 60, 30);
        txtPadL3B1.setBackground(corTextFieldEst);
        txtPadL3B1.setBorder(border);
        txtPadL3B1.setFont(font14Bold);
        txtPadL3B1.setForeground(corJTFfont);
        txtPadL3B1.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL3B1.setVisible(true);
        txtPadL3B1.setText("0");
        pnlPedidoInfo.add(txtPadL3B1);

        JLabel lblBlade3B2 = new JLabel("L3");
        lblBlade3B2.setBounds(215, 190, 75, 30);
        lblBlade3B2.setBorder(gray);
        lblBlade3B2.setForeground(corTextLabel);
        lblBlade3B2.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade3B2.setFont(font14);
        pnlPedidoInfo.add(lblBlade3B2);

        txtCorL3B2 = new JTextField();
        txtCorL3B2.setBounds(295, 190, 60, 30);
        txtCorL3B2.setBackground(corTextFieldEst);
        txtCorL3B2.setBorder(border);
        txtCorL3B2.setFont(font14Bold);
        txtCorL3B2.setForeground(corJTFfont);
        txtCorL3B2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL3B2.setVisible(true);
        txtCorL3B2.setText("0");
        pnlPedidoInfo.add(txtCorL3B2);

        txtPadL3B2 = new JTextField();
        txtPadL3B2.setBounds(360, 190, 60, 30);
        txtPadL3B2.setBackground(corTextFieldEst);
        txtPadL3B2.setBorder(border);
        txtPadL3B2.setFont(font14Bold);
        txtPadL3B2.setForeground(corJTFfont);
        txtPadL3B2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL3B2.setVisible(true);
        txtPadL3B2.setText("0");
        pnlPedidoInfo.add(txtPadL3B2);

        JLabel lblBlade3B3 = new JLabel("L3");
        lblBlade3B3.setBounds(430, 190, 75, 30);
        lblBlade3B3.setBorder(gray);
        lblBlade3B3.setForeground(corTextLabel);
        lblBlade3B3.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade3B3.setFont(font14);
        pnlPedidoInfo.add(lblBlade3B3);

        txtCorL3B3 = new JTextField();
        txtCorL3B3.setBounds(510, 190, 60, 30);
        txtCorL3B3.setBackground(corTextFieldEst);
        txtCorL3B3.setBorder(border);
        txtCorL3B3.setFont(font14Bold);
        txtCorL3B3.setForeground(corJTFfont);
        txtCorL3B3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL3B3.setVisible(true);
        txtCorL3B3.setText("0");
        pnlPedidoInfo.add(txtCorL3B3);

        txtPadL3B3 = new JTextField();
        txtPadL3B3.setBounds(575, 190, 60, 30);
        txtPadL3B3.setBackground(corTextFieldEst);
        txtPadL3B3.setBorder(border);
        txtPadL3B3.setFont(font14Bold);
        txtPadL3B3.setForeground(corJTFfont);
        txtPadL3B3.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL3B3.setVisible(true);
        txtPadL3B3.setText("0");
        pnlPedidoInfo.add(txtPadL3B3);

        // ------------------------------------------------------------------------

        JLabel lblProB1 = new JLabel("PROC BLOCO 1");
        lblProB1.setBounds(0, 240, 140, 30);
        lblProB1.setBorder(gray);
        lblProB1.setForeground(corTextLabel);
        lblProB1.setHorizontalAlignment(SwingConstants.CENTER);
        lblProB1.setFont(font14);
        pnlPedidoInfo.add(lblProB1);

        txtProB1 = new JTextField();
        txtProB1.setBounds(145, 240, 60, 30);
        txtProB1.setBackground(corTextFieldEst);
        txtProB1.setBorder(border);
        txtProB1.setFont(font14Bold);
        txtProB1.setForeground(corJTFfont);
        txtProB1.setHorizontalAlignment(SwingConstants.CENTER);
        txtProB1.setVisible(true);
        txtProB1.setText("0");
        pnlPedidoInfo.add(txtProB1);

        JLabel lblProB2 = new JLabel("PROC BLOCO 2");
        lblProB2.setBounds(215, 240, 140, 30);
        lblProB2.setBorder(gray);
        lblProB2.setForeground(corTextLabel);
        lblProB2.setHorizontalAlignment(SwingConstants.CENTER);
        lblProB2.setFont(font14);
        pnlPedidoInfo.add(lblProB2);

        txtProB2 = new JTextField();
        txtProB2.setBounds(360, 240, 60, 30);
        txtProB2.setBackground(corTextFieldEst);
        txtProB2.setBorder(border);
        txtProB2.setFont(font14Bold);
        txtProB2.setForeground(corJTFfont);
        txtProB2.setHorizontalAlignment(SwingConstants.CENTER);
        txtProB2.setVisible(true);
        txtProB2.setText("0");
        pnlPedidoInfo.add(txtProB2);

        JLabel lblProB3 = new JLabel("PROC BLOCO 3");
        lblProB3.setBounds(430, 240, 140, 30);
        lblProB3.setBorder(gray);
        lblProB3.setForeground(corTextLabel);
        lblProB3.setHorizontalAlignment(SwingConstants.CENTER);
        lblProB3.setFont(font14);
        pnlPedidoInfo.add(lblProB3);

        txtProB3 = new JTextField();
        txtProB3.setBounds(575, 240, 60, 30);
        txtProB3.setBackground(corTextFieldEst);
        txtProB3.setBorder(border);
        txtProB3.setFont(font14Bold);
        txtProB3.setForeground(corJTFfont);
        txtProB3.setHorizontalAlignment(SwingConstants.CENTER);
        txtProB3.setVisible(true);
        txtProB3.setText("0");
        pnlPedidoInfo.add(txtProB3);

        // ------------------------------------------------------------------------

        // ----- Dados Processamento Pedido
        // ------------------------------------------------
        JLabel lblNumPedido = new JLabel("Nº PEDIDO");
        lblNumPedido.setBounds(0, 285, 140, 30);
        lblNumPedido.setBorder(gray);
        lblNumPedido.setForeground(corTextLabel);
        lblNumPedido.setHorizontalAlignment(SwingConstants.CENTER);
        lblNumPedido.setFont(font14);
        pnlPedidoInfo.add(lblNumPedido);

        txtNumPedido = new JTextField();
        txtNumPedido.setBounds(145, 285, 60, 30);
        txtNumPedido.setBackground(corTextFieldEst);
        txtNumPedido.setBorder(border);
        txtNumPedido.setFont(font14Bold);
        txtNumPedido.setForeground(corJTFfont);
        txtNumPedido.setHorizontalAlignment(SwingConstants.CENTER);
        txtNumPedido.setVisible(true);
        txtNumPedido.setText("0");
        pnlPedidoInfo.add(txtNumPedido);

        JLabel lblAndarEst = new JLabel("ANDAR EST");
        lblAndarEst.setBounds(215, 285, 140, 30);
        lblAndarEst.setBorder(gray);
        lblAndarEst.setForeground(corTextLabel);
        lblAndarEst.setHorizontalAlignment(SwingConstants.CENTER);
        lblAndarEst.setFont(font14);
        pnlPedidoInfo.add(lblAndarEst);

        txtAndarEst = new JTextField();
        txtAndarEst.setBounds(360, 285, 60, 30);
        txtAndarEst.setBackground(corTextFieldEst);
        txtAndarEst.setBorder(border);
        txtAndarEst.setFont(font14Bold);
        txtAndarEst.setForeground(corJTFfont);
        txtAndarEst.setHorizontalAlignment(SwingConstants.CENTER);
        txtAndarEst.setVisible(true);
        txtAndarEst.setText("0");
        pnlPedidoInfo.add(txtAndarEst);

        JLabel lblPosExped = new JLabel("POS EXPEDIÇÃO");
        lblPosExped.setBounds(430, 285, 140, 30);
        lblPosExped.setBorder(gray);
        lblPosExped.setForeground(corTextLabel);
        lblPosExped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosExped.setFont(font14);
        pnlPedidoInfo.add(lblPosExped);

        txtPosExped = new JTextField();
        txtPosExped.setBounds(575, 285, 60, 30);
        txtPosExped.setBackground(corTextFieldEst);
        txtPosExped.setBorder(border);
        txtPosExped.setFont(font14Bold);
        txtPosExped.setForeground(corJTFfont);
        txtPosExped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosExped.setVisible(true);
        txtPosExped.setText("0");
        pnlPedidoInfo.add(txtPosExped);

        // -----------------------------------------------------------------------/

        enviaPedidoButton = new JButton("ENVIAR PEDIDO");
        enviaPedidoButton.setBounds(225, 230, 205, 30);
        enviaPedidoButton.setForeground(corTextLabel);
        enviaPedidoButton.setBackground(corTextField);
        enviaPedidoButton.setEnabled(false);
        // enviaPedidoButton.setEnabled(!MainFrame.readOnly);

        enviaPedidoButton.addActionListener(e -> {
            intPedidoArray[0] = Integer.parseInt(txtCorB1.getText());
            intPedidoArray[1] = Integer.parseInt(txtPosB1.getText());
            intPedidoArray[2] = Integer.parseInt(txtCorL1B1.getText());
            intPedidoArray[3] = Integer.parseInt(txtCorL2B1.getText());
            intPedidoArray[4] = Integer.parseInt(txtCorL3B1.getText());
            intPedidoArray[5] = Integer.parseInt(txtPadL1B1.getText());
            intPedidoArray[6] = Integer.parseInt(txtPadL2B1.getText());
            intPedidoArray[7] = Integer.parseInt(txtPadL3B1.getText());
            intPedidoArray[8] = Integer.parseInt(txtProB1.getText());

            intPedidoArray[9] = Integer.parseInt(txtCorB2.getText());
            intPedidoArray[10] = Integer.parseInt(txtPosB2.getText());
            intPedidoArray[11] = Integer.parseInt(txtCorL1B2.getText());
            intPedidoArray[12] = Integer.parseInt(txtCorL2B2.getText());
            intPedidoArray[13] = Integer.parseInt(txtCorL3B2.getText());
            intPedidoArray[14] = Integer.parseInt(txtPadL1B2.getText());
            intPedidoArray[15] = Integer.parseInt(txtPadL2B2.getText());
            intPedidoArray[16] = Integer.parseInt(txtPadL3B2.getText());
            intPedidoArray[17] = Integer.parseInt(txtProB2.getText());

            intPedidoArray[18] = Integer.parseInt(txtCorB3.getText());
            intPedidoArray[19] = Integer.parseInt(txtPosB3.getText());
            intPedidoArray[20] = Integer.parseInt(txtCorL1B3.getText());
            intPedidoArray[21] = Integer.parseInt(txtCorL2B3.getText());
            intPedidoArray[22] = Integer.parseInt(txtCorL3B3.getText());
            intPedidoArray[23] = Integer.parseInt(txtPadL1B3.getText());
            intPedidoArray[24] = Integer.parseInt(txtPadL2B3.getText());
            intPedidoArray[25] = Integer.parseInt(txtPadL3B3.getText());
            intPedidoArray[26] = Integer.parseInt(txtProB3.getText());

            intPedidoArray[27] = Integer.parseInt(txtNumPedido.getText());
            intPedidoArray[28] = Integer.parseInt(txtAndarEst.getText());
            intPedidoArray[29] = Integer.parseInt(txtPosExped.getText());

            // readMatrixValues();
            byte[] bytePedidoArray = convertIntArrayToByteArray(intPedidoArray);
            // displayByteArray(bytePedidoArray);
            try {
                PanelStart.plcWriteEst = new PlcConnector(Panel1.textHostPLC.getText(), 102);

                try {
                    PanelStart.plcWriteEst.connect();
                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                PanelStart.plcWriteEst.writeBlock(9, 2, 60, bytePedidoArray);

                PanelStart.plcWriteEst.writeBlock(9, 68, 28, MainFrame.blockArray);

                try {
                    PanelStart.plcWriteEst.disconnect();
                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                ConfigPedido.registrarPedidoBtn.setText("EM PRODUÇÃO");

            } catch (Exception ex) {
                System.out.println("[ERRO ENVIO PLC] Não foi possível enviar os dados do pedido ao PLC.");
            }
        });

        startPedidoButton = new JButton("INICIAR PEDIDO");
        startPedidoButton.setBounds(440, 230, 205, 30);
        startPedidoButton.setForeground(corTextLabel);
        startPedidoButton.setBackground(corTextField);

        startPedidoButton.setEnabled(false);
        // startPedidoButton.setEnabled(!MainFrame.readOnly);

        startPedidoButton.addActionListener(e -> {

            try {

                // readMatrixValues();
                // byte[] byteArray = convertIntArrayToByteArray(blockArray);
                // displayByteArray(byteArray);

                // Inicialização do primeiro ciclo do pedido

                // MainFrame.loadMagazineEstoque();
                // MainFrame.loadMagazineEpedicao();

                MainFrame.posExpedArray[12] = MainFrame.posExpedArray[12] + 1;
                /*
                 * for(int c=0 ; c<28 ; c++){
                 * System.out.println("POS["+(c+1)+"]= "+ blockArray[c]);
                 * }
                 *
                 * for(int c=0 ; c<12 ; c++){
                 * System.out.println("POS["+(c+1)+"]= "+ MainFrame.posExpedArray[c]);
                 * }
                 */

                PanelStart.plcWriteEst = new PlcConnector(MainFrame.ipEstoque, 102);

                try {
                    PanelStart.plcWriteEst.connect();
                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                try {
                    // plcWrite = new PlcConnector(MainFrame.ipEstoque, 9, 64, 1, 0, 1);

                    PanelStart.plcWriteEst.writeBit(9, 64, 0, Boolean.parseBoolean("FALSE"));
                    PanelStart.plcWriteEst.writeBit(9, 64, 1, Boolean.parseBoolean("FALSE"));
                    PanelStart.plcWriteEst.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE"));

                    // Inicia pedido
                    PanelStart.plcWriteEst.writeBit(9, 62, 0, Boolean.parseBoolean("TRUE"));

                } catch (Exception ex) {

                }

                try {
                    PanelStart.plcWriteEst.disconnect();
                } catch (Exception e1) {

                    e1.printStackTrace();
                }

                // plcWrite = new PlcConnector(MainFrame.ipEstoque, 9, 62, 1, 0, 1);

                // ---------------------------------------------------------------------------

                // plcWrite = new PlcConnector(Panel1.textHostPLC.getText(), 9, 68, 28, 0, 1);
                // plcWrite.writeDataBlock(9, 68, blockArray);

                // Inicia pedido
                // plcWrite = new PlcConnector(MainFrame.ipEstoque, 9, 62, 1, 0, 1);

                // try {
                // Thread.sleep(1000);
                // } catch (InterruptedException e1) {
                // e1.printStackTrace();
                // }
                // plcWrite.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE"));

                // MainFrame.removeMagazineEstoque(Integer.parseInt(txtPosB1.getText()));
                // MainFrame.adicionaMagazineEstoque(Integer.parseInt(txtPosB1.getText()),
                // Integer.parseInt(txtCorB1.getText()));

                // MainFrame.startPanel.setVisible(true);

                MainFrame.cardLayout.show(MainFrame.painelPrincipal, "startPanel");
                PanelStart.pnlOpSmart.setVisible(true);
                PanelStart.button.setVisible(false);
                PanelStart.btnOkVoltar.setVisible(true);

                MainFrame.pedidoEmCurso = true;

                MainFrame.posExpedArray[12] = MainFrame.posExpedArray[12] + 1;

            } catch (Exception ex) {
            }

        });

        ImagePanel smart = new ImagePanel("src/main/java/com/smart40/smart/smart.png", 5, 5, 0.5);
        smart.setBounds(270, 20, 375, 200);
        // smart.setBackground(corBackPnl);
        smart.setBackground(Color.black);
        smart.setBorder(gray);
        smart.setLayout(null);
        pnlPedido.add(smart);

        pnlEst = new JPanel();
        pnlEst.setBounds(30, 95, 65, 70);
        pnlEst.setOpaque(false);
        // pnlEst.setBorder(yellow);
        smart.add(pnlEst);

        pnlPro = new JPanel();
        pnlPro.setBounds(100, 95, 65, 70);
        pnlPro.setOpaque(false);
        // pnlPro.setBorder(lGreenBorder);
        smart.add(pnlPro);

        pnlMon = new JPanel();
        pnlMon.setBounds(170, 95, 95, 70);
        pnlMon.setOpaque(false);
        // pnlMon.setBorder(yellow);
        smart.add(pnlMon);

        pnlExp = new JPanel();
        pnlExp.setBounds(270, 95, 65, 70);
        pnlExp.setOpaque(false);
        // pnlExp.setBorder(yellow);
        smart.add(pnlExp);

        orderFinishedButton = new JButton("OPERAÇÃO CONCLUÍDA");
        orderFinishedButton.setBounds(80, 115, 205, 30);
        orderFinishedButton.setForeground(corTextLabel);
        orderFinishedButton.setBackground(new Color(50, 50, 255));
        orderFinishedButton.setVisible(MainFrame.blockFinished);

        orderFinishedButton.addActionListener(e -> {
            MainFrame.blockFinished = false;
            MainFrame.stationWorkId = 0;
            MainFrame.pedidoEmCurso = false;
            MainFrame.updateDisplayStation();
            orderFinishedButton.setVisible(MainFrame.blockFinished);
        });
        smart.add(orderFinishedButton);

        pnlPedido.add(updateMagazineButton);
        pnlPedido.add(enviaPedidoButton);
        pnlPedido.add(startPedidoButton);
        add(pnlPedido);
    }

    public static void createMagazineEstoque() {
        // Criando a matriz de JTextFields e adicionando o FocusListener

        pnlArrayBlocks.removeAll();
        index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {

                if ((i == 4) && ((j == 4) || (j == 5))) {

                } else {
                    JTextField textField = new JTextField();
                    textFields[i][j] = textField;
                    textFields[i][j].setBackground(corTextField);

                    textFields[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                    textFields[i][j].setFont(font14Bold);

                    textFields[i][j].setText("0");
                    textFields[i][j].setText(String.valueOf(MainFrame.blockArray[index]));

                    updateTextFieldColor(textField);

                    // Adiciona o FocusListener para mudar a cor de fundo quando o campo perde foco
                    textField.addFocusListener(new FocusListener() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            // Não precisa de ação quando o campo ganha foco
                            textField.selectAll();

                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            // Quando o campo perde o foco, altera a cor de fundo de acordo com o valor
                            // digitado
                            updateTextFieldColor(textField);
                        }

                    });
                    pnlArrayBlocks.add(textField);
                    index++;
                }

            }
        }
        pnlArrayBlocks.revalidate();
        pnlArrayBlocks.repaint();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void saveMagazineEstoque() {
        readMatrixValues();
        // byte[] byteArray = convertIntArrayToByteArray(blockArray);
        // displayByteArray(byteArray);
        String positionBlocks = "";

        for (int i = 0; i < 28; i++) {
            positionBlocks += "P" + (i + 1) + "=" + String.valueOf(MainFrame.blockArray[i]) + "\n";

        }

        // Escreve o timestamp no arquivo
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/main/java/com/smart40/MagazineEst.txt", false))) {
            writer.write(positionBlocks); // Adiciona o timestamp e uma nova linha
            System.out.println("Arquivo TXT do Magazine Estoque atualizado com sucesso!");
            // JOptionPane.showMessageDialog(null, "Magazine atualizado com sucesso!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (MainFrame.readOnly == false) {

            PanelStart.plcWriteEst = new PlcConnector(MainFrame.ipEstoque, 102);

            try {
                PanelStart.plcWriteEst.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            // plcWrite = new PlcConnector(MainFrame.ipEstoque, 9, 68, 28, 0, 1);
            try {
                PanelStart.plcWriteEst.writeBlock(9, 68, 28, MainFrame.blockArray);
            } catch (Exception e) {

                e.printStackTrace();
            }

            try {
                PanelStart.plcWriteEst.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            System.out.println("DB9:68-95 Posições do Estoque atualizado com sucesso!");
        }
    }

    // Método para ler o conteúdo dos JTextFields e preencher o vetor de bytes
    private static void readMatrixValues() {
        int index = 0;
        try {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {

                    if ((i == 4) && ((j == 4) || (j == 5))) {

                    } else {
                        // Lendo o valor do JTextField e convertendo para número inteiro
                        int value = Integer.parseInt(textFields[i][j].getText());

                        // Garantindo que o valor esteja no intervalo de 0 a 3
                        if (value >= 0 && value <= 3) {
                            MainFrame.blockArray[index] = (byte) value;
                        } else {
                            throw new NumberFormatException("Os valores devem estar entre 0 e 3.");
                        }
                        index++;
                    }
                }
            }
            // JOptionPane.showMessageDialog(null, "Valores lidos com sucesso!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para converter int[] para byte[] (2 bytes para cada int)
    public static byte[] convertIntArrayToByteArray(int[] intArray) {
        byte[] byteArray = new byte[intArray.length * 2]; // Cada int será representado por 2 bytes

        for (int i = 0; i < intArray.length; i++) {
            int value = intArray[i];

            // Primeiro byte (mais significativo)
            byteArray[i * 2] = (byte) ((value >> 8) & 0xFF);

            // Segundo byte (menos significativo)
            byteArray[i * 2 + 1] = (byte) (value & 0xFF);
        }

        return byteArray;
    }

    // Método para alterar a cor de fundo do JTextField baseado no valor digitado
    private static void updateTextFieldColor(JTextField textField) {
        try {
            int value = Integer.parseInt(textField.getText());

            if (value >= 0 && value <= 3) {
                switch (value) {
                    case 0 -> {
                        textField.setBackground(Color.WHITE);
                        textField.setForeground(Color.BLACK);
                    }
                    case 1 -> {
                        textField.setBackground(Color.BLACK);
                        textField.setForeground(Color.WHITE);
                    }
                    case 2 -> {
                        textField.setBackground(Color.RED);
                        textField.setForeground(Color.WHITE);
                    }
                    case 3 -> {
                        textField.setBackground(Color.BLUE);
                        textField.setForeground(Color.WHITE);
                    }
                    default -> {
                        textField.setBackground(Color.WHITE);
                        textField.setForeground(Color.BLACK);
                    }
                }
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            // Caso o valor não seja válido, restaura o fundo para branco e mostra um erro
            textField.setBackground(Color.WHITE);
            textField.setForeground(Color.BLACK);
            JOptionPane.showMessageDialog(null, "Digite um valor entre 0 e 3", "Valor inválido",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para exibir o vetor com os valores lidos
    // private static void displayByteArray(byte[] byteArray) {
    // StringBuilder sb = new StringBuilder();
    // sb.append("Valores digitados: ");
    // for (int value : byteArray) {
    // sb.append(value).append(" ");
    // }
    // JOptionPane.showMessageDialog(null, sb.toString(), "Vetor de Bytes",
    // JOptionPane.INFORMATION_MESSAGE);
    // }
}