package com.smart40;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class ConfigPedido extends JPanel {
    // private JTextField textField;
    // private JButton button;

    public static JPanel pnlPedidoConf;

    public static JButton registrarPedidoBtn;

    @SuppressWarnings("FieldMayBeFinal")
    private static Color corBackPnl = new Color(10, 50, 50);
    @SuppressWarnings("FieldMayBeFinal")
    private static Color lightGreen = new Color(10, 255, 50);

    public static Border gray = BorderFactory.createLineBorder(Color.WHITE);
    public static Border red = BorderFactory.createLineBorder(Color.red, 2);
    public static Border greenOn = BorderFactory.createLineBorder(new Color(0, 255, 0), 2);
    public static Border greenOff = BorderFactory.createLineBorder(new Color(0, 128, 0), 2);
    public static Border yellow = BorderFactory.createLineBorder(Color.yellow, 1);
    public static Border lGreenBorder = BorderFactory.createLineBorder(lightGreen, 2);

    Border border = BorderFactory.createLineBorder(Color.WHITE, 1);

    Color corTextLabel = new Color(250, 250, 250);
    static Color corTextField = new Color(50, 90, 90);

    Color corTextFieldEst = new Color(10, 10, 10);

    static Color corJTFfont = new Color(50, 255, 255);

    // private static Color corBackPnlOn = new Color(10, 120, 50);

    Font font14 = new Font("Arial", Font.PLAIN, 14);
    static Font font14Bold = new Font("Arial", Font.BOLD, 14);

    public static int corBlk1 = 0;

    public String imgBlk1Path = "src/main/java/com/smart40/bloco/rblocoCor0.png";
    public String imgBlk2Path = "src/main/java/com/smart40/bloco/rblocoCor0.png";
    public String imgBlk3Path = "src/main/java/com/smart40/bloco/rblocoCor0.png";

    public String imgLeft1Path = "src/main/java/com/smart40/laminas/lamina1-2.png";
    public String imgLeft2Path = "src/main/java/com/smart40/laminas/lamina1-2.png";
    public String imgLeft3Path = "src/main/java/com/smart40/laminas/lamina1-2.png";

    public String imgFront1Path = "src/main/java/com/smart40/laminas/lamina2-2.png";
    public String imgFront2Path = "src/main/java/com/smart40/laminas/lamina2-2.png";
    public String imgFront3Path = "src/main/java/com/smart40/laminas/lamina2-2.png";

    public String imgRight1Path = "src/main/java/com/smart40/laminas/lamina3-2.png";
    public String imgRight2Path = "src/main/java/com/smart40/laminas/lamina3-2.png";
    public String imgRight3Path = "src/main/java/com/smart40/laminas/lamina3-2.png";

    public String padLeft1Path = "src/main/java/com/smart40/padrao/padrao1-0.png";
    public String padFront1Path = "src/main/java/com/smart40/padrao/padrao1-0.png";

    public String padLeft2Path = "src/main/java/com/smart40//padrao/padrao1-0.png";
    public String padFront2Path = "src/main/java/com/smart40/c/padrao/padrao1-0.png";

    public String padLeft3Path = "src/main/java/com/smart40/padrao/padrao1-0.png";
    public String padFront3Path = "src/main/java/com/smart40//padrao/padrao1-0.png";

    public JPanel confBlock1;
    public JPanel confBlock2;
    public JPanel confBlock3;

    public JPanel confBlocks;

    public JPanel confBlock1Parm;
    public JPanel confBlock2Parm;
    public JPanel confBlock3Parm;

    public ImagePanel tampa;

    public ImagePanel block1;
    public ImagePanel block2;
    public ImagePanel block3;

    public ImagePanel block1Ok;
    public ImagePanel block2Ok;
    public ImagePanel block3Ok;

    public ImagePanel laminaLeft1;
    public ImagePanel laminaLeft2;
    public ImagePanel laminaLeft3;

    public ImagePanel padraoLeft1;
    public ImagePanel padraoLeft2;
    public ImagePanel padraoLeft3;

    public ImagePanel laminaLeft1Ok;
    public ImagePanel laminaLeft2Ok;
    public ImagePanel laminaLeft3Ok;

    public ImagePanel padraoLeft1Ok;
    public ImagePanel padraoLeft2Ok;
    public ImagePanel padraoLeft3Ok;

    public ImagePanel laminaFront1;
    public ImagePanel laminaFront2;
    public ImagePanel laminaFront3;

    public ImagePanel padraoFront1;
    public ImagePanel padraoFront2;
    public ImagePanel padraoFront3;

    public ImagePanel laminaFront1Ok;
    public ImagePanel laminaFront2Ok;
    public ImagePanel laminaFront3Ok;

    public ImagePanel padraoFront1Ok;
    public ImagePanel padraoFront2Ok;
    public ImagePanel padraoFront3Ok;

    public ImagePanel laminaRight1;
    public ImagePanel laminaRight2;
    public ImagePanel laminaRight3;

    public ImagePanel laminaRight1Ok;
    public ImagePanel laminaRight2Ok;
    public ImagePanel laminaRight3Ok;

    public int[] blockBlack;

    public int[] blockRed;

    public int[] blockBlue;

    // Cria um JComboBox com as cores
    String[] tipoBlock = { " ", "SIMPLES", "DUPLO", "TRIPLO" };
    String[] coresBloco = { " ", "PRETO", "VERMELHO", "AZUL" };
    String[] coresLamina = { " ", "VERMELHO", "AZUL", "AMARELO", "VERDE", "PRETO", "BRANCO" };
    String[] padLamina = { " ", "CASA", "BARCO", "ESTRELA" };

    public JComboBox<String> cbxTipoBlock = new JComboBox<>(tipoBlock);

    public JComboBox<String> cbxCorBlock1 = new JComboBox<>(coresBloco);
    public JComboBox<String> cbxCorBlock2 = new JComboBox<>(coresBloco);
    public JComboBox<String> cbxCorBlock3 = new JComboBox<>(coresBloco);

    public JComboBox<String> cbxL1Blk1 = new JComboBox<>(coresLamina);
    public JComboBox<String> cbxL2Blk1 = new JComboBox<>(coresLamina);
    public JComboBox<String> cbxL3Blk1 = new JComboBox<>(coresLamina);

    public JComboBox<String> cbxL1Blk2 = new JComboBox<>(coresLamina);
    public JComboBox<String> cbxL2Blk2 = new JComboBox<>(coresLamina);
    public JComboBox<String> cbxL3Blk2 = new JComboBox<>(coresLamina);

    public JComboBox<String> cbxL1Blk3 = new JComboBox<>(coresLamina);
    public JComboBox<String> cbxL2Blk3 = new JComboBox<>(coresLamina);
    public JComboBox<String> cbxL3Blk3 = new JComboBox<>(coresLamina);

    public JComboBox<String> cbxP1Blk1 = new JComboBox<>(padLamina);
    public JComboBox<String> cbxP2Blk1 = new JComboBox<>(padLamina);
    public JComboBox<String> cbxP3Blk1 = new JComboBox<>(padLamina);

    public JComboBox<String> cbxP1Blk2 = new JComboBox<>(padLamina);
    public JComboBox<String> cbxP2Blk2 = new JComboBox<>(padLamina);
    public JComboBox<String> cbxP3Blk2 = new JComboBox<>(padLamina);

    public JComboBox<String> cbxP1Blk3 = new JComboBox<>(padLamina);
    public JComboBox<String> cbxP2Blk3 = new JComboBox<>(padLamina);
    public JComboBox<String> cbxP3Blk3 = new JComboBox<>(padLamina);

    public ConfigPedido(int w, int h) {
        // Configura o layout do painel
        setSize(w, h);
        setLayout(null);
        setVisible(true);

        // int x = 0;
        int y = 50;

        blockBlack = new int[29];
        blockRed = new int[29];
        blockBlue = new int[29];

        // blockBlackDisponivel();

        // setLayout(new FlowLayout());
        // this.setBackground(new Color(250, 200, 150));
        // setBackground(corBackPnl);
        setBackground(Color.black);

        pnlPedidoConf = new JPanel();
        pnlPedidoConf.setBounds(15, 10, 655, 660);
        pnlPedidoConf.setLayout(null);
        pnlPedidoConf.setBorder(greenOff);
        pnlPedidoConf.setOpaque(false);
        pnlPedidoConf.setBackground(corBackPnl);

        // ---- Configuração do tipo de monatgem -----------------------------

        JLabel lblTipoBlk = new JLabel("TIPO DE BLOCO");
        lblTipoBlk.setBounds(300, 20, 215, 30);
        lblTipoBlk.setBorder(gray);
        lblTipoBlk.setForeground(corTextLabel);
        lblTipoBlk.setHorizontalAlignment(SwingConstants.CENTER);
        lblTipoBlk.setFont(font14);
        pnlPedidoConf.add(lblTipoBlk);

        cbxTipoBlock.setBounds(520, 20, 120, 30);
        cbxTipoBlock.setBackground(corTextFieldEst);
        cbxTipoBlock.setBorder(border);
        cbxTipoBlock.setFont(font14Bold);
        cbxTipoBlock.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxTipoBlock.setVisible(true);
        pnlPedidoConf.add(cbxTipoBlock);

        // Centraliza o texto do JComboBox
        cbxTipoBlock.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });
        cbxTipoBlock.addActionListener((ActionEvent e) -> {
            confBlock1.setVisible(false);
            confBlock1Parm.setVisible(false);
            confBlock2.setVisible(false);
            confBlock2Parm.setVisible(false);
            confBlock3.setVisible(false);
            confBlock3Parm.setVisible(false);

            switch (cbxTipoBlock.getSelectedIndex()) {
                case 1 -> {
                    confBlock1.setVisible(true);
                    confBlock1Parm.setVisible(true);
                }
                case 2 -> {
                    confBlock1.setVisible(true);
                    confBlock1Parm.setVisible(true);
                    confBlock2.setVisible(true);
                    confBlock2Parm.setVisible(true);
                }
                case 3 -> {
                    confBlock1.setVisible(true);
                    confBlock1Parm.setVisible(true);
                    confBlock2.setVisible(true);
                    confBlock2Parm.setVisible(true);
                    confBlock3.setVisible(true);
                    confBlock3Parm.setVisible(true);
                }
                default -> {
                }
            }
            // updateCorBlock(1, corSelecionada);
        });
        // System.out.println(MainFrame.readOnly);
        registrarPedidoBtn = new JButton("REGISTRAR PEDIDO");
        registrarPedidoBtn.setBounds(10, 20, 260, 30);
        registrarPedidoBtn.setForeground(corTextLabel);
        registrarPedidoBtn.setBackground(corTextField);

        pnlPedidoConf.add(registrarPedidoBtn);

        registrarPedidoBtn.addActionListener(e -> {
            if (registrarPedidoBtn.getText().equals("REGISTRAR PEDIDO")) {

                blockBlackDisponivel();
                blockRedDisponivel();
                blockBlueDisponivel();
                gerarPedido();

                blocks();
                confBlocks.setVisible(true);
                confBlock1.setVisible(false);
                confBlock2.setVisible(false);
                confBlock3.setVisible(false);

                confBlock1Parm.setVisible(false);
                confBlock2Parm.setVisible(false);
                confBlock3Parm.setVisible(false);

                cbxTipoBlock.setEnabled(false);
                if (MainFrame.readOnly == false) {
                    Panel3.enviaPedidoButton.setEnabled(true);
                    Panel3.startPedidoButton.setEnabled(true);

                } else {
                    Panel3.enviaPedidoButton.setEnabled(false);
                    Panel3.startPedidoButton.setEnabled(false);
                }

            } else if (registrarPedidoBtn.getText().equals("EM PRODUÇÃO")) {
                resetBlocks();
                registrarPedidoBtn.setText("FINALIZADO");
                confBlocks.setVisible(false);

            } else if (registrarPedidoBtn.getText().equals("FINALIZADO")) {
                resetBlocks();
                registrarPedidoBtn.setText("REGISTRAR PEDIDO");
                confBlocks.setVisible(false);

            } else {
                registrarPedidoBtn.setText("REGISTRAR PEDIDO");
                confBlocks.setVisible(false);

                switch (cbxTipoBlock.getSelectedIndex()) {
                    case 1 -> {
                        confBlock1.setVisible(true);
                        confBlock1Parm.setVisible(true);
                    }
                    case 2 -> {
                        confBlock1.setVisible(true);
                        confBlock2.setVisible(true);
                        confBlock1Parm.setVisible(true);
                        confBlock2Parm.setVisible(true);
                    }
                    case 3 -> {
                        confBlock1.setVisible(true);
                        confBlock2.setVisible(true);
                        confBlock3.setVisible(true);
                        confBlock1Parm.setVisible(true);
                        confBlock2Parm.setVisible(true);
                        confBlock3Parm.setVisible(true);
                    }
                    default -> {
                    }
                }
                cbxTipoBlock.setEnabled(true);

                Panel3.enviaPedidoButton.setEnabled(false);
                Panel3.startPedidoButton.setEnabled(false);

            }
        });

        // Configuração dos itens do pedido - BLOCO 1
        // ----- Info Pedido ------------------------------------------------
        confBlock1 = new JPanel();
        confBlock1.setBounds(5, y + 0, 280, 220);
        confBlock1.setLayout(null);
        confBlock1.setOpaque(false);
        // confBlock1.setBorder(gray);

        confBlock1Parm = new JPanel();
        confBlock1Parm.setBounds(285, y + 0, 365, 220);
        confBlock1Parm.setLayout(null);
        confBlock1Parm.setOpaque(false);
        // confBlock1Parm.setBorder(gray);

        JLabel lblCorBlk1 = new JLabel("COR DO BLOCO 1");
        lblCorBlk1.setBounds(15, 25, 215, 30);
        lblCorBlk1.setBorder(gray);
        lblCorBlk1.setForeground(corTextLabel);
        lblCorBlk1.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk1.setFont(font14);
        confBlock1Parm.add(lblCorBlk1);

        cbxCorBlock1.setBounds(235, 25, 120, 30);
        cbxCorBlock1.setBackground(corTextFieldEst);
        cbxCorBlock1.setBorder(border);
        cbxCorBlock1.setFont(font14Bold);
        cbxCorBlock1.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxCorBlock1.setVisible(true);
        confBlock1Parm.add(cbxCorBlock1);

        // Centraliza o texto do JComboBox
        cbxCorBlock1.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });
        cbxCorBlock1.addActionListener((ActionEvent e) -> {
            updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        JLabel lblBladesB1 = new JLabel("LÂMINAS");
        lblBladesB1.setBounds(15, 65, 90, 30);
        lblBladesB1.setBorder(gray);
        lblBladesB1.setForeground(corTextLabel);
        lblBladesB1.setHorizontalAlignment(SwingConstants.CENTER);
        lblBladesB1.setFont(font14);
        confBlock1Parm.add(lblBladesB1);

        JLabel lblColorB1 = new JLabel("COR");
        lblColorB1.setBounds(110, 65, 120, 30);
        lblColorB1.setBorder(gray);
        lblColorB1.setForeground(corTextLabel);
        lblColorB1.setHorizontalAlignment(SwingConstants.CENTER);
        lblColorB1.setFont(font14);
        confBlock1Parm.add(lblColorB1);

        JLabel lblPadB1 = new JLabel("PADRÃO");
        lblPadB1.setBounds(235, 65, 120, 30);
        lblPadB1.setBorder(gray);
        lblPadB1.setForeground(corTextLabel);
        lblPadB1.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadB1.setFont(font14);
        confBlock1Parm.add(lblPadB1);

        // Centraliza o texto do JComboBox - LÃMINA 1

        JLabel lblBlade1B1 = new JLabel("ESQUERDA");
        lblBlade1B1.setBounds(15, 100, 90, 30);
        lblBlade1B1.setBorder(gray);
        lblBlade1B1.setForeground(corTextLabel);
        lblBlade1B1.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade1B1.setFont(font14);
        confBlock1Parm.add(lblBlade1B1);

        cbxL1Blk1.setBounds(110, 100, 120, 30);
        cbxL1Blk1.setBackground(corTextFieldEst);
        cbxL1Blk1.setBorder(border);
        cbxL1Blk1.setFont(font14Bold);
        cbxL1Blk1.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL1Blk1.setVisible(true);
        confBlock1Parm.add(cbxL1Blk1);

        cbxL1Blk1.addActionListener((ActionEvent e) -> {
            int corSelecionada = cbxL1Blk1.getSelectedIndex(); // Atualiza a variável

            updateCorLamina(1, 1, corSelecionada);
            updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - LÂMINA 1
        cbxL1Blk1.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP1Blk1.setBounds(235, 100, 120, 30);
        cbxP1Blk1.setBackground(corTextFieldEst);
        cbxP1Blk1.setBorder(border);
        cbxP1Blk1.setFont(font14Bold);
        cbxP1Blk1.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxP1Blk1.setVisible(true);
        confBlock1Parm.add(cbxP1Blk1);

        cbxP1Blk1.addActionListener((ActionEvent e) -> {
            updatePadLamina(1, 1, cbxP1Blk1.getSelectedIndex());
            updateCorLamina(1, 1, cbxL1Blk1.getSelectedIndex());
            updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - PADRÃO 1
        cbxP1Blk1.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        JLabel lblBlade2B1 = new JLabel("FRENTE");
        lblBlade2B1.setBounds(15, 135, 90, 30);
        lblBlade2B1.setBorder(gray);
        lblBlade2B1.setForeground(corTextLabel);
        lblBlade2B1.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade2B1.setFont(font14);
        confBlock1Parm.add(lblBlade2B1);

        cbxL2Blk1.setBounds(110, 135, 120, 30);
        cbxL2Blk1.setBackground(corTextFieldEst);
        cbxL2Blk1.setBorder(border);
        cbxL2Blk1.setFont(font14Bold);
        cbxL2Blk1.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL2Blk1.setVisible(true);
        confBlock1Parm.add(cbxL2Blk1);

        cbxL2Blk1.addActionListener((ActionEvent e) -> {
            updateCorLamina(1, 2, cbxL2Blk1.getSelectedIndex());
            updateCorLamina(1, 3, cbxL3Blk1.getSelectedIndex());
            updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - LÂMINA 2
        cbxL2Blk1.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP2Blk1.setBounds(235, 135, 120, 30);
        cbxP2Blk1.setBackground(corTextFieldEst);
        cbxP2Blk1.setBorder(border);
        cbxP2Blk1.setFont(font14Bold);
        cbxP2Blk1.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxP2Blk1.setVisible(true);
        confBlock1Parm.add(cbxP2Blk1);

        cbxP2Blk1.addActionListener((ActionEvent e) -> {
            // updatePadLamina(1, 1, cbxP1Blk1.getSelectedIndex());
            updatePadLamina(1, 2, cbxP2Blk1.getSelectedIndex());
            updateCorLamina(1, 2, cbxL2Blk1.getSelectedIndex());
            updateCorLamina(1, 3, cbxL3Blk1.getSelectedIndex());
            updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxP2Blk1.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        JLabel lblBlade3B1 = new JLabel("DIREITA");
        lblBlade3B1.setBounds(15, 170, 90, 30);
        lblBlade3B1.setBorder(gray);
        lblBlade3B1.setForeground(corTextLabel);
        lblBlade3B1.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade3B1.setFont(font14);
        confBlock1Parm.add(lblBlade3B1);

        cbxL3Blk1.setBounds(110, 170, 120, 30);
        cbxL3Blk1.setBackground(corTextFieldEst);
        cbxL3Blk1.setBorder(border);
        cbxL3Blk1.setFont(font14Bold);
        cbxL3Blk1.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL3Blk1.setVisible(true);
        confBlock1Parm.add(cbxL3Blk1);

        cbxL3Blk1.addActionListener((ActionEvent e) -> {
            updateCorLamina(1, 3, cbxL3Blk1.getSelectedIndex());
            updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxL3Blk1.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP3Blk1.setBounds(235, 170, 120, 30);
        cbxP3Blk1.setBackground(corTextFieldEst);
        cbxP3Blk1.setBorder(border);
        cbxP3Blk1.setFont(font14Bold);
        cbxP3Blk1.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxCorBlock1.setVisible(true);
        confBlock1Parm.add(cbxP3Blk1);

        cbxP3Blk1.addActionListener((ActionEvent e) -> {
            // updateCorLamina(1, 1, cbxP3Blk1.getSelectedIndex());
            // updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxP3Blk1.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        block1 = new ImagePanel(imgBlk1Path, 5, 5, 1);
        block1.setBounds(-20, 30, 400, 200);
        block1.setBackground(corBackPnl);
        block1.setOpaque(false);
        // smart.setBorder(gray);
        block1.setLayout(null);

        // --------- Configuração das Lâminas do Bloco 1
        // 1-------------------------------------------------

        laminaLeft1 = new ImagePanel("src/main/java/com/smart40/laminas/lamina1-0.png", 5, 5, 1);
        laminaLeft1.setBounds(-20, 30, 400, 200);
        laminaLeft1.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaLeft1.setOpaque(false);
        laminaLeft1.setLayout(null);

        laminaFront1 = new ImagePanel("src/main/java/com/smart40/laminas/lamina2-0.png", 5, 5, 1);
        laminaFront1.setBounds(-20, 30, 400, 200);
        laminaFront1.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaFront1.setOpaque(false);
        laminaFront1.setLayout(null);

        laminaRight1 = new ImagePanel("src/main/java/com/smart40/laminas/lamina3-0.png", 5, 5, 1);
        laminaRight1.setBounds(-20, 30, 400, 200);
        laminaRight1.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaRight1.setOpaque(false);
        laminaRight1.setLayout(null);

        // --------- Configuração dos Padrões do Bloco 1
        // 1-------------------------------------------------

        padraoLeft1 = new ImagePanel("src/main/java/com/smart40/padrao/padrao1-0.png", 5, 5, 0.5);
        padraoLeft1.setBounds(-20, 30, 400, 200);
        padraoLeft1.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoLeft1.setOpaque(false);
        padraoLeft1.setLayout(null);

        padraoFront1 = new ImagePanel("src/main/java/com/smart40/padrao/padrao1-0.png", 5, 5, 0.5);
        padraoFront1.setBounds(-20, 30, 400, 200);
        padraoFront1.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoFront1.setOpaque(false);
        padraoFront1.setLayout(null);

        confBlock1.add(padraoLeft1);
        confBlock1.add(padraoFront1);
        confBlock1.add(laminaFront1);
        confBlock1.add(laminaRight1);
        confBlock1.add(laminaLeft1);
        confBlock1.add(block1);

        pnlPedidoConf.add(confBlock1);
        pnlPedidoConf.add(confBlock1Parm);

        // --------- BLOCO 2 -------------------------------------

        // Configuração dos itens do pedido - BLOCO 2
        // ----- Info Pedido ------------------------------------------------
        confBlock2 = new JPanel();
        confBlock2.setBounds(5, y + 195, 280, 220);
        confBlock2.setLayout(null);
        confBlock2.setOpaque(false);
        // confBlock1.setBorder(gray);

        confBlock2Parm = new JPanel();
        confBlock2Parm.setBounds(285, y + 195, 365, 220);
        confBlock2Parm.setLayout(null);
        confBlock2Parm.setOpaque(false);
        // confBlock1Parm.setBorder(gray);

        JLabel lblCorBlk2 = new JLabel("COR DO BLOCO 2");
        lblCorBlk2.setBounds(15, 25, 215, 30);
        lblCorBlk2.setBorder(gray);
        lblCorBlk2.setForeground(corTextLabel);
        lblCorBlk2.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk2.setFont(font14);
        confBlock2Parm.add(lblCorBlk2);

        cbxCorBlock2.setBounds(235, 25, 120, 30);
        cbxCorBlock2.setBackground(corTextFieldEst);
        cbxCorBlock2.setBorder(border);
        cbxCorBlock2.setFont(font14Bold);
        cbxCorBlock2.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxCorBlock2.setVisible(true);
        confBlock2Parm.add(cbxCorBlock2);

        // Centraliza o texto do JComboBox
        cbxCorBlock2.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });
        cbxCorBlock2.addActionListener((ActionEvent e) -> {
            updateCorBlock(2, cbxCorBlock2.getSelectedIndex());
        });

        JLabel lblBladesB2 = new JLabel("LÂMINAS");
        lblBladesB2.setBounds(15, 65, 90, 30);
        lblBladesB2.setBorder(gray);
        lblBladesB2.setForeground(corTextLabel);
        lblBladesB2.setHorizontalAlignment(SwingConstants.CENTER);
        lblBladesB2.setFont(font14);
        confBlock2Parm.add(lblBladesB2);

        JLabel lblColorB2 = new JLabel("COR");
        lblColorB2.setBounds(110, 65, 120, 30);
        lblColorB2.setBorder(gray);
        lblColorB2.setForeground(corTextLabel);
        lblColorB2.setHorizontalAlignment(SwingConstants.CENTER);
        lblColorB2.setFont(font14);
        confBlock2Parm.add(lblColorB2);

        JLabel lblPadB2 = new JLabel("PADRÃO");
        lblPadB2.setBounds(235, 65, 120, 30);
        lblPadB2.setBorder(gray);
        lblPadB2.setForeground(corTextLabel);
        lblPadB2.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadB2.setFont(font14);
        confBlock2Parm.add(lblPadB2);

        // Centraliza o texto do JComboBox - LÃMINA 1

        JLabel lblBlade1B2 = new JLabel("ESQUERDA");
        lblBlade1B2.setBounds(15, 100, 90, 30);
        lblBlade1B2.setBorder(gray);
        lblBlade1B2.setForeground(corTextLabel);
        lblBlade1B2.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade1B2.setFont(font14);
        confBlock2Parm.add(lblBlade1B2);

        cbxL1Blk2.setBounds(110, 100, 120, 30);
        cbxL1Blk2.setBackground(corTextFieldEst);
        cbxL1Blk2.setBorder(border);
        cbxL1Blk2.setFont(font14Bold);
        cbxL1Blk2.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL1Blk2.setVisible(true);
        confBlock2Parm.add(cbxL1Blk2);

        cbxL1Blk2.addActionListener((ActionEvent e) -> {
            updateCorLamina(2, 1, cbxL1Blk2.getSelectedIndex());
            updateCorBlock(2, cbxCorBlock2.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - LÂMINA 1
        cbxL1Blk2.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP1Blk2.setBounds(235, 100, 120, 30);
        cbxP1Blk2.setBackground(corTextFieldEst);
        cbxP1Blk2.setBorder(border);
        cbxP1Blk2.setFont(font14Bold);
        cbxP1Blk2.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxP1Blk2.setVisible(true);
        confBlock2Parm.add(cbxP1Blk2);

        cbxP1Blk2.addActionListener((ActionEvent e) -> {
            updatePadLamina(2, 1, cbxP1Blk2.getSelectedIndex());
            updateCorLamina(2, 1, cbxL1Blk2.getSelectedIndex());
            updateCorBlock(2, cbxCorBlock2.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - PADRÃO 1
        cbxP1Blk2.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        JLabel lblBlade2B2 = new JLabel("FRENTE");
        lblBlade2B2.setBounds(15, 135, 90, 30);
        lblBlade2B2.setBorder(gray);
        lblBlade2B2.setForeground(corTextLabel);
        lblBlade2B2.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade2B2.setFont(font14);
        confBlock2Parm.add(lblBlade2B2);

        cbxL2Blk2.setBounds(110, 135, 120, 30);
        cbxL2Blk2.setBackground(corTextFieldEst);
        cbxL2Blk2.setBorder(border);
        cbxL2Blk2.setFont(font14Bold);
        cbxL2Blk2.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL2Blk2.setVisible(true);
        confBlock2Parm.add(cbxL2Blk2);

        cbxL2Blk2.addActionListener((ActionEvent e) -> {
            updateCorLamina(2, 2, cbxL2Blk2.getSelectedIndex());
            updateCorLamina(2, 3, cbxL3Blk2.getSelectedIndex());
            updateCorBlock(2, cbxCorBlock2.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - LÂMINA 2
        cbxL2Blk2.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP2Blk2.setBounds(235, 135, 120, 30);
        cbxP2Blk2.setBackground(corTextFieldEst);
        cbxP2Blk2.setBorder(border);
        cbxP2Blk2.setFont(font14Bold);
        cbxP2Blk2.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxCorBlock2.setVisible(true);
        confBlock2Parm.add(cbxP2Blk2);

        cbxP2Blk2.addActionListener((ActionEvent e) -> {
            updatePadLamina(2, 2, cbxP2Blk2.getSelectedIndex());
            updateCorLamina(2, 2, cbxL2Blk2.getSelectedIndex());
            updateCorLamina(2, 3, cbxL3Blk2.getSelectedIndex());
            updateCorBlock(2, cbxCorBlock2.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxP2Blk2.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        JLabel lblBlade3B2 = new JLabel("DIREITA");
        lblBlade3B2.setBounds(15, 170, 90, 30);
        lblBlade3B2.setBorder(gray);
        lblBlade3B2.setForeground(corTextLabel);
        lblBlade3B2.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade3B2.setFont(font14);
        confBlock2Parm.add(lblBlade3B2);

        cbxL3Blk2.setBounds(110, 170, 120, 30);
        cbxL3Blk2.setBackground(corTextFieldEst);
        cbxL3Blk2.setBorder(border);
        cbxL3Blk2.setFont(font14Bold);
        cbxL3Blk2.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL3Blk2.setVisible(true);
        confBlock2Parm.add(cbxL3Blk2);

        cbxL3Blk2.addActionListener((ActionEvent e) -> {
            updateCorLamina(2, 3, cbxL3Blk2.getSelectedIndex());
            updateCorBlock(2, cbxCorBlock2.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxL3Blk2.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP3Blk2.setBounds(235, 170, 120, 30);
        cbxP3Blk2.setBackground(corTextFieldEst);
        cbxP3Blk2.setBorder(border);
        cbxP3Blk2.setFont(font14Bold);
        cbxP3Blk2.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxP3Blk2.setVisible(true);
        confBlock2Parm.add(cbxP3Blk2);

        cbxP3Blk2.addActionListener((ActionEvent e) -> {
            // updateCorLamina(1, 1, cbxP3Blk1.getSelectedIndex());
            // updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxP3Blk2.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        block2 = new ImagePanel(imgBlk1Path, 5, 5, 1);
        block2.setBounds(-20, 30, 400, 200);
        block2.setBackground(corBackPnl);
        block2.setOpaque(false);
        // smart.setBorder(gray);
        block2.setLayout(null);

        // --------- Configuração das Lâminas do Bloco 2
        // 1-------------------------------------------------

        laminaLeft2 = new ImagePanel("src/main/java/com/smart40/laminas/lamina1-0.png", 5, 5, 1);
        laminaLeft2.setBounds(-20, 30, 400, 200);
        laminaLeft2.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaLeft2.setOpaque(false);
        laminaLeft2.setLayout(null);

        laminaFront2 = new ImagePanel("src/main/java/com/smart40/laminas/lamina2-0.png", 5, 5, 1);
        laminaFront2.setBounds(-20, 30, 400, 200);
        laminaFront2.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaFront2.setOpaque(false);
        laminaFront2.setLayout(null);

        laminaRight2 = new ImagePanel("src/main/java/com/smart40/laminas/lamina3-0.png", 5, 5, 1);
        laminaRight2.setBounds(-20, 30, 400, 200);
        laminaRight2.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaRight2.setOpaque(false);
        laminaRight2.setLayout(null);

        // --------- Configuração dos Padrões do Bloco 2
        // 1-------------------------------------------------

        padraoLeft2 = new ImagePanel("src/main/java/com/smart40/padrao/padrao1-0.png", 5, 5, 0.5);
        padraoLeft2.setBounds(-20, 30, 400, 200);
        padraoLeft2.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoLeft2.setOpaque(false);
        padraoLeft2.setLayout(null);

        padraoFront2 = new ImagePanel("src/main/java/com/smart40/padrao/padrao1-0.png", 5, 5, 0.5);
        padraoFront2.setBounds(-20, 30, 400, 200);
        padraoFront2.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoFront2.setOpaque(false);
        padraoFront2.setLayout(null);

        confBlock2.add(padraoLeft2);
        confBlock2.add(padraoFront2);

        confBlock2.add(laminaFront2);
        confBlock2.add(laminaRight2);
        confBlock2.add(laminaLeft2);
        confBlock2.add(block2);

        pnlPedidoConf.add(confBlock2);
        pnlPedidoConf.add(confBlock2Parm);

        // --------- BLOCO 3 -------------------------------------

        // Configuração dos itens do pedido - BLOCO 3
        // ----- Info Pedido ------------------------------------------------
        confBlock3 = new JPanel();
        confBlock3.setBounds(5, y + 395, 280, 220);
        confBlock3.setLayout(null);
        confBlock3.setOpaque(false);
        // confBlock1.setBorder(gray);

        confBlock3Parm = new JPanel();
        confBlock3Parm.setBounds(285, y + 395, 365, 220);
        confBlock3Parm.setLayout(null);
        confBlock3Parm.setOpaque(false);
        // confBlock1Parm.setBorder(gray);

        JLabel lblCorBlk3 = new JLabel("COR DO BLOCO 3");
        lblCorBlk3.setBounds(15, 25, 215, 30);
        lblCorBlk3.setBorder(gray);
        lblCorBlk3.setForeground(corTextLabel);
        lblCorBlk3.setHorizontalAlignment(SwingConstants.CENTER);
        lblCorBlk3.setFont(font14);
        confBlock3Parm.add(lblCorBlk3);

        cbxCorBlock3.setBounds(235, 25, 120, 30);
        cbxCorBlock3.setBackground(corTextFieldEst);
        cbxCorBlock3.setBorder(border);
        cbxCorBlock3.setFont(font14Bold);
        cbxCorBlock3.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxCorBlock3.setVisible(true);
        confBlock3Parm.add(cbxCorBlock3);

        cbxCorBlock3.addActionListener((ActionEvent e) -> {
            updateCorBlock(3, cbxCorBlock3.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxCorBlock3.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        JLabel lblBladesB3 = new JLabel("LÂMINAS");
        lblBladesB3.setBounds(15, 65, 90, 30);
        lblBladesB3.setBorder(gray);
        lblBladesB3.setForeground(corTextLabel);
        lblBladesB3.setHorizontalAlignment(SwingConstants.CENTER);
        lblBladesB3.setFont(font14);
        confBlock3Parm.add(lblBladesB3);

        JLabel lblColorB3 = new JLabel("COR");
        lblColorB3.setBounds(110, 65, 120, 30);
        lblColorB3.setBorder(gray);
        lblColorB3.setForeground(corTextLabel);
        lblColorB3.setHorizontalAlignment(SwingConstants.CENTER);
        lblColorB3.setFont(font14);
        confBlock3Parm.add(lblColorB3);

        JLabel lblPadB3 = new JLabel("PADRÃO");
        lblPadB3.setBounds(235, 65, 120, 30);
        lblPadB3.setBorder(gray);
        lblPadB3.setForeground(corTextLabel);
        lblPadB3.setHorizontalAlignment(SwingConstants.CENTER);
        lblPadB3.setFont(font14);
        confBlock3Parm.add(lblPadB3);

        // Centraliza o texto do JComboBox - LÃMINA 1

        JLabel lblBlade1B3 = new JLabel("ESQUERDA");
        lblBlade1B3.setBounds(15, 100, 90, 30);
        lblBlade1B3.setBorder(gray);
        lblBlade1B3.setForeground(corTextLabel);
        lblBlade1B3.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade1B3.setFont(font14);
        confBlock3Parm.add(lblBlade1B3);

        cbxL1Blk3.setBounds(110, 100, 120, 30);
        cbxL1Blk3.setBackground(corTextFieldEst);
        cbxL1Blk3.setBorder(border);
        cbxL1Blk3.setFont(font14Bold);
        cbxL1Blk3.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL1Blk3.setVisible(true);
        confBlock3Parm.add(cbxL1Blk3);

        cbxL1Blk3.addActionListener((ActionEvent e) -> {
            updateCorLamina(3, 1, cbxL1Blk3.getSelectedIndex());
            updateCorBlock(3, cbxCorBlock3.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - LÂMINA 1
        cbxL1Blk3.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP1Blk3.setBounds(235, 100, 120, 30);
        cbxP1Blk3.setBackground(corTextFieldEst);
        cbxP1Blk3.setBorder(border);
        cbxP1Blk3.setFont(font14Bold);
        cbxP1Blk3.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxP1Blk3.setVisible(true);
        confBlock3Parm.add(cbxP1Blk3);

        cbxP1Blk3.addActionListener((ActionEvent e) -> {
            updatePadLamina(3, 1, cbxP1Blk3.getSelectedIndex());
            updateCorLamina(3, 1, cbxL1Blk3.getSelectedIndex());
            updateCorBlock(3, cbxCorBlock3.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - PADRÃO 1
        cbxP1Blk3.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        JLabel lblBlade2B3 = new JLabel("FRENTE");
        lblBlade2B3.setBounds(15, 135, 90, 30);
        lblBlade2B3.setBorder(gray);
        lblBlade2B3.setForeground(corTextLabel);
        lblBlade2B3.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade2B3.setFont(font14);
        confBlock3Parm.add(lblBlade2B3);

        cbxL2Blk3.setBounds(110, 135, 120, 30);
        cbxL2Blk3.setBackground(corTextFieldEst);
        cbxL2Blk3.setBorder(border);
        cbxL2Blk3.setFont(font14Bold);
        cbxL2Blk3.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL2Blk3.setVisible(true);
        confBlock3Parm.add(cbxL2Blk3);

        cbxL2Blk3.addActionListener((ActionEvent e) -> {
            updateCorLamina(3, 2, cbxL2Blk3.getSelectedIndex());
            updateCorLamina(3, 3, cbxL3Blk3.getSelectedIndex());
            updateCorBlock(3, cbxCorBlock3.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox - LÂMINA 2
        cbxL2Blk3.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP2Blk3.setBounds(235, 135, 120, 30);
        cbxP2Blk3.setBackground(corTextFieldEst);
        cbxP2Blk3.setBorder(border);
        cbxP2Blk3.setFont(font14Bold);
        cbxP2Blk3.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxCorBlock3.setVisible(true);
        confBlock3Parm.add(cbxP2Blk3);

        cbxP2Blk3.addActionListener((ActionEvent e) -> {
            updatePadLamina(3, 2, cbxP2Blk3.getSelectedIndex());
            updateCorLamina(3, 2, cbxL2Blk3.getSelectedIndex());
            updateCorLamina(3, 3, cbxL3Blk3.getSelectedIndex());
            updateCorBlock(3, cbxCorBlock3.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxP2Blk3.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        JLabel lblBlade3B3 = new JLabel("DIREITA");
        lblBlade3B3.setBounds(15, 170, 90, 30);
        lblBlade3B3.setBorder(gray);
        lblBlade3B3.setForeground(corTextLabel);
        lblBlade3B3.setHorizontalAlignment(SwingConstants.CENTER);
        lblBlade3B3.setFont(font14);
        confBlock3Parm.add(lblBlade3B3);

        cbxL3Blk3.setBounds(110, 170, 120, 30);
        cbxL3Blk3.setBackground(corTextFieldEst);
        cbxL3Blk3.setBorder(border);
        cbxL3Blk3.setFont(font14Bold);
        cbxL3Blk3.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxL3Blk3.setVisible(true);
        confBlock3Parm.add(cbxL3Blk3);

        cbxL3Blk3.addActionListener((ActionEvent e) -> {
            updateCorLamina(3, 3, cbxL3Blk3.getSelectedIndex());
            updateCorBlock(3, cbxCorBlock3.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxL3Blk3.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        cbxP3Blk3.setBounds(235, 170, 120, 30);
        cbxP3Blk3.setBackground(corTextFieldEst);
        cbxP3Blk3.setBorder(border);
        cbxP3Blk3.setFont(font14Bold);
        cbxP3Blk3.setForeground(corJTFfont);
        // cbxCorBlock1.setHorizontalAlignment(SwingConstants.CENTER);
        cbxP3Blk3.setVisible(true);
        confBlock3Parm.add(cbxP3Blk3);

        cbxP3Blk3.addActionListener((ActionEvent e) -> {
            // updateCorLamina(1, 1, cbxP3Blk1.getSelectedIndex());
            // updateCorBlock(1, cbxCorBlock1.getSelectedIndex());
        });

        // Centraliza o texto do JComboBox
        cbxP3Blk3.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHeight) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHeight);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });

        block3 = new ImagePanel(imgBlk3Path, 5, 5, 1);
        block3.setBounds(-20, 30, 400, 200);
        block3.setBackground(corBackPnl);
        block3.setOpaque(false);
        // smart.setBorder(gray);
        block3.setLayout(null);

        // --------- Configuração das Lâminas do Bloco 3
        // 1-------------------------------------------------

        laminaLeft3 = new ImagePanel("src/main/java/com/smart40/laminas/lamina1-0.png", 5, 5, 1);
        laminaLeft3.setBounds(-20, 30, 400, 200);
        laminaLeft3.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaLeft3.setOpaque(false);
        laminaLeft3.setLayout(null);

        laminaFront3 = new ImagePanel("src/main/java/com/smart40/laminas/lamina2-0.png", 5, 5, 1);
        laminaFront3.setBounds(-20, 30, 400, 200);
        laminaFront3.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaFront3.setOpaque(false);
        laminaFront3.setLayout(null);

        laminaRight3 = new ImagePanel("src/main/java/com/smart40/laminas/lamina3-0.png", 5, 5, 1);
        laminaRight3.setBounds(-20, 30, 400, 200);
        laminaRight3.setBackground(corBackPnl);
        // smart.setBorder(gray);
        laminaRight3.setOpaque(false);
        laminaRight3.setLayout(null);

        // --------- Configuração dos Padrões do Bloco 3
        // 1-------------------------------------------------

        padraoLeft3 = new ImagePanel("src/main/java/com/smart40/padrao/padrao1-0.png", 5, 5, 0.5);
        padraoLeft3.setBounds(-20, 30, 400, 200);
        padraoLeft3.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoLeft3.setOpaque(false);
        padraoLeft3.setLayout(null);

        padraoFront3 = new ImagePanel("src/main/java/com/smart40/padrao/padrao1-0.png", 5, 5, 0.5);
        padraoFront3.setBounds(-20, 30, 400, 200);
        padraoFront3.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoFront3.setOpaque(false);
        padraoFront3.setLayout(null);

        confBlock3.add(padraoLeft3);
        confBlock3.add(padraoFront3);

        confBlock3.add(laminaFront3);
        confBlock3.add(laminaRight3);
        confBlock3.add(laminaLeft3);
        confBlock3.add(block3);

        pnlPedidoConf.add(confBlock3);
        pnlPedidoConf.add(confBlock3Parm);

        confBlocks = new JPanel();
        confBlocks.setBounds(10, y + 0, 635, 630);
        confBlocks.setLayout(null);
        confBlocks.setOpaque(false);
        confBlocks.setBackground(corBackPnl);
        confBlocks.setVisible(true);
        pnlPedidoConf.add(confBlocks);

        add(pnlPedidoConf);

    }

    public void resetBlocks() {

        cbxTipoBlock.setSelectedIndex(3);

        cbxCorBlock1.setSelectedIndex(0);
        cbxCorBlock2.setSelectedIndex(0);
        cbxCorBlock3.setSelectedIndex(0);

        cbxL1Blk1.setSelectedIndex(0);
        cbxL2Blk1.setSelectedIndex(0);
        cbxL3Blk1.setSelectedIndex(0);

        cbxP1Blk1.setSelectedIndex(0);
        cbxP2Blk1.setSelectedIndex(0);
        cbxP3Blk1.setSelectedIndex(0);

        cbxL1Blk2.setSelectedIndex(0);
        cbxL2Blk2.setSelectedIndex(0);
        cbxL3Blk2.setSelectedIndex(0);

        cbxP1Blk2.setSelectedIndex(0);
        cbxP2Blk2.setSelectedIndex(0);
        cbxP3Blk2.setSelectedIndex(0);

        cbxL1Blk3.setSelectedIndex(0);
        cbxL2Blk3.setSelectedIndex(0);
        cbxL3Blk3.setSelectedIndex(0);

        cbxP1Blk3.setSelectedIndex(0);
        cbxP2Blk3.setSelectedIndex(0);
        cbxP3Blk3.setSelectedIndex(0);

        updateCorBlock(1, 0);

        updateCorLamina(1, 1, 0);
        updateCorLamina(1, 2, 0);
        updateCorLamina(1, 3, 0);

        updatePadLamina(1, 1, 0);
        updatePadLamina(1, 2, 0);
        updatePadLamina(1, 3, 0);

        //confBlock1.remove(confBlock1Parm);
        //confBlock1.add(confBlock1Parm);
        //confBlock1Parm.revalidate();
        //confBlock1Parm.repaint();

        updateCorBlock(2, 0);

        updateCorLamina(2, 1, 0);
        updateCorLamina(2, 2, 0);
        updateCorLamina(2, 3, 0);

        updatePadLamina(2, 1, 0);
        updatePadLamina(2, 2, 0);
        updatePadLamina(2, 3, 0);

        //confBlock2.remove(confBlock2Parm);
        //confBlock2.add(confBlock2Parm);
        //confBlock2Parm.revalidate();
        //confBlock2Parm.repaint();

        updateCorBlock(3, 0);

        updateCorLamina(3, 1, 0);
        updateCorLamina(3, 2, 0);
        updateCorLamina(3, 3, 0);

        updatePadLamina(3, 1, 0);
        updatePadLamina(3, 2, 0);
        updatePadLamina(3, 3, 0);

        //confBlock3.remove(confBlock3Parm);
        //confBlock3.add(confBlock3Parm);
        //confBlock3Parm.revalidate();
        //confBlock3Parm.repaint();

    }

    public void updateCorBlock(int block, int color) {

        switch (block) {
            case 1 -> {
                confBlock1.remove(block1);
                imgBlk1Path = switch (color) {
                    case 1 -> "src/main/java/com/smart40/bloco/rblocoCor1.png";
                    case 2 -> "src/main/java/com/smart40/bloco/rblocoCor2.png";
                    case 3 -> "src/main/java/com/smart40/bloco/rblocoCor3.png";
                    default -> "src/main/java/com/smart40/bloco/rblocoCor0.png";
                };
                block1 = new ImagePanel(imgBlk1Path, 5, 5, 1);
                block1.setBounds(-20, 30, 400, 200);
                block1.setBackground(corBackPnl);
                block1.setOpaque(false);
                // smart.setBorder(gray);
                block1.setLayout(null);
                confBlock1.add(block1);
                confBlock1.revalidate();
                confBlock1.repaint();
            }

            case 2 -> {
                confBlock2.remove(block2);
                imgBlk2Path = switch (color) {
                    case 1 -> "src/main/java/com/smart40/bloco/rblocoCor1.png";
                    case 2 -> "src/main/java/com/smart40/bloco/rblocoCor2.png";
                    case 3 -> "src/main/java/com/smart40/bloco/rblocoCor3.png";
                    default -> "src/main/java/com/smart40/bloco/rblocoCor0.png";
                };
                block2 = new ImagePanel(imgBlk2Path, 5, 5, 1);
                block2.setBounds(-20, 30, 400, 200);
                block2.setBackground(corBackPnl);
                block2.setOpaque(false);
                // smart.setBorder(gray);
                block2.setLayout(null);
                confBlock2.add(block2);
                confBlock2.revalidate();
                confBlock2.repaint();
            }
            case 3 -> {
                confBlock3.remove(block3);
                imgBlk3Path = switch (color) {
                    case 1 -> "src/main/java/com/smart40/bloco/rblocoCor1.png";
                    case 2 -> "src/main/java/com/smart40/bloco/rblocoCor2.png";
                    case 3 -> "src/main/java/com/smart40/bloco/rblocoCor3.png";
                    default -> "src/main/java/com/smart40/bloco/rblocoCor0.png";
                };
                block3 = new ImagePanel(imgBlk3Path, 5, 5, 1);
                block3.setBounds(-20, 30, 400, 200);
                block3.setBackground(corBackPnl);
                block3.setOpaque(false);
                // smart.setBorder(gray);
                block3.setLayout(null);
                confBlock3.add(block3);
                confBlock3.revalidate();
                confBlock3.repaint();
            }
            default -> {
            }
        }

    }

    public void updateCorLamina(int block, int blade, int color) {

        switch (block) {
            case 1 -> {
                switch (blade) {
                    case 1 -> {
                        confBlock1.remove(laminaLeft1);
                        imgLeft1Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina1-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina1-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina1-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina1-4.png";
                            case 5 -> "src/main/java/com/smart40/laminas/lamina1-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina1-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina1-0.png";
                        };
                        // System.out.println(imgLeft1Path);
                        laminaLeft1 = new ImagePanel(imgLeft1Path, 5, 5, 1);
                        laminaLeft1.setBounds(-20, 30, 400, 200);
                        laminaLeft1.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaLeft1.setOpaque(false);
                        laminaLeft1.setLayout(null);
                        confBlock1.add(laminaLeft1);
                    }
                    case 2 -> {
                        confBlock1.remove(laminaFront1);
                        imgFront1Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina2-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina2-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina2-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina2-4.png";
                            case 5 -> "src/main/java/com/smart40/laminas/lamina2-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina2-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina2-0.png";
                        };
                        laminaFront1 = new ImagePanel(imgFront1Path, 5, 5, 1);
                        laminaFront1.setBounds(-20, 30, 400, 200);
                        laminaFront1.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaFront1.setOpaque(false);
                        laminaFront1.setLayout(null);
                        confBlock1.add(laminaFront1);
                    }
                    case 3 -> {
                        confBlock1.remove(laminaRight1);
                        imgRight1Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina3-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina3-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina3-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina3-4.png";
                            case 5 -> "src/main/java/com/smart40/laminas/lamina3-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina3-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina3-0.png";
                        };
                        laminaRight1 = new ImagePanel(imgRight1Path, 5, 5, 1);
                        laminaRight1.setBounds(-20, 30, 400, 200);
                        laminaRight1.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaRight1.setOpaque(false);
                        laminaRight1.setLayout(null);
                        confBlock1.add(laminaRight1);
                    }
                    default -> {
                    }
                }
                confBlock1.revalidate();
                confBlock1.repaint();
            }
            case 2 -> {
                switch (blade) {
                    case 1 -> {
                        confBlock2.remove(laminaLeft2);
                        imgLeft2Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina1-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina1-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina1-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina1-4.png";
                            case 5 -> "src/main/java/com/smart40/laminas/lamina1-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina1-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina1-0.png";
                        };
                        // System.out.println(imgLeft1Path);
                        laminaLeft2 = new ImagePanel(imgLeft2Path, 5, 5, 1);
                        laminaLeft2.setBounds(-20, 30, 400, 200);
                        laminaLeft2.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaLeft2.setOpaque(false);
                        laminaLeft2.setLayout(null);
                        confBlock2.add(laminaLeft2);
                    }
                    case 2 -> {
                        confBlock2.remove(laminaFront2);
                        imgFront2Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina2-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina2-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina2-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina2-4.png";
                            case 5 -> "src/main/java/com/smart40/laminas/lamina2-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina2-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina2-0.png";
                        };
                        laminaFront2 = new ImagePanel(imgFront2Path, 5, 5, 1);
                        laminaFront2.setBounds(-20, 30, 400, 200);
                        laminaFront2.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaFront2.setOpaque(false);
                        laminaFront2.setLayout(null);
                        confBlock2.add(laminaFront2);
                    }
                    case 3 -> {
                        confBlock2.remove(laminaRight2);
                        imgRight2Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina3-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina3-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina3-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina3-4.png";
                            case 5 -> "src/main/java/com/smart40/c/laminas/lamina3-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina3-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina3-0.png";
                        };
                        laminaRight2 = new ImagePanel(imgRight2Path, 5, 5, 1);
                        laminaRight2.setBounds(-20, 30, 400, 200);
                        laminaRight2.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaRight2.setOpaque(false);
                        laminaRight2.setLayout(null);
                        confBlock2.add(laminaRight2);
                    }
                    default -> {
                    }
                }
            }
            case 3 -> {
                switch (blade) {
                    case 1 -> {
                        confBlock3.remove(laminaLeft3);
                        imgLeft3Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina1-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina1-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina1-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina1-4.png";
                            case 5 -> "src/main/java/com/smart40/laminas/lamina1-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina1-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina1-0.png";
                        };
                        // System.out.println(imgLeft1Path);
                        laminaLeft3 = new ImagePanel(imgLeft3Path, 5, 5, 1);
                        laminaLeft3.setBounds(-20, 30, 400, 200);
                        laminaLeft3.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaLeft3.setOpaque(false);
                        laminaLeft3.setLayout(null);
                        confBlock3.add(laminaLeft3);
                    }
                    case 2 -> {
                        confBlock3.remove(laminaFront3);
                        imgFront3Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina2-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina2-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina2-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina2-4.png";
                            case 5 -> "src/main/java/com/smart40/laminas/lamina2-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina2-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina2-0.png";
                        };
                        laminaFront3 = new ImagePanel(imgFront3Path, 5, 5, 1);
                        laminaFront3.setBounds(-20, 30, 400, 200);
                        laminaFront3.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaFront3.setOpaque(false);
                        laminaFront3.setLayout(null);
                        confBlock3.add(laminaFront3);
                    }
                    case 3 -> {
                        confBlock3.remove(laminaRight3);
                        imgRight3Path = switch (color) {
                            case 1 -> "src/main/java/com/smart40/laminas/lamina3-1.png";
                            case 2 -> "src/main/java/com/smart40/laminas/lamina3-2.png";
                            case 3 -> "src/main/java/com/smart40/laminas/lamina3-3.png";
                            case 4 -> "src/main/java/com/smart40/laminas/lamina3-4.png";
                            case 5 -> "src/main/java/com/smart40/laminas/lamina3-5.png";
                            case 6 -> "src/main/java/com/smart40/laminas/lamina3-6.png";
                            default -> "src/main/java/com/smart40/laminas/lamina3-0.png";
                        };
                        laminaRight3 = new ImagePanel(imgRight3Path, 5, 5, 1);
                        laminaRight3.setBounds(-20, 30, 400, 200);
                        laminaRight3.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        laminaRight3.setOpaque(false);
                        laminaRight3.setLayout(null);
                        confBlock3.add(laminaRight3);
                    }
                    default -> {
                    }
                }
            }
            default -> {
            }
        }

    }

    public void updatePadLamina(int block, int blade, int pad) {

        switch (block) {
            case 1 -> {
                switch (blade) {
                    case 1 -> {
                        confBlock1.remove(padraoLeft1);
                        padLeft1Path = switch (pad) {
                            case 1 -> "src/main/java/com/smart40/padrao/padrao1-1.png";
                            case 2 -> "src/main/java/com/smart40/padrao/padrao2-1.png";
                            case 3 -> "src/main/java/com/smart40/padrao/padrao3-1.png";
                            default -> "src/main/java/com/smart40/padrao/padrao1-0.png";
                        };
                        // System.out.println(imgLeft1Path);
                        padraoLeft1 = new ImagePanel(padLeft1Path, 5, 5, 0.5);
                        padraoLeft1.setBounds(-20, 30, 400, 200);
                        padraoLeft1.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        padraoLeft1.setOpaque(false);
                        padraoLeft1.setLayout(null);
                        confBlock1.add(padraoLeft1);
                    }
                    case 2 -> {
                        confBlock1.remove(padraoFront1);
                        padFront1Path = switch (pad) {
                            case 1 -> "src/main/java/com/smart40/padrao/padrao1-2.png";
                            case 2 -> "src/main/java/com/smart40/padrao/padrao2-2.png";
                            case 3 -> "src/main/java/com/smart40/padrao/padrao3-2.png";
                            default -> "src/main/java/com/smart40/padrao/padrao1-0.png";
                        };
                        padraoFront1 = new ImagePanel(padFront1Path, 5, 5, 0.5);
                        padraoFront1.setBounds(-20, 30, 400, 200);
                        padraoFront1.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        padraoFront1.setOpaque(false);
                        padraoFront1.setLayout(null);
                        confBlock1.add(padraoFront1);
                    }
                    case 3 -> {
                    }
                    default -> {
                    }
                }
                confBlock1.revalidate();
                confBlock1.repaint();
            }
            case 2 -> {
                switch (blade) {
                    case 1 -> {
                        confBlock2.remove(padraoLeft2);
                        padLeft2Path = switch (pad) {
                            case 1 -> "src/main/java/com/smart40/padrao/padrao1-1.png";
                            case 2 -> "src/main/java/com/smart40/padrao/padrao2-1.png";
                            case 3 -> "src/main/java/com/smart40/padrao/padrao3-1.png";
                            default -> "src/main/java/com/smart40/padrao/padrao1-0.png";
                        };
                        // System.out.println(imgLeft1Path);
                        padraoLeft2 = new ImagePanel(padLeft2Path, 5, 5, 0.5);
                        padraoLeft2.setBounds(-20, 30, 400, 200);
                        padraoLeft2.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        padraoLeft2.setOpaque(false);
                        padraoLeft2.setLayout(null);
                        confBlock2.add(padraoLeft2);
                    }
                    case 2 -> {
                        confBlock2.remove(padraoFront2);
                        padFront2Path = switch (pad) {
                            case 1 -> "src/main/java/com/smart40/padrao/padrao1-2.png";
                            case 2 -> "src/main/java/com/smart40/padrao/padrao2-2.png";
                            case 3 -> "src/main/java/com/smart40/padrao/padrao3-2.png";
                            default -> "src/main/java/com/smart40/padrao/padrao1-0.png";
                        };
                        padraoFront2 = new ImagePanel(padFront2Path, 5, 5, 0.5);
                        padraoFront2.setBounds(-20, 30, 400, 200);
                        padraoFront2.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        padraoFront2.setOpaque(false);
                        padraoFront2.setLayout(null);
                        confBlock2.add(padraoFront2);
                    }
                    case 3 -> {
                    }
                    default -> {
                    }
                }
            }
            case 3 -> {
                switch (blade) {
                    case 1 -> {
                        confBlock3.remove(padraoLeft3);
                        padLeft3Path = switch (pad) {
                            case 1 -> "src/main/java/com/smart40/padrao/padrao1-1.png";
                            case 2 -> "src/main/java/com/smart40/padrao/padrao2-1.png";
                            case 3 -> "src/main/java/com/smart40/padrao/padrao3-1.png";
                            default -> "src/main/java/com/smart40/padrao/padrao1-0.png";
                        };
                        // System.out.println(imgLeft1Path);
                        padraoLeft3 = new ImagePanel(padLeft3Path, 5, 5, 0.5);
                        padraoLeft3.setBounds(-20, 30, 400, 200);
                        padraoLeft3.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        padraoLeft3.setOpaque(false);
                        padraoLeft3.setLayout(null);
                        confBlock3.add(padraoLeft3);
                    }

                    case 2 -> {
                        confBlock3.remove(padraoFront3);
                        padFront3Path = switch (pad) {
                            case 1 -> "src/main/java/com/smart40/padrao/padrao1-2.png";
                            case 2 -> "src/main/java/com/smart40/padrao/padrao2-2.png";
                            case 3 -> "src/main/java/com/smart40/padrao/padrao3-2.png";
                            default -> "src/main/java/com/smart40/padrao/padrao1-0.png";
                        };
                        padraoFront3 = new ImagePanel(padFront3Path, 5, 5, 0.5);
                        padraoFront3.setBounds(-20, 30, 400, 200);
                        padraoFront3.setBackground(corBackPnl);
                        // smart.setBorder(gray);
                        padraoFront3.setOpaque(false);
                        padraoFront3.setLayout(null);
                        confBlock3.add(padraoFront3);
                    }
                    case 3 -> {
                    }
                    default -> {
                    }
                }
            }
            default -> {
            }
        }

    }

    public void gerarPedido() {

        int nBlack = 1;
        int nRed = 1;
        int nBlue = 1;

        switch (cbxTipoBlock.getSelectedIndex()) {
            case 1 -> {
                Panel3.txtCorB1.setText(String.valueOf(cbxCorBlock1.getSelectedIndex()));
                Panel3.txtCorL1B1.setText(String.valueOf(cbxL1Blk1.getSelectedIndex()));
                Panel3.txtCorL2B1.setText(String.valueOf(cbxL2Blk1.getSelectedIndex()));
                Panel3.txtCorL3B1.setText(String.valueOf(cbxL3Blk1.getSelectedIndex()));
                Panel3.txtPadL1B1.setText(String.valueOf(cbxP1Blk1.getSelectedIndex()));
                Panel3.txtPadL2B1.setText(String.valueOf(cbxP2Blk1.getSelectedIndex()));
                Panel3.txtPadL3B1.setText(String.valueOf(cbxP3Blk1.getSelectedIndex()));
                switch (cbxCorBlock1.getSelectedIndex()) {
                    case 1 -> {
                        if (blockBlack[0] > nBlack) {
                            Panel3.txtPosB1.setText(String.valueOf(blockBlack[nBlack]));
                            nBlack++;
                        } else {

                        }
                    }
                    case 2 -> {
                        if (blockRed[0] > nRed) {
                            Panel3.txtPosB1.setText(String.valueOf(blockRed[nRed]));
                            nRed++;
                        } else {

                        }
                    }
                    case 3 -> {
                        if (blockBlue[0] > nBlue) {
                            Panel3.txtPosB1.setText(String.valueOf(blockBlue[nBlue]));
                            nBlue++;
                        } else {

                        }
                    }
                    default -> {
                    }
                }
            }
            case 2 -> {
                Panel3.txtCorB2.setText(String.valueOf(cbxCorBlock1.getSelectedIndex()));
                Panel3.txtCorL1B2.setText(String.valueOf(cbxL1Blk1.getSelectedIndex()));
                Panel3.txtCorL2B2.setText(String.valueOf(cbxL2Blk1.getSelectedIndex()));
                Panel3.txtCorL3B2.setText(String.valueOf(cbxL3Blk1.getSelectedIndex()));
                Panel3.txtPadL1B2.setText(String.valueOf(cbxP1Blk1.getSelectedIndex()));
                Panel3.txtPadL2B2.setText(String.valueOf(cbxP2Blk1.getSelectedIndex()));
                Panel3.txtPadL3B2.setText(String.valueOf(cbxP3Blk1.getSelectedIndex()));
                switch (cbxCorBlock1.getSelectedIndex()) {
                    case 1 -> {
                        if (blockBlack[0] > nBlack) {
                            Panel3.txtPosB2.setText(String.valueOf(blockBlack[nBlack]));
                            nBlack++;
                        } else {

                        }
                    }
                    case 2 -> {
                        if (blockRed[0] > nRed) {
                            Panel3.txtPosB2.setText(String.valueOf(blockRed[nRed]));
                            nRed++;
                        } else {

                        }
                    }
                    case 3 -> {
                        if (blockBlue[0] > nBlue) {
                            Panel3.txtPosB2.setText(String.valueOf(blockBlue[nBlue]));
                            nBlue++;
                        } else {

                        }
                    }
                    default -> {
                    }
                }
                Panel3.txtCorB1.setText(String.valueOf(cbxCorBlock2.getSelectedIndex()));
                Panel3.txtCorL1B1.setText(String.valueOf(cbxL1Blk2.getSelectedIndex()));
                Panel3.txtCorL2B1.setText(String.valueOf(cbxL2Blk2.getSelectedIndex()));
                Panel3.txtCorL3B1.setText(String.valueOf(cbxL3Blk2.getSelectedIndex()));
                Panel3.txtPadL1B1.setText(String.valueOf(cbxP1Blk2.getSelectedIndex()));
                Panel3.txtPadL2B1.setText(String.valueOf(cbxP2Blk2.getSelectedIndex()));
                Panel3.txtPadL3B1.setText(String.valueOf(cbxP3Blk2.getSelectedIndex()));
                switch (cbxCorBlock2.getSelectedIndex()) {
                    case 1 -> {
                        if (blockBlack[0] > nBlack) {
                            Panel3.txtPosB1.setText(String.valueOf(blockBlack[nBlack]));
                            nBlack++;
                        } else {

                        }
                    }
                    case 2 -> {
                        if (blockRed[0] > nRed) {
                            Panel3.txtPosB1.setText(String.valueOf(blockRed[nRed]));
                            nRed++;
                        } else {

                        }
                    }
                    case 3 -> {
                        if (blockBlue[0] > nBlue) {
                            Panel3.txtPosB1.setText(String.valueOf(blockBlue[nBlue]));
                            nBlue++;
                        } else {

                        }
                    }
                    default -> {
                    }
                }
            }
            case 3 -> {
                Panel3.txtCorB3.setText(String.valueOf(cbxCorBlock1.getSelectedIndex()));
                Panel3.txtCorL1B3.setText(String.valueOf(cbxL1Blk1.getSelectedIndex()));
                Panel3.txtCorL2B3.setText(String.valueOf(cbxL2Blk1.getSelectedIndex()));
                Panel3.txtCorL3B3.setText(String.valueOf(cbxL3Blk1.getSelectedIndex()));
                Panel3.txtPadL1B3.setText(String.valueOf(cbxP1Blk1.getSelectedIndex()));
                Panel3.txtPadL2B3.setText(String.valueOf(cbxP2Blk1.getSelectedIndex()));
                Panel3.txtPadL3B3.setText(String.valueOf(cbxP3Blk1.getSelectedIndex()));
                switch (cbxCorBlock1.getSelectedIndex()) {
                    case 1 -> {
                        if (blockBlack[0] > nBlack) {
                            Panel3.txtPosB3.setText(String.valueOf(blockBlack[nBlack]));
                            nBlack++;
                        } else {

                        }
                    }
                    case 2 -> {
                        if (blockRed[0] > nRed) {
                            Panel3.txtPosB3.setText(String.valueOf(blockRed[nRed]));
                            nRed++;
                        } else {

                        }
                    }
                    case 3 -> {
                        if (blockBlue[0] > nBlue) {
                            Panel3.txtPosB3.setText(String.valueOf(blockBlue[nBlue]));
                            nBlue++;
                        } else {

                        }
                    }
                    default -> {
                    }
                }
                Panel3.txtCorB2.setText(String.valueOf(cbxCorBlock2.getSelectedIndex()));
                Panel3.txtCorL1B2.setText(String.valueOf(cbxL1Blk2.getSelectedIndex()));
                Panel3.txtCorL2B2.setText(String.valueOf(cbxL2Blk2.getSelectedIndex()));
                Panel3.txtCorL3B2.setText(String.valueOf(cbxL3Blk2.getSelectedIndex()));
                Panel3.txtPadL1B2.setText(String.valueOf(cbxP1Blk2.getSelectedIndex()));
                Panel3.txtPadL2B2.setText(String.valueOf(cbxP2Blk2.getSelectedIndex()));
                Panel3.txtPadL3B2.setText(String.valueOf(cbxP3Blk2.getSelectedIndex()));
                switch (cbxCorBlock2.getSelectedIndex()) {
                    case 1 -> {
                        if (blockBlack[0] > nBlack) {
                            Panel3.txtPosB2.setText(String.valueOf(blockBlack[nBlack]));
                            nBlack++;
                        } else {

                        }
                    }
                    case 2 -> {
                        if (blockRed[0] > nRed) {
                            Panel3.txtPosB2.setText(String.valueOf(blockRed[nRed]));
                            nRed++;
                        } else {

                        }
                    }
                    case 3 -> {
                        if (blockBlue[0] > nBlue) {
                            Panel3.txtPosB2.setText(String.valueOf(blockBlue[nBlue]));
                            nBlue++;
                        } else {

                        }
                    }
                    default -> {
                    }
                }
                Panel3.txtCorB1.setText(String.valueOf(cbxCorBlock3.getSelectedIndex()));
                Panel3.txtCorL1B1.setText(String.valueOf(cbxL1Blk3.getSelectedIndex()));
                Panel3.txtCorL2B1.setText(String.valueOf(cbxL2Blk3.getSelectedIndex()));
                Panel3.txtCorL3B1.setText(String.valueOf(cbxL3Blk3.getSelectedIndex()));
                Panel3.txtPadL1B1.setText(String.valueOf(cbxP1Blk3.getSelectedIndex()));
                Panel3.txtPadL2B1.setText(String.valueOf(cbxP2Blk3.getSelectedIndex()));
                Panel3.txtPadL3B1.setText(String.valueOf(cbxP3Blk3.getSelectedIndex()));
                switch (cbxCorBlock3.getSelectedIndex()) {
                    case 1 -> {
                        if (blockBlack[0] > nBlack) {
                            Panel3.txtPosB1.setText(String.valueOf(blockBlack[nBlack]));
                            nBlack++;
                        } else {

                        }
                    }
                    case 2 -> {
                        if (blockRed[0] > nRed) {
                            Panel3.txtPosB1.setText(String.valueOf(blockRed[nRed]));
                            nRed++;
                        } else {

                        }
                    }
                    case 3 -> {
                        if (blockBlue[0] > nBlue) {
                            Panel3.txtPosB1.setText(String.valueOf(blockBlue[nBlue]));
                            nBlue++;
                        } else {

                        }
                    }
                    default -> {
                    }
                }
            }
            default -> {
            }
        }
        Panel3.txtAndarEst.setText(String.valueOf(cbxTipoBlock.getSelectedIndex()));
        Panel3.txtNumPedido.setText(String.valueOf(MainFrame.posExpedArray[12] + 1));
        // MainFrame.posExpedArray[12] = MainFrame.posExpedArray[12];

    }

    public void blocks() {

        int xBlocks = 120;
        int yBlocks = 150;

        confBlocks.removeAll();

        registrarPedidoBtn.setText("EDITAR PEDIDO");

        tampa = new ImagePanel("src/main/java/com/smart40/bloco/rTampa1.png", 5, 5, 1);
        tampa.setBounds(xBlocks + 20, yBlocks + 3, 500, 200);
        tampa.setOpaque(false);
        tampa.setLayout(null);

        block1Ok = new ImagePanel(imgBlk1Path, 5, 5, 1);
        block1Ok.setBounds(xBlocks + 20, yBlocks + 30, 500, 200);
        block1Ok.setOpaque(false);
        block1Ok.setLayout(null);

        laminaLeft1Ok = new ImagePanel(imgLeft1Path, 5, 5, 1);
        laminaLeft1Ok.setBounds(xBlocks + 20, yBlocks + 30, 500, 200);
        laminaLeft1Ok.setBackground(corBackPnl);
        laminaLeft1Ok.setOpaque(false);
        laminaLeft1Ok.setLayout(null);

        padraoLeft1Ok = new ImagePanel(padLeft1Path, 5, 5, 0.5);
        padraoLeft1Ok.setBounds(xBlocks + 20, yBlocks + 30, 500, 200);
        padraoLeft1Ok.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoLeft1Ok.setOpaque(false);
        padraoLeft1Ok.setLayout(null);

        laminaFront1Ok = new ImagePanel(imgFront1Path, 5, 5, 1);
        laminaFront1Ok.setBounds(xBlocks + 20, yBlocks + 30, 500, 200);
        laminaFront1Ok.setBackground(corBackPnl);
        laminaFront1Ok.setOpaque(false);
        laminaFront1Ok.setLayout(null);

        padraoFront1Ok = new ImagePanel(padFront1Path, 5, 5, 0.5);
        padraoFront1Ok.setBounds(xBlocks + 20, yBlocks + 30, 500, 200);
        padraoFront1Ok.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoFront1Ok.setOpaque(false);
        padraoFront1Ok.setLayout(null);

        laminaRight1Ok = new ImagePanel(imgRight1Path, 5, 5, 1);
        laminaRight1Ok.setBounds(xBlocks + 20, yBlocks + 30, 500, 200);
        laminaRight1Ok.setBackground(corBackPnl);
        laminaRight1Ok.setOpaque(false);
        laminaRight1Ok.setLayout(null);

        block2Ok = new ImagePanel(imgBlk2Path, 5, 5, 1);
        block2Ok.setBounds(xBlocks + 20, yBlocks + 95, 500, 200);
        block2Ok.setOpaque(false);
        block2Ok.setLayout(null);

        laminaLeft2Ok = new ImagePanel(imgLeft2Path, 5, 5, 1);
        laminaLeft2Ok.setBounds(xBlocks + 20, yBlocks + 95, 500, 200);
        laminaLeft2Ok.setBackground(corBackPnl);
        laminaLeft2Ok.setOpaque(false);
        laminaLeft2Ok.setLayout(null);

        padraoLeft2Ok = new ImagePanel(padLeft2Path, 5, 5, 0.5);
        padraoLeft2Ok.setBounds(xBlocks + 20, yBlocks + 95, 500, 200);
        padraoLeft2Ok.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoLeft2Ok.setOpaque(false);
        padraoLeft2Ok.setLayout(null);

        laminaFront2Ok = new ImagePanel(imgFront2Path, 5, 5, 1);
        laminaFront2Ok.setBounds(xBlocks + 20, yBlocks + 95, 500, 200);
        laminaFront2Ok.setBackground(corBackPnl);
        laminaFront2Ok.setOpaque(false);
        laminaFront2Ok.setLayout(null);

        padraoFront2Ok = new ImagePanel(padFront2Path, 5, 5, 0.5);
        padraoFront2Ok.setBounds(xBlocks + 20, yBlocks + 95, 500, 200);
        padraoFront2Ok.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoFront2Ok.setOpaque(false);
        padraoFront2Ok.setLayout(null);

        laminaRight2Ok = new ImagePanel(imgRight2Path, 5, 5, 1);
        laminaRight2Ok.setBounds(xBlocks + 20, yBlocks + 95, 500, 200);
        laminaRight2Ok.setBackground(corBackPnl);
        laminaRight2Ok.setOpaque(false);
        laminaRight2Ok.setLayout(null);

        block3Ok = new ImagePanel(imgBlk3Path, 5, 5, 1);
        block3Ok.setBounds(xBlocks + 20, yBlocks + 160, 500, 200);
        block3Ok.setOpaque(false);
        block3Ok.setLayout(null);

        laminaLeft3Ok = new ImagePanel(imgLeft3Path, 5, 5, 1);
        laminaLeft3Ok.setBounds(xBlocks + 20, yBlocks + 160, 500, 200);
        laminaLeft3Ok.setBackground(corBackPnl);
        laminaLeft3Ok.setOpaque(false);
        laminaLeft3Ok.setLayout(null);

        padraoLeft3Ok = new ImagePanel(padLeft3Path, 5, 5, 0.5);
        padraoLeft3Ok.setBounds(xBlocks + 20, yBlocks + 160, 500, 200);
        padraoLeft3Ok.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoLeft3Ok.setOpaque(false);
        padraoLeft3Ok.setLayout(null);

        laminaFront3Ok = new ImagePanel(imgFront3Path, 5, 5, 1);
        laminaFront3Ok.setBounds(xBlocks + 20, yBlocks + 160, 500, 200);
        laminaFront3Ok.setBackground(corBackPnl);
        laminaFront3Ok.setOpaque(false);
        laminaFront3Ok.setLayout(null);

        padraoFront3Ok = new ImagePanel(padFront3Path, 5, 5, 0.5);
        padraoFront3Ok.setBounds(xBlocks + 20, yBlocks + 160, 500, 200);
        padraoFront3Ok.setBackground(corBackPnl);
        // smart.setBorder(gray);
        padraoFront3Ok.setOpaque(false);
        padraoFront3Ok.setLayout(null);

        laminaRight3Ok = new ImagePanel(imgRight3Path, 5, 5, 1);
        laminaRight3Ok.setBounds(xBlocks + 20, yBlocks + 160, 500, 200);
        laminaRight3Ok.setBackground(corBackPnl);
        laminaRight3Ok.setOpaque(false);
        laminaRight3Ok.setLayout(null);

        confBlocks.add(tampa);

        switch (cbxTipoBlock.getSelectedIndex()) {
            case 1 -> {
                confBlocks.add(padraoLeft1Ok);
                confBlocks.add(padraoFront1Ok);
                confBlocks.add(laminaLeft1Ok);
                confBlocks.add(laminaFront1Ok);
                confBlocks.add(laminaRight1Ok);
                confBlocks.add(block1Ok);
            }
            case 2 -> {
                confBlocks.add(padraoLeft2Ok);
                confBlocks.add(padraoFront2Ok);
                confBlocks.add(padraoLeft1Ok);
                confBlocks.add(padraoFront1Ok);
                confBlocks.add(laminaLeft1Ok);
                confBlocks.add(laminaFront1Ok);
                confBlocks.add(laminaRight1Ok);
                confBlocks.add(block1Ok);
                confBlocks.add(laminaLeft2Ok);
                confBlocks.add(laminaFront2Ok);
                confBlocks.add(laminaRight2Ok);
                confBlocks.add(block2Ok);
            }
            case 3 -> {
                confBlocks.add(padraoLeft1Ok);
                confBlocks.add(padraoFront1Ok);
                confBlocks.add(laminaLeft1Ok);
                confBlocks.add(laminaFront1Ok);
                confBlocks.add(laminaRight1Ok);
                confBlocks.add(block1Ok);
                confBlocks.add(padraoLeft2Ok);
                confBlocks.add(padraoFront2Ok);
                confBlocks.add(laminaLeft2Ok);
                confBlocks.add(laminaFront2Ok);
                confBlocks.add(laminaRight2Ok);
                confBlocks.add(block2Ok);
                confBlocks.add(padraoLeft3Ok);
                confBlocks.add(padraoFront3Ok);
                confBlocks.add(laminaLeft3Ok);
                confBlocks.add(laminaFront3Ok);
                confBlocks.add(laminaRight3Ok);
                confBlocks.add(block3Ok);
            }
            default -> {
            }
        }

        confBlocks.revalidate();
        confBlocks.repaint();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void blockBlackDisponivel() {

        int index = 0;
        // int totalBlock = 0;
        // int cont = 0;
        // Lê o arquivo linha por linha
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/smart40/magazineest.txt"))) {
            String linha;

            // Enquanto houver linhas no arquivo, continue lendo
            while ((linha = reader.readLine()) != null) {

                // System.out.println("AQUI - "+linha.substring(linha.indexOf("=") + 1,
                // linha.length()));

                if (linha.substring(linha.indexOf("=") + 1, linha.length()).equals("1")) {
                    blockBlack[index + 1] = Integer.parseInt(linha.substring(1, linha.indexOf("=")));
                    index++;
                }

                // blockArray[index] = Byte.parseByte(linha.substring(linha.indexOf("=") + 1,
                // linha.length()));

            }
            blockBlack[0] = index;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // for (int c = 0; c < index + 1; c++) {
        // System.out.println("blockBlack[c] = " + blockBlack[c]);
        // }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void blockRedDisponivel() {
        int index = 0;
        // int totalBlock = 0;
        // int cont = 0;
        // Lê o arquivo linha por linha
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/smart40/magazineest.txt"))) {
            String linha;

            // Enquanto houver linhas no arquivo, continue lendo
            while ((linha = reader.readLine()) != null) {

                if (linha.substring(linha.indexOf("=") + 1, linha.length()).equals("2")) {
                    blockRed[index + 1] = Integer.parseInt(linha.substring(1, linha.indexOf("=")));
                    index++;
                }

            }
            blockRed[0] = index;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // for (int c = 0; c < index + 1; c++) {
        // System.out.println("blockRed[c] = " + blockRed[c]);
        // }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void blockBlueDisponivel() {
        int index = 0;
        // int totalBlock = 0;
        // int cont = 0;
        // Lê o arquivo linha por linha
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/smart40/magazineest.txt"))) {
            String linha;

            // Enquanto houver linhas no arquivo, continue lendo
            while ((linha = reader.readLine()) != null) {

                if (linha.substring(linha.indexOf("=") + 1, linha.length()).equals("3")) {
                    blockBlue[index + 1] = Integer.parseInt(linha.substring(1, linha.indexOf("=")));
                    index++;
                }

            }
            blockBlue[0] = index;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // for (int c = 0; c < index + 1; c++) {
        // System.out.println("blockBlue[c] = " + blockBlue[c]);
        // }
    }
}