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
import java.awt.Rectangle;

public class GamePanel extends JPanel implements KeyListener {
    boolean up = false, right = false, left = false, down = false;
    private Player p = new Player();
    private Asteroid a = new Asteroid();
    long lastTime;
    int sc = 80; // nombre d'étoiles en arrière plan
    int ac = 4; // nombre d'asteroids
    double[] sx = new double[sc];
    double[] sy = new double[sc];
    double[] ax = new double[ac];
    double[] ay = new double[ac];
    Random r = new Random();
    double sp = 240; // vitesse des étoiles => px/s
    double ap = 120; // vitesse des asteroids => px/s


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

        // afficher les asteroids
        g2.setColor(a.c);

        for(int i = 0; i < ac; i++) {
            g2.fillOval((int) ax[i], (int) ay[i], a.w, a.h);
        }

        // afficher le joueur
        g2.setColor(p.c);
        g2.fill(new Ellipse2D.Double(p.x, p.y, p.w, p.h));
    }

    // gérer les déplacements

    private void updateGame(double dt) {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        if (up)
            p.y -= p.speed * dt;
        if (down)
            p.y += p.speed * dt;
        if (left)
            p.x -= p.speed * dt;
        if (right)
            p.x += p.speed * dt;

        // gérer les limites de la fenètre
        if (p.x < 0) {
            p.x = 0;
        }

        if (p.x > getWidth() - p.w)  {
            p.x = getWidth() - p.w;
        }

        if (p.y < 0) {
            p.y = 0;
        }

        if (p.y > getHeight() - p.h) {
            p.y = getHeight() - p.h;
        }

        for (int i = 0; i < sc; i++) {
            sy[i] -= sp * dt;
            if (sy[i] < -2) {
                sy[i] = getHeight();
                sx[i] = r.nextInt(getWidth());
            }
        }

        for (int i = 0; i < ac; i++) {
            if(i > 1 && ax[i] == ax[i-1]) {
                ax[i] = r.nextInt(getWidth() - a.w + 1);
            }

            ay[i] += sp * dt;
 
            if (ay[i] >= getHeight()) {
                ay[i] = -a.h;
                ax[i] = r.nextInt(getWidth() - a.w + 1);
            }
        }

        Rectangle playerCollision = new Rectangle(
            (int) p.x, (int) p.y, p.w, p.h
        );

        for (int i = 0; i < ac; i++) {
            Rectangle asteroidCollision = new Rectangle(
                (int) ax[i], (int) ay[i], a.w, a.h
            );

            if(playerCollision.intersects(asteroidCollision)) {
                p.hp -= 1;
            }
        }

        if(p.hp == 0) {
            System.out.println("Mort!");
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

        // gérer les asteroids
        for(int i = 0; i < ac; i++) {
            ax[i] = r.nextInt(Main.W - a.w + 1);
            ay[i] = -a.h;
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