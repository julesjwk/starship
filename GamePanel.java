import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {
    boolean up = false, right = false, left = false, down = false;
    public Player p = new Player();
    long lastTime;
    int sc = 80;
    double[] sx = new double[sc];
    double[] sy = new double[sc];
    Random r = new Random();
    double sp = 240;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // fluidifier les mouvements
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // afficher les étoiles
        g2.setColor(Color.WHITE);

        for(int i = 0; i < sc; i++) {
            g2.fillOval((int) sx[i], (int) sy[i], 2, 2);
        }

        // afficher le joueur
        g2.setColor(p.c);
        g2.fill(new Ellipse2D.Double(p.x, p.y, p.w, p.h));
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

        if (p.x > Main.W) {
            p.x = p.x % Main.W;
        }

        if (p.y > Main.H) {
            p.y = p.y % Main.H;
        }

        for (int i = 0; i < sc; i++) {
            sy[i] -= sp * dt;
            if (sy[i] < -2) {
                sy[i] = getHeight();
                sx[i] = r.nextInt(getWidth());
            }
        }
    }

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);

        // gérer l'affichage de l'arrière plan
        setBackground(Color.BLACK);

        for(int i = 0; i < sc; i++) {
            sx[i] = r.nextInt(Main.W);
            sy[i] = r.nextInt(Main.H);
        }

        // vérifier périodiquement si il y a eu un changement
        lastTime = System.nanoTime();

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