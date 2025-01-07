package src;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in); // Scanner to read user input

    public static void main(String[] args) {
        printMainStoryStart(); // Print the main story start

        Room room = new Room(); // Initialize the rooms
        Player player = new Player(room); // Create a player and set the starting room
        Combat combat = new Combat(); // Create a combat instance

        System.out.println(room.getRooms().get("village gate")); // Print the description of the starting room

        // Main game loop
        while (true) {
            System.out.println("You are currently in the village gate"); // Print the current location
            System.out.println("Which direction would you like to go? (North, South, East, West) or type 'i' to view your inventory."); // Prompt the player for a direction or inventory command
            String userChoice = scanner.nextLine().toLowerCase(); // Read the player's input

            // Handle inventory commands
            if (userChoice.equals("inventory") || userChoice.equals("i")) {
                player.getInventory().showEquippedItems();
                player.getInventory().showItems();
            } else if (userChoice.startsWith("equip ")) {
                String itemName = userChoice.substring(6);
                player.getInventory().equipItem(itemName);
            } else if (userChoice.equals("exit")) {
                break;
            } else {
                // Handle movement and combat
                String currentRoom = player.getLocation(); // Example starting room
                if (room.getRooms().containsKey(currentRoom)) {
                    room.enterRoom(currentRoom, player.getInventory());
                    Map<String, Combat.Enemy> enemies = room.getEnemiesInRoom(currentRoom);
                    for (Combat.Enemy enemy : enemies.values()) {
                        combat.engageCombat(player, enemy);
                    }
                } else {
                    System.out.println("Invalid direction.");
                }
            }
        }

        scanner.close();
    }

    private static void printMainStoryStart() {
        System.out.println("Ten years ago, the world was forever changed...");
        System.out.println();
        System.out.println("It all began with a mysterious virus outbreak. The virus spread rapidly, turning humans into mindless, flesh-eating zombies.");
        System.out.println("Cities fell, governments collapsed, and civilization as we knew it crumbled.");
        System.out.println();
        System.out.println("Now, ten years later, the world is a desolate wasteland. Small pockets of survivors struggle to stay alive,");
        System.out.println("fighting off the relentless hordes of the undead and scavenging for resources.");
        System.out.println();
        System.out.println("In this harsh new reality, trust is a rare commodity, and danger lurks around every corner.");
        System.out.println("But amidst the darkness, a glimmer of hope remains. A group of survivors has discovered a potential cure,");
        System.out.println("and they embark on a perilous journey to save humanity from the brink of extinction.");
        System.out.println();
        System.out.println("This is their story...");
    }
}