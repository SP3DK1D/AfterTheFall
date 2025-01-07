package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private Map<String, String> rooms; // Room descriptions
    private Map<String, List<String>> neighborNS; // North-South neighbors
    private Map<String, List<String>> neighborWE; // West-East neighbors
    private Map<String, List<String>> roomItems; // Items in each room

    // Constructor to initialize rooms and neighbors
    public Room() {
        rooms = new HashMap<>();
        neighborNS = new HashMap<>();
        neighborWE = new HashMap<>();
        roomItems = new HashMap<>();
        createRooms();
    }

    // Initialize rooms and neighbors
    private void createRooms() {
        // Village prison celll
        rooms.put("village prison cell", "You wake up to the sound of footsteps outside your cell in the village prison.\n"
                + "You look around to see the wooden bars on the window and the rough stone walls.\n"
                + "Dust settles on the floor, and the faint scent of dampness lingers in the air.\n"
                + "A village guard approaches your cell. \"Get up,\" he says, \"The village elder wants to see you.\"\n"
                + "He leads you to the village square to the north.");
        neighborNS.put("village prison cell", Arrays.asList("village square", "STOP"));
        neighborWE.put("village prison cell", Arrays.asList("STOP", "STOP"));
        roomItems.put("village prison cell", Arrays.asList());

        // Village square
        rooms.put("village square", "You find yourself in the village square, a bustling area with villagers going about their daily routines.\n"
                + "The village elder stands in the center, waiting for you. He explains that the village is in dire need of a cure for a mysterious illness,\n"
                + "and you have been chosen to embark on this dangerous journey. To the west is the village gate, and to the east is the supply hut.");
        neighborNS.put("village square", Arrays.asList("STOP", "village prison cell"));
        neighborWE.put("village square", Arrays.asList("village gate", "supply hut"));
        roomItems.put("village square", Arrays.asList());

        // Village gate
        rooms.put("village gate", "You stand at the village gate, the entrance to the outside world. The gate is made of sturdy wood, and beyond it lies the unknown.\n"
                + "The village elder hands you a map and some supplies. \"Good luck,\" he says. To the north is the forest path.");
        neighborNS.put("village gate", Arrays.asList("forest path", "STOP"));
        neighborWE.put("village gate", Arrays.asList("STOP", "village square"));
        roomItems.put("village gate", Arrays.asList("Basic Helmet", "Basic Vest", "Basic Pants", "Basic Shoes", "Basic Weapon"));

        // Supply hut
        rooms.put("supply hut", "The supply hut is filled with various items that might be useful on your journey.\n"
                + "You see food, water, and basic medical supplies. To the west is the village square.");
        neighborNS.put("supply hut", Arrays.asList("STOP", "STOP"));
        neighborWE.put("supply hut", Arrays.asList("village square", "STOP"));
        roomItems.put("supply hut", Arrays.asList());

        // Forest path
        rooms.put("forest path", "You walk along the forest path, surrounded by tall trees and the sounds of wildlife.\n"
                + "The path is narrow and winding, and you must stay alert for any dangers. To the north is the abandoned cabin, and to the east is a hidden stream.");
        neighborNS.put("forest path", Arrays.asList("abandoned cabin", "village gate"));
        neighborWE.put("forest path", Arrays.asList("STOP", "hidden stream"));
        roomItems.put("forest path", Arrays.asList());

        // Hidden stream
        rooms.put("hidden stream", "You find a hidden stream, its clear water flowing gently over smooth stones.\n"
                + "The area is peaceful, a stark contrast to the chaos of the world beyond. To the west is the forest path.");
        neighborNS.put("hidden stream", Arrays.asList("STOP", "STOP"));
        neighborWE.put("hidden stream", Arrays.asList("forest path", "STOP"));
        roomItems.put("hidden stream", Arrays.asList());

        // Abandoned cabin
        rooms.put("abandoned cabin", "You arrive at an abandoned cabin in the middle of the forest. The cabin is old and decrepit, but it might hold some useful items.\n"
                + "To the north is the mountain trail, and to the west is a hidden grove.");
        neighborNS.put("abandoned cabin", Arrays.asList("mountain trail", "forest path"));
        neighborWE.put("abandoned cabin", Arrays.asList("hidden grove", "STOP"));
        roomItems.put("abandoned cabin", Arrays.asList());

        // Hidden grove
        rooms.put("hidden grove", "You discover a hidden grove, a small clearing surrounded by tall trees.\n"
                + "The grove is filled with wildflowers and the sound of birdsong. To the east is the abandoned cabin.");
        neighborNS.put("hidden grove", Arrays.asList("STOP", "STOP"));
        neighborWE.put("hidden grove", Arrays.asList("STOP", "abandoned cabin"));
        roomItems.put("hidden grove", Arrays.asList());

        // Mountain trail
        rooms.put("mountain trail", "You follow the mountain trail, which leads you higher and higher into the mountains.\n"
                + "The air is thin and cold, and the path is treacherous. To the north is the mountain cave, and to the east is a narrow ledge.");
        neighborNS.put("mountain trail", Arrays.asList("mountain cave", "abandoned cabin"));
        neighborWE.put("mountain trail", Arrays.asList("STOP", "narrow ledge"));
        roomItems.put("mountain trail", Arrays.asList());

        // Narrow ledge
        rooms.put("narrow ledge", "You carefully navigate a narrow ledge, with steep drops on either side.\n"
                + "The view from the ledge is breathtaking, with the landscape below showing signs of nature's recovery. To the west is the mountain trail.");
        neighborNS.put("narrow ledge", Arrays.asList("STOP", "STOP"));
        neighborWE.put("narrow ledge", Arrays.asList("mountain trail", "STOP"));
        roomItems.put("narrow ledge", Arrays.asList());

        // Mountain cave
        rooms.put("mountain cave", "You enter the mountain cave, a dark and eerie place. The cave is filled with strange rock formations and the sound of dripping water.\n"
                + "Deep inside the cave, you find a hidden lab where the cure is being developed. To the north is the mountain pass.");
        neighborNS.put("mountain cave", Arrays.asList("mountain pass", "mountain trail"));
        neighborWE.put("mountain cave", Arrays.asList("STOP", "STOP"));
        roomItems.put("mountain cave", Arrays.asList());

        // Mountain pass
        rooms.put("mountain pass", "You exit the mountain cave and find yourself on a narrow mountain pass.\n"
                + "The pass is treacherous, with steep drops on either side. The view from the pass is breathtaking, with the landscape below showing signs of nature's recovery. To the north is the city outskirts.");
        neighborNS.put("mountain pass", Arrays.asList("city outskirts", "mountain cave"));
        neighborWE.put("mountain pass", Arrays.asList("STOP", "STOP"));
        roomItems.put("mountain pass", Arrays.asList());

        // City outskirts
        rooms.put("city outskirts", "You reach the outskirts of a once-thriving city, now overrun by zombies.\n"
                + "The buildings are in ruins, and the streets are littered with debris. To the north is the city center, and to the east is an abandoned park.");
        neighborNS.put("city outskirts", Arrays.asList("city center", "mountain pass"));
        neighborWE.put("city outskirts", Arrays.asList("STOP", "abandoned park"));
        roomItems.put("city outskirts", Arrays.asList());

        // Abandoned park
        rooms.put("abandoned park", "You find an abandoned park, its playgrounds and benches now overgrown with weeds.\n"
                + "The park is eerily quiet, a reminder of the world that once was. To the west is the city outskirts.");
        neighborNS.put("abandoned park", Arrays.asList("STOP", "STOP"));
        neighborWE.put("abandoned park", Arrays.asList("city outskirts", "STOP"));
        roomItems.put("abandoned park", Arrays.asList());

        // City center
        rooms.put("city center", "You enter the city center, where the chaos is even more apparent.\n"
                + "Zombies roam the streets, and you must move carefully to avoid them. To the north is the hospital, and to the east is a deserted mall.");
        neighborNS.put("city center", Arrays.asList("hospital", "city outskirts"));
        neighborWE.put("city center", Arrays.asList("STOP", "deserted mall"));
        roomItems.put("city center", Arrays.asList());

        // Deserted mall
        rooms.put("deserted mall", "You explore a deserted mall, its once-bustling shops now empty and abandoned.\n"
                + "The mall is a haunting reminder of the life that was lost. To the west is the city center.");
        neighborNS.put("deserted mall", Arrays.asList("STOP", "STOP"));
        neighborWE.put("deserted mall", Arrays.asList("city center", "STOP"));
        roomItems.put("deserted mall", Arrays.asList());

        // Hospital
        rooms.put("hospital", "You arrive at the hospital, a key location in your search for the cure.\n"
                + "The building is in disrepair, and the smell of decay is overwhelming. To the north is the research lab, and to the east is the emergency room.");
        neighborNS.put("hospital", Arrays.asList("research lab", "city center"));
        neighborWE.put("hospital", Arrays.asList("STOP", "emergency room"));
        roomItems.put("hospital", Arrays.asList());

        // Emergency room
        rooms.put("emergency room", "You enter the emergency room, where the chaos of the outbreak is still evident.\n"
                + "Medical supplies are scattered everywhere, and the air is thick with the scent of antiseptic. To the west is the hospital.");
        neighborNS.put("emergency room", Arrays.asList("STOP", "STOP"));
        neighborWE.put("emergency room", Arrays.asList("hospital", "STOP"));
        roomItems.put("emergency room", Arrays.asList());

        // Research lab
        rooms.put("research lab", "You enter the research lab, where scientists once worked to find a cure.\n"
                + "The lab is filled with equipment and research notes. To the north is the quarantine zone, and to the east is the storage room.");
        neighborNS.put("research lab", Arrays.asList("quarantine zone", "hospital"));
        neighborWE.put("research lab", Arrays.asList("STOP", "storage room"));
        roomItems.put("research lab", Arrays.asList());

        // Storage room
        rooms.put("storage room", "You find the storage room, filled with supplies and equipment.\n"
                + "The room is cluttered, but you might find something useful here. To the west is the research lab.");
        neighborNS.put("storage room", Arrays.asList("STOP", "STOP"));
        neighborWE.put("storage room", Arrays.asList("research lab", "STOP"));
        roomItems.put("storage room", Arrays.asList());

        // Quarantine zone
        rooms.put("quarantine zone", "You reach the quarantine zone, a heavily guarded area where the infected were once held.\n"
                + "The area is eerily quiet, and you must be cautious. To the north is the military base, and to the east is the guard post.");
        neighborNS.put("quarantine zone", Arrays.asList("military base", "research lab"));
        neighborWE.put("quarantine zone", Arrays.asList("STOP", "guard post"));
        roomItems.put("quarantine zone", Arrays.asList());

        // Guard post
        rooms.put("guard post", "You find the guard post, where soldiers once monitored the quarantine zone.\n"
                + "The post is abandoned, but it might hold some useful information. To the west is the quarantine zone.");
        neighborNS.put("guard post", Arrays.asList("STOP", "STOP"));
        neighborWE.put("guard post", Arrays.asList("quarantine zone", "STOP"));
        roomItems.put("guard post", Arrays.asList());

        // Military base
        rooms.put("military base", "You arrive at the military base, the final destination in your journey.\n"
                + "The base is fortified, and you see soldiers preparing for an assault.");
        neighborNS.put("military base", Arrays.asList("STOP", "quarantine zone"));
        neighborWE.put("military base", Arrays.asList("STOP", "STOP"));
        roomItems.put("military base", Arrays.asList());
    }

    // Get room descriptions
    public Map<String, String> getRooms() {
        return rooms;
    }

    // Get north-south neighbors
    public Map<String, List<String>> getNeighborNS() {
        return neighborNS;
    }

    // Get west-east neighbors
    public Map<String, List<String>> getNeighborWE() {
        return neighborWE;
    }

    // Get room items
    public Map<String, List<String>> getRoomItems() {
        return roomItems;
    }
}