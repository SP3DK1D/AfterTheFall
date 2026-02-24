package src;

import java.util.List;
import java.util.Random;

public class Player {
    private final Inventory inventory;
    private final Room world;

    private String location = "safehouse";

    private int level = 1;
    private int xp = 0;
    private int scraps = 20;
    private int maxHealth = 100;
    private int health = 100;
    private int energy = 3;
    private int streak = 0;
    private boolean coreStabilizer = false;

    public Player(Room world) {
        this.world = world;
        this.inventory = new Inventory();
    }

    public String move(String direction) {
        String next = world.go(location, direction);
        if (next.isBlank()) {
            return "You can't go that way.";
        }
        location = next;
        energy = Math.min(3 + level / 3, energy + 1);
        return "You travel " + direction + " to " + location + ".";
    }

    public void autoLoot() {
        List<String> items = world.collectLoot(location);
        for (String item : items) {
            if (item.equalsIgnoreCase("medkit crate")) {
                inventory.addConsumable("medkit", 2);
                System.out.println("Found medkits x2.");
            } else if (item.equalsIgnoreCase("bomb cache")) {
                inventory.addConsumable("bomb", 2);
                System.out.println("Found bombs x2.");
            } else if (item.equalsIgnoreCase("energy crate")) {
                inventory.addConsumable("energy drink", 2);
                System.out.println("Found energy drinks x2.");
            } else if (item.equalsIgnoreCase("scrap")) {
                scraps += 15;
                System.out.println("Found 15 scraps.");
            } else if (item.equalsIgnoreCase("core stabilizer")) {
                coreStabilizer = true;
                System.out.println("You found the Core Stabilizer. Final sequence unlocked.");
            } else {
                inventory.addItem(item);
                System.out.println("Looted: " + item);
                if (item.toLowerCase().contains("machete") || item.toLowerCase().contains("bat")
                        || item.toLowerCase().contains("railgun") || item.toLowerCase().contains("armor")
                        || item.toLowerCase().contains("plating") || item.toLowerCase().contains("jacket")) {
                    inventory.equip(item);
                }
            }
        }
    }

    public int attackDamage(Random random) {
        int crit = random.nextInt(100) < 15 ? 8 : 0;
        return 6 + inventory.weaponBonus() + level + random.nextInt(6) + crit;
    }

    public int useSkill(Random random) {
        if (energy <= 0) {
            return 0;
        }
        energy--;
        return 14 + (level * 2) + random.nextInt(10);
    }

    public boolean useItem(String item) {
        if (!inventory.useConsumable(item)) {
            return false;
        }
        switch (item) {
            case "medkit" -> {
                health = Math.min(maxHealth, health + 35);
                System.out.println("You patched yourself up (+35 HP).");
            }
            case "energy drink" -> {
                energy += 2;
                System.out.println("Adrenaline surge! (+2 energy)");
            }
            case "bomb" -> System.out.println("You throw a bomb. Massive damage on your next attack!");
            default -> {
                return false;
            }
        }
        return true;
    }

    public void rest() {
        int heal = 8 + level;
        health = Math.min(maxHealth, health + heal);
        System.out.println("You take a tactical breather and recover " + heal + " HP.");
    }

    public void shopMenu() {
        System.out.println("\n=== BLACK MARKET SHOP ===");
        System.out.println("Scraps: " + scraps);
        System.out.println("1) medkit (15)");
        System.out.println("2) energy drink (12)");
        System.out.println("3) bomb (20)");
        System.out.println("4) titan plating (equip, 60)");
        System.out.println("5) weapon tune-up (+2 ATK, 45)");
    }

    public boolean buy(String choice) {
        return switch (choice) {
            case "1" -> buyConsumable("medkit", 15);
            case "2" -> buyConsumable("energy drink", 12);
            case "3" -> buyConsumable("bomb", 20);
            case "4" -> {
                if (scraps < 60) {
                    yield false;
                }
                scraps -= 60;
                inventory.addItem("Titan Plating");
                inventory.equip("Titan Plating");
                yield true;
            }
            case "5" -> {
                if (scraps < 45) {
                    yield false;
                }
                scraps -= 45;
                inventory.upgradeWeapon();
                yield true;
            }
            default -> false;
        };
    }

    private boolean buyConsumable(String item, int price) {
        if (scraps < price) {
            return false;
        }
        scraps -= price;
        inventory.addConsumable(item, 1);
        return true;
    }

    public void winBattle(int gainedXp, int gainedScraps) {
        xp += gainedXp;
        scraps += gainedScraps;
        streak++;
        if (streak % 3 == 0) {
            energy += 1;
            System.out.println("Combat streak! Bonus +1 energy.");
        }
        while (xp >= level * 70) {
            xp -= level * 70;
            level++;
            maxHealth += 12;
            health = maxHealth;
            energy += 1;
            System.out.println("LEVEL UP! You are now level " + level + ".");
        }
    }

    public void takeDamage(int incoming) {
        health -= incoming;
        streak = 0;
    }

    public int getDefense() {
        return level / 2 + inventory.armorBonus();
    }

    public boolean isAlive() {
        return health > 0;
    }

    public String getLocation() {
        return location;
    }

    public int getHealth() {
        return health;
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

    public Inventory getInventory() {
        return inventory;
    }

    public String objective() {
        if (!coreStabilizer) {
            return "Objective: Find the Core Stabilizer and reach Reactor Tower.";
        }
        return "Objective: Deliver the stabilizer at Reactor Tower and survive the final wave.";
    }

    public boolean hasCoreStabilizer() {
        return coreStabilizer;
    }
}
