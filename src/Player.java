package src;

import java.util.List;
import java.util.Map;

/**
 * The Player class represents the player in the game.
 * It handles the player's current location, movement, and inventory.
 */
public class Player {
    public Room currentRoom; // The current room object containing all room data
    public String location; // The current location of the player as a string
    public Inventory inventory; // The player's inventory
    public Combat combat; // The combat system

    /**
     * Constructor to initialize the player with a starting room.
     * @param startingRoom The starting room for the player.
     */
    public Player(Room startingRoom) {
        currentRoom = startingRoom;
        location = "village prison cell"; // Starting location
        inventory = new Inventory(); // Initialize the player's inventory
        combat = new Combat(); // Initialize the combat system
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

            if (location.equals("forest path")) {
                List<String> encounters = currentRoom.getEncounters().get(location);
                for (String encounters : encounters) {
                    combat.combatPVE(encounters);
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
}
