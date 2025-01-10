package src;

import java.util.List;
import java.util.Map;


public class Player {
    public Room currentRoom; // The current room the player is in (Is just an object of the Room class that is used to hold the information of the rooms)
    public String location; // The current location of the player (Used to look at the rooms description from the current room)
    public Inventory inventory; // The player's inventory
    
   
    // The starting room for the player.
    public Player(Room startingRoom) {
        currentRoom = startingRoom;
        location = "village prison cell"; // Starting location
        inventory = new Inventory(); // Initialize the player's inventory
    }

    
     // Moves the player in the specified direction the direction to move (north, south, east, west)
     
    public void move(String direction) {
        // Get the neighboring rooms in the north-south and west-east directions
        Map<String, List<String>> neighborNS = currentRoom.getNeighborNS();
        Map<String, List<String>> neighborWE = currentRoom.getNeighborWE();
        //makes the nextRoom variable null(empty)
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
            System.out.println("You move " + direction + " to " + location);
            // Print the description of the new room
            System.out.println(currentRoom.getRooms().get(location));
        }

         // Check if the player has reached the village gate and give basic items
         if (location.equals("village gate")) {
            List<String> items = currentRoom.getItems().get(location);
            for (String item : items) {
                inventory.addItem(item);
            }
        }
    }
    
        
         // Returns the current room the player is in.
         //currentRoom is used to get the rooms neighbors
        public Room getCurrentRoom() {
        return currentRoom;
    }

    // Returns the current location of the player.
    //location is used to look at the rooms description
    public String getLocation() {
        return location;
    }

    public Inventory getInventory() {
        return inventory;
    }
}

    
            
        
    

  
