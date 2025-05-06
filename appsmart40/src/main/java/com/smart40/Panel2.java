package com.smart40;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

//import java.io.IOException;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileReader;
//import java.io.FileWriter;

public class Panel2 extends JPanel {

    public static JPanel pnlPedidoVar;

    public static JButton btnExibirProdcao;

    public static JCheckBox chkReadCiclic;
    public static JCheckBox chkReadOnly;

    // ********** Status das Bancadas */

    // private static JLabel lblStatEstoq;
    // private static JTextField txtStatEstoq;
    // private static JCheckBox chkEstoq;
    public static JCheckBox chkRecOpEst;

    // private static JLabel lblStatProces;
    // private static JTextField txtStatProces;
    // private static JCheckBox chkProces;
    public static JCheckBox chkRecOpPro;

    // private static JLabel lblStatMontag;
    // private static JTextField txtStatMontag;
    // private static JCheckBox chkMontag;
    public static JCheckBox chkRecOpMon;

    // private static JLabel lblStatExpedi;
    // private static JTextField txtStatExpedi;
    // private static JCheckBox chkExpedi;
    public static JCheckBox chkRecOpExp;

    public static JCheckBox chkStartPed;

    public static JCheckBox chkRecEstoq;

    public static JCheckBox chkIniGuard;

    public static JCheckBox chkRecExpedi;

    public static JCheckBox chkIniGuardExp;

    // --------- Info pedido ------------------
    public static JTextField txtCorBlk1;
    public static JTextField txtCorBlk2;
    public static JTextField txtCorBlk3;

    public static JTextField txtPosBlk1;
    public static JTextField txtPosBlk2;
    public static JTextField txtPosBlk3;

    public static JTextField txtProBlk1;
    public static JTextField txtProBlk2;
    public static JTextField txtProBlk3;

    public static JTextField txtCorL1Blk1;
    public static JTextField txtCorL1Blk2;
    public static JTextField txtCorL1Blk3;

    public static JTextField txtCorL2Blk1;
    public static JTextField txtCorL2Blk2;
    public static JTextField txtCorL2Blk3;

    public static JTextField txtCorL3Blk1;
    public static JTextField txtCorL3Blk2;
    public static JTextField txtCorL3Blk3;

    public static JTextField txtPadL1Blk1;
    public static JTextField txtPadL1Blk2;
    public static JTextField txtPadL1Blk3;

    public static JTextField txtPadL2Blk1;
    public static JTextField txtPadL2Blk2;
    public static JTextField txtPadL2Blk3;

    public static JTextField txtPadL3Blk1;
    public static JTextField txtPadL3Blk2;
    public static JTextField txtPadL3Blk3;

    public static JTextField txtNumPedEst;
    public static JTextField txtAndarEst;
    public static JTextField txtPosExpEst;

    public static JTextField txtPosGuard;
    public static JTextField txtPosGuardExp;

    // Posições MAGAZINE EXPEDIÇÃO

    public static JLabel lblPos1Exped;
    public static JTextField txtPos1Exped;

    public static JLabel lblPos2Exped;
    public static JTextField txtPos2Exped;

    public static JLabel lblPos3Exped;
    public static JTextField txtPos3Exped;

    public static JLabel lblPos4Exped;
    public static JTextField txtPos4Exped;

    public static JLabel lblPos5Exped;
    public static JTextField txtPos5Exped;

    public static JLabel lblPos6Exped;
    public static JTextField txtPos6Exped;

    public static JLabel lblPos7Exped;
    public static JTextField txtPos7Exped;

    public static JLabel lblPos8Exped;
    public static JTextField txtPos8Exped;

    public static JLabel lblPos9Exped;
    public static JTextField txtPos9Exped;

    public static JLabel lblPos10Exped;
    public static JTextField txtPos10Exped;

    public static JLabel lblPos11Exped;
    public static JTextField txtPos11Exped;

    public static JLabel lblPos12Exped;
    public static JTextField txtPos12Exped;
    public static Color corBackPnl = new Color(10, 50, 50);
    public static Color lightGreen = new Color(10, 255, 50);

    public static Border gray = BorderFactory.createLineBorder(Color.WHITE);
    public static Border red = BorderFactory.createLineBorder(Color.red, 2);
    public static Border greenOn = BorderFactory.createLineBorder(new Color(0, 255, 0), 2);
    public static Border greenOff = BorderFactory.createLineBorder(new Color(0, 128, 0), 2);
    public static Border yellow = BorderFactory.createLineBorder(Color.yellow, 1);
    public static Border lGreenBorder = BorderFactory.createLineBorder(lightGreen, 2);

    Border border = BorderFactory.createLineBorder(Color.WHITE, 1);

    Color corTextLabel = new Color(250, 250, 250);
    Color corTextField = new Color(50, 90, 90);

    Color corTextFieldEst = new Color(10, 10, 10);
    Color corTextFieldExp = new Color(10, 10, 10);

    static Color corJTFfont = new Color(50, 255, 255);
    Color corJLblTitleBg = new Color(30, 100, 130);

    // private static Color corBackPnlOn = new Color(10, 120, 50);

    Font font14 = new Font("Arial", Font.PLAIN, 14);
    Font font14Bold = new Font("Arial", Font.BOLD, 14);
    // Font font16 = new Font("Arial", Font.PLAIN, 16);
    // Font font18 = new Font("Arial", Font.PLAIN, 18);
    // Font font22 = new Font("Arial", Font.BOLD, 22);

    // Font fontTitle = new Font("Arial", Font.BOLD, 32);

    public Panel2(MainFrame mainframe, int w, int h) {
        // Configura o layout do painel
        setSize(w, h);
        setLayout(null);
        setVisible(true);

        // setBorder(gray);
        setVisible(false);
        // setBackground(corBackPnl);
        setBackground(Color.black);

        // setLayout(new FlowLayout());
        // this.setBackground(new Color(100, 250, 200));

        // Cria os componentes
        pnlPedidoVar = new JPanel();
        pnlPedidoVar.setBounds(15, 10, 655, 660);
        pnlPedidoVar.setLayout(null);
        pnlPedidoVar.setBorder(greenOff);
        pnlPedidoVar.setBackground(corBackPnl);
        pnlPedidoVar.setOpaque(false);

        // ----Info Pedido - Estoque -------------------------------------

        int pedidoX = 0;
        int pedidoY = 0;

        JLabel lblCriarPedido = new JLabel("DB9 CLP ESTOQUE");
        lblCriarPedido.setBounds(pedidoX + 225, pedidoY + 10, 205, 30);
        lblCriarPedido.setBorder(gray);
        lblCriarPedido.setForeground(corTextLabel);
        lblCriarPedido.setBackground(corJLblTitleBg);
        lblCriarPedido.setHorizontalAlignment(SwingConstants.CENTER);
        lblCriarPedido.setFont(font14Bold);
        lblCriarPedido.setOpaque(true);
        pnlPedidoVar.add(lblCriarPedido);

        // ---- RecebidoOP -------------------------------------------------------
        JLabel lblRecebidoOp = new JLabel("Recebido OP");
        lblRecebidoOp.setBounds(pedidoX + 10, pedidoY + 10, 120, 30);
        lblRecebidoOp.setBorder(gray);
        lblRecebidoOp.setForeground(corTextLabel);
        lblRecebidoOp.setHorizontalAlignment(SwingConstants.CENTER);
        lblRecebidoOp.setFont(font14);
        pnlPedidoVar.add(lblRecebidoOp);

        chkRecOpEst = new JCheckBox();
        chkRecOpEst.setBounds(pedidoX + 135, pedidoY + 10, 80, 30);
        chkRecOpEst.setBackground(corTextField);
        chkRecOpEst.setText("FALSE");
        chkRecOpEst.addActionListener((ActionEvent e) -> {
            if (chkRecOpEst.isSelected())
                chkRecOpEst.setText("TRUE");
            else
                chkRecOpEst.setText("FALSE");
        });
        chkRecOpEst.setFont(font14Bold);
        chkRecOpEst.setForeground(corJTFfont);
        chkRecOpEst.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkRecOpEst);

