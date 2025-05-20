package com.smart40;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class PanelStatus extends JPanel {

    // private MainFrame mainFrame;

    public static JLabel lblStatEstoq;
    public static JTextField txtStatEstoq;
    public static JCheckBox chkEstoq;
    public static JCheckBox chkRecOpEst;

    public static JLabel lblStatProces;
    public static JTextField txtStatProces;
    public static JCheckBox chkProces;
    public static JCheckBox chkRecOpPro;

    public static JLabel lblStatMontag;
    public static JTextField txtStatMontag;
    public static JCheckBox chkMontag;
    public static JCheckBox chkRecOpMon;

    public static JLabel lblStatExpedi;
    public static JTextField txtStatExpedi;
    public static JCheckBox chkExpedi;
    public static JCheckBox chkRecOpExp;

    private static final Color corBackPnl = new Color(10, 50, 50);
    Color corTextLabel = new Color(250, 250, 250);
    Color corTextField = new Color(50, 90, 90);

    Color corTextFieldSts = new Color(10, 10, 10);

    static Color corJTFfont = new Color(50, 255, 255);
    //Color corJLblTitle = new Color(30, 100, 190);

    private static final Color corBackPnlOn = new Color(10, 120, 50);

    Font font14 = new Font("Arial", Font.PLAIN, 14);
    Font font14Bold = new Font("Arial", Font.BOLD, 14);
    //Font font16 = new Font("Arial", Font.PLAIN, 16);
    //Font font18 = new Font("Arial", Font.PLAIN, 18);
    //Font font22 = new Font("Arial", Font.BOLD, 22);

    //Font fontTitle = new Font("Arial", Font.BOLD, 32);

    Border gray = BorderFactory.createLineBorder(Color.WHITE);

    Border border = BorderFactory.createLineBorder(Color.WHITE, 1);

    public PanelStatus(MainFrame mainFrame, int w, int h) {
        // Configura o layout do painel
        setSize(w, h);
        setLayout(null);
        setVisible(true);
        //setOpaque(false);
        // this.mainFrame = mainFrame;

        // setBorder(gray);
        setVisible(true);
        //setBackground(corBackPnl);
        setBackground(Color.black);

        // this.setBackground(new Color(180, 200, 250));

        // Cria os componentes
        lblStatEstoq = new JLabel("ESTOQUE");
        lblStatEstoq.setBounds(15, 15, 120, 30);
        lblStatEstoq.setBorder(gray);
        lblStatEstoq.setForeground(corTextLabel);
        lblStatEstoq.setOpaque(true);
        lblStatEstoq.setBackground(corBackPnl);
        lblStatEstoq.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatEstoq.setFont(font14);
        add(lblStatEstoq);

        txtStatEstoq = new JTextField();
        txtStatEstoq.setBounds(140, 15, 130, 30);
        txtStatEstoq.setBackground(corTextFieldSts);
        txtStatEstoq.setBorder(border);
        txtStatEstoq.setFont(font14Bold);
        txtStatEstoq.setForeground(corJTFfont);
        txtStatEstoq.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtStatEstoq);

        chkEstoq = new JCheckBox();
        chkEstoq.setBounds(275, 15, 40, 30);
        chkEstoq.setBackground(corTextField);
        chkEstoq.addActionListener((ActionEvent e) -> {
            if (chkEstoq.isSelected()) {
                lblStatEstoq.setBackground(corBackPnlOn);
            } else {
                lblStatEstoq.setBackground(corBackPnl);
            }
        });
        chkEstoq.setFont(font14Bold);
        chkEstoq.setForeground(corJTFfont);
        chkEstoq.setHorizontalAlignment(SwingConstants.CENTER);
        add(chkEstoq);

        lblStatProces = new JLabel("PROCESSO");
        lblStatProces.setBounds(15, 55, 120, 30);
        lblStatProces.setBorder(gray);
        lblStatProces.setForeground(corTextLabel);
        lblStatProces.setOpaque(true);
        lblStatProces.setBackground(corBackPnl);
        lblStatProces.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatProces.setFont(font14);
        add(lblStatProces);

        txtStatProces = new JTextField();
        txtStatProces.setBounds(140, 55, 130, 30);
        txtStatProces.setBackground(corTextFieldSts);
        txtStatProces.setBorder(border);
        txtStatProces.setFont(font14Bold);
        txtStatProces.setForeground(corJTFfont);
        txtStatProces.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtStatProces);

        chkProces = new JCheckBox();
        chkProces.setBounds(275, 55, 40, 30);
        chkProces.setBackground(corTextField);
        chkProces.addActionListener((ActionEvent e) -> {
            if (chkProces.isSelected()) {
                lblStatProces.setBackground(corBackPnlOn);
            } else {
                lblStatProces.setBackground(corBackPnl);
            }
        });
        chkProces.setFont(font14Bold);
        chkProces.setForeground(corJTFfont);
        chkProces.setHorizontalAlignment(SwingConstants.CENTER);
        add(chkProces);

        lblStatMontag = new JLabel("MONTAGEM");
        lblStatMontag.setBounds(370, 15, 120, 30);
        lblStatMontag.setBorder(gray);
        lblStatMontag.setForeground(corTextLabel);
        lblStatMontag.setOpaque(true);
        lblStatMontag.setBackground(corBackPnl);
        lblStatMontag.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatMontag.setFont(font14);
        add(lblStatMontag);

        txtStatMontag = new JTextField();
        txtStatMontag.setBounds(495, 15, 130, 30);
        txtStatMontag.setBackground(corTextFieldSts);
        txtStatMontag.setBorder(border);
        txtStatMontag.setFont(font14Bold);
        txtStatMontag.setForeground(corJTFfont);
        txtStatMontag.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtStatMontag);

        chkMontag = new JCheckBox();
        chkMontag.setBounds(630, 15, 40, 30);
        chkMontag.setBackground(corTextField);
        chkMontag.addActionListener((ActionEvent e) -> {
            if (chkMontag.isSelected()) {
                lblStatMontag.setBackground(corBackPnlOn);
            } else {
                lblStatMontag.setBackground(corBackPnl);
            }
        });
        chkMontag.setFont(font14Bold);
        chkMontag.setForeground(corJTFfont);
        chkMontag.setHorizontalAlignment(SwingConstants.CENTER);
        add(chkMontag);

        lblStatExpedi = new JLabel("EXPEDIÇÃO");
        lblStatExpedi.setBounds(370, 55, 120, 30);
        lblStatExpedi.setBorder(gray);
        lblStatExpedi.setForeground(corTextLabel);
        lblStatExpedi.setOpaque(true);
        lblStatExpedi.setBackground(corBackPnl);
        lblStatExpedi.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatExpedi.setFont(font14);
        add(lblStatExpedi);

        txtStatExpedi = new JTextField();
        txtStatExpedi.setBounds(495, 55, 130, 30);
        txtStatExpedi.setBackground(corTextFieldSts);
        txtStatExpedi.setBorder(border);
        txtStatExpedi.setFont(font14Bold);
        txtStatExpedi.setForeground(corJTFfont);
        txtStatExpedi.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtStatExpedi);

        chkExpedi = new JCheckBox();
        chkExpedi.setBounds(630, 55, 40, 30);
        chkExpedi.setBackground(corTextField);
        chkExpedi.addActionListener((ActionEvent e) -> {
            if (chkExpedi.isSelected()) {
                lblStatExpedi.setBackground(corBackPnlOn);
            } else {
                lblStatExpedi.setBackground(corBackPnl);
            }
        });
        chkExpedi.setFont(font14Bold);
        chkExpedi.setForeground(corJTFfont);
        chkExpedi.setHorizontalAlignment(SwingConstants.CENTER);
        add(chkExpedi);

        // Adiciona os componentes ao painel

    }

}
