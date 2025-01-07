package src;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in); // Scanner to read user input

    public static void main(String[] args) {
        printMainStoryStart(); // Print the main story start

        Room startingRoom = new Room(); // Initialize the starting room
        Player player = new Player(startingRoom); // Create a player and set the starting room

        System.out.println(player.getCurrentRoom().getRooms().get(player.getLocation())); // Print the description of the starting room

        // Main game loop
        while (true) {
            System.out.println("You are currently in the " + player.getLocation()); // Print the current location
            System.out.println("Which direction would you like to go?"); // Prompt the player for a direction or inventory command
            String userChoice = scanner.nextLine().toLowerCase(); // Read the player's input

            // Handle inventory commands
            if (userChoice.equals("inventory") || userChoice.equals("i")) {
                player.getInventory().showInventory();
            } else if (userChoice.startsWith("drop")|| userChoice.equals("d")) {
                String item = userChoice.substring(7);
                player.getInventory().removeItem(item);
            } else if (userChoice.startsWith("equip ")|| userChoice.equals("eq")) {
                String[] parts = userChoice.split(" ", 3);
                if (parts.length == 3) {
                    String slot = parts[1];
                    String item = parts[2];
                    player.getInventory().equipItem(slot, item);
                } else {
                    System.out.println("Invalid equip command. Use 'equip <slot> <item>'.");
                }
            } else {
                player.move(userChoice); // Move the player in the chosen direction
            }
        }
    }

    // Prints the main story start of the game
    public static void printMainStoryStart() {
        System.out.println("________________________________________________________________________________________________________________");
        System.out.println();
        System.out.println("▄▄▄        █████▒▄▄▄█████▓▓█████  ██▀███        ▄▄▄█████▓ ██░ ██ ▓█████         █████▒▄▄▄       ██▓     ██▓    \r\n" + //
                        "▒████▄    ▓██   ▒ ▓  ██▒ ▓▒▓█   ▀ ▓██ ▒ ██▒      ▓  ██▒ ▓▒▓██░ ██▒▓█   ▀       ▓██   ▒▒████▄    ▓██▒    ▓██▒    \r\n" + //
                        "▒██  ▀█▄  ▒████ ░ ▒ ▓██░ ▒░▒███   ▓██ ░▄█ ▒      ▒ ▓██░ ▒░▒██▀▀██░▒███         ▒████ ░▒██  ▀█▄  ▒██░    ▒██░    \r\n" + //
                        "░██▄▄▄▄██ ░▓█▒  ░ ░ ▓██▓ ░ ▒▓█  ▄ ▒██▀▀█▄        ░ ▓██▓ ░ ░▓█ ░██ ▒▓█  ▄       ░▓█▒  ░░██▄▄▄▄██ ▒██░    ▒██░    \r\n" + //
                        " ▓█   ▓██▒░▒█░      ▒██▒ ░ ░▒████▒░██▓ ▒██▒        ▒██▒ ░ ░▓█▒░██▓░▒████▒      ░▒█░    ▓█   ▓██▒░██████▒░██████▒\r\n" + //
                        " ▒▒   ▓▒█░ ▒ ░      ▒ ░░   ░░ ▒░ ░░ ▒▓ ░▒▓░        ▒ ░░    ▒ ░░▒░▒░░ ▒░ ░       ▒ ░    ▒▒   ▓▒█░░ ▒░▓  ░░ ▒░▓  ░\r\n" + //
                        "  ▒   ▒▒ ░ ░          ░     ░ ░  ░  ░▒ ░ ▒░          ░     ▒ ░▒░ ░ ░ ░  ░       ░       ▒   ▒▒ ░░ ░ ▒  ░░ ░ ▒  ░\r\n" + //
                        "  ░   ▒    ░ ░      ░         ░     ░░   ░         ░       ░  ░░ ░   ░          ░ ░     ░   ▒     ░ ░     ░ ░   \r\n" + //
                        "      ░  ░                    ░  ░   ░                     ░  ░  ░   ░  ░                   ░  ░    ░  ░    ░  ░\r"  //
               
                );//ART by patorjk.com
        System.out.println("________________________________________________________________________________________________________________");
        System.out.println();
        System.out.println("--CONTROLS--");
        System.out.println("-To move, type 'north(n)', 'south(s)', 'east(e)', or 'west(w)'.-");
        System.out.println("-To view your inventory, type 'inventory(i)'.-");
        System.out.println("-To drop an item from your inventory, type 'drop(d) <item>'.-");
        System.out.println("-To equip an item, type 'equip(eq) <slot> <item>'.-");
        System.out.println("PRESS ENTER TO BEGIN");
        scanner.nextLine(); //Wait for user to press enter
    
        System.out.println();
        System.out.println("Ten years ago, the world was forever changed...");
        System.out.println();
        System.out.println("It all began with a mysterious virus outbreak. The virus spread rapidly, turning humans into mindless, flesh-eating creatures.");
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
        System.out.println("PRESS ENTER TO CONTINUE");
        scanner.nextLine(); //Wait for user to press enter
        System.out.println();
    }
}