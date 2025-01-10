package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Combat {

    public List<String> encounters;
    public HashMap<String, Integer> encounterHealth;
    public HashMap<String, Integer> encounterDamage;
    public HashMap<String, Integer> encounterDefence;



    public Combat() {
        // Combat combat = new Combat();
        encounters = new ArrayList<>();
        encounterHealth = new HashMap<>();
        encounterDamage = new HashMap<>();
        encounterDefence = new HashMap<>();
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
        encounters.add("zombie");
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
}

