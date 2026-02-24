package src;

/** Item marker on the map. */
public class ItemEntity {
    private final String name;
    private final Position position;

    public ItemEntity(String name, Position position) {
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
