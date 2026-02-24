package src;

/** Player state and progression. */
public class Player {
    private Position position;
    private final Inventory inventory;

    private int level = 1;
    private int xp = 0;
    private int scraps = 20;
    private int maxHealth = 100;
    private int health = 100;
    private int energy = 3;
    private boolean coreStabilizer = false;

    public Player(Position start) {
        this.position = start;
        this.inventory = new Inventory();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int attackDamage() {
        return 6 + inventory.weaponBonus() + level;
    }

    public int skillDamage() {
        if (energy <= 0) {
            return 0;
        }
        energy--;
        return 14 + (level * 2);
    }

    public boolean useItem(String item) {
        if (!inventory.useConsumable(item)) {
            return false;
        }
        switch (item) {
            case "medkit" -> health = Math.min(maxHealth, health + 35);
            case "energy drink" -> energy += 2;
            case "bomb" -> energy += 1;
            default -> {
                return false;
            }
        }
        return true;
    }

    public void rest() {
        int heal = 8 + level;
        health = Math.min(maxHealth, health + heal);
    }

    public void addScraps(int amount) {
        scraps += amount;
    }

    public void gainRewards(int gainedXp, int gainedScraps) {
        xp += gainedXp;
        scraps += gainedScraps;
        while (xp >= level * 70) {
            xp -= level * 70;
            level++;
            maxHealth += 12;
            health = maxHealth;
            energy += 1;
        }
    }

    public int getDefense() {
        return level / 2 + inventory.armorBonus();
    }

    public void takeDamage(int incoming) {
        health -= incoming;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getScraps() {
        return scraps;
    }

    public int getEnergy() {
        return energy;
    }

    public int getHealth() {
        return health;
    }

    public boolean hasCoreStabilizer() {
        return coreStabilizer;
    }

    public void setCoreStabilizer(boolean coreStabilizer) {
        this.coreStabilizer = coreStabilizer;
    }

    public String objective() {
        if (!coreStabilizer) {
            return "Objective: Clear enemies, loot gear, and secure the zone.";
        }
        return "Objective: Stabilizer found. Finish clearing remaining enemies.";
    }
}
