package src;

import java.util.Scanner;

/** Turn-based combat (deterministic update order). */
public class Combat {

    public boolean fight(Player player, Enemy enemy, Scanner scanner) {
        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\n=== COMBAT ===");
            System.out.println("Enemy: " + enemy.getName() + " | HP: " + enemy.getHp());
            System.out.println("You  : HP " + player.getHealth() + " | Energy " + player.getEnergy());
            System.out.println("Actions: [A]ttack, S[K]ill, [I]tem, [F]lee");
            System.out.print("> ");

            String action = scanner.nextLine().trim().toLowerCase();

            if (action.equals("a") || action.equals("attack")) {
                enemy.takeDamage(player.attackDamage());
                System.out.println("You attack for " + player.attackDamage() + " damage.");
            } else if (action.equals("k") || action.equals("skill")) {
                int damage = player.skillDamage();
                if (damage <= 0) {
                    System.out.println("Not enough energy.");
                } else {
                    enemy.takeDamage(damage);
                    System.out.println("Skill blast deals " + damage + " damage.");
                }
            } else if (action.equals("i") || action.equals("item")) {
                System.out.print("Use item (medkit / energy drink / bomb): ");
                String item = scanner.nextLine().trim().toLowerCase();
                if (!player.useItem(item)) {
                    System.out.println("You don't have that item.");
                } else {
                    System.out.println("Used " + item + ".");
                }
            } else if (action.equals("f") || action.equals("flee")) {
                System.out.println("You escape from combat.");
                return false;
            } else {
                System.out.println("Invalid action.");
            }

            if (enemy.isAlive()) {
                int incoming = Math.max(1, enemy.getAttack() - player.getDefense());
                player.takeDamage(incoming);
                System.out.println(enemy.getName() + " hits you for " + incoming + ".");
            }
        }

        if (!player.isAlive()) {
            return false;
        }

        player.gainRewards(enemy.getXpReward(), enemy.getScrapReward());
        System.out.println("Victory! +" + enemy.getXpReward() + " XP, +" + enemy.getScrapReward() + " scraps.");
        return true;
    }
}
