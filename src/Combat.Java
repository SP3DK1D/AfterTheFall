package src;

import java.util.Random;
import java.util.Scanner;

public class Combat {
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();
    
    public List<String> encounters;

    public Combat() {
        encounters = new ArrayList<>();
        int ecounter = new hashmap();
        int encounterHealth = new hashmap();
        int encounterDamage = new hashmap();
        int encounterDefence = new hashmap();
    }

    public void encounterData() {
        encounters.add("bandit");
        encounterHealth.put("bandit", 10);
        encounterDamage.put("bandit", 2);
        encounterDefence.put("bandit", 1);

        encounters.add("mutated bandit");
        encounterHealth.put("zombie", 20);
        encounterDamage.put("zombie", 4);
        encounterDefence.put("zombie", 2);

        encounters.add("zombie");
        encounterHealth.put("mutated zombie", 30);
        encounterDamage.put("mutated zombie", 6);
        encounterDefence.put("mutated zombie", 3);

        encounters.add("giant zombie");
        encounterHealth.put("giant zombie", 40);
        encounterDamage.put("giant zombie", 8);
        encounterDefence.put("giant zombie", 4);

        encounters.add("city boss zombie");
        encounterHealth.put("city boss zombie", 50);
        encounterDamage.put("city boss zombie", 10);
        encounterDefence.put("city boss zombie", 5);
    }

    public void engageCombat(Player player, Enemy enemy) {
        System.out.println("Combat started with " + enemy.getName());
        while (enemy.getHealth() > 0 && player.getHealth() > 0) {
            System.out.println("You can block(b) or attack(a) choose one!");

            String choice = scanner.nextLine().toLowerCase();
            switch (choice) {
                case "attack":
                case "a":
                    if (random.nextInt(100) < 50) {
                        System.out.println("You attack the " + enemy.getName() + "!");
                        int playerDamage = player.getWeapon().getAttack();
                        enemy.takeDamage(playerDamage);
                        System.out.println("You deal " + playerDamage + " damage!");
                    } else {
                        System.out.println("Your attack missed!");
                    }
                    break;

                case "block":
                case "b":
                    if (random.nextInt(100) < 50) {
                        System.out.println("You blocked " + enemy.getName() + "'s attack!");
                        System.out.println("You take no damage!");
                    } else {
                        System.out.println("You failed to block!");
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please choose again.");
                    continue;
            }

            // Enemy's turn to attack
            if (enemy.getHealth() > 0) {
                if (random.nextInt(100) < 50) {
                    System.out.println(enemy.getName() + " attacks you!");
                    int enemyDamage = enemy.getAttack();
                    player.takeDamage(enemyDamage);
                    System.out.println("You take " + enemyDamage + " damage!");
                } else {
                    System.out.println(enemy.getName() + " missed their attack!");
                }
            }

            if (player.getHealth() <= 0) {
                System.out.println("You have been defeated by the " + enemy.getName() + "!");
                System.out.println("Game Over!");
                System.exit(0);
            }

            if (enemy.getHealth() <= 0) {
                System.out.println("You have defeated the " + enemy.getName() + "!");
                System.out.println("You gain 10 experience points!");
                player.gainExperience(10);
            }
        }
    }

    public static class Enemy {
        private String name;
        private int health;
        private int attack;

        public Enemy(String name, int health, int attack) {
            this.name = name;
            this.health = health;
            this.attack = attack;
        }

        public String getName() {
            return name;
        }

        public int getHealth() {
            return health;
        }

        public void takeDamage(int damage) {
            health -= damage;
        }

        public int getAttack() {
            return attack;
        }

        @Override
        public String toString() {
            return name + " (Health: " + health + ", Attack: " + attack + ")";
        }
    }
}
