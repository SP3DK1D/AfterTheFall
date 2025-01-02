package src;

import java.util.Scanner;

/**
 * The Main class is the entry point of the game.
 * It initializes the game and starts the main story.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printMainStoryStart();

        Room startingRoom = new Room(); // Initialize with the starting room
        Player player = new Player(startingRoom);

        while (true) {
            System.out.println("You are currently in the " + player.getLocation());
            System.out.println("Which direction would you like to go? (North, South, East, West)");
            String userChoice = scanner.nextLine();
            player.move(userChoice);
        }
    }

    /**
     * Prints the main story start of the game.
     */
    public static void printMainStoryStart() {
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
    }
}