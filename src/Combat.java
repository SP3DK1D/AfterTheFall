package src;

import java.util.Random;
import java.util.Scanner;

public class Combat {
    private final Random random = new Random();

    public boolean fight(Player player, String enemy, Scanner scanner) {
        int enemyHp = enemyBaseHp(enemy) + random.nextInt(8);
        int enemyAtk = enemyBaseAtk(enemy);

        System.out.println("\n\u001B[31m⚔ Encounter: " + enemy.toUpperCase() + " appears!\u001B[0m\n");

        while (player.isAlive() && enemyHp > 0) {
            String intent = random.nextInt(100) < 30 ? "heavy strike" : "quick slash";
            System.out.println("You HP " + player.getHealth() + " | Enemy HP " + enemyHp + " | Intent: " + intent);
            System.out.println("Actions: attack(a), skill(k), item(i), flee(f)");
            String action = scanner.nextLine().trim().toLowerCase();

            if (action.equals("attack") || action.equals("a")) {
                int damage = player.attackDamage(random);
                enemyHp -= damage;
                System.out.println("You hit for " + damage + " damage.");
            } else if (action.equals("skill") || action.equals("k")) {
                int damage = player.useSkill(random);
                if (damage <= 0) {
                    System.out.println("No energy! You fail to cast skill.");
                } else {
                    enemyHp -= damage;
                    System.out.println("Skill blast deals " + damage + " damage!");
                }
            } else if (action.equals("item") || action.equals("i")) {
                System.out.println("Use: medkit | energy drink | bomb");
                String itemChoice = scanner.nextLine().trim().toLowerCase();
                if (!player.useItem(itemChoice)) {
                    System.out.println("You don't have that item.");
                }
            } else if (action.equals("flee") || action.equals("f")) {
                if (random.nextInt(100) < 35) {
                    System.out.println("You escaped!");
                    return false;
                }
                System.out.println("Couldn't escape!");
            } else {
                System.out.println("You hesitate and lose tempo.");
            }

            if (enemyHp > 0) {
                int intentBonus = intent.equals("heavy strike") ? 4 : 0;
                int incoming = Math.max(1, enemyAtk + random.nextInt(4) + intentBonus - player.getDefense());
                player.takeDamage(incoming);
                System.out.println(enemy + " hits you for " + incoming + ".");
            }
        }

        if (!player.isAlive()) {
            return false;
        }

        int xp = 25 + random.nextInt(25);
        int scraps = 15 + random.nextInt(20);
        player.winBattle(xp, scraps);
        System.out.println("Victory! +" + xp + " XP and +" + scraps + " scraps.");
        return true;
    }

    private int enemyBaseHp(String enemy) {
        return switch (enemy) {
            case "raider champion" -> 65;
            case "mutant brute" -> 90;
            case "sentry overmind" -> 105;
            case "omega abomination" -> 140;
            default -> 50;
        };
    }

    private int enemyBaseAtk(String enemy) {
        return switch (enemy) {
            case "raider champion" -> 10;
            case "mutant brute" -> 14;
            case "sentry overmind" -> 16;
            case "omega abomination" -> 19;
            default -> 8;
        };
    }
}
