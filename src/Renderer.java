package src;

import java.util.HashSet;
import java.util.Set;

/** Viewport/camera renderer with layered drawing and HUD/debug panels. */
public class Renderer {
    private static final int VIEW_W = 60;
    private static final int VIEW_H = 25;

    private final TerminalController terminal;

    public Renderer(TerminalController terminal) {
        this.terminal = terminal;
    }

    public void render(WorldMap world, Player player, String message, boolean debug, long frameMs, String inputMode,
            Set<Position> flashes) {
        clear();

        int camX = clamp(player.getPosition().getX() - VIEW_W / 2, 0, Math.max(0, world.getWidth() - VIEW_W));
        int camY = clamp(player.getPosition().getY() - VIEW_H / 2, 0, Math.max(0, world.getHeight() - VIEW_H));

        System.out.println("AFTER THE FALL // ASCII SURVIVAL");
        System.out.println("Legend @ # . + E N * ~");

        Set<Position> enemyPos = new HashSet<>();
        for (Enemy e : world.getEnemies()) {
            enemyPos.add(e.getPosition());
        }
        Set<Position> itemPos = new HashSet<>();
        for (ItemEntity item : world.getItems()) {
            itemPos.add(item.getPosition());
        }
        Set<Position> npcPos = new HashSet<>();
        for (Npc npc : world.getNpcs()) {
            npcPos.add(npc.getPosition());
        }

        for (int y = 0; y < VIEW_H; y++) {
            StringBuilder row = new StringBuilder();
            int worldY = camY + y;
            for (int x = 0; x < VIEW_W; x++) {
                int worldX = camX + x;
                Position p = new Position(worldX, worldY);

                // terrain layer
                char symbol = world.inBounds(p) ? world.tileAt(p) : ' ';

                // entity layer
                if (itemPos.contains(p)) symbol = '*';
                if (npcPos.contains(p)) symbol = 'N';
                if (enemyPos.contains(p)) symbol = 'E';
                if (player.getPosition().equals(p)) symbol = '@';

                // effects layer
                if (flashes.contains(p)) symbol = '!';
                row.append(symbol);
            }
            System.out.println(row);
        }

        System.out.println("HP:" + player.getHealth() + " LVL:" + player.getLevel() + " XP:" + player.getXp()
                + " SCRAP:" + player.getScraps() + " ENERGY:" + player.getEnergy());
        System.out.println("Enemies Left: " + world.getEnemies().size() + "  Objective: Clear all enemies.");
        System.out.println("Hotkeys: WASD move | I inventory | R rest | ` debug | Q quit");
        if (message != null && !message.isBlank()) {
            System.out.println("> " + message);
        }
        if (debug) {
            System.out.println("[DEBUG] seed=" + world.getSeed() + " player=" + player.getPosition().getX() + ","
                    + player.getPosition().getY() + " enemies=" + world.getEnemies().size() + " frameMs=" + frameMs
                    + " inputMode=" + inputMode);
        }
    }

    private void clear() {
        if (terminal.isAnsiSupported()) {
            System.out.print("\u001b[H\u001b[2J");
            System.out.flush();
        } else {
            for (int i = 0; i < 40; i++) {
                System.out.println();
            }
        }
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
