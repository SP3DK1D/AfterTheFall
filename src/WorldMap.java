package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Procedurally-generated map with rooms + corridors. */
public class WorldMap {
    private final char[][] tiles;
    private final int width;
    private final int height;
    private final long seed;

    private final List<Enemy> enemies = new ArrayList<>();
    private final List<ItemEntity> items = new ArrayList<>();
    private final List<Npc> npcs = new ArrayList<>();
    private Position playerStart;
    private Position goalDoor;

    public WorldMap(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.tiles = new char[height][width];
        generate();
    }

    private void generate() {
        Random random = new Random(seed);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = '#';
            }
        }

        List<Rect> rooms = new ArrayList<>();
        int roomCount = 22;
        for (int i = 0; i < roomCount; i++) {
            int rw = 6 + random.nextInt(10);
            int rh = 4 + random.nextInt(7);
            int rx = 1 + random.nextInt(Math.max(2, width - rw - 2));
            int ry = 1 + random.nextInt(Math.max(2, height - rh - 2));
            Rect room = new Rect(rx, ry, rw, rh);
            if (intersectsAny(room, rooms)) {
                continue;
            }
            carveRoom(room);
            if (!rooms.isEmpty()) {
                Position prev = rooms.get(rooms.size() - 1).center();
                Position curr = room.center();
                carveCorridor(prev, curr, random);
            }
            rooms.add(room);
        }

        if (rooms.isEmpty()) {
            Rect emergency = new Rect(2, 2, 10, 6);
            carveRoom(emergency);
            rooms.add(emergency);
        }

        playerStart = rooms.get(0).center();
        goalDoor = rooms.get(rooms.size() - 1).center();
        tiles[goalDoor.getY()][goalDoor.getX()] = '+';

        addWaterPatches(random);
        placeEntities(random, rooms);
    }

    private void addWaterPatches(Random random) {
        int waterTiles = (width * height) / 35;
        for (int i = 0; i < waterTiles; i++) {
            Position p = randomFloor(random);
            if (p != null && !p.equals(playerStart) && !p.equals(goalDoor)) {
                tiles[p.getY()][p.getX()] = '~';
            }
        }
    }

    private void placeEntities(Random random, List<Rect> rooms) {
        int enemyCount = 14;
        for (int i = 0; i < enemyCount; i++) {
            Position pos = randomFloor(random);
            if (pos == null || pos.equals(playerStart)) {
                continue;
            }
            int tier = 1 + random.nextInt(3);
            enemies.add(new Enemy(
                    tier == 1 ? "crawler" : tier == 2 ? "raider" : "mutant",
                    28 + tier * 16,
                    5 + tier * 4,
                    18 + tier * 12,
                    12 + tier * 9,
                    pos,
                    6 + tier));
        }

        for (int i = 0; i < 8; i++) {
            Position pos = randomFloor(random);
            if (pos != null) {
                items.add(new ItemEntity(i % 3 == 0 ? "medkit crate" : i % 3 == 1 ? "energy crate" : "scrap", pos));
            }
        }

        items.add(new ItemEntity("Core Stabilizer", randomFloor(random)));
        npcs.add(new Npc("trader", rooms.get(Math.max(0, rooms.size() / 3)).center()));
    }

    private void carveRoom(Rect room) {
        for (int y = room.y; y < room.y + room.h; y++) {
            for (int x = room.x; x < room.x + room.w; x++) {
                if (inBounds(x, y)) {
                    tiles[y][x] = '.';
                }
            }
        }
    }

    private void carveCorridor(Position a, Position b, Random random) {
        int x = a.getX();
        int y = a.getY();
        boolean horizontalFirst = random.nextBoolean();
        if (horizontalFirst) {
            while (x != b.getX()) {
                x += Integer.compare(b.getX(), x);
                tiles[y][x] = '.';
            }
            while (y != b.getY()) {
                y += Integer.compare(b.getY(), y);
                tiles[y][x] = '.';
            }
        } else {
            while (y != b.getY()) {
                y += Integer.compare(b.getY(), y);
                tiles[y][x] = '.';
            }
            while (x != b.getX()) {
                x += Integer.compare(b.getX(), x);
                tiles[y][x] = '.';
            }
        }
    }

    private boolean intersectsAny(Rect room, List<Rect> rooms) {
        for (Rect other : rooms) {
            if (room.intersects(other)) {
                return true;
            }
        }
        return false;
    }

    private Position randomFloor(Random random) {
        for (int tries = 0; tries < 400; tries++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (tiles[y][x] == '.') {
                return new Position(x, y);
            }
        }
        return null;
    }

    public boolean isWalkable(Position p) {
        if (!inBounds(p)) {
            return false;
        }
        char t = tiles[p.getY()][p.getX()];
        return t == '.' || t == '+' || t == '~';
    }

    public boolean inBounds(Position p) { return inBounds(p.getX(), p.getY()); }
    private boolean inBounds(int x, int y) { return x >= 0 && x < width && y >= 0 && y < height; }

    public char tileAt(Position p) { return tiles[p.getY()][p.getX()]; }
    public Position getPlayerStart() { return playerStart; }
    public Position getGoalDoor() { return goalDoor; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public long getSeed() { return seed; }

    public List<Enemy> getEnemies() { return enemies; }
    public List<ItemEntity> getItems() { return items; }
    public List<Npc> getNpcs() { return npcs; }

    public Enemy enemyAt(Position p) {
        for (Enemy enemy : enemies) {
            if (enemy.getPosition().equals(p)) {
                return enemy;
            }
        }
        return null;
    }

    public Npc npcAt(Position p) {
        for (Npc npc : npcs) {
            if (npc.getPosition().equals(p)) {
                return npc;
            }
        }
        return null;
    }

    public ItemEntity pickupItemAt(Position p) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getPosition().equals(p)) {
                return items.remove(i);
            }
        }
        return null;
    }

    private static class Rect {
        final int x;
        final int y;
        final int w;
        final int h;

        Rect(int x, int y, int w, int h) {
            this.x = x; this.y = y; this.w = w; this.h = h;
        }

        Position center() {
            return new Position(x + w / 2, y + h / 2);
        }

        boolean intersects(Rect other) {
            return x < other.x + other.w + 1 && x + w + 1 > other.x && y < other.y + other.h + 1 && y + h + 1 > other.y;
        }
    }
}
