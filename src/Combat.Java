package src;

import java.util.*;

public class Combat {
    
    public List<String> encounters;
    public Map<String, Integer> encounterHealth;
    public Map<String, Integer> encounterDamage;
    public Map<String, Integer> encounterDefence;

    public Combat() {
        encounters = new ArrayList<>();
        encounterHealth = new HashMap<>();
        encounterDamage = new HashMap<>();
        encounterDefence = new HashMap<>();
    }

    public void encounterData() {
        encounters.add("bandit");
        encounterHealth.put("bandit", 10);
        encounterDamage.put("bandit", 2);
        encounterDefence.put("bandit", 1);

        encounters.add("mutated bandit");
        encounterHealth.put("mutated bandit", 20);
        encounterDamage.put("mutated bandit", 4);
        encounterDefence.put("mutated bandit", 2);

        encounters.add("zombie");
        encounterHealth.put("zombie", 30);
        encounterDamage.put("zombie", 6);
        encounterDefence.put("zombie", 3);

        encounters.add("giant zombie");
        encounterHealth.put("giant zombie", 40);
        encounterDamage.put("giant zombie", 8);
        encounterDefence.put("giant zombie", 4);
    }

    public boolean checkRoomEncounter(String room) {
        return encounters.contains(room);
    }

    public void fightEncounter(String encounter, Player player) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int encounterHP = encounterHealth.get(encounter);
        int playerHP = player.getHealth();

        while (encounterHP > 0 && playerHP > 0) {
            System.out.println("Choose your action: attack or block");
            String action = scanner.nextLine();

            if (action.equalsIgnoreCase("attack")) {
                if (random.nextBoolean()) {
                    encounterHP -= player.getDamage();
                    System.out.println("Attack successful! " + encounter + " HP: " + encounterHP);
                } else {
                    System.out.println("Attack missed!");
                }
            } else if (action.equalsIgnoreCase("block")) {
                if (random.nextBoolean()) {
                    System.out.println("Block successful! No damage taken.");
                } else {
                    playerHP -= encounterDamage.get(encounter);
                    System.out.println("Block failed! Player HP: " + playerHP);
                }
            }

            if (encounterHP > 0) {
                if (random.nextBoolean()) {
                    playerHP -= encounterDamage.get(encounter);
                    System.out.println(encounter + " attacks! Player HP: " + playerHP);
                } else {
                    System.out.println(encounter + " missed!");
                }
            }
        }

        if (playerHP > 0) {
            System.out.println("You defeated the " + encounter + "!");
            player.advanceRoom();
        } else {
            System.out.println("You were defeated by the " + encounter + "!");
            player.retreatRoom();
        }
    }
}
