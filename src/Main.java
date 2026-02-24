package src;

import java.util.Scanner;

public class Main {
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Room world = new Room();
        Player player = new Player(world);
        Combat combat = new Combat();

        printIntro();

        boolean reactorWon = false;
        while (player.isAlive() && !reactorWon) {
            showHud(player, world);
            System.out.print("\n" + CYAN + "Command > " + RESET);
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "n", "north", "s", "south", "e", "east", "w", "west" -> {
                    String normalized = normalizeDirection(command);
                    System.out.println(player.move(normalized));
                    System.out.println(world.getDescription(player.getLocation()));
                    player.autoLoot();

                    if (player.getLocation().equals("black market")) {
                        player.shopMenu();
                        System.out.print("Buy item # or press enter to skip: ");
                        String buy = scanner.nextLine().trim();
                        if (!buy.isBlank()) {
                            System.out.println(player.buy(buy) ? "Purchased." : "Not enough scraps / invalid option.");
                        }
                    }

                    String enemy = world.getEncounter(player.getLocation());
                    if (!enemy.isBlank()) {
                        boolean survived = combat.fight(player, enemy, scanner);
                        if (!survived && player.isAlive()) {
                            System.out.println("You retreat and regroup.");
                        }
                    }

                    if (player.getLocation().equals("reactor tower") && player.isAlive() && player.hasCoreStabilizer()) {
                        reactorWon = true;
                    }
                }
                case "inventory", "i" -> player.getInventory().showInventory();
                case "equip" -> {
                    System.out.print("Item name to equip: ");
                    String item = scanner.nextLine().trim();
                    player.getInventory().equip(item);
                }
                case "stats" -> showStats(player);
                case "map", "m" -> System.out.println(world.renderMap(player.getLocation()));
                case "rest", "r" -> player.rest();
                case "objective", "o" -> System.out.println(player.objective());
                case "help" -> printHelp();
                case "quit", "q" -> {
                    System.out.println("You abandon the run... for now.");
                    return;
                }
                default -> System.out.println("Unknown command. Type help.");
            }
        }

        if (player.isAlive() && reactorWon) {
            System.out.println("\n" + GREEN + "You install the Cure Core atop the reactor." + RESET);
            System.out.println(GREEN + "Drones broadcast the antidote into the storm clouds." + RESET);
            System.out.println(GREEN + "Humanity gets another sunrise. You win! 🎉" + RESET);
        } else {
            System.out.println("\nYou fell in the ruins. The city waits for another hero.");
        }
    }

    private static void printIntro() {
        System.out.println(YELLOW + "====================================" + RESET);
        System.out.println(YELLOW + " AFTER THE FALL: OVERDRIVE EDITION" + RESET);
        System.out.println(YELLOW + "====================================" + RESET);
        System.out.println("Stylized terminal RPG with map, quests, loot, skills, leveling, shops, and boss fights.");
        printHelp();
    }

    private static void printHelp() {
        System.out.println("\nCommands: n/s/e/w, map(m), objective(o), rest(r), inventory(i), equip, stats, help, quit");
        System.out.println("Combat: attack(a), skill(k), item(i), flee(f)");
    }

    private static void showHud(Player player, Room world) {
        System.out.println("\n--- " + player.getLocation().toUpperCase() + " ---");
        System.out.println(world.getDescription(player.getLocation()));
        System.out.println("Exits: " + world.exitsText(player.getLocation()));
    }

    private static void showStats(Player player) {
        System.out.println("\n=== STATS ===");
        System.out.println("Level: " + player.getLevel());
        System.out.println("HP: " + player.getHealth());
        System.out.println("Energy: " + player.getEnergy());
        System.out.println("XP: " + player.getXp());
        System.out.println("Scraps: " + player.getScraps());
    }

    private static String normalizeDirection(String direction) {
        return switch (direction) {
            case "n" -> "north";
            case "s" -> "south";
            case "e" -> "east";
            case "w" -> "west";
            default -> direction;
        };
    }
}
