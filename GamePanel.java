import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {
    boolean up = false, right = false, left = false, down = false;
    public Player p = new Player();

    public long lastTime = System.nanoTime();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillOval((int) p.x, (int) p.y, p.w, p.h);
        g.setColor(p.c);
    }

    private void updateGame(double dt) {
        if (up)
            p.y -= p.speed * dt;
        if (down)
            p.y += p.speed * dt;
        if (left)
            p.x -= p.speed * dt;
        if (right)
            p.x += p.speed * dt;
    }

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);

        Timer t = new Timer(16, e -> {
            long now = System.nanoTime();
            double dt = (now - lastTime) / 1e9;
            lastTime = now;
            updateGame(dt);
            repaint();
        });
        t.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // ...
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        switch (k) {
            case KeyEvent.VK_UP:
                up = true;
                System.out.println("Touche haut pressée");
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                System.out.println("Touche bas pressée");
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                System.out.println("Touche gauche pressée");
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                System.out.println("Touche droite pressée");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        switch (k) {
            case KeyEvent.VK_UP:
                up = false;
                System.out.println("Touche haut relâchée");
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                System.out.println("Touche bas relâchée");
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                System.out.println("Touche gauche relâchée");
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                System.out.println("Touche droite relâchée");
                break;
        }
    }
}