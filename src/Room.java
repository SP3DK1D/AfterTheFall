package src;

/**
 * Backward-compatible wrapper kept so older references to Room still compile.
 * New code uses WorldMap.
 */
public class Room extends WorldMap {
    public Room() {
        super();
    }
}
