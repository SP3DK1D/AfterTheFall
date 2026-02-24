package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldMap {
    private final int width;
    private final int height;
    private final int[][] tiles;
    private final long seed;
    private final Random random;

    private final List<Enemy> enemies = new ArrayList<>();
    private final List<ItemEntity> items = new ArrayList<>();
    private final List<Npc> npcs = new ArrayList<>();

    private Position playerStart;

    public static final int WALL = 0;
    public static final int FLOOR = 1;
    public static final int WATER = 2;

    public WorldMap(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.random = new Random(seed);
        this.tiles = new int[height][width];
        generate();
    }

    private void generate() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = WALL;
            }
        }

        List<Rect> rooms = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            int rw = 7 + random.nextInt(9);
            int rh = 6 + random.nextInt(8);
            int rx = 1 + random.nextInt(Math.max(2, width - rw - 2));
            int ry = 1 + random.nextInt(Math.max(2, height - rh - 2));
            Rect room = new Rect(rx, ry, rw, rh);
            if (intersectsAny(room, rooms)) {
                continue;
            }
            carveRoom(room);
            if (!rooms.isEmpty()) {
                carveCorridor(rooms.get(rooms.size() - 1).center(), room.center());
            }
            rooms.add(room);
        }

        if (rooms.isEmpty()) {
            Rect fallback = new Rect(3, 3, 10, 8);
            carveRoom(fallback);
            rooms.add(fallback);
        }

        playerStart = rooms.get(0).center();

        int waterCount = (width * height) / 25;
        for (int i = 0; i < waterCount; i++) {
            Position p = randomFloor();
            if (p != null) {
                tiles[p.getY()][p.getX()] = WATER;
            }
        }

        for (int i = 0; i < 20; i++) {
            Position p = randomFloor();
            if (p != null && !p.equals(playerStart)) {
                int tier = 1 + random.nextInt(3);
                enemies.add(new Enemy(
                        tier == 1 ? "Crawler" : tier == 2 ? "Raider" : "Mutant",
                        34 + tier * 20,
                        6 + tier * 5,
                        25 + tier * 12,
                        16 + tier * 10,
                        p,
                        6 + tier));
            }
        }

        for (int i = 0; i < 16; i++) {
            Position p = randomFloor();
            if (p != null) {
                String name = switch (i % 4) {
                    case 0 -> "medkit crate";
                    case 1 -> "energy crate";
                    case 2 -> "scrap";
                    default -> "Survivor Machete";
                };
                items.add(new ItemEntity(name, p));
            }
        }

        Position npcSpot = rooms.get(Math.min(rooms.size() - 1, 2)).center();
        npcs.add(new Npc("Trader", npcSpot));
    }

    private boolean intersectsAny(Rect room, List<Rect> rooms) {
        for (Rect other : rooms) {
            if (room.intersects(other)) {
                return true;
            }
        }
        return false;
    }

    private void carveRoom(Rect room) {
        for (int y = room.y; y < room.y + room.h; y++) {
            for (int x = room.x; x < room.x + room.w; x++) {
                if (inBounds(x, y)) {
                    tiles[y][x] = FLOOR;
                }
            }
        }
    }

    private void carveCorridor(Position a, Position b) {
        int x = a.getX();
        int y = a.getY();
        while (x != b.getX()) {
            x += Integer.compare(b.getX(), x);
            tiles[y][x] = FLOOR;
        }
        while (y != b.getY()) {
            y += Integer.compare(b.getY(), y);
            tiles[y][x] = FLOOR;
        }
    }

    private Position randomFloor() {
        for (int t = 0; t < 600; t++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (tiles[y][x] == FLOOR) {
                return new Position(x, y);
            }
        }
        return null;
    }

    public boolean inBounds(Position p) {
        return inBounds(p.getX(), p.getY());
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isWalkable(Position p) {
        if (!inBounds(p)) {
            return false;
        }
        return tiles[p.getY()][p.getX()] != WALL;
    }

    public int tileAt(Position p) {
        return tiles[p.getY()][p.getX()];
    }

    public Position getPlayerStart() {
        return playerStart;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public List<Npc> getNpcs() {
        return npcs;
    }

    public Enemy enemyAt(Position p) {
        for (Enemy e : enemies) {
            if (e.getPosition().equals(p)) {
                return e;
            }
        }
        return null;
    }

    public ItemEntity pickupItemAt(Position p) {
        for (int i = 0; i < items.size(); i++) {
            ItemEntity item = items.get(i);
            if (item.getPosition().equals(p)) {
                items.remove(i);
                return item;
            }
        }
        return null;
    }

    public Npc npcAt(Position p) {
        for (Npc n : npcs) {
            if (n.getPosition().equals(p)) {
                return n;
            }
        }
        return null;
    }

    public long getSeed() {
        return seed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private static class Rect {
        int x, y, w, h;

        Rect(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        Position center() {
            return new Position(x + w / 2, y + h / 2);
        }

        boolean intersects(Rect o) {
            return x < o.x + o.w + 1 && x + w + 1 > o.x && y < o.y + o.h + 1 && y + h + 1 > o.y;
        }
    }
}
