package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Combat {
    public List<String> encounters;
    public Map<String, Integer> encounterHealth;
    public Map<String, Integer> encounterDamage;
    public Map<String, Integer> encounterDefence;
    public int playerHealth;
    public boolean blockNextAttack;
    public String currentRoom;

    public Combat() {
        encounters = new ArrayList<>();
        encounterHealth = new HashMap<>();
        encounterDamage = new HashMap<>();
        encounterDefence = new HashMap<>();
        currentRoom = "village prison cell"; // players currentRoom
        playerHealth = 100; // players health
        blockNextAttack = false; // part of the combat system where if the block us successful the next attack will be blocked
        encounterData(); // idk why i gotta put this here but it works
    }

    public void encounterData() {
        // bandit
        encounters.add("bandit");
        encounterHealth.put("bandit", 10);
        encounterDamage.put("bandit", 2);
        encounterDefence.put("bandit", 1);

        // mutated bandit
        encounters.add("zombie");
        encounterHealth.put("zombie", 20);
        encounterDamage.put("zombie", 4);
        encounterDefence.put("zombie", 2);

        // zombie
        encounters.add("mutated zombie");
        encounterHealth.put("mutated zombie", 30);
        encounterDamage.put("mutated zombie", 6);
        encounterDefence.put("mutated zombie", 3);

        // giant zombie
        encounters.add("giant zombie");
        encounterHealth.put("giant zombie", 40);
        encounterDamage.put("giant zombie", 8);
        encounterDefence.put("giant zombie", 4);

        // city boss zombie
        encounters.add("city boss zombie");
        encounterHealth.put("city boss zombie", 50);
        encounterDamage.put("city boss zombie", 10);
        encounterDefence.put("city boss zombie", 5);
    }

    public void checkRoomForEncounter(String roomEncounter) {
        if (encounters.contains(roomEncounter)) {
            System.out.println("\n*** You have encountered a " + roomEncounter + "! ***\n");
            startCombat(roomEncounter);
        }
    }

    private void startCombat(String encounter) {
        Integer encounterHP = encounterHealth.get(encounter);
        if (encounterHP == null) {
            System.out.println("Error: Encounter health not found for " + encounter);
            return;
        }
        Scanner scanner = new Scanner(System.in);

        while (playerHealth > 0 && encounterHP > 0) {
            System.out.println("\n====================================");
            System.out.println("Player Health: " + playerHealth + " | " + encounter + " Health: " + encounterHP);
            System.out.println("====================================");
            System.out.println("Choose your action: (a) Attack (b) Block");
            System.out.print("> ");
            String action = scanner.nextLine();

            if (action.equals("a")) {
                if (Math.random() > 0.5) {
                    int damage = 10; // Assuming player deals 10 damage
                    encounterHP -= damage;
                    System.out.println("\n>>> You successfully attacked the " + encounter + " for " + damage + " damage!");
                } else {
                    System.out.println("\n>>> Your attack missed!");
                    if (!blockNextAttack && Math.random() > 0.5) {
                        playerHealth -= encounterDamage.get(encounter);
                        System.out.println(">>> The " + encounter + " attacked you for " + encounterDamage.get(encounter) + " damage!");
                    } else {
                        System.out.println(">>> The " + encounter + "'s attack missed!");
                    }
                    blockNextAttack = false;
                }
            } else if (action.equals("b")) {
                if (Math.random() > 0.5) {
                    blockNextAttack = true;
                    System.out.println("\n>>> You successfully blocked the next attack!");
                } else {
                    System.out.println("\n>>> Your block failed!");
                    if (Math.random() > 0.5) {
                        playerHealth -= encounterDamage.get(encounter);
                        System.out.println(">>> The " + encounter + " attacked you for " + encounterDamage.get(encounter) + " damage!");
                    } else {
                        System.out.println(">>> The " + encounter + "'s attack missed!");
                    }
                }
            } else {
                System.out.println("\nInvalid action. Please choose (1) Attack or (2) Block.");
            }
        }

        if (playerHealth <= 0) {
            System.out.println("\n*** YOU DIED ***");
            playerHealth = 100; // Reset player health for retry
            startCombat(encounter); // Restart combat
        } else if (encounterHP <= 0) {
            System.out.println("\n*** YOU DEFEATED THE " + encounter.toUpperCase() + " ***");
            // Print the current room description after winning the fight
            Room room = new Room(); 
            System.out.println(room.getRooms().get(this.currentRoom));
        }
    }
}
