package com.smart40;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class PanelBanner extends JPanel {

    // private MainFrame mainFrame;
    // private static Color corBackPnl = new Color(10, 50, 50);

    public static JTextField txtIpRede;

    public static JButton btnRwd;
    public static JButton btnFwd;

    public static JButton btnAtivarBancada;
    public static JButton btnExibirProducao;

    Color corTextLabel = new Color(250, 250, 250);
    Color corTextField = new Color(50, 90, 90);

    Color corTextFieldSts = new Color(10, 10, 10);

    static Color corJTFfont = new Color(50, 255, 255);
    // Color corJLblTitle = new Color(30, 100, 190);

    // private static Color corBackPnlOn = new Color(10, 120, 50);

    Font font14 = new Font("Arial", Font.PLAIN, 14);
    Font font14Bold = new Font("Arial", Font.BOLD, 14);
    // Font font16 = new Font("Arial", Font.PLAIN, 16);
    // Font font18 = new Font("Arial", Font.PLAIN, 18);
    // Font font22 = new Font("Arial", Font.BOLD, 22);

    // Font fontTitle = new Font("Arial", Font.BOLD, 32);

    Border gray = BorderFactory.createLineBorder(Color.WHITE);

    Border border = BorderFactory.createLineBorder(Color.WHITE, 1);

    public PanelBanner(MainFrame mainFrame, int w, int h) {
        // Configura o layout do painel
        setSize(w, h);
        setLayout(null);
        setVisible(true);
        // setOpaque(false);
        // this.mainFrame = mainFrame;
        // setBackground(corBackPnl);
        setBackground(Color.black);
        // this.setBackground(new Color(180, 200, 250));

        // Cria os componentes

        // JLabel lbl = new JLabel("Banner");
        // lbl.setBounds(10, 10, 100, 30);

        JLabel lblIpRede = new JLabel("ENDEREÇO IP SMART4.0");
        lblIpRede.setBounds(15, 15, 210, 30);
        lblIpRede.setBorder(gray);
        lblIpRede.setForeground(corTextLabel);
        lblIpRede.setHorizontalAlignment(SwingConstants.CENTER);
        lblIpRede.setFont(font14);
        add(lblIpRede);

        txtIpRede = new JTextField(/* "10.74.241.0" */);
        txtIpRede.setText(PanelStart.txtIpRedeSmart.getText());
        txtIpRede.setBounds(230, 15, 135, 30);
        txtIpRede.setBackground(corTextFieldSts);
        txtIpRede.setBorder(border);
        txtIpRede.setFont(font14Bold);
        txtIpRede.setForeground(corJTFfont);
        txtIpRede.setHorizontalAlignment(SwingConstants.CENTER);
        add(txtIpRede);

        btnRwd = new JButton("<");
        btnRwd.setBounds(15, 70, 50, 25);
        btnFwd = new JButton(">");
        btnFwd.setBounds(65, 70, 50, 25);

        btnFwd.addActionListener((ActionEvent e) -> {
            MainFrame.xForm++;
            if (MainFrame.xForm > 3) {
                MainFrame.xForm = 3;
            }
            MainFrame.updPanels();
        });

        btnRwd.addActionListener((ActionEvent e) -> {
            MainFrame.xForm--;
            if (MainFrame.xForm < 1) {
                MainFrame.xForm = 1;
            }
            MainFrame.updPanels();
        });

        btnAtivarBancada = new JButton("ATIVAR BANCADA SMART 4.0");
        btnAtivarBancada.setBounds(380, 15, 290, 30);
        btnAtivarBancada.setFont(font14);
        btnAtivarBancada.setEnabled(true);
        btnAtivarBancada.setForeground(corTextLabel);
        btnAtivarBancada.setBackground(corTextField);

        btnAtivarBancada.addActionListener((ActionEvent e) -> {
            if (btnAtivarBancada.getText().equals("ATIVAR BANCADA SMART 4.0") & PanelStart.bancadaOn) {
                btnAtivarBancada.setText("DESATIVAR BANCADA SMART 4.0");
                ConfigPedido.pnlPedidoConf.setBorder(ConfigPedido.greenOn);
                Panel1.pnlPLCVar.setBorder(Panel1.greenOn);
                Panel2.pnlPedidoVar.setBorder(Panel2.greenOn);
                Panel3.pnlPedido.setBorder(Panel3.greenOn);

                if (!MainFrame.connectPlcs) {
                    MainFrame.connectPlcs = true;
                }
                if (MainFrame.connectedPlcs) {
                    MainFrame.connectPlcs = false;
                }

                Panel2.chkReadCiclic.setSelected(true);

                // Panel2.chkReadCiclic.

            } else {
                btnAtivarBancada.setText("ATIVAR BANCADA SMART 4.0");
                ConfigPedido.pnlPedidoConf.setBorder(ConfigPedido.greenOff);
                Panel1.pnlPLCVar.setBorder(Panel1.greenOff);
                Panel2.pnlPedidoVar.setBorder(Panel2.greenOff);
                Panel3.pnlPedido.setBorder(Panel3.greenOff);

                Panel2.chkReadCiclic.setSelected(false);
            }

            MainFrame.leituraCiclica();

            // MainFrame.cardLayout.show(MainFrame.painelPrincipal, "startPanel");
            // PanelStart.pnlOpSmart.setVisible(true);
            // PanelStart.button.setVisible(false);
            // PanelStart.btnOkVoltar.setVisible(true);

            // revalidate();
            // repaint();
        });

        btnExibirProducao = new JButton("EXIBIR PRODUÇÃO");
        btnExibirProducao.setBounds(380, 55, 290, 30);
        btnExibirProducao.setFont(font14);
        btnExibirProducao.setEnabled(true);
        btnExibirProducao.setForeground(corTextLabel);
        btnExibirProducao.setBackground(corTextField);

        btnExibirProducao.addActionListener((ActionEvent e) -> {
            ConfigPedido.registrarPedidoBtn.setText("FINALIZADO");
            Panel2.btnExibirProdcao.doClick();
        });

        // Adiciona os componentes ao painel
        add(btnExibirProducao);
        add(btnAtivarBancada);
        add(btnFwd);
        add(btnRwd);

    }

}
