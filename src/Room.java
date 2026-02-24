package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private final Map<String, String> descriptions = new HashMap<>();
    private final Map<String, Map<String, String>> exits = new HashMap<>();
    private final Map<String, List<String>> loot = new HashMap<>();
    private final Map<String, String> encounter = new HashMap<>();
    private final Map<String, int[]> coordinates = new HashMap<>();

    public Room() {
        addRoom("safehouse", "Your candle-lit safehouse. A hand-drawn map says: FIND THE CURE CORE.", 1, 4);
        addRoom("flooded metro", "Dark tunnels drip toxic water. You hear growls and old subway speakers.", 1, 3);
        addRoom("black market", "A neon bazaar where survivors barter junk for miracles.", 2, 3);
        addRoom("hospital ruins", "Broken beds, flickering lights, and cabinets full of half-used meds.", 1, 2);
        addRoom("drone nest", "Collapsed parking tower full of sentry drones and rare salvage.", 2, 2);
        addRoom("reactor tower", "The final climb. Storms rage around a humming reactor cradle.", 1, 1);

        connect("safehouse", "north", "flooded metro");
        connect("flooded metro", "south", "safehouse");
        connect("flooded metro", "east", "black market");
        connect("black market", "west", "flooded metro");
        connect("flooded metro", "north", "hospital ruins");
        connect("hospital ruins", "south", "flooded metro");
        connect("hospital ruins", "east", "drone nest");
        connect("drone nest", "west", "hospital ruins");
        connect("hospital ruins", "north", "reactor tower");
        connect("reactor tower", "south", "hospital ruins");

        loot.put("safehouse", new ArrayList<>(List.of("Torn Jacket")));
        loot.put("flooded metro", new ArrayList<>(List.of("Survivor Machete", "scrap")));
        loot.put("black market", new ArrayList<>(List.of("Riot Armor", "bomb cache")));
        loot.put("hospital ruins", new ArrayList<>(List.of("Electro Bat", "medkit crate")));
        loot.put("drone nest", new ArrayList<>(List.of("Railgun Prototype", "energy crate", "scrap")));
        loot.put("reactor tower", new ArrayList<>(List.of("Titan Plating", "core stabilizer")));

        encounter.put("flooded metro", "crawler swarm");
        encounter.put("black market", "raider champion");
        encounter.put("hospital ruins", "mutant brute");
        encounter.put("drone nest", "sentry overmind");
        encounter.put("reactor tower", "omega abomination");
    }

    private void addRoom(String key, String description, int x, int y) {
        descriptions.put(key, description);
        exits.put(key, new HashMap<>());
        loot.put(key, new ArrayList<>());
        coordinates.put(key, new int[] {x, y});
    }

    private void connect(String from, String direction, String to) {
        exits.get(from).put(direction, to);
    }

    public String getDescription(String room) {
        return descriptions.get(room);
    }

    public String go(String currentRoom, String direction) {
        return exits.get(currentRoom).getOrDefault(direction, "");
    }

    public List<String> collectLoot(String room) {
        List<String> items = loot.get(room);
        List<String> dropped = new ArrayList<>(items);
        items.clear();
        return dropped;
    }

    public String getEncounter(String room) {
        return encounter.getOrDefault(room, "");
    }

    public String exitsText(String room) {
        return String.join(", ", exits.get(room).keySet());
    }

    public String renderMap(String currentRoom) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nCity Grid Map\n");
        for (int y = 1; y <= 4; y++) {
            for (int x = 1; x <= 2; x++) {
                String roomName = roomAt(x, y);
                if (roomName == null) {
                    sb.append("      ");
                    continue;
                }
                String token = roomName.equals(currentRoom) ? "[P]" : "[" + symbol(roomName) + "]";
                sb.append(token).append("  ");
            }
            sb.append("\n");
        }
        sb.append("Legend: P=Player S=Safehouse M=Metro B=BlackMarket H=Hospital D=DroneNest R=Reactor\n");
        return sb.toString();
    }

    private String roomAt(int x, int y) {
        for (Map.Entry<String, int[]> entry : coordinates.entrySet()) {
            int[] pos = entry.getValue();
            if (pos[0] == x && pos[1] == y) {
                return entry.getKey();
            }
        }
        return null;
    }

    private String symbol(String room) {
        return switch (room) {
            case "safehouse" -> "S";
            case "flooded metro" -> "M";
            case "black market" -> "B";
            case "hospital ruins" -> "H";
            case "drone nest" -> "D";
            case "reactor tower" -> "R";
            default -> "?";
        };
    }
}
