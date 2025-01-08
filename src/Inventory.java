package src;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    public List<String> items; // List to store the items in the inventory
    public String helmet; // Slot for the helmet
    public String vest; // Slot for the vest
    public String pants; // Slot for the pants
    public String shoes; // Slot for the shoes
    public String weapon; // Slot for the weapon

    // Constructor to initialize the inventory
    public Inventory() {
        items = new ArrayList<>();
        helmet = "None";
        vest = "None";
        pants = "None";
        shoes = "None";
        weapon = "None";
    }

    // Adds an item to the inventory
    public void addItem(String item) {
        items.add(item);
        System.out.println("** " + item + " has been added to your inventory.**");
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
                System.out.println("Invalid slot.");
                return;
        }
        System.out.println(item + " has been equipped to your " + slot + ".");
    }
}