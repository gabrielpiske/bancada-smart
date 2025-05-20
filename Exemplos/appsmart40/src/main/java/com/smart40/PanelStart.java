package com.smart40;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

//import java.util.jar.Manifest;

public class PanelStart extends JPanel {

    public static PlcConnector plcWriteEst;
    public static PlcConnector plcWritePro;
    public static PlcConnector plcWriteMon;
    public static PlcConnector plcWriteExp;

    public static boolean bancadaOn = false;
    public static boolean estoqueOn = false;
    public static boolean processoOn = false;
    public static boolean montagemOn = false;
    public static boolean expedicaoOn = false;
    public static JButton button;
    public static JButton orderFinishedButton;

    public static JButton btnOkVoltar;

    public static JLabel lblStatusEst;
    public static JLabel lblStatusPro;
    public static JLabel lblStatusMon;
    public static JLabel lblStatusExp;

    public static ImagePanel pnlOpEst;
    public static ImagePanel pnlOpPro;
    public static ImagePanel pnlOpMon;
    public static ImagePanel pnlOpExp;

    public static JPanel pnlPLCBlock;
    public static JPanel pnlPLCPedido;

    public static JPanel pnlOpSmart;

    public static JTextField txtIpRedeSmart = new JTextField();

    /*----- Variáveis de definição de Fontes Cores e Bordas */
    // private static Color corBackPnl = new Color(10, 50, 50);
    Color corTextLabel = new Color(250, 250, 250);
    Color corTextField = new Color(50, 90, 90);

    Color corTextFieldEst = new Color(10, 10, 10);

    static Color corJTFfont = new Color(50, 255, 255);
    // Color corJLblTitle = new Color(30, 100, 190);
    // private static Color corBackPnlOn = new Color(10, 120, 50);
    Font fontArial12 = new Font("Arial", Font.PLAIN, 12);
    Font font14Consolas = new Font("Consolas", Font.PLAIN, 14);
    static Font font14 = new Font("Arial", Font.PLAIN, 14);
    Font font12Bold = new Font("Arial", Font.BOLD, 12);
    Font font14Bold = new Font("Arial", Font.BOLD, 14);
    // Font font15 = new Font("Arial", Font.PLAIN, 15);
    // Font font16 = new Font("Arial", Font.PLAIN, 16);
    // Font font18 = new Font("Arial", Font.PLAIN, 18);
    // Font font22 = new Font("Arial", Font.BOLD, 22);
    // Font fontTitle = new Font("Arial", Font.BOLD, 32);
    static Border gray = BorderFactory.createLineBorder(Color.WHITE);
    Border border = BorderFactory.createLineBorder(Color.WHITE, 1);
    /*----------------------------------------------------------------------- */

    // private MainFrame mainFrame;

    public PanelStart(MainFrame mainFrame, int w, int h) {
        // Configura o layout do painel
        setSize(w, h);
        setLayout(null);
        // this.mainFrame = mainFrame;

        // setLayout(new FlowLayout());
        // this.setBackground(new Color(200, 100, 200));

        ImagePanel back = new ImagePanel("src/main/java/com/smart40/background/back_verde.png", -400, -100, 1);
        back.setBounds(0, 0, 1380, 800);
        // Cria os componentes
        // Panel1 pnl1 = new Panel1(150, 200);
        // Panel2 pnl2 = new Panel2(150, 200);

        // MainFrame form = new MainFrame();

        ImagePanel smart = new ImagePanel("src/main/java/com/smart40/smart/smart.png", 100, 5, 1);
        smart.setBounds(250, 20, 995, 400);
        // smart.setBackground(corBackPnl);
        // smart.setBorder(gray);
        smart.setOpaque(false);
        smart.setLayout(null);
        add(smart);

        JPanel consoleStatus = new JPanel();
        consoleStatus.setBounds(60, 200, 260, 300);
        // consoleStatus.setLayout(null);
        consoleStatus.setBorder(gray);
        consoleStatus.setOpaque(false);

        JTextArea jtaConsole = new JTextArea();
        jtaConsole.setBounds(10, 10, 250, 290);
        jtaConsole.setFont(font14Consolas);
        jtaConsole.setForeground(corJTFfont);
        jtaConsole.setOpaque(false);
        jtaConsole.setText("""
                PEDIDO N\u00ba..........: 18
                TEMPO DE PRODU\u00c7\u00c3O..: 4:35
                TIPO DE BLOCO......: SIMPLES""");

        // consoleStatus.add(jtaConsole);

        JLabel lblStsConsole = new JLabel();
        lblStsConsole.setText("Tempo de produção: 4:15");
        lblStsConsole.setBounds(10, 10, 300, 30);
        lblStsConsole.setFont(font14Consolas);
        lblStsConsole.setForeground(corJTFfont);
        // consoleStatus.add(lblStsConsole);

        // Criação do JTextArea com rolagem
        JTextArea textArea = new JTextArea();
        textArea.setBounds(10, 10, 260, 300);
        textArea.setFont(font14Consolas);
        textArea.setForeground(corJTFfont);
        textArea.setBackground(Color.black);

        textArea.setLineWrap(true); // Quebra de linha automática
        textArea.setWrapStyleWord(true); // Quebra de linha por palavra
        textArea.setPreferredSize(new Dimension(380, 250)); // Define o tamanho preferido
        // textArea.setOpaque(false);
        // consoleStatus.add(textArea);
        // add(consoleStatus);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(60, 200, 260, 300);
        scrollPane.setBackground(Color.black);
        scrollPane.setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // Desativa a barra de rolagem
                                                                                     // vertical
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Barra de rolagem
                                                                                             // horizontal, se
                                                                                             // necessário
        // add(scrollPane);

        // Exemplo de lista de mensagens
        List<String> messages = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            messages.add("Mensagem " + i);
        }

