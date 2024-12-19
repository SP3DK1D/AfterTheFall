package src;

public class Player {
    public static String location;//players location at all times
    
    public Player() {
    Player player = new Player();//adds player object
    Player.location = "prisonCell";//sets player starting location
    }
     
/** Movement
 * This function allows the player to move between rooms by entering south(s), north(n), east(e), or west(w)
 * and giving a "You can't go that way, try another way." message if the player tries to move in a direction that is not possible
 * @param direction
 */
    public void Movement(String direction) {
      direction = direction.toLowerCase();//sets the direction input to lower case to allow for differing inputs
        if(direction.equals("north") || direction.equals("n")) {
           if(){

           }
           else{
               System.out.println("You can't go that way, try another way.");
           }
           
        } else if(direction.equals("south") || direction.equals("s")) {
            if(){

            }
            else{
                System.out.println("You can't go that way, try another way.");
            }
        } else if(direction.equals("east") || direction.equals("e")) {
            if(){

            }
            else{
                System.out.println("You can't go that way, try another way.");
            }
        } else if(direction.equals("west") || direction.equals("w")) {
            if(){

            }
            else{
                System.out.println("You can't go that way, try another way.");
            }
        } 
    }
}
