package src;

import java.util.HashMap;
import java.util.Map;

public class Player {
    public static String location; // players location at all times
    private static Map<String, Room> rooms = new HashMap<>(); // map to hold locations and Room objects
    
    public Player() {
    Player player = new Player(); 
    Player.location = "prisonCell";//sets player starting location
    }
     
/** Movement
 * This function allows the player to move between rooms by entering south(s), north(n), east(e), or west(w)
 * and giving a "You can't go that way, try another way." message if the player tries to move in a direction that is not possible
 * @param direction
 */
    public void Movement(String direction) {
      direction = direction.toLowerCase();//sets the direction input to lower case to allow for differing inputs
      Room current = rooms.get(location);//gets the current room
      String[] temp = current.getNeighbors();//gives a connection between the directions and the rooms
      
       // if the player enters a valid direction it will move the player to the new location
       if(direction.equals("north") || direction.equals("n")) {
           if(!temp[0].equals("^")){

           }
           else{
               System.out.println("You can't go that way, try another way.");
           }
           
        } else if(direction.equals("south") || direction.equals("s")) {
            if(!temp[1].equals("^")){
                location = temp[1];
            }
            else{
                System.out.println("You can't go that way, try another way.");
            }
        } else if(direction.equals("east") || direction.equals("e")) {
            if(!temp[2].equals("^")){
                location = temp[2];
            }
            else{
                System.out.println("You can't go that way, try another way.");
            }
        } else if(direction.equals("west") || direction.equals("w")) {
            if(!temp[3].equals("^")){
                location = temp[3];
            }
            else{
                System.out.println("You can't go that way, try another way.");
            }
        } else { // if the player enters a direction that is not valid
			System.out.println("Sorry, not valid direction, try again!");
		}
        } 
    }
}
