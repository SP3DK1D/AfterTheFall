package src;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/** Main deterministic game loop: input -> update -> render. */
public class Game {
    private final Random random;
    private final TerminalController terminal = new TerminalController();
    private final InputHandler input;
    private final WorldMap world;
    private final Player player;
    private final Renderer renderer;
    private final Combat combat = new Combat();

    private boolean running = true;
    private boolean debugOverlay = false;
    private String lastMessage = "Welcome to the procedural ruins.";
    private final Set<Position> flashes = new HashSet<>();
    private long lastFrameMs = 0;
    private int tick = 0;

    public Game(long seed, boolean requestInstantMode) {
        this.random = new Random(seed);
        this.world = new WorldMap(120, 60, seed);
        this.player = new Player(world.getPlayerStart());
        boolean instant = requestInstantMode && terminal.enableRawMode();
        this.input = new InputHandler(instant);
        this.renderer = new Renderer(terminal);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            input.stop();
            terminal.restore();
        }));
    }

    public void run() {
        input.start();

        while (running && player.isAlive()) {
            long frameStart = System.currentTimeMillis();

            renderer.render(world, player, lastMessage, debugOverlay, lastFrameMs,
                    input.isInstantMode() ? "raw" : "enter", flashes);

            Character key = input.pollKey();
            if (key != null) {
                update(key);
            }

            if (tick % 3 == 0) {
                updateEnemies();
            }
            tick++;

            if (world.getEnemies().isEmpty()) {
                running = false;
                lastMessage = "All enemies cleared. Area secured. You win!";
            }

            flashes.clear();
            sleep(input.isInstantMode() ? 65 : 0);
            lastFrameMs = System.currentTimeMillis() - frameStart;
        }

        input.stop();
        terminal.restore();

        if (!player.isAlive()) {
            System.out.println("\nYou were defeated. Game over.");
        } else {
            System.out.println("\n" + lastMessage);
        }
    }

    private void update(char key) {
        switch (Character.toLowerCase(key)) {
            case 'w' -> attemptMove(0, -1);
            case 'a' -> attemptMove(-1, 0);
            case 's' -> attemptMove(0, 1);
            case 'd' -> attemptMove(1, 0);
            case 'i' -> {
                System.out.println(player.getInventory().render());
                if (!input.isInstantMode()) {
                    System.out.print("Press Enter to continue...");
                    input.readLineBlocking();
                }
                lastMessage = "Checked inventory.";
            }
            case 'r' -> {
                player.rest();
                lastMessage = "You rest and recover HP.";
            }
            case 'q' -> {
                running = false;
                lastMessage = "You ended the run.";
            }
            case '`' -> {
                debugOverlay = !debugOverlay;
                lastMessage = "Debug overlay " + (debugOverlay ? "ON" : "OFF") + ".";
            }
            default -> {
                if (!input.isInstantMode()) {
                    lastMessage = "Unknown command.";
                }
            }
        }
    }

    private void attemptMove(int dx, int dy) {
        Position next = player.getPosition().translate(dx, dy);
        if (!world.isWalkable(next)) {
            lastMessage = "Blocked.";
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
                lastMessage = "Core Stabilizer secured.";
                return;
            }
            if (name.equalsIgnoreCase("energy crate")) {
                player.getInventory().addConsumable("energy drink", 2);
                lastMessage = "Energy drinks x2 found.";
                return;
            }
            if (name.equalsIgnoreCase("medkit crate")) {
                player.getInventory().addConsumable("medkit", 2);
                lastMessage = "Medkits x2 found.";
                return;
            }
            if (name.equalsIgnoreCase("scrap")) {
                player.addScraps(20);
                lastMessage = "You scavenged 20 scraps.";
                return;
            }
            player.getInventory().addItem(name);
            player.getInventory().autoEquipIfPossible(name);
            lastMessage = "Picked up " + name + ".";
            return;
        }

        Enemy enemy = world.enemyAt(p);
        if (enemy != null) {
            boolean won = combat.fight(player, enemy, input);
            if (won) {
                world.getEnemies().remove(enemy);
                flashes.add(p);
                lastMessage = "Enemy defeated.";
            } else if (player.isAlive()) {
                lastMessage = "You escaped/fumbled the fight.";
            } else {
                lastMessage = "You were slain.";
            }
            return;
        }

        Npc npc = world.npcAt(p);
        if (npc != null) {
            openShop();
            return;
        }

        if (world.tileAt(p) == '+') {
            lastMessage = player.hasCoreStabilizer() ? "Door unlocked, but enemies remain." : "Goal door found.";
            return;
        }

        lastMessage = world.tileAt(p) == '~' ? "Splash... moving through water." : "Moved.";
    }

    private void updateEnemies() {
        for (Enemy enemy : world.getEnemies()) {
            Position current = enemy.getPosition();
            int dx = player.getPosition().getX() - current.getX();
            int dy = player.getPosition().getY() - current.getY();
            int dist = Math.abs(dx) + Math.abs(dy);

            Position next;
            if (dist <= enemy.getDetectionRadius()) {
                // greedy chase
                int stepX = Integer.compare(dx, 0);
                int stepY = Integer.compare(dy, 0);
                Position horizontal = current.translate(stepX, 0);
                Position vertical = current.translate(0, stepY);
                next = tryEnemyStep(horizontal, vertical, current);
            } else {
                // wander
                int dir = random.nextInt(4);
                next = switch (dir) {
                    case 0 -> current.translate(1, 0);
                    case 1 -> current.translate(-1, 0);
                    case 2 -> current.translate(0, 1);
                    default -> current.translate(0, -1);
                };
                if (!canEnemyMove(next)) {
                    next = current;
                }
            }

            if (next.equals(player.getPosition())) {
                boolean won = combat.fight(player, enemy, input);
                if (won) {
                    flashes.add(next);
                }
            } else if (canEnemyMove(next)) {
                enemy.setPosition(next);
            }
        }
        world.getEnemies().removeIf(e -> !e.isAlive());
    }

    private Position tryEnemyStep(Position horizontal, Position vertical, Position fallback) {
        if (canEnemyMove(horizontal)) {
            return horizontal;
        }
        if (canEnemyMove(vertical)) {
            return vertical;
        }
        return fallback;
    }

    private boolean canEnemyMove(Position p) {
        if (!world.isWalkable(p)) {
            return false;
        }
        if (world.npcAt(p) != null) {
            return false;
        }
        return world.enemyAt(p) == null;
    }

    private void openShop() {
        System.out.println("\nShop: 1)medkit 2)energy drink 3)bomb 4)titan plating 5)tune-up");
        System.out.print("Buy # (Enter skip): ");
        String choice = input.readLineBlocking().trim();
        if (choice.isBlank()) {
            lastMessage = "You leave the trader.";
            return;
        }
        boolean bought = player.getInventory().buyUpgradeForScraps(player, choice);
        lastMessage = bought ? "Purchase complete." : "Could not buy.";
    }

    private void sleep(long ms) {
        if (ms <= 0) {
            return;
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
