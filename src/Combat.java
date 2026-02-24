package src;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Combat {

    public boolean fight(JComponent parent, Player player, Enemy enemy) {
        while (player.isAlive() && enemy.isAlive()) {
            int score = timingMiniGame(parent, enemy.getAttack());
            if (score >= 85) {
                enemy.takeDamage(player.attackDamage() + 20);
            } else if (score >= 50) {
                enemy.takeDamage(player.attackDamage() + 6);
            } else {
                int incoming = Math.max(1, enemy.getAttack() - player.getDefense());
                player.takeDamage(incoming);
            }
        }
        if (!player.isAlive()) {
            return false;
        }
        player.gainRewards(enemy.getXpReward(), enemy.getScrapReward());
        return true;
    }

    private int timingMiniGame(JComponent parent, int difficulty) {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Strike Timing");
        dialog.setLayout(new BorderLayout());

        JLabel label = new JLabel("Press SPACE when marker is in the green zone", JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        dialog.add(label, BorderLayout.NORTH);

        TimingPanel panel = new TimingPanel(difficulty);
        dialog.add(panel, BorderLayout.CENTER);

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "stop");
        panel.getActionMap().put("stop", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                panel.stopAndScore();
                dialog.dispose();
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        panel.start();
        dialog.setVisible(true);
        panel.stop();
        return panel.getScore();
    }

    private static class TimingPanel extends JPanel {
        private int marker = 0;
        private int dir = 1;
        private int score = 0;
        private final int sweetStart;
        private final int sweetEnd;
        private final Timer timer;

        TimingPanel(int difficulty) {
            setPreferredSize(new Dimension(520, 120));
            int sweetSize = Math.max(18, 90 - difficulty * 4);
            sweetStart = 220;
            sweetEnd = sweetStart + sweetSize;
            timer = new Timer(12, e -> {
                marker += dir * 6;
                if (marker <= 0 || marker >= 500) {
                    dir *= -1;
                }
                repaint();
            });
        }

        void start() {
            timer.start();
        }

        void stop() {
            timer.stop();
        }

        void stopAndScore() {
            int clamped = Math.max(0, Math.min(500, marker));
            if (clamped >= sweetStart && clamped <= sweetEnd) {
                score = 100;
            } else {
                int dist = Math.min(Math.abs(clamped - sweetStart), Math.abs(clamped - sweetEnd));
                score = Math.max(0, 100 - dist / 3);
            }
        }

        int getScore() {
            return score;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(20, 24, 30));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(30, 180, 120));
            g2.fillRoundRect(sweetStart, 45, sweetEnd - sweetStart, 24, 8, 8);

            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(10, 45, 500, 24, 8, 8);

            g2.setColor(new Color(250, 80, 80));
            g2.fillOval(10 + marker - 8, 40, 16, 34);
        }
    }
}
