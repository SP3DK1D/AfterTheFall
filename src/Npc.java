package src;

/** Basic NPC marker on the map. */
public class Npc {
    private final String name;
    private final Position position;

    public Npc(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }
}
