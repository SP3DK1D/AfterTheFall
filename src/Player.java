package src;

public class Player {
    public static String location;
    
    public Player() {
     location = "Prison cell";
    }

    public void Movement(String direction) {
      direction = direction.toLowerCase();
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
