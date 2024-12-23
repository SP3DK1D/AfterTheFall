package src;

import java.util.List;
import java.util.Map;

/**
 * The Main class is the entry point of the game.
 * It initializes the game and starts the main story.
 */
public class Main {
    static Map<String, List<String>> neighbors;
    static String location = Player.location;

    public static void main(String[] args) {
        Room room = new Room();
        neighbors = room.getNeighbors();

        
        printMainTitle();
        printMainStoryStart();
        test(location);
    }

    /**
     * Initializes the starting location of the player.
     */
    

    /**
     * Prints the main title of the game.
     */
    public static void printMainTitle() {
        System.out.println("________________________________________________________________________________________________________________");
        System.out.println();
        System.out.println(" ▄▄▄        █████▒▄▄▄█████▓▓█████  ██▀███        ▄▄▄█████▓ ██░ ██ ▓█████         █████▒▄▄▄       ██▓     ██▓    \r\n" + //
                "▒████▄    ▓██   ▒ ▓  ██▒ ▓▒▓█   ▀ ▓██ ▒ ██▒      ▓  ██▒ ▓▒▓██░ ██▒▓█   ▀       ▓██   ▒▒████▄    ▓██▒    ▓██▒    \r\n" + //
                "▒██  ▀█▄  ▒████ ░ ▒ ▓██░ ▒░▒███   ▓██ ░▄█ ▒      ▒ ▓██░ ▒░▒██▀▀██░▒███         ▒████ ░▒██  ▀█▄  ▒██░    ▒██░    \r\n" + //
                "░██▄▄▄▄██ ░▓█▒  ░ ░ ▓██▓ ░ ▒▓█  ▄ ▒██▀▀█▄        ░ ▓██▓ ░ ░▓█ ░██ ▒▓█  ▄       ░▓█▒  ░░██▄▄▄▄██ ▒██░    ▒██░    \r\n" + //
                " ▓█   ▓██▒░▒█░      ▒██▒ ░ ░▒████▒░██▓ ▒██▒        ▒██▒ ░ ░▓█▒░██▓░▒████▒      ░▒█░    ▓█   ▓██▒░██████▒░██████▒\r\n" + //
                " ▒▒   ▓▒█░ ▒ ░      ▒ ░░   ░░ ▒░ ░░ ▒▓ ░▒▓░        ▒ ░░    ▒ ░░▒░▒░░ ▒░ ░       ▒ ░    ▒▒   ▓▒█░░ ▒░▓  ░░ ▒░▓  ░\r\n" + //
                "  ▒   ▒▒ ░ ░          ░     ░ ░  ░  ░▒ ░ ▒░          ░     ▒ ░▒░ ░ ░ ░  ░       ░       ▒   ▒▒ ░░ ░ ▒  ░░ ░ ▒  ░\r\n" + //
                "  ░   ▒    ░ ░      ░         ░     ░░   ░         ░       ░  ░░ ░   ░          ░ ░     ░   ▒     ░ ░     ░ ░   \r\n" + //
                "      ░  ░                    ░  ░   ░                     ░  ░  ░   ░  ░                   ░  ░    ░  ░    ░  ░\r" + //
                "");//ART by
        System.out.println("________________________________________________________________________________________________________________");
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
        System.out.println("This is their story...");//Text by CHATGDP 4o
    }

    /**
     * Tests the neighbors of the given location.
     * @param location The location to test.
     */
    public static void test(String location) {
        List<String> roomNeighbors = neighbors.get(location);
        if (roomNeighbors != null) {
            // Access individual elements using get method
            System.out.println(roomNeighbors.get(0)); // prints "hallway"
            System.out.println(roomNeighbors.get(1)); // prints "courtyard"
        } else {
            System.out.println("No neighbors found for location: " + location);
        }
    }
}