        // Adiciona as mensagens ao JTextArea
        for (String message : messages) {
            addMessageToTextArea(textArea, message);
        }

        pnlOpSmart = new JPanel();
        pnlOpSmart.setBounds(160, 165, 586, 155);
        pnlOpSmart.setLayout(null);
        // pnlOpSmart.setBorder(gray);
        pnlOpSmart.setVisible(false);
        pnlOpSmart.setBackground(Color.BLACK);
        pnlOpSmart.setForeground(corTextLabel);
        pnlOpSmart.setOpaque(false);
        smart.add(pnlOpSmart);

        pnlOpEst = new ImagePanel("src/main/java/com/smart40/status/estoque.png", 12, 20, 1);
        pnlOpEst.setBounds(0, 30, 105, 125);
        pnlOpEst.setBorder(gray);
        // pnlOpEst.setBackground(Color.BLACK);
        // pnlOpEst.setBackground(new Color(240, 240, 10));
        pnlOpEst.setBackground(new Color(120, 255, 120, 0));
        pnlOpEst.setForeground(corTextLabel);
        pnlOpEst.setOpaque(true);
        pnlOpSmart.add(pnlOpEst);

        pnlOpPro = new ImagePanel("src/main/java/com/smart40/status/laminas.png", 12, 20, 1);
        pnlOpPro.setBounds(138, 30, 105, 125);
        pnlOpPro.setBorder(gray);
        pnlOpPro.setBackground(Color.BLACK);
        pnlOpPro.setBackground(new Color(255, 255, 10, 0));
        pnlOpPro.setForeground(corTextLabel);
        pnlOpPro.setOpaque(true);
        pnlOpSmart.add(pnlOpPro);

        pnlOpMon = new ImagePanel("src/main/java/com/smart40/status/montagem.png", 45, 20, 1);
        pnlOpMon.setBounds(276, 30, 172, 125);
        pnlOpMon.setBorder(gray);
        pnlOpMon.setBackground(new Color(255, 255, 10, 0));
        pnlOpMon.setForeground(corTextLabel);
        pnlOpMon.setOpaque(true);
        pnlOpSmart.add(pnlOpMon);

        pnlOpExp = new ImagePanel("src/main/java/com/smart40/status/expedicao.png", 12, 20, 1);
        pnlOpExp.setBounds(481, 30, 105, 125);
        pnlOpExp.setBorder(gray);
        pnlOpExp.setBackground(new Color(255, 255, 10, 0));
        pnlOpExp.setForeground(corTextLabel);
        pnlOpExp.setOpaque(true);
        pnlOpSmart.add(pnlOpExp);

        lblStatusEst = new JLabel("DESLIGADO");
        // lblStatusEst.setBounds(145, 165, 135, 30);
        lblStatusEst.setBounds(0, 0, 105, 30);
        lblStatusEst.setBorder(gray);
        lblStatusEst.setBackground(Color.BLACK);
        lblStatusEst.setForeground(corJTFfont);
        lblStatusEst.setOpaque(true);
        lblStatusEst.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatusEst.setFont(font12Bold);
        pnlOpSmart.add(lblStatusEst);

