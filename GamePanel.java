import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {
    boolean up = false, right = false, left = false, down = false;
    public Player p = new Player();

    public long lastTime = System.nanoTime();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // fluidifier les mouvements
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(p.c);
        g.fillOval((int) p.x, (int) p.y, p.w, p.h);
    }

    // gérer les déplacements

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

        // vérifier périodiquement si il y a eu un changement
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

    // à chaque touches pressée
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        switch (k) {
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
        }
    }


    // à chaque touches relachées
    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        switch (k) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
        }
    }
}