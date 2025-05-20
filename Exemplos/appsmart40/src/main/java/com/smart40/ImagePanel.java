package com.smart40;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel implements ActionListener, KeyListener {

    private Image image;
    public int x = 10, y = 30;
    private double escala = 1.0; // Escala da imagem

    public ImagePanel(String imgPath, int x, int y, double scale) {
        this.x = x;
        this.y = y;
        this.escala = scale;
        image = new ImageIcon(imgPath).getImage();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawImage(g);
    }

    private void drawImage(Graphics g) {
        int largura = (int) (image.getWidth(this) * escala);   // Ajusta a largura com base na escala
        int altura = (int) (image.getHeight(this) * escala);   // Ajusta a altura com base na escala
        g.drawImage(image, x, y, largura, altura, this);       // Desenha a imagem escalada
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Não é necessário implementar aqui
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                x -= 5;
                break;
            case KeyEvent.VK_RIGHT:
                x += 5;
                break;
            case KeyEvent.VK_UP:
                y -= 5;
                break;
            case KeyEvent.VK_DOWN:
                y += 5;
                break;
            case KeyEvent.VK_PLUS:  // Tecla '+' para aumentar a escala
            case KeyEvent.VK_EQUALS: // Alternativa caso o '+' esteja junto ao '=' no teclado
                escala += 0.1;
                break;
            case KeyEvent.VK_MINUS: // Tecla '-' para diminuir a escala
                escala -= 0.1;
                if (escala < 0.1) escala = 0.1;  // Impede que a escala seja menor que 10%
                break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Não é necessário implementar aqui
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Não é necessário implementar aqui
    }
}
