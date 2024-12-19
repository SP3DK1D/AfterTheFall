package src;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.HashMap;

public class ReadIn {
    public static void createRooms() {
        BufferedReader reader = new BufferedReader(new FileReader("Rooms.txt"));
        String textLine = reader.readLine();
        HashMap<String, Room> rooms = new HashMap<>();

        while (textLine != null) {
            String[] roomInfo = textLine.split(",");
            String roomName = textLine;
            textLine = reader.readLine();
            String[] roomNeighbors = textLine.split(",");
           
            for (int i = 0; i < roomNeighbors.length; i++) { // travers
              
                 // trim each name
                   roomNeighbors[i] = roomNeighbors[i].trim();
}
            textLine = reader.readLine();
            String roomDescription = textLine;

            while (!textLine.equals("STOP")) { // while reader has not hit "STOP"
					// add each line to overall string for description
					roomDescription = roomDescription + textLine + '\n';
					textLine = reader.readLine();
				}
            // put new room object and rooms name in HashMap
           
            Room room = new Room(roomDescription, roomNeighbors, roomName);
            rooms.put(roomName, room);
            
        }
        
        
    }

		


}

