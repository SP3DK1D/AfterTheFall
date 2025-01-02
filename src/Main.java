package src;

import java.util.Scanner;


public class Main {
    // Scanner object to read user input
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Print the main story start
        printMainStoryStart();

        // Initialize the starting room
        Room startingRoom = new Room();
        // Create a player and set the starting room
        Player player = new Player(startingRoom);

        // Print the description of the starting room(fixes the bug with it not printing the description of the starting room-lukas)
        System.out.println(player.getCurrentRoom().getRooms().get(player.getLocation()));

        // Main game loop
        while (true) {
            // Prints the updated loctaion after each move
            System.out.println("You are currently in the " + player.getLocation());
            // Asks the player for input
            System.out.println("Which direction would you like to go? (North, South, East, West)");
            // Read the player's input
            String userChoice = scanner.nextLine();
            // Sends the userChoice to the Player.java file to move the player
            player.move(userChoice);
        }
    }

    
     // Prints the main story start of the game and the starting title.
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
        System.out.println("After The Fall");
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
        System.out.println();
    }
}