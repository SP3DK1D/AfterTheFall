package src;

import java.util.List;
import java.util.Map;

public class Player {
    public String location;
    public Room currentRoom;
    public Combat combat;
    public Inventory inventory;

    public Player(Room currentRoom) {
        this.currentRoom = currentRoom;
        location = "village prison cell"; // starting room
        combat = new Combat(); // Combat class
        inventory = new Inventory(); //Inventory class 
    }

    public void move(String direction) {
        Map<String, List<String>> neighborNS = currentRoom.getNeighborNS();
        Map<String, List<String>> neighborWE = currentRoom.getNeighborWE();
        // makes the nextRoom variable null(empty)
        String nextRoom = null;
        // Determine the next room based on the direction
        switch (direction.toLowerCase()) {
            case "north":
            case "n":
                direction = "north";
                nextRoom = neighborNS.get(location).get(0);
                break;
            case "south":
            case "s":
                direction = "south";
                nextRoom = neighborNS.get(location).get(1);
                break;
            case "west":
            case "w":
                direction = "west";
                nextRoom = neighborWE.get(location).get(0);
                break;
            case "east":
            case "e":
                direction = "east";
                nextRoom = neighborWE.get(location).get(1);
                break;
            default:
                System.out.println("Invalid direction");
                return;
        }

        // Checks if there is a room in the specified direction
        if (nextRoom.equals("STOP")) {
            System.out.println("You can't go that way. Try again.");
        } else {
            // Update the player's location to the next room
            location = nextRoom;
            System.out.println();
            System.out.println("You move " + direction + " to " + location);
            // Print the description of the new room
            System.out.println();
            System.out.println(currentRoom.getRooms().get(location));

            // Check for encounter in the new room
            Map<String, List<String>> roomEncounters = currentRoom.getRoomEncounter();
            List<String> encounters = roomEncounters.get(location);
            if (encounters != null && !encounters.isEmpty()) {
                for (String encounter : encounters) {
                    combat.checkRoomForEncounter(encounter);
                }
            }
        }

        // Check if the player has reached the village gate and give basic items
        if (location.equals("village gate")) {
            List<String> items = currentRoom.getItems().get(location);
            for (String item : items) {
                inventory.addItem(item);
            }
        }

        // Check if the player has reached the bad ending and reset location
        if (location.equals("bad ending")) {
            System.out.println("You have reached the bad ending. Resetting location to village prison cell.");
            location = "village prison cell";
        }
    }

    // Returns the current room the player is in.
    public Room getCurrentRoom() {
        return currentRoom;
    }

    // Returns the current location of the player.
    public String getLocation() {
        return location;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