        // ---- Iniciar Pedido -------------------------------------------------------
        JLabel lblIniPedido = new JLabel("Start Pedido");
        lblIniPedido.setBounds(pedidoX + 440, pedidoY + 10, 120, 30);
        lblIniPedido.setBorder(gray);
        lblIniPedido.setForeground(corTextLabel);
        lblIniPedido.setHorizontalAlignment(SwingConstants.CENTER);
        lblIniPedido.setFont(font14);
        pnlPedidoVar.add(lblIniPedido);

        chkStartPed = new JCheckBox();
        chkStartPed.setBounds(pedidoX + 565, pedidoY + 10, 80, 30);
        chkStartPed.setBackground(corTextField);
        chkStartPed.setText("FALSE");
        chkStartPed.addActionListener((ActionEvent e) -> {
            if (chkStartPed.isSelected())
                chkStartPed.setText("TRUE");
            else
                chkStartPed.setText("FALSE");
        });
        chkStartPed.setFont(font14Bold);
        chkStartPed.setForeground(corJTFfont);
        chkStartPed.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkStartPed);

        // ----- Info Pedido ------------------------------------------------
        JLabel lblCorBlk1 = new JLabel("Cor Andar 1");
        lblCorBlk1.setBounds(pedidoX + 10, pedidoY + 45, 120, 30);
        lblCorBlk1.setBorder(gray);
        lblCorBlk1.setForeground(corTextLabel);
        lblCorBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk1.setFont(font14);
        pnlPedidoVar.add(lblCorBlk1);

        txtCorBlk1 = new JTextField();
        txtCorBlk1.setBounds(pedidoX + 135, pedidoY + 45, 80, 30);
        txtCorBlk1.setBackground(corTextFieldEst);
        txtCorBlk1.setBorder(border);
        txtCorBlk1.setFont(font14Bold);
        txtCorBlk1.setForeground(corJTFfont);
        txtCorBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorBlk1.setVisible(true);
        txtCorBlk1.setText("0");
        pnlPedidoVar.add(txtCorBlk1);

        JLabel lblPosBlk1 = new JLabel("Pos Andar 1");
        lblPosBlk1.setBounds(pedidoX + 10, pedidoY + 80, 120, 30);
        lblPosBlk1.setBorder(gray);
        lblPosBlk1.setForeground(corTextLabel);
        lblPosBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosBlk1.setFont(font14);
        pnlPedidoVar.add(lblPosBlk1);

        txtPosBlk1 = new JTextField();
        txtPosBlk1.setBounds(pedidoX + 135, pedidoY + 80, 80, 30);
        txtPosBlk1.setBackground(corTextFieldEst);
        txtPosBlk1.setBorder(border);
        txtPosBlk1.setFont(font14Bold);
        txtPosBlk1.setForeground(corJTFfont);
        txtPosBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosBlk1.setVisible(true);
        txtPosBlk1.setText("0");
        pnlPedidoVar.add(txtPosBlk1);

        JLabel lblCorL1Blk1 = new JLabel("Cor L1 B1");
        lblCorL1Blk1.setBounds(pedidoX + 10, pedidoY + 115, 120, 30);
        lblCorL1Blk1.setBorder(gray);
        lblCorL1Blk1.setForeground(corTextLabel);
        lblCorL1Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL1Blk1.setFont(font14);
        pnlPedidoVar.add(lblCorL1Blk1);

        txtCorL1Blk1 = new JTextField();
        txtCorL1Blk1.setBounds(pedidoX + 135, pedidoY + 115, 80, 30);
        txtCorL1Blk1.setBackground(corTextFieldEst);
        txtCorL1Blk1.setBorder(border);
        txtCorL1Blk1.setFont(font14Bold);
        txtCorL1Blk1.setForeground(corJTFfont);
        txtCorL1Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL1Blk1.setVisible(true);
        txtCorL1Blk1.setText("0");
        pnlPedidoVar.add(txtCorL1Blk1);

        JLabel lblCorL2Blk1 = new JLabel("Cor L2 B1");
        lblCorL2Blk1.setBounds(pedidoX + 10, pedidoY + 150, 120, 30);
        lblCorL2Blk1.setBorder(gray);
        lblCorL2Blk1.setForeground(corTextLabel);
        lblCorL2Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL2Blk1.setFont(font14);
        pnlPedidoVar.add(lblCorL2Blk1);

        txtCorL2Blk1 = new JTextField();
        txtCorL2Blk1.setBounds(pedidoX + 135, pedidoY + 150, 80, 30);
        txtCorL2Blk1.setBackground(corTextFieldEst);
        txtCorL2Blk1.setBorder(border);
        txtCorL2Blk1.setFont(font14Bold);
        txtCorL2Blk1.setForeground(corJTFfont);
        txtCorL2Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL2Blk1.setVisible(true);
        txtCorL2Blk1.setText("0");
        pnlPedidoVar.add(txtCorL2Blk1);

        JLabel lblCorL3Blk1 = new JLabel("Cor L3 B1");
        lblCorL3Blk1.setBounds(pedidoX + 10, pedidoY + 185, 120, 30);
        lblCorL3Blk1.setBorder(gray);
        lblCorL3Blk1.setForeground(corTextLabel);
        lblCorL3Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL3Blk1.setFont(font14);
        pnlPedidoVar.add(lblCorL3Blk1);

        txtCorL3Blk1 = new JTextField();
        txtCorL3Blk1.setBounds(pedidoX + 135, pedidoY + 185, 80, 30);
        txtCorL3Blk1.setBackground(corTextFieldEst);
        txtCorL3Blk1.setBorder(border);
        txtCorL3Blk1.setFont(font14Bold);
        txtCorL3Blk1.setForeground(corJTFfont);
        txtCorL3Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL3Blk1.setVisible(true);
        txtCorL3Blk1.setText("0");
        pnlPedidoVar.add(txtCorL3Blk1);

        JLabel lblPadL1Blk1 = new JLabel("Pad L1 B1");
        lblPadL1Blk1.setBounds(pedidoX + 10, pedidoY + 220, 120, 30);
        lblPadL1Blk1.setBorder(gray);
        lblPadL1Blk1.setForeground(corTextLabel);
        lblPadL1Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL1Blk1.setFont(font14);
        pnlPedidoVar.add(lblPadL1Blk1);

        txtPadL1Blk1 = new JTextField();
        txtPadL1Blk1.setBounds(pedidoX + 135, pedidoY + 220, 80, 30);
        txtPadL1Blk1.setBackground(corTextFieldEst);
        txtPadL1Blk1.setBorder(border);
        txtPadL1Blk1.setFont(font14Bold);
        txtPadL1Blk1.setForeground(corJTFfont);
        txtPadL1Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL1Blk1.setVisible(true);
        txtPadL1Blk1.setText("0");
        pnlPedidoVar.add(txtPadL1Blk1);

        JLabel lblPadL2Blk1 = new JLabel("Pad L2 B1");
        lblPadL2Blk1.setBounds(pedidoX + 10, pedidoY + 255, 120, 30);
        lblPadL2Blk1.setBorder(gray);
        lblPadL2Blk1.setForeground(corTextLabel);
        lblPadL2Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL2Blk1.setFont(font14);
        pnlPedidoVar.add(lblPadL2Blk1);

        txtPadL2Blk1 = new JTextField();
        txtPadL2Blk1.setBounds(pedidoX + 135, pedidoY + 255, 80, 30);
        txtPadL2Blk1.setBackground(corTextFieldEst);
        txtPadL2Blk1.setBorder(border);
        txtPadL2Blk1.setFont(font14Bold);
        txtPadL2Blk1.setForeground(corJTFfont);
        txtPadL2Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL2Blk1.setVisible(true);
        txtPadL2Blk1.setText("0");
        pnlPedidoVar.add(txtPadL2Blk1);

        JLabel lblPadL3Blk1 = new JLabel("Pad L3 B1");
        lblPadL3Blk1.setBounds(pedidoX + 10, pedidoY + 290, 120, 30);
        lblPadL3Blk1.setBorder(gray);
        lblPadL3Blk1.setForeground(corTextLabel);
        lblPadL3Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL3Blk1.setFont(font14);
        pnlPedidoVar.add(lblPadL3Blk1);

        txtPadL3Blk1 = new JTextField();
        txtPadL3Blk1.setBounds(pedidoX + 135, pedidoY + 290, 80, 30);
        txtPadL3Blk1.setBackground(corTextFieldEst);
        txtPadL3Blk1.setBorder(border);
        txtPadL3Blk1.setFont(font14Bold);
        txtPadL3Blk1.setForeground(corJTFfont);
        txtPadL3Blk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL3Blk1.setVisible(true);
        txtPadL3Blk1.setText("0");
        pnlPedidoVar.add(txtPadL3Blk1);

        JLabel lblProBlk1 = new JLabel("Pro Andar 1");
        lblProBlk1.setBounds(pedidoX + 10, pedidoY + 325, 120, 30);
        lblProBlk1.setBorder(gray);
        lblProBlk1.setForeground(corTextLabel);
        lblProBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblProBlk1.setFont(font14);
        pnlPedidoVar.add(lblProBlk1);

        txtProBlk1 = new JTextField();
        txtProBlk1.setBounds(pedidoX + 135, pedidoY + 325, 80, 30);
        txtProBlk1.setBackground(corTextFieldEst);
        txtProBlk1.setBorder(border);
        txtProBlk1.setFont(font14Bold);
        txtProBlk1.setForeground(corJTFfont);
        txtProBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        txtProBlk1.setVisible(true);
        txtProBlk1.setText("0");
        pnlPedidoVar.add(txtProBlk1);

        // ----Bloco 2---------

        JLabel lblCorBlk2 = new JLabel("Cor Andar 2");
        lblCorBlk2.setBounds(pedidoX + 225, pedidoY + 45, 120, 30);
        lblCorBlk2.setBorder(gray);
        lblCorBlk2.setForeground(corTextLabel);
        lblCorBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk2.setFont(font14);
        pnlPedidoVar.add(lblCorBlk2);

        txtCorBlk2 = new JTextField();
        txtCorBlk2.setBounds(pedidoX + 350, pedidoY + 45, 80, 30);
        txtCorBlk2.setBackground(corTextFieldEst);
        txtCorBlk2.setBorder(border);
        txtCorBlk2.setFont(font14Bold);
        txtCorBlk2.setForeground(corJTFfont);
        txtCorBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorBlk2.setVisible(true);
        txtCorBlk2.setText("0");
        pnlPedidoVar.add(txtCorBlk2);

        JLabel lblPosBlk2 = new JLabel("Pos Andar 2");
        lblPosBlk2.setBounds(pedidoX + 225, pedidoY + 80, 120, 30);
        lblPosBlk2.setBorder(gray);
        lblPosBlk2.setForeground(corTextLabel);
        lblPosBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosBlk2.setFont(font14);
        pnlPedidoVar.add(lblPosBlk2);

        txtPosBlk2 = new JTextField();
        txtPosBlk2.setBounds(pedidoX + 350, pedidoY + 80, 80, 30);
        txtPosBlk2.setBackground(corTextFieldEst);
        txtPosBlk2.setBorder(border);
        txtPosBlk2.setFont(font14Bold);
        txtPosBlk2.setForeground(corJTFfont);
        txtPosBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosBlk2.setVisible(true);
        txtPosBlk2.setText("0");
        pnlPedidoVar.add(txtPosBlk2);

        JLabel lblCorL1Blk2 = new JLabel("Cor L1 B2");
        lblCorL1Blk2.setBounds(pedidoX + 225, pedidoY + 115, 120, 30);
        lblCorL1Blk2.setBorder(gray);
        lblCorL1Blk2.setForeground(corTextLabel);
        lblCorL1Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL1Blk2.setFont(font14);

        pnlPedidoVar.add(lblCorL1Blk2);

        txtCorL1Blk2 = new JTextField();
        txtCorL1Blk2.setBounds(pedidoX + 350, pedidoY + 115, 80, 30);
        txtCorL1Blk2.setBackground(corTextFieldEst);
        txtCorL1Blk2.setBorder(border);
        txtCorL1Blk2.setFont(font14Bold);
        txtCorL1Blk2.setForeground(corJTFfont);
        txtCorL1Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL1Blk2.setVisible(true);
        txtCorL1Blk2.setText("0");
        pnlPedidoVar.add(txtCorL1Blk2);

        JLabel lblCorL2Blk2 = new JLabel("Cor L2 B2");
        lblCorL2Blk2.setBounds(pedidoX + 225, pedidoY + 150, 120, 30);
        lblCorL2Blk2.setBorder(gray);
        lblCorL2Blk2.setForeground(corTextLabel);
        lblCorL2Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL2Blk2.setFont(font14);
        pnlPedidoVar.add(lblCorL2Blk2);

        txtCorL2Blk2 = new JTextField();
        txtCorL2Blk2.setBounds(pedidoX + 350, pedidoY + 150, 80, 30);
        txtCorL2Blk2.setBackground(corTextFieldEst);
        txtCorL2Blk2.setBorder(border);
        txtCorL2Blk2.setFont(font14Bold);
        txtCorL2Blk2.setForeground(corJTFfont);
        txtCorL2Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL2Blk2.setVisible(true);
        txtCorL2Blk2.setText("0");
        pnlPedidoVar.add(txtCorL2Blk2);

        JLabel lblCorL3Blk2 = new JLabel("Cor L3 B2");
        lblCorL3Blk2.setBounds(pedidoX + 225, pedidoY + 185, 120, 30);
        lblCorL3Blk2.setBorder(gray);
        lblCorL3Blk2.setForeground(corTextLabel);
        lblCorL3Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL3Blk2.setFont(font14);
        pnlPedidoVar.add(lblCorL3Blk2);

        txtCorL3Blk2 = new JTextField();
        txtCorL3Blk2.setBounds(pedidoX + 350, pedidoY + 185, 80, 30);
        txtCorL3Blk2.setBackground(corTextFieldEst);
        txtCorL3Blk2.setBorder(border);
        txtCorL3Blk2.setFont(font14Bold);
        txtCorL3Blk2.setForeground(corJTFfont);
        txtCorL3Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL3Blk2.setVisible(true);
        txtCorL3Blk2.setText("0");
        pnlPedidoVar.add(txtCorL3Blk2);

        JLabel lblPadL1Blk2 = new JLabel("Pad L1 B2");
        lblPadL1Blk2.setBounds(pedidoX + 225, pedidoY + 220, 120, 30);
        lblPadL1Blk2.setBorder(gray);
        lblPadL1Blk2.setForeground(corTextLabel);
        lblPadL1Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL1Blk2.setFont(font14);
        pnlPedidoVar.add(lblPadL1Blk2);

        txtPadL1Blk2 = new JTextField();
        txtPadL1Blk2.setBounds(pedidoX + 350, pedidoY + 220, 80, 30);
        txtPadL1Blk2.setBackground(corTextFieldEst);
        txtPadL1Blk2.setBorder(border);
        txtPadL1Blk2.setFont(font14Bold);
        txtPadL1Blk2.setForeground(corJTFfont);
        txtPadL1Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL1Blk2.setVisible(true);
        txtPadL1Blk2.setText("0");
        pnlPedidoVar.add(txtPadL1Blk2);

        JLabel lblPadL2Blk2 = new JLabel("Pad L2 B2");
        lblPadL2Blk2.setBounds(pedidoX + 225, pedidoY + 255, 120, 30);
        lblPadL2Blk2.setBorder(gray);
        lblPadL2Blk2.setForeground(corTextLabel);
        lblPadL2Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL2Blk2.setFont(font14);
        pnlPedidoVar.add(lblPadL2Blk2);

        txtPadL2Blk2 = new JTextField();
        txtPadL2Blk2.setBounds(pedidoX + 350, pedidoY + 255, 80, 30);
        txtPadL2Blk2.setBackground(corTextFieldEst);
        txtPadL2Blk2.setBorder(border);
        txtPadL2Blk2.setFont(font14Bold);
        txtPadL2Blk2.setForeground(corJTFfont);
        txtPadL2Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL2Blk2.setVisible(true);
        txtPadL2Blk2.setText("0");
        pnlPedidoVar.add(txtPadL2Blk2);

        JLabel lblPadL3Blk2 = new JLabel("Pad L3 B2");
        lblPadL3Blk2.setBounds(pedidoX + 225, pedidoY + 290, 120, 30);
        lblPadL3Blk2.setBorder(gray);
        lblPadL3Blk2.setForeground(corTextLabel);
        lblPadL3Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL3Blk2.setFont(font14);
        pnlPedidoVar.add(lblPadL3Blk2);

        txtPadL3Blk2 = new JTextField();
        txtPadL3Blk2.setBounds(pedidoX + 350, pedidoY + 290, 80, 30);
        txtPadL3Blk2.setBackground(corTextFieldEst);
        txtPadL3Blk2.setBorder(border);
        txtPadL3Blk2.setFont(font14Bold);
        txtPadL3Blk2.setForeground(corJTFfont);
        txtPadL3Blk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL3Blk2.setVisible(true);
        txtPadL3Blk2.setText("0");
        pnlPedidoVar.add(txtPadL3Blk2);

        JLabel lblProBlk2 = new JLabel("Pro Andar 2");
        lblProBlk2.setBounds(pedidoX + 225, pedidoY + 325, 120, 30);
        lblProBlk2.setBorder(gray);
        lblProBlk2.setForeground(corTextLabel);
        lblProBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblProBlk2.setFont(font14);
        pnlPedidoVar.add(lblProBlk2);

        txtProBlk2 = new JTextField();
        txtProBlk2.setBounds(pedidoX + 350, pedidoY + 325, 80, 30);
        txtProBlk2.setBackground(corTextFieldEst);
        txtProBlk2.setBorder(border);
        txtProBlk2.setFont(font14Bold);
        txtProBlk2.setForeground(corJTFfont);
        txtProBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        txtProBlk2.setVisible(true);
        txtProBlk2.setText("0");
        pnlPedidoVar.add(txtProBlk2);

        // ----Bloco 3---------

        JLabel lblCorBlk3 = new JLabel("Cor Andar 3");
        lblCorBlk3.setBounds(pedidoX + 440, pedidoY + 45, 120, 30);
        lblCorBlk3.setBorder(gray);
        lblCorBlk3.setForeground(corTextLabel);
        lblCorBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk3.setFont(font14);
        pnlPedidoVar.add(lblCorBlk3);

        txtCorBlk3 = new JTextField();
        txtCorBlk3.setBounds(pedidoX + 565, pedidoY + 45, 80, 30);
        txtCorBlk3.setBackground(corTextFieldEst);
        txtCorBlk3.setBorder(border);
        txtCorBlk3.setFont(font14Bold);
        txtCorBlk3.setForeground(corJTFfont);
        txtCorBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorBlk3.setVisible(true);
        txtCorBlk3.setText("0");
        pnlPedidoVar.add(txtCorBlk3);

        JLabel lblPosBlk3 = new JLabel("Pos Andar 3");
        lblPosBlk3.setBounds(pedidoX + 440, pedidoY + 80, 120, 30);
        lblPosBlk3.setBorder(gray);
        lblPosBlk3.setForeground(corTextLabel);
        lblPosBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosBlk3.setFont(font14);
        pnlPedidoVar.add(lblPosBlk3);

        txtPosBlk3 = new JTextField();
        txtPosBlk3.setBounds(pedidoX + 565, pedidoY + 80, 80, 30);
        txtPosBlk3.setBackground(corTextFieldEst);
        txtPosBlk3.setBorder(border);
        txtPosBlk3.setFont(font14Bold);
        txtPosBlk3.setForeground(corJTFfont);
        txtPosBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosBlk3.setVisible(true);
        txtPosBlk3.setText("0");
        pnlPedidoVar.add(txtPosBlk3);

        JLabel lblCorL1Blk3 = new JLabel("Cor L1 B3");
        lblCorL1Blk3.setBounds(pedidoX + 440, pedidoY + 115, 120, 30);
        lblCorL1Blk3.setBorder(gray);
        lblCorL1Blk3.setForeground(corTextLabel);
        lblCorL1Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL1Blk3.setFont(font14);
        pnlPedidoVar.add(lblCorL1Blk3);

        txtCorL1Blk3 = new JTextField();
        txtCorL1Blk3.setBounds(pedidoX + 565, pedidoY + 115, 80, 30);
        txtCorL1Blk3.setBackground(corTextFieldEst);
        txtCorL1Blk3.setBorder(border);
        txtCorL1Blk3.setFont(font14Bold);
        txtCorL1Blk3.setForeground(corJTFfont);
        txtCorL1Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL1Blk3.setVisible(true);
        txtCorL1Blk3.setText("0");
        pnlPedidoVar.add(txtCorL1Blk3);

        JLabel lblCorL2Blk3 = new JLabel("Cor L2 B3");
        lblCorL2Blk3.setBounds(pedidoX + 440, pedidoY + 150, 120, 30);
        lblCorL2Blk3.setBorder(gray);
        lblCorL2Blk3.setForeground(corTextLabel);
        lblCorL2Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL2Blk3.setFont(font14);
        pnlPedidoVar.add(lblCorL2Blk3);

        txtCorL2Blk3 = new JTextField();
        txtCorL2Blk3.setBounds(pedidoX + 565, pedidoY + 150, 80, 30);
        txtCorL2Blk3.setBackground(corTextFieldEst);
        txtCorL2Blk3.setBorder(border);
        txtCorL2Blk3.setFont(font14Bold);
        txtCorL2Blk3.setForeground(corJTFfont);
        txtCorL2Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL2Blk3.setVisible(true);
        txtCorL2Blk3.setText("0");
        pnlPedidoVar.add(txtCorL2Blk3);

        JLabel lblCorL3Blk3 = new JLabel("Cor L3 B3");
        lblCorL3Blk3.setBounds(pedidoX + 440, pedidoY + 185, 120, 30);
        lblCorL3Blk3.setBorder(gray);
        lblCorL3Blk3.setForeground(corTextLabel);
        lblCorL3Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorL3Blk3.setFont(font14);
        pnlPedidoVar.add(lblCorL3Blk3);

        txtCorL3Blk3 = new JTextField();
        txtCorL3Blk3.setBounds(pedidoX + 565, pedidoY + 185, 80, 30);
        txtCorL3Blk3.setBackground(corTextFieldEst);
        txtCorL3Blk3.setBorder(border);
        txtCorL3Blk3.setFont(font14Bold);
        txtCorL3Blk3.setForeground(corJTFfont);
        txtCorL3Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtCorL3Blk3.setVisible(true);
        txtCorL3Blk3.setText("0");
        pnlPedidoVar.add(txtCorL3Blk3);

        JLabel lblPadL1Blk3 = new JLabel("Pad L1 B3");
        lblPadL1Blk3.setBounds(pedidoX + 440, pedidoY + 220, 120, 30);
        lblPadL1Blk3.setBorder(gray);
        lblPadL1Blk3.setForeground(corTextLabel);
        lblPadL1Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL1Blk3.setFont(font14);
        pnlPedidoVar.add(lblPadL1Blk3);

        txtPadL1Blk3 = new JTextField();
        txtPadL1Blk3.setBounds(pedidoX + 565, pedidoY + 220, 80, 30);
        txtPadL1Blk3.setBackground(corTextFieldEst);
        txtPadL1Blk3.setBorder(border);
        txtPadL1Blk3.setFont(font14Bold);
        txtPadL1Blk3.setForeground(corJTFfont);
        txtPadL1Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL1Blk3.setVisible(true);
        txtPadL1Blk3.setText("0");
        pnlPedidoVar.add(txtPadL1Blk3);

        JLabel lblPadL2Blk3 = new JLabel("Pad L2 B3");
        lblPadL2Blk3.setBounds(pedidoX + 440, pedidoY + 255, 120, 30);
        lblPadL2Blk3.setBorder(gray);
        lblPadL2Blk3.setForeground(corTextLabel);
        lblPadL2Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL2Blk3.setFont(font14);
        pnlPedidoVar.add(lblPadL2Blk3);

        txtPadL2Blk3 = new JTextField();
        txtPadL2Blk3.setBounds(pedidoX + 565, pedidoY + 255, 80, 30);
        txtPadL2Blk3.setBackground(corTextFieldEst);
        txtPadL2Blk3.setBorder(border);
        txtPadL2Blk3.setFont(font14Bold);
        txtPadL2Blk3.setForeground(corJTFfont);
        txtPadL2Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL2Blk3.setVisible(true);
        txtPadL2Blk3.setText("0");
        pnlPedidoVar.add(txtPadL2Blk3);

        JLabel lblPadL3Blk3 = new JLabel("Pad L3 B3");
        lblPadL3Blk3.setBounds(pedidoX + 440, pedidoY + 290, 120, 30);
        lblPadL3Blk3.setBorder(gray);
        lblPadL3Blk3.setForeground(corTextLabel);
        lblPadL3Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadL3Blk3.setFont(font14);
        pnlPedidoVar.add(lblPadL3Blk3);

        txtPadL3Blk3 = new JTextField();
        txtPadL3Blk3.setBounds(pedidoX + 565, pedidoY + 290, 80, 30);
        txtPadL3Blk3.setBackground(corTextFieldEst);
        txtPadL3Blk3.setBorder(border);
        txtPadL3Blk3.setFont(font14Bold);
        txtPadL3Blk3.setForeground(corJTFfont);
        txtPadL3Blk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtPadL3Blk3.setVisible(true);
        txtPadL3Blk3.setText("0");
        pnlPedidoVar.add(txtPadL3Blk3);

        JLabel lblProBlk3 = new JLabel("Pro Andar 3");
        lblProBlk3.setBounds(pedidoX + 440, pedidoY + 325, 120, 30);
        lblProBlk3.setBorder(gray);
        lblProBlk3.setForeground(corTextLabel);
        lblProBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblProBlk3.setFont(font14);
        pnlPedidoVar.add(lblProBlk3);

        txtProBlk3 = new JTextField();
        txtProBlk3.setBounds(pedidoX + 565, pedidoY + 325, 80, 30);
        txtProBlk3.setBackground(corTextFieldEst);
        txtProBlk3.setBorder(border);
        txtProBlk3.setFont(font14Bold);
        txtProBlk3.setForeground(corJTFfont);
        txtProBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        txtProBlk3.setVisible(true);
        txtProBlk3.setText("0");
        pnlPedidoVar.add(txtProBlk3);

        JLabel lblNumPedEst = new JLabel("Num Pedido");
        lblNumPedEst.setBounds(pedidoX + 10, pedidoY + 360, 120, 30);
        lblNumPedEst.setBorder(gray);
        lblNumPedEst.setForeground(corTextLabel);
        lblNumPedEst.setHorizontalAlignment(SwingConstants.CENTER);
        lblNumPedEst.setFont(font14);
        pnlPedidoVar.add(lblNumPedEst);

        txtNumPedEst = new JTextField();
        txtNumPedEst.setBounds(pedidoX + 135, pedidoY + 360, 80, 30);
        txtNumPedEst.setBackground(corTextFieldEst);
        txtNumPedEst.setBorder(border);
        txtNumPedEst.setFont(font14Bold);
        txtNumPedEst.setForeground(corJTFfont);
        txtNumPedEst.setHorizontalAlignment(SwingConstants.CENTER);
        txtNumPedEst.setVisible(true);
        txtNumPedEst.setText("0");
        pnlPedidoVar.add(txtNumPedEst);

        JLabel lblAndarEst = new JLabel("Andar Est");
        lblAndarEst.setBounds(pedidoX + 225, pedidoY + 360, 120, 30);
        lblAndarEst.setBorder(gray);
        lblAndarEst.setForeground(corTextLabel);
        lblAndarEst.setHorizontalAlignment(SwingConstants.CENTER);
        lblAndarEst.setFont(font14);
        pnlPedidoVar.add(lblAndarEst);

        txtAndarEst = new JTextField();
        txtAndarEst.setBounds(pedidoX + 350, pedidoY + 360, 80, 30);
        txtAndarEst.setBackground(corTextFieldEst);
        txtAndarEst.setBorder(border);
        txtAndarEst.setFont(font14Bold);
        txtAndarEst.setForeground(corJTFfont);
        txtAndarEst.setHorizontalAlignment(SwingConstants.CENTER);
        txtAndarEst.setVisible(true);
        txtAndarEst.setText("0");
        pnlPedidoVar.add(txtAndarEst);

        JLabel lblPosExpEst = new JLabel("Pos Exped");
        lblPosExpEst.setBounds(pedidoX + 440, pedidoY + 360, 120, 30);
        lblPosExpEst.setBorder(gray);
        lblPosExpEst.setForeground(corTextLabel);
        lblPosExpEst.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosExpEst.setFont(font14);
        pnlPedidoVar.add(lblPosExpEst);

        txtPosExpEst = new JTextField();
        txtPosExpEst.setBounds(pedidoX + 565, pedidoY + 360, 80, 30);
        txtPosExpEst.setBackground(corTextFieldEst);
        txtPosExpEst.setBorder(border);
        txtPosExpEst.setFont(font14Bold);
        txtPosExpEst.setForeground(corJTFfont);
        txtPosExpEst.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosExpEst.setVisible(true);
        txtPosExpEst.setText("0");
        pnlPedidoVar.add(txtPosExpEst);

        // ---- RecebidoOP
        // Estoque-------------------------------------------------------
        JLabel lblRecEstoq = new JLabel("Receb Estoque");
        lblRecEstoq.setBounds(pedidoX + 10, pedidoY + 395, 120, 30);
        lblRecEstoq.setBorder(gray);
        lblRecEstoq.setForeground(corTextLabel);
        lblRecEstoq.setHorizontalAlignment(SwingConstants.CENTER);
        lblRecEstoq.setFont(font14);
        pnlPedidoVar.add(lblRecEstoq);

        chkRecEstoq = new JCheckBox();
        chkRecEstoq.setBounds(pedidoX + 135, pedidoY + 395, 80, 30);
        chkRecEstoq.setBackground(corTextField);
        chkRecEstoq.setText("FALSE");
        chkRecEstoq.addActionListener((ActionEvent e) -> {
            if (chkRecEstoq.isSelected())
                chkRecEstoq.setText("TRUE");
            else
                chkRecEstoq.setText("FALSE");
        });
        chkRecEstoq.setFont(font14Bold);
        chkRecEstoq.setForeground(corJTFfont);
        chkRecEstoq.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkRecEstoq);

        // ---- Iniciar Guardar
        // Estoque-------------------------------------------------------
        JLabel lblIniGuard = new JLabel("Start Guardar");
        lblIniGuard.setBounds(pedidoX + 225, pedidoY + 395, 120, 30);
        lblIniGuard.setBorder(gray);
        lblIniGuard.setForeground(corTextLabel);
        lblIniGuard.setHorizontalAlignment(SwingConstants.CENTER);
        lblIniGuard.setFont(font14);
        pnlPedidoVar.add(lblIniGuard);

        chkIniGuard = new JCheckBox();
        chkIniGuard.setBounds(pedidoX + 350, pedidoY + 395, 80, 30);
        chkIniGuard.setBackground(corTextField);
        chkIniGuard.setText("FALSE");
        chkIniGuard.addActionListener((ActionEvent e) -> {
            if (chkIniGuard.isSelected())
                chkIniGuard.setText("TRUE");
            else
                chkIniGuard.setText("FALSE");
        });
        chkIniGuard.setFont(font14Bold);
        chkIniGuard.setForeground(corJTFfont);
        chkIniGuard.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkIniGuard);

        // ---- Posição Guardar
        // Estoque-------------------------------------------------------
        JLabel lblPosGuard = new JLabel("Pos Guardar");
        lblPosGuard.setBounds(pedidoX + 440, pedidoY + 395, 120, 30);
        lblPosGuard.setBorder(gray);
        lblPosGuard.setForeground(corTextLabel);
        lblPosGuard.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosGuard.setFont(font14);
        pnlPedidoVar.add(lblPosGuard);

        txtPosGuard = new JTextField();
        txtPosGuard.setBounds(pedidoX + 565, pedidoY + 395, 80, 30);
        txtPosGuard.setBackground(corTextFieldEst);
        txtPosGuard.setBorder(border);
        txtPosGuard.setFont(font14Bold);
        txtPosGuard.setForeground(corJTFfont);
        txtPosGuard.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosGuard.setVisible(true);
        txtPosGuard.setText("0");
        pnlPedidoVar.add(txtPosGuard);

        // ---- Recebido Op Processo
        // -------------------------------------------------------

        JLabel lblProcesso = new JLabel("PROCESSO");
        lblProcesso.setBounds(pedidoX + 10, pedidoY + 470, 205, 30);
        lblProcesso.setBorder(gray);
        lblProcesso.setForeground(corTextLabel);
        lblProcesso.setHorizontalAlignment(SwingConstants.CENTER);
        lblProcesso.setFont(font14);
        pnlPedidoVar.add(lblProcesso);

        JLabel lblRecOpPro = new JLabel("Rec Op Proc");
        lblRecOpPro.setBounds(pedidoX + 10, pedidoY + 505, 120, 30);
        lblRecOpPro.setBorder(gray);
        lblRecOpPro.setForeground(corTextLabel);
        lblRecOpPro.setHorizontalAlignment(SwingConstants.CENTER);
        lblRecOpPro.setFont(font14);
        pnlPedidoVar.add(lblRecOpPro);

        chkRecOpPro = new JCheckBox();
        chkRecOpPro.setBounds(pedidoX + 135, pedidoY + 505, 80, 30);
        chkRecOpPro.setBackground(corTextField);
        chkRecOpPro.setText("FALSE");
        chkRecOpPro.addActionListener((ActionEvent e) -> {
            if (chkRecOpPro.isSelected())
                chkRecOpPro.setText("TRUE");
            else
                chkRecOpPro.setText("FALSE");
        });
        chkRecOpPro.setFont(font14Bold);
        chkRecOpPro.setForeground(corJTFfont);
        chkRecOpPro.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkRecOpPro);
        // --------------------------------------------------------

        // ---- Recebido Op Montagem ------------------------------

        JLabel lblMontagem = new JLabel("MONTAGEM");
        lblMontagem.setBounds(pedidoX + 225, pedidoY + 470, 205, 30);
        lblMontagem.setBorder(gray);
        lblMontagem.setForeground(corTextLabel);
        lblMontagem.setHorizontalAlignment(SwingConstants.CENTER);
        lblMontagem.setFont(font14);
        pnlPedidoVar.add(lblMontagem);

        JLabel lblRecOpMon = new JLabel("Rec Op Mont");
        lblRecOpMon.setBounds(pedidoX + 225, pedidoY + 505, 120, 30);
        lblRecOpMon.setBorder(gray);
        lblRecOpMon.setForeground(corTextLabel);
        lblRecOpMon.setHorizontalAlignment(SwingConstants.CENTER);
        lblRecOpMon.setFont(font14);
        pnlPedidoVar.add(lblRecOpMon);

        chkRecOpMon = new JCheckBox();
        chkRecOpMon.setBounds(pedidoX + 350, pedidoY + 505, 80, 30);
        chkRecOpMon.setBackground(corTextField);
        chkRecOpMon.setText("FALSE");
        chkRecOpMon.addActionListener((ActionEvent e) -> {
            if (chkRecOpMon.isSelected())
                chkRecOpMon.setText("TRUE");
            else
                chkRecOpMon.setText("FALSE");
        });
        chkRecOpMon.setFont(font14Bold);
        chkRecOpMon.setForeground(corJTFfont);
        chkRecOpMon.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkRecOpMon);

        // ---- Recebido Op Expedição ------------------------------

        JLabel lblExpedicao = new JLabel("EXPEDIÇÃO");
        lblExpedicao.setBounds(pedidoX + 440, pedidoY + 470, 205, 30);
        lblExpedicao.setBorder(gray);
        lblExpedicao.setForeground(corTextLabel);
        lblExpedicao.setHorizontalAlignment(SwingConstants.CENTER);
        lblExpedicao.setFont(font14);
        pnlPedidoVar.add(lblExpedicao);

        JLabel lblRecOpExp = new JLabel("Rec Op Expedi");
        lblRecOpExp.setBounds(pedidoX + 440, pedidoY + 505, 120, 30);
        lblRecOpExp.setBorder(gray);
        lblRecOpExp.setForeground(corTextLabel);
        lblRecOpExp.setHorizontalAlignment(SwingConstants.CENTER);
        lblRecOpExp.setFont(font14);
        pnlPedidoVar.add(lblRecOpExp);

        chkRecOpExp = new JCheckBox();
        chkRecOpExp.setBounds(pedidoX + 565, pedidoY + 505, 80, 30);
        chkRecOpExp.setBackground(corTextField);
        chkRecOpExp.setText("FALSE");
        chkRecOpExp.addActionListener((ActionEvent e) -> {
            if (chkRecOpExp.isSelected())
                chkRecOpExp.setText("TRUE");
            else
                chkRecOpExp.setText("FALSE");
        });
        chkRecOpExp.setFont(font14Bold);
        chkRecOpExp.setForeground(corJTFfont);
        chkRecOpExp.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkRecOpExp);

        // ---- Recebido OP
        // Expedição-------------------------------------------------------
        JLabel lblRecExpedi = new JLabel("Receb Expedição");
        lblRecExpedi.setBounds(pedidoX + 10, pedidoY + 550, 120, 30);
        lblRecExpedi.setBorder(gray);
        lblRecExpedi.setForeground(corTextLabel);
        lblRecExpedi.setHorizontalAlignment(SwingConstants.CENTER);
        lblRecExpedi.setFont(font14);
        pnlPedidoVar.add(lblRecExpedi);

        chkRecExpedi = new JCheckBox();
        chkRecExpedi.setBounds(pedidoX + 135, pedidoY + 550, 80, 30);
        chkRecExpedi.setBackground(corTextField);
        chkRecExpedi.setText("FALSE");
        chkRecExpedi.addActionListener((ActionEvent e) -> {
            if (chkRecExpedi.isSelected())
                chkRecExpedi.setText("TRUE");
            else
                chkRecExpedi.setText("FALSE");
        });
        chkRecExpedi.setFont(font14Bold);
        chkRecExpedi.setForeground(corJTFfont);
        chkRecExpedi.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkRecExpedi);

        // ---- Iniciar Guardar
        // Expedição-------------------------------------------------------
        JLabel lblIniGuardExp = new JLabel("Start Guard Exp");
        lblIniGuardExp.setBounds(pedidoX + 225, pedidoY + 550, 120, 30);
        lblIniGuardExp.setBorder(gray);
        lblIniGuardExp.setForeground(corTextLabel);
        lblIniGuardExp.setHorizontalAlignment(SwingConstants.CENTER);
        lblIniGuardExp.setFont(font14);
        pnlPedidoVar.add(lblIniGuardExp);

        chkIniGuardExp = new JCheckBox();
        chkIniGuardExp.setBounds(pedidoX + 350, pedidoY + 550, 80, 30);
        chkIniGuardExp.setBackground(corTextField);
        chkIniGuardExp.setText("FALSE");
        chkIniGuardExp.addActionListener((ActionEvent e) -> {

            PanelStart.plcWriteExp = new PlcConnector(MainFrame.ipExpedicao, 102); // IniciarGuardarExpedicao
            try {
                PanelStart.plcWriteExp.connect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

            if (chkIniGuardExp.isSelected()) {
                chkIniGuardExp.setText("TRUE");

                try {
                    PanelStart.plcWriteExp.writeBit(9, 2, 1, Boolean.parseBoolean("TRUE"));
                } catch (Exception ex) {

                }

            } else {
                chkIniGuardExp.setText("FALSE");

                // PanelStart.plcWriteExp = new PlcConnector(MainFrame.ipExpedicao,102); //
                // IniciarGuardarExpedicao
                try {
                    PanelStart.plcWriteExp.writeBit(9, 2, 1, Boolean.parseBoolean("FALSE"));
                } catch (Exception ex) {

                }
            }

            try {
                PanelStart.plcWriteExp.disconnect();
            } catch (Exception e1) {

                e1.printStackTrace();
            }

        });
        chkIniGuardExp.setFont(font14Bold);
        chkIniGuardExp.setForeground(corJTFfont);
        chkIniGuardExp.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkIniGuardExp);

        // ---- Posição Guardar Expedição
        // -------------------------------------------------------
        JLabel lblPosGuardExp = new JLabel("Pos Guard Exp");
        lblPosGuardExp.setBounds(pedidoX + 440, pedidoY + 550, 120, 30);
        lblPosGuardExp.setBorder(gray);
        lblPosGuardExp.setForeground(corTextLabel);
        lblPosGuardExp.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosGuardExp.setFont(font14);
        pnlPedidoVar.add(lblPosGuardExp);

        txtPosGuardExp = new JTextField();
        txtPosGuardExp.setBounds(pedidoX + 565, pedidoY + 550, 80, 30);
        txtPosGuardExp.setBackground(corTextFieldExp);
        txtPosGuardExp.setBorder(border);
        txtPosGuardExp.setFont(font14Bold);
        txtPosGuardExp.setForeground(corJTFfont);
        txtPosGuardExp.setHorizontalAlignment(SwingConstants.CENTER);
        txtPosGuardExp.setVisible(true);
        txtPosGuardExp.setText("0");
        pnlPedidoVar.add(txtPosGuardExp);

        // ---- Posições Magazine Expedição (Leitura da DB9 e DB20)
        // -------------------------------------------------------

        JLabel lblMagazine = new JLabel("MAGAZINE ESTAÇÃO EXPEDIÇÃO");
        lblMagazine.setBounds(pedidoX + 10, pedidoY + 615, 418, 30);
        lblMagazine.setBorder(gray);
        lblMagazine.setForeground(corTextLabel);
        lblMagazine.setHorizontalAlignment(SwingConstants.CENTER);
        lblMagazine.setFont(font14);
        // pnlPedidoVar.add(lblMagazine);

        // --------------------------------------------------------------------------------

        lblPos1Exped = new JLabel("01");
        lblPos1Exped.setBounds(pedidoX + 10, pedidoY + 650, 30, 30);
        lblPos1Exped.setBorder(gray);
        lblPos1Exped.setForeground(corTextLabel);
        lblPos1Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos1Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos1Exped);

        txtPos1Exped = new JTextField();
        txtPos1Exped.setBounds(pedidoX + 45, pedidoY + 650, 60, 30);
        txtPos1Exped.setBackground(corTextFieldExp);
        txtPos1Exped.setBorder(border);
        txtPos1Exped.setFont(font14Bold);
        txtPos1Exped.setForeground(corJTFfont);
        txtPos1Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos1Exped.setVisible(true);
        txtPos1Exped.setText("0");
        // pnlPedidoVar.add(txtPos1Exped);

        lblPos2Exped = new JLabel("02");
        lblPos2Exped.setBounds(pedidoX + 118, pedidoY + 650, 30, 30);
        lblPos2Exped.setBorder(gray);
        lblPos2Exped.setForeground(corTextLabel);
        lblPos2Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos2Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos2Exped);

        txtPos2Exped = new JTextField();
        txtPos2Exped.setBounds(pedidoX + 153, pedidoY + 650, 60, 30);
        txtPos2Exped.setBackground(corTextFieldExp);
        txtPos2Exped.setBorder(border);
        txtPos2Exped.setFont(font14Bold);
        txtPos2Exped.setForeground(corJTFfont);
        txtPos2Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos2Exped.setVisible(true);
        txtPos2Exped.setText("0");
        // pnlPedidoVar.add(txtPos2Exped);

        lblPos3Exped = new JLabel("03");
        lblPos3Exped.setBounds(pedidoX + 225, pedidoY + 650, 30, 30);
        lblPos3Exped.setBorder(gray);
        lblPos3Exped.setForeground(corTextLabel);
        lblPos3Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos3Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos3Exped);

        txtPos3Exped = new JTextField();
        txtPos3Exped.setBounds(pedidoX + 260, pedidoY + 650, 60, 30);
        txtPos3Exped.setBackground(corTextFieldExp);
        txtPos3Exped.setBorder(border);
        txtPos3Exped.setFont(font14Bold);
        txtPos3Exped.setForeground(corJTFfont);
        txtPos3Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos3Exped.setVisible(true);
        txtPos3Exped.setText("0");
        // pnlPedidoVar.add(txtPos3Exped);

        lblPos4Exped = new JLabel("04");
        lblPos4Exped.setBounds(pedidoX + 333, pedidoY + 650, 30, 30);
        lblPos4Exped.setBorder(gray);
        lblPos4Exped.setForeground(corTextLabel);
        lblPos4Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos4Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos4Exped);

        txtPos4Exped = new JTextField();
        txtPos4Exped.setBounds(pedidoX + 368, pedidoY + 650, 60, 30);
        txtPos4Exped.setBackground(corTextFieldExp);
        txtPos4Exped.setBorder(border);
        txtPos4Exped.setFont(font14Bold);
        txtPos4Exped.setForeground(corJTFfont);
        txtPos4Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos4Exped.setVisible(true);
        txtPos4Exped.setText("0");
        // pnlPedidoVar.add(txtPos4Exped);

        lblPos5Exped = new JLabel("05");
        lblPos5Exped.setBounds(pedidoX + 10, pedidoY + 685, 30, 30);
        lblPos5Exped.setBorder(gray);
        lblPos5Exped.setForeground(corTextLabel);
        lblPos5Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos5Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos5Exped);

        txtPos5Exped = new JTextField();
        txtPos5Exped.setBounds(pedidoX + 45, pedidoY + 685, 60, 30);
        txtPos5Exped.setBackground(corTextFieldExp);
        txtPos5Exped.setBorder(border);
        txtPos5Exped.setFont(font14Bold);
        txtPos5Exped.setForeground(corJTFfont);
        txtPos5Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos5Exped.setVisible(true);
        txtPos5Exped.setText("0");
        // pnlPedidoVar.add(txtPos5Exped);

        lblPos6Exped = new JLabel("06");
        lblPos6Exped.setBounds(pedidoX + 118, pedidoY + 685, 30, 30);
        lblPos6Exped.setBorder(gray);
        lblPos6Exped.setForeground(corTextLabel);
        lblPos6Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos6Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos6Exped);

        txtPos6Exped = new JTextField();
        txtPos6Exped.setBounds(pedidoX + 153, pedidoY + 685, 60, 30);
        txtPos6Exped.setBackground(corTextFieldExp);
        txtPos6Exped.setBorder(border);
        txtPos6Exped.setFont(font14Bold);
        txtPos6Exped.setForeground(corJTFfont);
        txtPos6Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos6Exped.setVisible(true);
        txtPos6Exped.setText("0");
        // pnlPedidoVar.add(txtPos6Exped);

        lblPos7Exped = new JLabel("07");
        lblPos7Exped.setBounds(pedidoX + 225, pedidoY + 685, 30, 30);
        lblPos7Exped.setBorder(gray);
        lblPos7Exped.setForeground(corTextLabel);
        lblPos7Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos7Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos7Exped);

        txtPos7Exped = new JTextField();
        txtPos7Exped.setBounds(pedidoX + 260, pedidoY + 685, 60, 30);
        txtPos7Exped.setBackground(corTextFieldExp);
        txtPos7Exped.setBorder(border);
        txtPos7Exped.setFont(font14Bold);
        txtPos7Exped.setForeground(corJTFfont);
        txtPos7Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos7Exped.setVisible(true);
        txtPos7Exped.setText("0");
        // pnlPedidoVar.add(txtPos7Exped);

        lblPos8Exped = new JLabel("08");
        lblPos8Exped.setBounds(pedidoX + 333, pedidoY + 685, 30, 30);
        lblPos8Exped.setBorder(gray);
        lblPos8Exped.setForeground(corTextLabel);
        lblPos8Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos8Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos8Exped);

        txtPos8Exped = new JTextField();
        txtPos8Exped.setBounds(pedidoX + 368, pedidoY + 685, 60, 30);
        txtPos8Exped.setBackground(corTextFieldExp);
        txtPos8Exped.setBorder(border);
        txtPos8Exped.setFont(font14Bold);
        txtPos8Exped.setForeground(corJTFfont);
        txtPos8Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos8Exped.setVisible(true);
        txtPos8Exped.setText("0");
        // pnlPedidoVar.add(txtPos8Exped);

        lblPos9Exped = new JLabel("09");
        lblPos9Exped.setBounds(pedidoX + 10, pedidoY + 720, 30, 30);
        lblPos9Exped.setBorder(gray);
        lblPos9Exped.setForeground(corTextLabel);
        lblPos9Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos9Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos9Exped);

        txtPos9Exped = new JTextField();
        txtPos9Exped.setBounds(pedidoX + 45, pedidoY + 720, 60, 30);
        txtPos9Exped.setBackground(corTextFieldExp);
        txtPos9Exped.setBorder(border);
        txtPos9Exped.setFont(font14Bold);
        txtPos9Exped.setForeground(corJTFfont);
        txtPos9Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos9Exped.setVisible(true);
        txtPos9Exped.setText("0");
        // pnlPedidoVar.add(txtPos9Exped);

        lblPos10Exped = new JLabel("10");
        lblPos10Exped.setBounds(pedidoX + 118, pedidoY + 720, 30, 30);
        lblPos10Exped.setBorder(gray);
        lblPos10Exped.setForeground(corTextLabel);
        lblPos10Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos10Exped.setFont(font14);
        pnlPedidoVar.add(lblPos10Exped);

        txtPos10Exped = new JTextField();
        txtPos10Exped.setBounds(pedidoX + 153, pedidoY + 720, 60, 30);
        txtPos10Exped.setBackground(corTextFieldExp);
        txtPos10Exped.setBorder(border);
        txtPos10Exped.setFont(font14Bold);
        txtPos10Exped.setForeground(corJTFfont);
        txtPos10Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos10Exped.setVisible(true);
        txtPos10Exped.setText("0");
        // pnlPedidoVar.add(txtPos10Exped);

        lblPos11Exped = new JLabel("11");
        lblPos11Exped.setBounds(pedidoX + 225, pedidoY + 720, 30, 30);
        lblPos11Exped.setBorder(gray);
        lblPos11Exped.setForeground(corTextLabel);
        lblPos11Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos11Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos11Exped);

        txtPos11Exped = new JTextField();
        txtPos11Exped.setBounds(pedidoX + 260, pedidoY + 720, 60, 30);
        txtPos11Exped.setBackground(corTextFieldExp);
        txtPos11Exped.setBorder(border);
        txtPos11Exped.setFont(font14Bold);
        txtPos11Exped.setForeground(corJTFfont);
        txtPos11Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos11Exped.setVisible(true);
        txtPos11Exped.setText("0");
        // pnlPedidoVar.add(txtPos11Exped);

        lblPos12Exped = new JLabel("12");
        lblPos12Exped.setBounds(pedidoX + 333, pedidoY + 720, 30, 30);
        lblPos12Exped.setBorder(gray);
        lblPos12Exped.setForeground(corTextLabel);
        lblPos12Exped.setHorizontalAlignment(SwingConstants.CENTER);
        lblPos12Exped.setFont(font14);
        // pnlPedidoVar.add(lblPos12Exped);

        txtPos12Exped = new JTextField();
        txtPos12Exped.setBounds(pedidoX + 368, pedidoY + 720, 60, 30);
        txtPos12Exped.setBackground(corTextFieldExp);
        txtPos12Exped.setBorder(border);
        txtPos12Exped.setFont(font14Bold);
        txtPos12Exped.setForeground(corJTFfont);
        txtPos12Exped.setHorizontalAlignment(SwingConstants.CENTER);
        txtPos12Exped.setVisible(true);
        txtPos12Exped.setText("0");
        // pnlPedidoVar.add(txtPos12Exped);

        // --------------------------------------------------------------------------------

        chkReadCiclic = new JCheckBox();
        chkReadCiclic.setBounds(pedidoX + 10, pedidoY + 615, 205, 30);
        chkReadCiclic.setBackground(corTextField);
        chkReadCiclic.setText("INICIAR LEITURA");

        chkReadCiclic.setFont(font14Bold);
        chkReadCiclic.setForeground(corJTFfont);
        // chkReadCiclic.setSelected(!MainFrame.readOnly);
        chkReadCiclic.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkReadCiclic);

        chkReadOnly = new JCheckBox();
        chkReadOnly.setBounds(pedidoX + 225, pedidoY + 615, 205, 30);
        chkReadOnly.setBackground(corTextField);
        chkReadOnly.setText("READ ONLY");
        chkReadOnly.addActionListener((ActionEvent e) -> {
            if (chkReadOnly.isSelected()) {
                MainFrame.readOnly = true;
                Panel3.enviaPedidoButton.setEnabled(false);
                Panel3.startPedidoButton.setEnabled(false);
                Panel3.updateMagazineButton.setEnabled(false);
                Panel3.pnlPedido.setEnabled(false);
                Panel3.pnlPedido.setBorder(gray);
            } else {
                MainFrame.readOnly = false;
                Panel3.enviaPedidoButton.setEnabled(true);
                Panel3.startPedidoButton.setEnabled(true);

                Panel3.updateMagazineButton.setEnabled(true);
                ConfigPedido.registrarPedidoBtn.setEnabled(true);

                Panel3.pnlPedido.setEnabled(true);
                Panel3.pnlPedido.setBorder(Panel3.greenOn);

            }
        });

        chkReadOnly.setFont(font14Bold);
        chkReadOnly.setForeground(corJTFfont);
        chkReadOnly.setHorizontalAlignment(SwingConstants.CENTER);
        pnlPedidoVar.add(chkReadOnly);

        btnExibirProdcao = new JButton("EXIBIR PRODUÇÃO");
        btnExibirProdcao.setBounds(pedidoX + 440, pedidoY + 615, 205, 30);
        btnExibirProdcao.setFont(font14);
        btnExibirProdcao.setEnabled(true);
        btnExibirProdcao.setForeground(corTextLabel);
        btnExibirProdcao.setBackground(corTextField);

        btnExibirProdcao.addActionListener((ActionEvent e) -> {
            MainFrame.cardLayout.show(MainFrame.painelPrincipal, "startPanel");
            PanelStart.pnlOpSmart.setVisible(true);
            PanelStart.button.setVisible(false);
            PanelStart.btnOkVoltar.setVisible(true);

            // revalidate();
            // repaint();
        });
        // btnS7PLCGiro.setBorder(border);
        pnlPedidoVar.add(btnExibirProdcao);

        // --------------------------------------------------------

        // Adiciona os componentes ao painel

        add(pnlPedidoVar);

    }

}
