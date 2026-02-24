package src;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

public class GamePanel extends JPanel {
    private static final int TILE = 20;
    private static final int VIEW_W = 48;
    private static final int VIEW_H = 30;
    private static final int SIDE_W = 300;

    private final WorldMap world;
    private final Player player;
    private final Combat combat = new Combat();
    private final Timer loop;

    private final Set<Integer> held = new HashSet<>();
    private String message = "Explore, loot, and clear all enemies.";
    private int flashTicks = 0;
    private Position flashPos;

    public GamePanel(long seed) {
        this.world = new WorldMap(140, 90, seed);
        this.player = new Player(world.getPlayerStart());

        setFocusable(true);
        setDoubleBuffered(true);
        setBackground(new Color(8, 10, 14));

        setupInput();

        loop = new Timer(16, this::update);
        loop.start();
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        return new java.awt.Dimension(VIEW_W * TILE + SIDE_W, VIEW_H * TILE);
    }

    private void setupInput() {
        bind("pressed W", () -> held.add(java.awt.event.KeyEvent.VK_W));
        bind("pressed A", () -> held.add(java.awt.event.KeyEvent.VK_A));
        bind("pressed S", () -> held.add(java.awt.event.KeyEvent.VK_S));
        bind("pressed D", () -> held.add(java.awt.event.KeyEvent.VK_D));
        bind("released W", () -> held.remove(java.awt.event.KeyEvent.VK_W));
        bind("released A", () -> held.remove(java.awt.event.KeyEvent.VK_A));
        bind("released S", () -> held.remove(java.awt.event.KeyEvent.VK_S));
        bind("released D", () -> held.remove(java.awt.event.KeyEvent.VK_D));

        bind("pressed I", () -> JOptionPane.showMessageDialog(this, player.getInventory().render(), "Inventory", JOptionPane.INFORMATION_MESSAGE));
        bind("pressed R", () -> {
            player.rest();
            message = "You take a short breather.";
        });
        bind("pressed Q", () -> {
            int choice = JOptionPane.showConfirmDialog(this, "Quit run?", "Quit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void bind(String keystroke, Runnable action) {
        String key = keystroke + Math.random();
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke(keystroke), key);
        getActionMap().put(key, new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void update(ActionEvent e) {
        int dx = 0;
        int dy = 0;
        if (held.contains(java.awt.event.KeyEvent.VK_W)) dy = -1;
        if (held.contains(java.awt.event.KeyEvent.VK_S)) dy = 1;
        if (held.contains(java.awt.event.KeyEvent.VK_A)) dx = -1;
        if (held.contains(java.awt.event.KeyEvent.VK_D)) dx = 1;

        if (dx != 0 || dy != 0) {
            tryMove(dx, dy);
        }

        updateEnemyAI();

        if (flashTicks > 0) {
            flashTicks--;
        }

        if (world.getEnemies().isEmpty()) {
            loop.stop();
            JOptionPane.showMessageDialog(this, "Area cleared! You win.");
            System.exit(0);
        }

        if (!player.isAlive()) {
            loop.stop();
            JOptionPane.showMessageDialog(this, "You were defeated.");
            System.exit(0);
        }

        repaint();
    }

    private void tryMove(int dx, int dy) {
        Position next = player.getPosition().translate(dx, dy);
        if (!world.isWalkable(next)) {
            return;
        }

        player.setPosition(next);
        ItemEntity item = world.pickupItemAt(next);
        if (item != null) {
            String name = item.getName();
            if (name.equalsIgnoreCase("medkit crate")) {
                player.getInventory().addConsumable("medkit", 2);
                message = "Found medkits x2";
            } else if (name.equalsIgnoreCase("energy crate")) {
                player.getInventory().addConsumable("energy drink", 2);
                message = "Found energy drinks x2";
            } else if (name.equalsIgnoreCase("scrap")) {
                player.addScraps(20);
                message = "Scavenged 20 scraps";
            } else {
                player.getInventory().addItem(name);
                player.getInventory().autoEquipIfPossible(name);
                message = "Looted " + name;
            }
        }

        Enemy enemy = world.enemyAt(next);
        if (enemy != null) {
            boolean won = combat.fight(this, player, enemy);
            if (won) {
                world.getEnemies().remove(enemy);
                flashPos = next;
                flashTicks = 10;
                message = "Defeated " + enemy.getName();
            }
        }

        if (world.npcAt(next) != null) {
            openShop();
        }
    }

    private void openShop() {
        String choice = JOptionPane.showInputDialog(this,
                "Shop\n1) medkit 15\n2) energy drink 12\n3) bomb 20\n4) titan plating 60\n5) weapon tune-up 45\nEnter number:");
        if (choice == null || choice.isBlank()) {
            return;
        }
        boolean bought = player.getInventory().buyUpgradeForScraps(player, choice.trim());
        message = bought ? "Purchase complete" : "Cannot buy";
    }

    private void updateEnemyAI() {
        for (Enemy enemy : world.getEnemies()) {
            Position current = enemy.getPosition();
            int dx = player.getPosition().getX() - current.getX();
            int dy = player.getPosition().getY() - current.getY();
            int distance = Math.abs(dx) + Math.abs(dy);

            Position next = current;
            if (distance <= enemy.getDetectionRadius()) {
                Position h = current.translate(Integer.compare(dx, 0), 0);
                Position v = current.translate(0, Integer.compare(dy, 0));
                if (canEnemyMove(h)) next = h;
                else if (canEnemyMove(v)) next = v;
            }

            if (next.equals(player.getPosition())) {
                int incoming = Math.max(1, enemy.getAttack() - player.getDefense());
                player.takeDamage(incoming);
                message = enemy.getName() + " hit you for " + incoming;
            } else if (canEnemyMove(next)) {
                enemy.setPosition(next);
            }
        }
    }

    private boolean canEnemyMove(Position p) {
        return world.isWalkable(p) && world.enemyAt(p) == null && !player.getPosition().equals(p);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int camX = Math.max(0, Math.min(world.getWidth() - VIEW_W, player.getPosition().getX() - VIEW_W / 2));
        int camY = Math.max(0, Math.min(world.getHeight() - VIEW_H, player.getPosition().getY() - VIEW_H / 2));

        for (int y = 0; y < VIEW_H; y++) {
            for (int x = 0; x < VIEW_W; x++) {
                Position p = new Position(camX + x, camY + y);
                int px = x * TILE;
                int py = y * TILE;

                int tile = world.tileAt(p);
                if (tile == WorldMap.WALL) {
                    g2.setColor(new Color(38, 44, 62));
                } else if (tile == WorldMap.WATER) {
                    g2.setColor(new Color(30, 92, 170));
                } else {
                    g2.setColor(new Color(24, 28, 35));
                }
                g2.fillRect(px, py, TILE, TILE);

                g2.setColor(new Color(0, 0, 0, 35));
                g2.drawRect(px, py, TILE, TILE);
            }
        }

        for (ItemEntity item : world.getItems()) drawEntity(g2, item.getPosition(), camX, camY, new Color(255, 210, 84), '*');
        for (Npc npc : world.getNpcs()) drawEntity(g2, npc.getPosition(), camX, camY, new Color(92, 222, 170), 'N');
        for (Enemy enemy : world.getEnemies()) drawEntity(g2, enemy.getPosition(), camX, camY, new Color(255, 96, 120), 'E');
        drawEntity(g2, player.getPosition(), camX, camY, new Color(160, 220, 255), '@');

        if (flashTicks > 0 && flashPos != null) {
            int sx = (flashPos.getX() - camX) * TILE;
            int sy = (flashPos.getY() - camY) * TILE;
            g2.setColor(new Color(255, 240, 120, 120));
            g2.fillOval(sx - 8, sy - 8, TILE + 16, TILE + 16);
        }

        drawHud(g2);
    }

    private void drawEntity(Graphics2D g2, Position p, int camX, int camY, Color color, char glyph) {
        int sx = (p.getX() - camX) * TILE;
        int sy = (p.getY() - camY) * TILE;
        if (sx < 0 || sy < 0 || sx >= VIEW_W * TILE || sy >= VIEW_H * TILE) {
            return;
        }
        g2.setColor(color);
        g2.fillOval(sx + 3, sy + 3, TILE - 6, TILE - 6);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2.drawString(String.valueOf(glyph), sx + 7, sy + 14);
    }

    private void drawHud(Graphics2D g2) {
        int x = VIEW_W * TILE;
        int w = SIDE_W;
        int h = VIEW_H * TILE;

        GradientPaint gp = new GradientPaint(x, 0, new Color(16, 18, 24), x, h, new Color(11, 13, 18));
        g2.setPaint(gp);
        g2.fillRect(x, 0, w, h);

        g2.setColor(new Color(90, 120, 180));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(x + 1, 0, x + 1, h);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.drawString("AFTER THE FALL", x + 20, 40);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.drawString("HP: " + player.getHealth(), x + 20, 90);
        g2.drawString("Level: " + player.getLevel(), x + 20, 120);
        g2.drawString("XP: " + player.getXp(), x + 20, 150);
        g2.drawString("Scraps: " + player.getScraps(), x + 20, 180);
        g2.drawString("Energy: " + player.getEnergy(), x + 20, 210);
        g2.drawString("Enemies Left: " + world.getEnemies().size(), x + 20, 250);

        g2.setColor(new Color(180, 220, 255));
        g2.drawString("Controls", x + 20, 300);
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("WASD Move", x + 20, 330);
        g2.drawString("I Inventory", x + 20, 355);
        g2.drawString("R Rest", x + 20, 380);
        g2.drawString("Q Quit", x + 20, 405);

        g2.setColor(new Color(210, 220, 240));
        g2.drawString("Seed: " + world.getSeed(), x + 20, 450);

        g2.setColor(new Color(255, 220, 120));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g2.drawString(message, x + 20, h - 24);
    }
}
