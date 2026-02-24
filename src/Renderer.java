package src;

/** Renders world grid and HUD in terminal. */
public class Renderer {

    public void render(WorldMap world, Player player, String message) {
        clearLightweight();

        System.out.println("AFTER THE FALL - ASCII RPG");
        System.out.println("Legend: @ player, # wall, . floor, + door, E enemy, N npc, * item, ~ water");

        for (int y = 0; y < world.getHeight(); y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < world.getWidth(); x++) {
                row.append(world.symbolAt(new Position(x, y), player.getPosition()));
            }
            System.out.println(row);
        }

        System.out.println();
        System.out.println("HP: " + player.getHealth() + "  LVL: " + player.getLevel() + "  XP: " + player.getXp()
                + "  SCRAP: " + player.getScraps() + "  ENERGY: " + player.getEnergy());
        System.out.println(player.objective());
        System.out.println("Commands: W/A/S/D move | I inventory | M map help | R rest | Q quit");

        if (message != null && !message.isBlank()) {
            System.out.println("\n> " + message);
        }
    }

    /** Portable clear strategy: ANSI plus newline fallback. */
    private void clearLightweight() {
        System.out.print("\u001b[H\u001b[2J");
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }
}
