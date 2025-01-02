package src;

import java.util.List;
import java.util.Map;

public class Player {
    private Room currentRoom;
    private String location;

    public Player(Room startingRoom) {
        this.currentRoom = startingRoom;
        this.location = "village prison cell"; // Starting location
    }

    public void move(String direction) {
        Map<String, List<String>> neighborNS = currentRoom.getNeighborNS();
        Map<String, List<String>> neighborWE = currentRoom.getNeighborWE();

        String nextRoom = null;
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

        if (nextRoom.equals("STOP")) {
            System.out.println("You can't go that way. Try again.");
        } else {
            location = nextRoom;
            System.out.println("You move " + direction + " to " + location);
            System.out.println(currentRoom.getRooms().get(location));
        }
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public String getLocation() {
        return location;
    }
}
