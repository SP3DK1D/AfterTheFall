package src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** 2D ASCII world: walls, floors, entities, and interactions. */
public class WorldMap {
    private final char[][] tiles;
    private final int width;
    private final int height;

    private final List<Enemy> enemies = new ArrayList<>();
    private final List<ItemEntity> items = new ArrayList<>();
    private final List<Npc> npcs = new ArrayList<>();

    private final Position playerStart = new Position(2, 2);
    private final Position reactorDoor = new Position(36, 16);

    public WorldMap() {
        String[] rows = new String[] {
                "########################################",
                "#....#...........#..............#......#",
                "#....#...........#..............#......#",
                "#....#####..######....~~~~......#..N...#",
                "#........................~~~~....#......#",
                "#..##########....................###.####",
                "#..#........#...#######..............#..#",
                "#..#........#...#.....#......####....#..#",
                "#..#....N...#...#.....#.........#....#..#",
                "#..######.###...#.....#######...#.####..#",
                "#.........#.....#...........#...#.......#",
                "#.#####...#.....#####.#####.#...#####...#",
                "#.....#...#...........#...#.#.......#...#",
                "#.....#####...#########...#.#####...#...#",
                "#.............#...........#.....#...#...#",
                "#..E......*...#....N......#..*..#...#...#",
                "#.............#...........#.....#...+...#",
                "#....*....E...#############.....#######.#",
                "#.......................................#",
                "########################################"
        };

        height = rows.length;
        width = rows[0].length();
        tiles = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = rows[y].charAt(x);
            }
        }

        // Convert map placeholders into entity lists for cleaner rendering.
        consumeMapEntities();
        seedEntities();
    }

    private void consumeMapEntities() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = tiles[y][x];
                if (c == 'E' || c == 'N' || c == '*') {
                    tiles[y][x] = '.';
                }
            }
        }
    }

    private void seedEntities() {
        enemies.add(new Enemy("crawler swarm", 56, 8, 30, 25, new Position(3, 15)));
        enemies.add(new Enemy("mutant brute", 90, 14, 45, 35, new Position(11, 17)));

        npcs.add(new Npc("black market trader", new Position(33, 3)));
        npcs.add(new Npc("medic", new Position(9, 8)));
        npcs.add(new Npc("engineer", new Position(23, 15)));

        items.add(new ItemEntity("Survivor Machete", new Position(10, 15)));
        items.add(new ItemEntity("Core Stabilizer", new Position(28, 15)));
        items.add(new ItemEntity("energy crate", new Position(5, 17)));
    }

    public Position getPlayerStart() {
        return playerStart;
    }

    public boolean inBounds(Position p) {
        return p.getX() >= 0 && p.getX() < width && p.getY() >= 0 && p.getY() < height;
    }

    public boolean isWalkable(Position p) {
        if (!inBounds(p)) {
            return false;
        }
        char tile = tiles[p.getY()][p.getX()];
        return tile != '#' && tile != ' ';
    }

    public char tileAt(Position p) {
        return tiles[p.getY()][p.getX()];
    }

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
        Iterator<ItemEntity> iterator = items.iterator();
        while (iterator.hasNext()) {
            ItemEntity item = iterator.next();
            if (item.getPosition().equals(p)) {
                iterator.remove();
                return item;
            }
        }
        return null;
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public boolean isReactorDoor(Position p) {
        return p.equals(reactorDoor) || tileAt(p) == '+';
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char symbolAt(Position p, Position playerPos) {
        if (playerPos.equals(p)) {
            return '@';
        }
        if (enemyAt(p) != null) {
            return 'E';
        }
        if (npcAt(p) != null) {
            return 'N';
        }
        for (ItemEntity item : items) {
            if (item.getPosition().equals(p)) {
                return '*';
            }
        }
        return tileAt(p);
    }
}
