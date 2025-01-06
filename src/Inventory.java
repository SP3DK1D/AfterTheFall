package src;

import java.util.ArrayList;
import java.util.Scanner;

class Item {
    private String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

class Armor extends Item {
    public Armor(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

class Weapon extends Item {
    public Weapon(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

public class Inventory {
    private ArrayList<Item> items;
    private Armor hat;
    private Armor vest;
    private Armor pants;
    private Armor shoes;
    private Weapon weapon;

    public Inventory() {
        items = new ArrayList<>();
    }

    public void findItem(Item item) {
        items.add(item);
        System.out.println("You found a " + item.getName() + "!");
    }

    public void showItems() {
        if (items.isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("Your inventory contains:");
            for (Item item : items) {
                System.out.println("- " + item);
            }
        }
    }

    public void equipItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                if (item instanceof Armor) {
                    Armor armor = (Armor) item;
                    if (itemName.toLowerCase().contains("hat")) {
                        hat = armor;
                    } else if (itemName.toLowerCase().contains("vest")) {
                        vest = armor;
                    } else if (itemName.toLowerCase().contains("pants")) {
                        pants = armor;
                    } else if (itemName.toLowerCase().contains("shoes")) {
                        shoes = armor;
                    }
                    System.out.println("You equipped " + itemName + ".");
                } else if (item instanceof Weapon) {
                    weapon = (Weapon) item;
                    System.out.println("You equipped " + itemName + ".");
                }
                items.remove(item);
                return;
            }
        }
        System.out.println("Item not found in inventory.");
    }

    public void showEquippedItems() {
        System.out.println("Equipped items:");
        System.out.println("Hat: " + (hat != null ? hat : "None"));
        System.out.println("Vest: " + (vest != null ? vest : "None"));
        System.out.println("Pants: " + (pants != null ? pants : "None"));
        System.out.println("Shoes: " + (shoes != null ? shoes : "None"));
        System.out.println("Weapon: " + (weapon != null ? weapon : "None"));
    }

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("Welcome to the game! Type 'i' to see your inventory, 'equip [item]' to equip an item, 'show equipped' to see equipped items, or 'exit' to quit.");

        // Example items found in the game
        inventory.findItem(new Armor("Steel Hat"));
        inventory.findItem(new Armor("Kevlar Vest"));
        inventory.findItem(new Armor("Combat Pants"));
        inventory.findItem(new Armor("Running Shoes"));
        inventory.findItem(new Weapon("Sword"));

        while (true) {
            System.out.print("> ");
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("i")) {
                inventory.showItems();
            } else if (input.startsWith("equip ")) {
                String itemName = input.substring(6);
                inventory.equipItem(itemName);
            } else if (input.equalsIgnoreCase("show equipped")) {
                inventory.showEquippedItems();
            } else if (input.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println("Invalid command. Type 'i' to see your inventory, 'equip [item]' to equip an item, 'show equipped' to see equipped items, or 'exit' to quit.");
            }
        }

        scanner.close();
    }
}