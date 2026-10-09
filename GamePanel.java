import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Rectangle;

public class GamePanel extends JPanel implements KeyListener {
    boolean up = false, right = false, left = false, down = false;
    private Player p = new Player();
    long lastTime;
    int sc = 80; // nombre d'étoiles en arrière plan
    int ac = 10; // nombre d'asteroids
    double[] sx = new double[sc];
    double[] sy = new double[sc];
    ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
    Random r = new Random();
    double sp = 240; // vitesse des étoiles => px/s
    double ap = 420; // vitesse des asteroids => px/s
    double spawnT = 0;
    double spawnI = 2; // interval entre deux apparitions d'asteroids


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
        for(Asteroid a : asteroids) {
            g2.setColor(a.c);
            g2.fillOval(
                (int) a.x,
                (int) a.y,
                a.w,
                a.h
            );
        }

        // afficher le joueur
        g2.setColor(p.c);
        g2.fill(new Ellipse2D.Double(p.x, p.y, p.w, p.h));
    }

    // créer un asteroid
    private void spawnAsteroid() {
        Asteroid a = new Asteroid();

        a.x = r.nextInt(getWidth() - a.w + 1);
        a.y = -a.h;

        asteroids.add(a);
    }

    // gérer les déplacements

    private void updateGame(double dt) {
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }

        spawnT += dt;

        if(spawnT >= spawnI) {
            spawnAsteroid();
            spawnT -= spawnI;
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

        for(int i = asteroids.size() - 1; i >= 0; i--) {
            Asteroid a = asteroids.get(i);
            a.y += ap * dt;
            if (a.y >= getHeight()) {
                asteroids.remove(i);
            }
        }

        // collisions
        Rectangle playerCollision = new Rectangle(
            (int) p.x, (int) p.y, p.w, p.h
        );

        for (Asteroid a : asteroids) {
            Rectangle asteroidCollision = new Rectangle(
                (int) a.x, (int) a.y, a.w, a.h
            );

            boolean collision = playerCollision.intersects(asteroidCollision);

            if (collision && !a.coll) {
                p.hp -= 1;
                System.out.println("-1 hp");
            }

            a.coll = collision;
        }

        // game over
        if(p.hp <= 0) {
            System.exit(0);
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