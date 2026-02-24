package src;

import java.util.Scanner;

/** Main deterministic game loop: input -> update -> render. */
public class Game {
    private final Scanner scanner = new Scanner(System.in);
    private final WorldMap world = new WorldMap();
    private final Player player = new Player(world.getPlayerStart());
    private final Renderer renderer = new Renderer();
    private final Combat combat = new Combat();

    private boolean running = true;
    private String lastMessage = "Welcome to the ruins.";

    public void run() {
        while (running && player.isAlive()) {
            renderer.render(world, player, lastMessage);
            System.out.print("\nInput > ");
            String input = scanner.nextLine().trim().toLowerCase();
            update(input);
        }

        if (!player.isAlive()) {
            System.out.println("\nYou were defeated. Game over.");
        }
    }

    private void update(String input) {
        if (input.isBlank()) {
            lastMessage = "No input detected.";
            return;
        }

        char key = input.charAt(0);
        switch (key) {
            case 'w' -> attemptMove(0, -1);
            case 'a' -> attemptMove(-1, 0);
            case 's' -> attemptMove(0, 1);
            case 'd' -> attemptMove(1, 0);
            case 'i' -> {
                System.out.println(player.getInventory().render());
                waitEnter();
                lastMessage = "Checked inventory.";
            }
            case 'm' -> {
                lastMessage = "Map is always visible. Use legend above the grid.";
            }
            case 'r' -> {
                player.rest();
                lastMessage = "You rest and recover some HP.";
            }
            case 'q' -> {
                running = false;
                lastMessage = "You ended the run.";
            }
            default -> lastMessage = "Unknown command.";
        }
    }

    private void attemptMove(int dx, int dy) {
        Position next = player.getPosition().translate(dx, dy);
        if (!world.isWalkable(next)) {
            lastMessage = "You bump into a wall.";
            return;
        }

        player.setPosition(next);
        handleTileInteractions();
    }

    private void handleTileInteractions() {
        Position p = player.getPosition();

        ItemEntity item = world.pickupItemAt(p);
        if (item != null) {
            String name = item.getName();
            if (name.equalsIgnoreCase("core stabilizer")) {
                player.setCoreStabilizer(true);
                lastMessage = "You found the Core Stabilizer!";
                return;
            }
            if (name.equalsIgnoreCase("energy crate")) {
                player.getInventory().addConsumable("energy drink", 2);
                lastMessage = "You found energy drinks x2.";
                return;
            }

            player.getInventory().addItem(name);
            player.getInventory().autoEquipIfPossible(name);
            lastMessage = "Picked up " + name + ".";
            return;
        }

        Enemy enemy = world.enemyAt(p);
        if (enemy != null) {
            boolean won = combat.fight(player, enemy, scanner);
            if (won) {
                world.removeEnemy(enemy);
                lastMessage = "Enemy defeated.";
            } else if (player.isAlive()) {
                lastMessage = "You fled combat.";
            } else {
                lastMessage = "You were slain.";
            }
            return;
        }

        Npc npc = world.npcAt(p);
        if (npc != null) {
            openShop(npc.getName());
            return;
        }

        if (world.isReactorDoor(p)) {
            if (player.hasCoreStabilizer()) {
                lastMessage = "You reached the Reactor and stabilized it. You win!";
                running = false;
            } else {
                lastMessage = "The reactor door is sealed. You need the Core Stabilizer.";
            }
            return;
        }

        char tile = world.tileAt(p);
        if (tile == '~') {
            lastMessage = "Water slows your boots.";
        } else if (tile == '+') {
            lastMessage = "You stand at a reinforced door.";
        } else {
            lastMessage = "Moved.";
        }
    }

    private void openShop(String npcName) {
        System.out.println("\nYou meet " + npcName + ". Black Market offers:");
        System.out.println("1) medkit (15)");
        System.out.println("2) energy drink (12)");
        System.out.println("3) bomb (20)");
        System.out.println("4) titan plating (60)");
        System.out.println("5) weapon tune-up +2 ATK (45)");
        System.out.print("Buy # (or Enter to skip): ");
        String choice = scanner.nextLine().trim();
        if (choice.isBlank()) {
            lastMessage = "You leave the shop.";
            return;
        }

        boolean bought = player.getInventory().buyUpgradeForScraps(player, choice);
        lastMessage = bought ? "Purchase successful." : "Purchase failed (invalid choice or not enough scraps).";
    }

    private void waitEnter() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}
