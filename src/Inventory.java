package src;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private final List<String> bag = new ArrayList<>();
    private final Map<String, Integer> consumables = new LinkedHashMap<>();

    private String weapon = "Rusty Pipe";
    private String armor = "Torn Jacket";
    private int weaponMod = 0;

    public Inventory() {
        consumables.put("medkit", 1);
        consumables.put("energy drink", 1);
        consumables.put("bomb", 0);
    }

    public void addItem(String item) {
        bag.add(item);
    }

    public int weaponBonus() {
        int base = switch (weapon.toLowerCase()) {
            case "survivor machete" -> 5;
            case "electro bat" -> 7;
            case "railgun prototype" -> 10;
            default -> 2;
        };
        return base + weaponMod;
    }

    public int armorBonus() {
        return switch (armor.toLowerCase()) {
            case "riot armor" -> 4;
            case "titan plating" -> 6;
            default -> 1;
        };
    }

    public void equip(String item) {
        String lowered = item.toLowerCase();
        if (lowered.contains("machete") || lowered.contains("bat") || lowered.contains("railgun")) {
            weapon = item;
            System.out.println("You equipped " + item + " as your weapon.");
            return;
        }
        if (lowered.contains("armor") || lowered.contains("plating") || lowered.contains("jacket")) {
            armor = item;
            System.out.println("You equipped " + item + " as your armor.");
            return;
        }
        System.out.println("You can't equip that.");
    }

    public boolean useConsumable(String item) {
        String key = item.toLowerCase();
        Integer amount = consumables.get(key);
        if (amount == null || amount <= 0) {
            return false;
        }
        consumables.put(key, amount - 1);
        return true;
    }

    public void addConsumable(String item, int amount) {
        String key = item.toLowerCase();
        consumables.put(key, consumables.getOrDefault(key, 0) + amount);
    }

    public void upgradeWeapon() {
        weaponMod += 2;
        System.out.println("Your weapon is reinforced. Permanent ATK +2.");
    }

    public void showInventory() {
        System.out.println("\n\u001B[36m=== INVENTORY ===\u001B[0m");
        System.out.println("Weapon: " + weapon + " (ATK +" + weaponBonus() + ")");
        System.out.println("Armor : " + armor + " (DEF +" + armorBonus() + ")");
        System.out.println("Bag items: " + (bag.isEmpty() ? "none" : String.join(", ", bag)));
        System.out.println("Consumables:");
        consumables.forEach((k, v) -> System.out.println("- " + k + " x" + v));
    }
}
