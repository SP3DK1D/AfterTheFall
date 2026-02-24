package src;

/** Compatibility wrapper. Prefer WorldMap. */
public class Room extends WorldMap {
    public Room() {
        super(120, 60, System.currentTimeMillis());
    }
}
