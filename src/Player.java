package src;


import java.util.List;
import java.util.Map;

public class Player {
    public static String location;
    static Map<String, List<String>> neighbors;
    List<String> roomNeighbors = neighbors.get(location);
    public Player() {
    Room room = new Room();
    neighbors = room.getNeighbors();
    location = "Prison cell";//sets the starting location of the player
    }

    public void Movement(String direction) {
      direction = direction.toLowerCase();
        if(direction.equals("north") || direction.equals("n")) {
           if(neighbors.get(location) != null){
               location = roomNeighbors.get(1);
           } else {
               System.out.println("You can't go that way, try another way.");
           }
           
        } else if(direction.equals("south") || direction.equals("s")) {
            if(neighbors.get(location) != null){
                
                location = roomNeighbors.get(0);
            } else {
                System.out.println("You can't go that way, try another way.");
            }
            
        } else if(direction.equals("east") || direction.equals("e")) {
            if(neighbors.get(location) != null){
                location = roomNeighbors.get(2);
            } else {
                System.out.println("You can't go that way, try another way.");
            }
        } else if(direction.equals("west") || direction.equals("w")) {
            if(neighbors.get(location) != null){
                location = roomNeighbors.get(3);
            } else {
                System.out.println("You can't go that way, try another way.");
            }
        } 
    }
}
/* 
* ADD A EAST WEST INDEX HASHMAP AS WELL AS A NORTH SOUTH INDEX HASHMAP
*/
 