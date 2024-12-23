package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Room {
    private HashMap<String, String> rooms;
    private HashMap<String, List<String>> neighbors;

    public Room() {
        rooms = new HashMap<>();
        neighbors = new HashMap<>();
        initializeRooms();
    }

    private void initializeRooms() {
        rooms.put("Prison cell", "You wake up to the sound of footsteps in your prison cell. You look around your cell to see the rusted bars on the window and peeling paint on the walls, revealing its many years of neglect. Dust settles on the floor, and the faint scent of mold lingers in the stagnant air as the footsteps approach your cell. Half awake, you hear the keys clank agaist your cell door, its a prison guard. \"Get up\" he says as he pulls you to the hallway to your north. Enter ' hallway ' , to continue. ");
        neighbors.put("Prison cell", Arrays.asList("hallway", "courtyard"));

        
        rooms.put("hallway", "You find yourself in a dimly lit hallway, the walls are lined with flickering lights and the floor is covered in a thin layer of dust. The hallway stretches out before you, leading to a set of double doors at the end. To your south is the prison cell you just came from. Enter ' courtyard ' to continue. ");
        neighbors.put("hallway", Arrays.asList("Prison cell", "courtyard"));
    
    }

    public HashMap<String, String> getRooms() {
        return rooms;
    }

    public HashMap<String, List<String>> getNeighbors() {
        return neighbors;
    }
}
