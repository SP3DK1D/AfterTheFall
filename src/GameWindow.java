package src;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameWindow extends JFrame {
    public GameWindow(long seed) {
        setTitle("After The Fall - Neon Drift");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel(seed);
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
    }

    public static void launch(long seed) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow(seed);
            window.setVisible(true);
        });
    }
}