        lblStatusPro = new JLabel("DESLIGADO");
        // lblStatusPro.setBounds(285, 165, 135, 30);
        lblStatusPro.setBounds(138, 0, 105, 30);
        lblStatusPro.setBorder(gray);
        lblStatusPro.setBackground(Color.BLACK);
        lblStatusPro.setForeground(corJTFfont);
        lblStatusPro.setOpaque(true);
        lblStatusPro.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatusPro.setFont(font12Bold);
        pnlOpSmart.add(lblStatusPro);

        lblStatusMon = new JLabel("DESLIGADO");
        // lblStatusMon.setBounds(425, 165, 200, 30);
        // lblStatusMon.setText(PanelStatus.txtStatMontag.getText().trim());
        lblStatusMon.setBounds(276, 0, 172, 30);
        lblStatusMon.setBorder(gray);
        lblStatusMon.setBackground(Color.BLACK);
        lblStatusMon.setForeground(corJTFfont);
        lblStatusMon.setOpaque(true);
        lblStatusMon.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatusMon.setFont(font12Bold);
        pnlOpSmart.add(lblStatusMon);

        lblStatusExp = new JLabel("DESLIGADO");
        lblStatusExp.setBounds(481, 0, 105, 30);
        lblStatusExp.setBorder(gray);
        lblStatusExp.setBackground(Color.BLACK);
        lblStatusExp.setForeground(corJTFfont);
        lblStatusExp.setOpaque(true);
        lblStatusExp.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatusExp.setFont(font12Bold);
        pnlOpSmart.add(lblStatusExp);

        JLabel lblIpRede = new JLabel("ENDEREÇO IP SMART4.0");
        lblIpRede.setBounds(143, 360, 277, 30);
        lblIpRede.setBorder(gray);
        lblIpRede.setBackground(Color.BLACK);
        lblIpRede.setForeground(corTextLabel);
        lblIpRede.setOpaque(true);
        lblIpRede.setHorizontalAlignment(SwingConstants.CENTER);
        lblIpRede.setFont(font14);
        smart.add(lblIpRede);

        txtIpRedeSmart.setText("10.74.241.10");
        txtIpRedeSmart.setBounds(421, 360, 203, 30);
        txtIpRedeSmart.setBackground(corTextFieldEst);
        txtIpRedeSmart.setOpaque(true);
        txtIpRedeSmart.setBorder(border);
        txtIpRedeSmart.setFont(font14Bold);
        txtIpRedeSmart.setForeground(corJTFfont);
        txtIpRedeSmart.setHorizontalAlignment(SwingConstants.CENTER);
        smart.add(txtIpRedeSmart);

        JLabel lblIpEst = new JLabel(" EST: ");
        lblIpEst.setBounds(143, 325, 138, 30);
        lblIpEst.setBorder(gray);
        lblIpEst.setBackground(Color.BLACK);
        lblIpEst.setForeground(corTextLabel);
        lblIpEst.setOpaque(true);
        lblIpEst.setVisible(false);
        lblIpEst.setHorizontalAlignment(SwingConstants.CENTER);
        lblIpEst.setFont(fontArial12);
        smart.add(lblIpEst);

        JLabel lblIpPro = new JLabel(" PRO: ");
        lblIpPro.setBounds(282, 325, 138, 30);
        lblIpPro.setBorder(gray);
        lblIpPro.setBackground(Color.BLACK);
        lblIpPro.setForeground(corTextLabel);
        lblIpPro.setOpaque(true);
        lblIpPro.setVisible(false);
        lblIpPro.setHorizontalAlignment(SwingConstants.CENTER);
        lblIpPro.setFont(fontArial12);
        smart.add(lblIpPro);

        JLabel lblIpMon = new JLabel(" MON: ");
        lblIpMon.setBounds(421, 325, 203, 30);
        lblIpMon.setBorder(gray);
        lblIpMon.setBackground(Color.BLACK);
        lblIpMon.setForeground(corTextLabel);
        lblIpMon.setOpaque(true);
        lblIpMon.setVisible(false);
        lblIpMon.setHorizontalAlignment(SwingConstants.CENTER);
        lblIpMon.setFont(fontArial12);
        smart.add(lblIpMon);

        JLabel lblIpExp = new JLabel(" EXP: ");
        lblIpExp.setBounds(625, 325, 138, 30);
        lblIpExp.setBorder(gray);
        lblIpExp.setBackground(Color.BLACK);
        lblIpExp.setForeground(corTextLabel);
        lblIpExp.setOpaque(true);
        lblIpExp.setVisible(false);
        lblIpExp.setHorizontalAlignment(SwingConstants.CENTER);
        lblIpExp.setFont(fontArial12);
        smart.add(lblIpExp);

