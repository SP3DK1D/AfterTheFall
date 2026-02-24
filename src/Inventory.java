package src;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Player inventory, consumables, and equipment bonuses. */
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

    public void autoEquipIfPossible(String item) {
        String lowered = item.toLowerCase();
        if (lowered.contains("machete") || lowered.contains("bat") || lowered.contains("railgun")) {
            weapon = item;
            return;
        }
        if (lowered.contains("armor") || lowered.contains("plating") || lowered.contains("jacket")) {
            armor = item;
        }
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

    public boolean buyUpgradeForScraps(Player player, String choice) {
        return switch (choice) {
            case "1" -> buyConsumable(player, "medkit", 15);
            case "2" -> buyConsumable(player, "energy drink", 12);
            case "3" -> buyConsumable(player, "bomb", 20);
            case "4" -> {
                if (player.getScraps() < 60) {
                    yield false;
                }
                player.addScraps(-60);
                addItem("Titan Plating");
                autoEquipIfPossible("Titan Plating");
                yield true;
            }
            case "5" -> {
                if (player.getScraps() < 45) {
                    yield false;
                }
                player.addScraps(-45);
                weaponMod += 2;
                yield true;
            }
            default -> false;
        };
    }

    private boolean buyConsumable(Player player, String item, int price) {
        if (player.getScraps() < price) {
            return false;
        }
        player.addScraps(-price);
        addConsumable(item, 1);
        return true;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTORY ===\n");
        sb.append("Weapon: ").append(weapon).append(" (ATK +").append(weaponBonus()).append(")\n");
        sb.append("Armor : ").append(armor).append(" (DEF +").append(armorBonus()).append(")\n");
        sb.append("Bag   : ").append(bag.isEmpty() ? "none" : String.join(", ", bag)).append("\n");
        sb.append("Consumables:\n");
        consumables.forEach((k, v) -> sb.append("- ").append(k).append(" x").append(v).append("\n"));
        return sb.toString();
    }
}
