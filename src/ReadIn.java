package src;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.io.IOException;
import java.util.*;

public class ReadIn {
    public static HashMap<String, Room> createRooms() {

		BufferedReader reader = new BufferedReader(new FileReader("Rooms.txt"));
        String line = reader.readLine();
        HashMap<String, Room> rooms = new HashMap<String, Room>();

        while (line != null) { // while we can still read from file
            String name = line;

            line = reader.readLine();

            String[] neighbors = line.split(",");
            for (int i = 0; i < neighbors.length; i++) { // travers
                                                            // neighbors
                // trim each name
                neighbors[i] = neighbors[i].trim();
            }

            line = reader.readLine();
            String description = "";

            while (!line.equals("END")) { // while reader has not hit "END"
                // add each line to overall string for description
                description = description + line + '\n';
                line = reader.readLine();
            }

            // put new room object and rooms name in HashMap
            rooms.put(name, new Room(name, description, neighbors, items));

            line = reader.readLine(); // move line to beginning of next
                                        // section of text
                                        System.out.println(name);
                                        System.out.println(description);
                                        
        }
}
public String getName() {
    return name_;
}
}