        JButton btnIpConf = new JButton("OK");
        btnIpConf.setBounds(625, 360, 138, 30);

        btnIpConf.setForeground(corTextLabel);
        btnIpConf.setBackground(corTextField);
        btnIpConf.setVisible(true);
        smart.add(btnIpConf);

        btnIpConf.addActionListener((ActionEvent e) -> {
            // Criar um SwingWorker para realizar as operações de forma assíncrona
            lblIpEst.setVisible(false);
            lblIpPro.setVisible(false);
            lblIpMon.setVisible(false);
            lblIpExp.setVisible(false);

            SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                @Override
                protected Void doInBackground() throws Exception {
                    String[] netIp = PanelStart.txtIpRedeSmart.getText().split("\\.");

                    // Verificar os IPs e atualizar a GUI conforme os resultados
                    if (isReachable(netIp[0] + "." + netIp[1] + "." + netIp[2] + ".10", 500)) {
                        publish("EST: " + netIp[0] + "." + netIp[1] + "." + netIp[2] + ".10 Ok");
                        MainFrame.ipEstoque = netIp[0] + "." + netIp[1] + "." + netIp[2] + ".10";
                        estoqueOn = true;
                    } else {
                        publish("EST: " + netIp[0] + "." + netIp[1] + "." + netIp[2] + ".10 --");
                    }

                    if (isReachable(netIp[0] + "." + netIp[1] + "." + netIp[2] + ".20", 500)) {
                        publish("PRO: " + netIp[0] + "." + netIp[1] + "." + netIp[2] + ".20 Ok");
                        MainFrame.ipProcesso = netIp[0] + "." + netIp[1] + "." + netIp[2] + ".20";
                        processoOn = true;
                    } else {
                        publish("PRO: " + netIp[0] + "." + netIp[1] + "." + netIp[2] + ".20 --");
                    }

                    if (isReachable(netIp[0] + "." + netIp[1] + "." + netIp[2] + ".30", 500)) {
                        publish("MON: " + netIp[0] + "." + netIp[1] + "." + netIp[2] + ".30 Ok");
                        MainFrame.ipMontagem = netIp[0] + "." + netIp[1] + "." + netIp[2] + ".30";
                        montagemOn = true;
                    } else {
                        publish("MON: " + netIp[0] + "." + netIp[1] + "." + netIp[2] + ".30 --");
                    }

                    if (isReachable(netIp[0] + "." + netIp[1] + "." + netIp[2] + ".40", 500)) {
                        publish("EXP: " + netIp[0] + "." + netIp[1] + "." + netIp[2] + ".40 Ok");
                        MainFrame.ipExpedicao = netIp[0] + "." + netIp[1] + "." + netIp[2] + ".40";
                        expedicaoOn = true;
                    } else {
                        publish("EXP: " + netIp[0] + "." + netIp[1] + "." + netIp[2] + ".40 --");
                    }

                    return null;
                }

                @Override
                protected void process(java.util.List<String> chunks) {
                    // Esse método é chamado a cada vez que o método publish() for chamado no
                    // doInBackground
                    for (String message : chunks) {
                        // Atualiza os JLabels na GUI conforme a mensagem recebida
                        if (message.startsWith("EST:")) {
                            lblIpEst.setText(message);
                            lblIpEst.setForeground(message.contains("Ok") ? Color.green : Color.red);
                            lblIpEst.setVisible(true);
                        } else if (message.startsWith("PRO:")) {
                            lblIpPro.setText(message);
                            lblIpPro.setForeground(message.contains("Ok") ? Color.green : Color.red);
                            lblIpPro.setVisible(true);
                        } else if (message.startsWith("MON:")) {
                            lblIpMon.setText(message);
                            lblIpMon.setForeground(message.contains("Ok") ? Color.green : Color.red);
                            lblIpMon.setVisible(true);
                        } else if (message.startsWith("EXP:")) {
                            lblIpExp.setText(message);
                            lblIpExp.setForeground(message.contains("Ok") ? Color.green : Color.red);
                            lblIpExp.setVisible(true);
                        }
                    }
                }

                @Override
                protected void done() {
                    // Esse método pode ser usado para realizar alguma ação final, se necessário
                }
            };

            // Iniciar a execução do SwingWorker
            worker.execute();
        });

        JTextField txtTeste = new JTextField();
        txtTeste.setText("TESTE");
        txtTeste.setBounds(600, 360, 135, 30);
        txtTeste.setBackground(corTextFieldEst);
        txtTeste.setOpaque(true);
        txtTeste.setBorder(border);
        txtTeste.setFont(font14Bold);
        txtTeste.setForeground(corJTFfont);
        txtTeste.setVisible(false);
        txtTeste.setHorizontalAlignment(SwingConstants.CENTER);
        smart.add(txtTeste);

        txtIpRedeSmart.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                // MainFrame.stationWorkId = Integer.parseInt(txtIpRedeSmart.getText());
                // MainFrame.updateDisplayStation();
                // revalidate();
                // repaint();
            }

        });

        JPanel pnlSmartResumo = new JPanel();
        pnlSmartResumo.setBounds(250, 430, 915, 345);
        // pnlSmartResumo.setBorder(gray);
        pnlSmartResumo.setLayout(null);
        pnlSmartResumo.setOpaque(false);
        add(pnlSmartResumo);

        button = new JButton("ATUALIZAR DADOS DA BANCADA SMART 4.0");
        button.setBounds(5, 300, 905, 40);

        button.setForeground(corTextLabel);
        button.setBackground(corTextField);

        btnOkVoltar = new JButton("CONFIGURAR PEDIDO");
        btnOkVoltar.setBounds(5, 300, 905, 40);

        btnOkVoltar.setForeground(corTextLabel);
        btnOkVoltar.setBackground(corTextField);
        btnOkVoltar.setVisible(false);

        btnOkVoltar.addActionListener((ActionEvent e) -> {
            MainFrame.cardLayout.show(MainFrame.painelPrincipal, "pnlConfigPanels");
            PanelBanner.btnRwd.doClick();
            MainFrame.updPanels();
            ConfigPedido.registrarPedidoBtn.doClick();
            MainFrame.pnlConfigPanels.revalidate();
            MainFrame.pnlConfigPanels.repaint();
        });

        // Adiciona os componentes ao painel

        JLabel lblMagzine = new JLabel("MAGAZINE ESTAÇÃO ESTOQUE");
        lblMagzine.setBounds(5, 5, 285, 30);
        lblMagzine.setBorder(gray);
        lblMagzine.setForeground(corTextLabel);
        lblMagzine.setHorizontalAlignment(SwingConstants.CENTER);
        lblMagzine.setFont(font14);
        pnlSmartResumo.add(lblMagzine);

        pnlPLCBlock = new JPanel();
        pnlPLCBlock.setBounds(5, 40, 285, 240);
        pnlPLCBlock.setLayout(null);
        pnlPLCBlock.setBorder(gray);
        pnlPLCBlock.setOpaque(false);
        // pnlPLCBlock.setBackground(corBackPnl);
        pnlSmartResumo.add(pnlPLCBlock);

        MainFrame.loadMagazineEstoque();
        updateEstoque();

        JLabel lblMagzineExp = new JLabel("MAGAZINE ESTAÇÃO EXPEDIÇÃO");
        lblMagzineExp.setBounds(355, 5, 555, 30);
        lblMagzineExp.setBorder(gray);
        lblMagzineExp.setForeground(corTextLabel);
        lblMagzineExp.setHorizontalAlignment(SwingConstants.CENTER);
        lblMagzineExp.setFont(font14);
        pnlSmartResumo.add(lblMagzineExp);

        pnlPLCPedido = new JPanel();
        pnlPLCPedido.setBounds(355, 40, 555, 240);
        pnlPLCPedido.setLayout(null);
        pnlPLCPedido.setBorder(gray);
        pnlPLCPedido.setOpaque(false);
        // pnlPLCBlock.setBackground(corBackPnl);
        pnlSmartResumo.add(pnlPLCPedido);

        MainFrame.loadMagazineExpedicao();
        updateExpedicao();

        pnlSmartResumo.add(button);
        pnlSmartResumo.add(btnOkVoltar);
        // add(btnOkVoltar);
        add(back);

        button.addActionListener((ActionEvent e) -> {
            System.out.println("Botão Start foi clicado");
            System.out.println("IP ESTOQUE....: " + MainFrame.ipEstoque);
            System.out.println("IP PROCESSO...: " + MainFrame.ipProcesso);
            System.out.println("IP MONTAGEM...: " + MainFrame.ipMontagem);
            System.out.println("IP EXPEDIÇÃO..: " + MainFrame.ipExpedicao);

            // int timeout = 2000;

            // if (isReachable(txtIpRedeSmart.getText(), timeout)) {
            // System.out.println("O endereço " + MainFrame.ipEstoque + " é alcançável.");
            // // MainFrame.readOnly = false;
            // bancadaOn = true;
            // } else {
            // System.out.println("O endereço " + MainFrame.ipEstoque + " não é
            // alcançável.");
            // MainFrame.readOnly = true;
            // // bancadaOn = false;
            // JOptionPane.showMessageDialog(null,
            // "A tentativa de comunicação com a bancada Smart 4.0 falhou. \n\nVerifique se
            // a bancada está ligada e tente novamente.");
            // bancadaOn = false;
            // }
            //
            if (estoqueOn & processoOn & montagemOn & expedicaoOn) {
                System.out.println("A bancada está ligada.");
                // MainFrame.readOnly = false;
                bancadaOn = true;
            } else {
                MainFrame.readOnly = true;
                // bancadaOn = false;
                JOptionPane.showMessageDialog(null,
                        "A tentativa de comunicação com a bancada Smart 4.0 falhou. \n\nVerifique se a bancada está ligada e tente novamente.");
                bancadaOn = false;
            }

            if (MainFrame.readOnly == false & bancadaOn == true) {

                // Inicializa a estação ESTOQUE
                if (estoqueOn) {

                    plcWriteEst = new PlcConnector(MainFrame.ipEstoque, 102);

                    try {
                        plcWriteEst.connect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                    try {

                        plcWriteEst.writeBit(9, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPEst

                    } catch (Exception e1) {
                        System.out.println("ERRO: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para FALSE");
                    }

                    try {
                        // plcWriteEst = new PlcConnector(MainFrame.ipEstoque, 102);
                        plcWriteEst.writeBit(9, 64, 0, Boolean.parseBoolean("FALSE")); // RecebidoEstoque

                    } catch (Exception e2) {
                        System.out.println("ERRO: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para FALSE");
                    }

                    try {
                        // plcWriteEst = new PlcConnector(MainFrame.ipEstoque, 102); // IniciarGuardar
                        plcWriteEst.writeBit(9, 64, 1, Boolean.parseBoolean("FALSE"));

                    } catch (Exception e3) {
                        System.out.println(
                                "ERRO: Atualização da Flag IniciarGuardarEstoque [DB9:64.1] para FALSE");
                    }
                    try {
                        // plcWriteEst = new PlcConnector(MainFrame.ipEstoque, 102);
                        plcWriteEst.writeInt(9, 66, 0);

                    } catch (Exception e4) {
                        System.out.println("ERRO: Atualização da PosicaoGuardarEstoque [DB9:66]");
                    }

                    if (plcWriteEst != null) {
                        try {
                            plcWriteEst.disconnect();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    MainFrame.updatePLCEstoque();
                }

                // Inicializa a estação PROCESSO
                if (processoOn) {

                    plcWritePro = new PlcConnector(MainFrame.ipProcesso, 102);
                    plcWriteMon = new PlcConnector(MainFrame.ipMontagem, 102);

                    try {
                        plcWritePro.connect();
                        plcWriteMon.connect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                    try {

                        plcWritePro.writeBit(2, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPProcesso
                    } catch (Exception e5) {
                        System.out.println("ERRO: Atualização da Flag RecebidoOPProcesso [DB2:0.0] para FALSE");
                    }

                    // Inicializa a estação MONTAGEM
                    try {

                        plcWriteMon.writeBit(57, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPMontagem
                    } catch (Exception e6) {
                        System.out.println("ERRO: Atualização da Flag RecebidoOPMontagem [DB57:0.0] para FALSE");
                    }

                    if (plcWritePro != null) {
                        try {
                            plcWritePro.disconnect();
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    }
                    if (plcWriteMon != null) {
                        try {
                            plcWriteMon.disconnect();
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    }
                }
                // Inicializa a estação EXPEDIÇÃO
                if (expedicaoOn) {

                    plcWriteExp = new PlcConnector(MainFrame.ipExpedicao, 102);

                    try {
                        plcWriteExp.connect();
                    } catch (Exception e1) {

                        e1.printStackTrace();
                    }

                    try {
                        // plcWriteExp = new PlcConnector(MainFrame.ipExpedicao, 102);
                        plcWriteExp.writeBit(9, 0, 0, Boolean.parseBoolean("FALSE")); // RecebidoOPExpedicao
                    } catch (Exception e7) {
                        System.out.println("ERRO: Atualização da Flag RecebidoOPExpedicao [DB9:0.0] para FALSE");
                    }

                    try {
                        // plcWriteExp = new PlcConnector(MainFrame.ipExpedicao, 102);
                        plcWriteExp.writeBit(9, 2, 0, Boolean.parseBoolean("FALSE")); // RecebidoExpedicao
                    } catch (Exception e8) {
                        System.out.println("ERRO: Atualização da Flag RecebidoOPExpedicao [DB9:2.0] para FALSE");
                    }

                    try {
                        // plcWriteExp = new PlcConnector(MainFrame.ipExpedicao, 102); // IniciarGuardar
                        plcWriteExp.writeBit(9, 2, 1, Boolean.parseBoolean("FALSE"));

                    } catch (Exception e9) {
                        System.out.println(
                                "ERRO: Atualização da Flag IniciarGuardarExpedicao [DB9:2.1] para FALSE");
                    }

                    if (plcWriteExp != null) {
                        try {
                            plcWriteExp.disconnect();
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    }

                    MainFrame.updatePlcExpedicao();
                }
            }

            if (bancadaOn) {

                // mainFrame.setPanels(true, true, true, true, true);
                // MainFrame.pnlConfigPanels.setVisible(true);
                PanelBanner.txtIpRede.setText(PanelStart.txtIpRedeSmart.getText());
                Panel1.textHostPLC.setText(PanelStart.txtIpRedeSmart.getText());
                PanelBanner.btnAtivarBancada.setEnabled(bancadaOn);
            } else {
                PanelBanner.btnAtivarBancada.setEnabled(bancadaOn);
            }

            if (MainFrame.painelPrincipal != null) {
                MainFrame.cardLayout.show(MainFrame.painelPrincipal, "pnlConfigPanels");

            } else {
                System.out.println("ERRO: painelPrincipal é null");
            }
        });

        orderFinishedButton = new JButton("OPERAÇÃO CONCLUÍDA");
        orderFinishedButton.setBounds(195, 230, 515, 40);
        orderFinishedButton.setForeground(corTextLabel);
        orderFinishedButton.setBackground(new Color(50, 50, 255));
        orderFinishedButton.setVisible(MainFrame.blockFinished);
        // orderFinishedButton.setVisible(true);
        smart.add(orderFinishedButton);

        orderFinishedButton.addActionListener(e -> {
            MainFrame.blockFinished = false;
            MainFrame.stationWorkId = 0;
            MainFrame.pedidoEmCurso = false;
            MainFrame.updateDisplayStation();
            Panel3.orderFinishedButton.doClick();
            orderFinishedButton.setVisible(MainFrame.blockFinished);

        });

    }

    public static boolean isReachable(String ipAddress, int timeout) {
        try {
            // Cria uma instância de InetAddress a partir do endereço IP fornecido
            InetAddress inet = InetAddress.getByName(ipAddress);
            // Tenta alcançar o endereço IP com o tempo limite especificado
            return inet.isReachable(timeout);
        } catch (IOException e) {
            System.out.println("Erro ao tentar alcançar o endereço: " + e.getMessage());
            return false;
        }
    }

    public static void updateEstoque() {
        int larguraEst = 36;
        int alturaEst = 36;
        int espacoEst = 10;

        pnlPLCBlock.removeAll();

        for (int i = 0; i < 28; i++) {
            JPanel panel = new JPanel();
            panel.setSize(larguraEst, alturaEst); // Define o tamanho do JPanel
            panel.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Adiciona uma
            // borda para visualização
            panel.setBackground(Color.LIGHT_GRAY); // Define a cor de fundo do JPanel

            JLabel label = new JLabel("P" + (i + 1), SwingConstants.CENTER);
            label.setSize(larguraEst, alturaEst); // Define o tamanho do JLabel
            label.setBorder(gray); // Adiciona uma borda para visualização
            label.setOpaque(true); // Necessário para definir a cor de fundo
            label.setForeground(corJTFfont); // Define a cor de fundo do JLabel

            if (MainFrame.readOnly == true) {
                // indexClor
                // System.out.println("Aqui em ReadOnly = TRUE");
                // for(int c = 0 ; c<28 ; c++){
                // System.out.println("indexColor["+c+"] = "+Panel1.indexColor[c]);
                // }
                switch (Panel1.indexColor[i]) {
                    case 1 -> label.setBackground(Color.BLACK); // Define a cor de fundo do JLabel
                    case 2 -> label.setBackground(Color.RED); // Define a cor de fundo do JLabel
                    case 3 -> label.setBackground(Color.BLUE); // Define a cor de fundo do JLabel
                    default -> label.setBackground(Color.WHITE); // Define a cor de fundo do JLabel
                }

            } else {
                // System.out.println("Aqui em ReadOnly = FALSE");
                switch (MainFrame.blockArray[i]) {
                    case 1 -> label.setBackground(Color.BLACK); // Define a cor de fundo do JLabel
                    case 2 -> label.setBackground(Color.RED); // Define a cor de fundo do JLabel
                    case 3 -> label.setBackground(Color.BLUE); // Define a cor de fundo do JLabel
                    default -> label.setBackground(Color.WHITE); // Define a cor de fundo do JLabel
                }
            }

            // Calcula a posição do JPanel
            int x = (i % 6) * (larguraEst + espacoEst);
            int y = (i / 6) * (alturaEst + espacoEst);

            // Define a posição do JLabel
            label.setLocation(x + 10, y + 10);

            // Adiciona o JLabel ao JPanel
            pnlPLCBlock.add(label);

        }
        pnlPLCBlock.revalidate();
        pnlPLCBlock.repaint();
    }

    public static void updateExpedicao() {
        // System.out.println("AQUI em UpdateExpedicao");
        int larguraExp = 120;
        int alturaExp = 60;
        int espacoExp = 15;

        pnlPLCPedido.removeAll();

        /*
         * for(int c=0 ; c<12 ; c++){
         * 
         * System.out.print(MainFrame.operacao[c]+" - ");
         * System.out.println("");
         * }
         */

        for (int i = 0; i < 12; i++) {
            JPanel panel = new JPanel();
            panel.setSize(larguraExp, alturaExp); // Define o tamanho do JPanel
            // panel.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // Adiciona uma
            // borda para visualização
            panel.setOpaque(true);
            panel.setBackground(Color.black); // Define a cor de fundo do JPanel

            JLabel label = new JLabel();

            if (MainFrame.readOnly == true) {
                if (MainFrame.operacao[i] > 0) {
                    label.setText("P" + (i + 1) + "= [ OP" + String.format("%04d", MainFrame.operacao[i]) + " ]");
                    label.setBorder(BorderFactory.createLineBorder(Color.red));
                } else {
                    label.setText("P" + (i + 1) + "= [ ______ ]");
                    label.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                }

            } else {

                if (MainFrame.posExpedArray[i] > 0) {
                    label.setText("P" + (i + 1) + "= [ OP" + String.format("%04d", MainFrame.posExpedArray[i]) + " ]");
                    label.setBorder(BorderFactory.createLineBorder(Color.red));
                } else {
                    label.setText("P" + (i + 1) + "= [ ______ ]");
                    label.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                }
            }
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setSize(larguraExp, alturaExp); // Define o tamanho do JLabel
            // label.setBorder(gray); // Adiciona uma borda para visualização
            label.setFont(font14);
            label.setBackground(Color.black);
            label.setOpaque(true); // Necessário para definir a cor de fundo
            label.setForeground(corJTFfont); // Define a cor de fundo do JLabel

            // if (MainFrame.blockArray[i] == 1) {
            // label.setBackground(Color.BLACK); // Define a cor de fundo do JLabel
            // } else if (MainFrame.blockArray[i] == 2) {
            // label.setBackground(Color.RED); // Define a cor de fundo do JLabel
            // } else if (MainFrame.blockArray[i] == 3) {
            // label.setBackground(Color.BLUE); // Define a cor de fundo do JLabel
            // } else {
            // label.setBackground(Color.WHITE); // Define a cor de fundo do JLabel
            // }
            //
            // Calcula a posição do JPanel
            int x = (i % 4) * (larguraExp + espacoExp);
            int y = (i / 4) * (alturaExp + espacoExp);

            // Define a posição do JLabel
            label.setLocation(x + 15, y + 15);

            // Adiciona o JLabel ao JPanel
            pnlPLCPedido.add(label);

        }
        pnlPLCPedido.revalidate();
        pnlPLCPedido.repaint();
    }

    // Método que adiciona a mensagem no JTextArea e garante que o texto desça para
    // a última linha
    private static void addMessageToTextArea(JTextArea textArea, String message) {
        // Adiciona a nova mensagem ao JTextArea
        textArea.append(message + "\n");

        // Move o cursor para o final do JTextArea para garantir que a última linha
        // esteja visível
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}