package src;

import java.util.List;
import java.util.Map;

/**
 * The Player class represents the player in the game.
 * It handles the player's current location, movement, and inventory.
 */
public class Player {
    private Room currentRoom; // The current room object containing all room data
    private String location; // The current location of the player as a string
    private Inventory inventory; // The player's inventory
    private int health;
    private int damage;
    private int currentRoomIndex;

    /**
     * Constructor to initialize the player with a starting room.
     * @param startingRoom The starting room for the player.
     */
    public Player(Room startingRoom) {
        currentRoom = startingRoom;
        location = "village prison cell"; // Starting location
        inventory = new Inventory(); // Initialize the player's inventory
        this.health = 100;
        this.damage = 10;
        this.currentRoomIndex = 0;
    }

    /**
     * Moves the player in the specified direction.
     * @param direction The direction to move (north, south, east, west).
     */
    public void move(String direction) {
        // Get the neighboring rooms in the north-south and west-east directions
        Map<String, List<String>> neighborNS = currentRoom.getNeighborNS();
        Map<String, List<String>> neighborWE = currentRoom.getNeighborWE();

        String nextRoom = null;
        // Determine the next room based on the direction
        switch (direction.toLowerCase()) {
            case "north":
            case "n":
                nextRoom = neighborNS.get(location).get(0);
                break;
            case "south":
            case "s":
                nextRoom = neighborNS.get(location).get(1);
                break;
            case "west":
            case "w":
                nextRoom = neighborWE.get(location).get(0);
                break;
            case "east":
            case "e":
                nextRoom = neighborWE.get(location).get(1);
                break;
            default:
                System.out.println("Invalid direction");
                return;
        }

        // Check if the next room is a valid move
        if (nextRoom.equals("STOP")) {
            System.out.println("You can't go that way. Try again.");
        } else {
            // Update the player's location to the next room
            location = nextRoom;
            System.out.println("You move " + direction + " to " + location);
            // Print the description of the new room
            System.out.println(currentRoom.getRooms().get(location));

            // Check if the player has reached the village gate and give basic items
            if (location.equals("village gate")) {
                List<String> items = currentRoom.getRoomItems().get(location);
                for (String item : items) {
                    inventory.addItem(item);
                }
            }
        }
    }

    /**
     * Returns the current room the player is in.
     * @return The current room.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Returns the current location of the player.
     * @return The current location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the player's inventory.
     * @return The player's inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void advanceRoom() {
        currentRoomIndex++;
        System.out.println("Advanced to room " + currentRoomIndex + " (dev note ignore)");
    }

    public void retreatRoom() {
        currentRoomIndex = Math.max(0, currentRoomIndex - 3);
        System.out.println("Retreated to room " + currentRoomIndex);
    }
}
