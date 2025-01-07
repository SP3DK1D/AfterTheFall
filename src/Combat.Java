package src;

import java.util.Random;
import java.util.Scanner;

public class Combat {
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();

    public void engageCombat(Player player, Enemy enemy) {
        System.out.println("Combat started with " + enemy.getName());
        while (enemy.getHealth() > 0 && player.getHealth() > 0) {
            System.out.println("Solve the following math problem to deal damage:");
            int a = random.nextInt(10) + 1;
            int b = random.nextInt(10) + 1;
            System.out.println(a + " + " + b + " = ?");
            int answer = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (answer == a + b) {
                int damage = player.getWeapon().getAttack();
                enemy.takeDamage(damage);
                System.out.println("Correct! You dealt " + damage + " damage to " + enemy.getName());
            } else {
                System.out.println("Incorrect! You missed your attack.");
            }

            if (enemy.getHealth() > 0) {
                int enemyDamage = enemy.getAttack();
                player.takeDamage(enemyDamage);
                System.out.println(enemy.getName() + " dealt " + enemyDamage + " damage to you");
            } else {
                System.out.println("You defeated " + enemy.getName());
            }

            if (player.getHealth() <= 0) {
                System.out.println("You have been defeated by " + enemy.getName());
                break;
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