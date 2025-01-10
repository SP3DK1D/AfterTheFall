package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private List<String> items; // List to store the items in the inventory
    private String helmet; // Slot for the helmet
    private String vest; // Slot for the vest
    private String pants; // Slot for the pants
    private String shoes; // Slot for the shoes
    private String weapon; // Slot for the weapon
    private String supply; // Slot for the supply
    private Map<String, Map<String, Integer>> itemDHD;
    private int xp = 0;
    private int level = 1;

    // creates the item slots and makes the items list an ArrayList
    public Inventory() {
        items = new ArrayList<>();
        helmet = "None";
        vest = "None";
        pants = "None";
        shoes = "None";
        weapon = "None";
        supply = "None";
        itemDHD = new HashMap<>();

        // make the item defence,health,damage
        itemDHD.put("advanced medical supplies", createItemDHD(0, 50, 0));
        itemDHD.put("basic medical supplies", createItemDHD(0, 20, 0));
        itemDHD.put("water", createItemDHD(0, 5, 0));
        itemDHD.put("food", createItemDHD(0, 10, 0));
        itemDHD.put("tatered Helmet", createItemDHD(5, 0, 0));
        itemDHD.put("tatered Vest", createItemDHD(10, 0, 0));
        itemDHD.put("tatered Pants", createItemDHD(2, 0, 0));
        itemDHD.put("tatered Shoes", createItemDHD(2, 0, 0));
        itemDHD.put("tatered Weapon", createItemDHD(0, 0, 10));
        itemDHD.put("basic helmet", createItemDHD(12, 0, 0));
        itemDHD.put("basic vest", createItemDHD(15, 0, 0));
        itemDHD.put("basic pants", createItemDHD(3, 0, 0));
        itemDHD.put("basic shoes", createItemDHD(3, 0, 0));
        itemDHD.put("basic weapon", createItemDHD(0, 0, 17));
        itemDHD.put("cave helmet", createItemDHD(15, 0, 0));
        itemDHD.put("cave vest", createItemDHD(17, 0, 0));
        itemDHD.put("cave pants", createItemDHD(4, 0, 0));
        itemDHD.put("cave shoes", createItemDHD(4, 0, 0));
        itemDHD.put("cave weapon", createItemDHD(0, 0, 20));
        itemDHD.put("advanced helmet", createItemDHD(20, 0, 0));
        itemDHD.put("advanced vest", createItemDHD(25, 0, 0));
        itemDHD.put("advanced pants", createItemDHD(6, 0, 0));
        itemDHD.put("advanced shoes", createItemDHD(6, 0, 0));
        itemDHD.put("advanced weapon", createItemDHD(0, 0, 25));
        itemDHD.put("military helmet", createItemDHD(10, 0, 0));
        itemDHD.put("military vest", createItemDHD(20, 0, 0));
        itemDHD.put("military pants", createItemDHD(7, 0, 0));
        itemDHD.put("military shoes", createItemDHD(7, 0, 0));
        itemDHD.put("military weapon", createItemDHD(0, 0, 30));
    }

    private Map<String, Integer> createItemDHD(int defense, int health, int damage) {
        Map<String, Integer> buffs = new HashMap<>();
        buffs.put("defense", defense);
        buffs.put("health", health);
        buffs.put("damage", damage);
        return buffs;
    }

    public List<String> getItems() {
        return items;
    }

    public Map<String, Map<String, Integer>> getItemDHD() {
        return itemDHD;
    }

    // Adds an item to the inventory
    public void addItem(String item) {
        items.add(item);
        System.out.println("** " + item + " has been added to your inventory.**");
        autoEquipItem(item);
    }

    // Automatically equips an item if it is better than the currently equipped item
    private void autoEquipItem(String item) {
        Map<String, Integer> newItemStats = itemDHD.get(item);
        if (newItemStats == null) {
            return;
        }

        switch (getItemType(item)) {
            case "helmet":
                if (isBetterItem(newItemStats, itemDHD.get(helmet))) {
                    equipItem("helmet", item);
                }
                break;
            case "vest":
                if (isBetterItem(newItemStats, itemDHD.get(vest))) {
                    equipItem("vest", item);
                }
                break;
            case "pants":
                if (isBetterItem(newItemStats, itemDHD.get(pants))) {
                    equipItem("pants", item);
                }
                break;
            case "shoes":
                if (isBetterItem(newItemStats, itemDHD.get(shoes))) {
                    equipItem("shoes", item);
                }
                break;
            case "weapon":
                if (isBetterItem(newItemStats, itemDHD.get(weapon))) {
                    equipItem("weapon", item);
                }
                break;
            default:
                break;
        }
    }

    // Determines the type of item based on its name
    private String getItemType(String item) {
        if (item.toLowerCase().contains("helmet")) {
            return "helmet";
        } else if (item.toLowerCase().contains("vest")) {
            return "vest";
        } else if (item.toLowerCase().contains("pants")) {
            return "pants";
        } else if (item.toLowerCase().contains("shoes")) {
            return "shoes";
        } else if (item.toLowerCase().contains("weapon")) {
            return "weapon";
        } else {
            return "unknown";
        }
    }

    // Compares two items and returns true if the new item is better
    private boolean isBetterItem(Map<String, Integer> newItem, Map<String, Integer> currentItem) {
        if (currentItem == null) {
            return true;
        }
        int newDefense = newItem.getOrDefault("defense", 0);
        int newHealth = newItem.getOrDefault("health", 0);
        int newDamage = newItem.getOrDefault("damage", 0);

        int currentDefense = currentItem.getOrDefault("defense", 0);
        int currentHealth = currentItem.getOrDefault("health", 0);
        int currentDamage = currentItem.getOrDefault("damage", 0);

        return newDefense > currentDefense || newHealth > currentHealth || newDamage > currentDamage;
    }

    // Removes an item from the inventory
    public void removeItem(String item) {
        if (items.remove(item)) {
            System.out.println("** " + item + " has been removed from your inventory.**");
        } else {
            System.out.println(item + " is not in your inventory.");
        }
    }

    // Displays the items in the inventory
    public void showInventory() {
        System.out.println("Equipment:");
        System.out.println("Helmet: " + helmet);
        System.out.println("Vest: " + vest);
        System.out.println("Pants: " + pants);
        System.out.println("Shoes: " + shoes);
        System.out.println("Weapon: " + weapon);
        System.out.println("Supply: " + supply);
        System.out.println();
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Your inventory contains:");
            for (String item : items) {
                System.out.println("- " + item);
            }
        }
    }

    // Equips an item to the specified slot
    public void equipItem(String slot, String item) {
        switch (slot.toLowerCase()) {
            case "helmet":
                helmet = item;
                break;
            case "vest":
                vest = item;
                break;
            case "pants":
                pants = item;
                break;
            case "shoes":
                shoes = item;
                break;
            case "weapon":
                weapon = item;
                break;
            default:
                supply = item;
                break;
        }
        System.out.println(item + " has been equipped to your " + slot + ".");
    }

    // Equips an item based on its name
    public void equipItemByName(String item) {
        String itemType = getItemType(item);
        if (itemType.equals("unknown")) {
            equipItem("supply", item);
        } else {
            equipItem(itemType, item);
        }
    }

    // Method to add XP and check for level-up
    public void addXP(int amount) {
        xp += amount;
        System.out.println("You gained " + amount + " XP!");
        checkLevelUp();
    }

    // Method to check if the player has enough XP to level up
    private void checkLevelUp() {
        int xpForNextLevel = level * 50;
        if (xp >= xpForNextLevel) {
            level++;
            xp -= xpForNextLevel;
            System.out.println("Congratulations! You've reached level " + level + "!");
        }
    }

    // Method to simulate beating an encounter
    public void beatEncounter() {
        addXP(25);
    }
}
//auto equp by ai