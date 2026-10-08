import javax.swing.JFrame;

class Main {
    public static final int H = 700;
    public static final int W = 1000;
    public String t = "Starship";

    void run() {
        // fenêtre
        JFrame f = new JFrame();
        f.setSize(W, H);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setTitle(t);

        // panneau de jeu
        GamePanel gp = new GamePanel();
        f.add(gp);

        // touches
        gp.requestFocusInWindow();

        f.setVisible(true);
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
