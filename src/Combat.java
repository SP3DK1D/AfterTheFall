package src;

/** Mini-game combat: timing bar. */
public class Combat {

    public boolean fight(Player player, Enemy enemy, InputHandler input) {
        System.out.println("\nCombat start vs " + enemy.getName() + "!");
        while (player.isAlive() && enemy.isAlive()) {
            int performance = timingMiniGame(enemy, input);
            if (performance >= 85) {
                int dmg = player.attackDamage() + 18;
                enemy.takeDamage(dmg);
                System.out.println("CRIT! " + dmg + " damage.");
            } else if (performance >= 45) {
                int dmg = player.attackDamage() + 5;
                enemy.takeDamage(dmg);
                System.out.println("Solid hit: " + dmg + " damage.");
            } else {
                int incoming = Math.max(1, enemy.getAttack() - player.getDefense());
                player.takeDamage(incoming);
                System.out.println("Missed timing. Enemy hits for " + incoming + ".");
            }
            System.out.println("Enemy HP: " + Math.max(0, enemy.getHp()) + " | Your HP: " + player.getHealth());
        }

        if (!player.isAlive()) {
            return false;
        }
        player.gainRewards(enemy.getXpReward(), enemy.getScrapReward());
        System.out.println("Victory! +" + enemy.getXpReward() + " XP, +" + enemy.getScrapReward() + " scraps.");
        return true;
    }

    private int timingMiniGame(Enemy enemy, InputHandler input) {
        int barSize = 30;
        int sweetSize = Math.max(2, 8 - enemy.getAttack() / 4);
        int sweetStart = (barSize / 2) - (sweetSize / 2);
        int marker = 0;
        int dir = 1;
        int loops = 55;

        System.out.println("Timing mini-game: press SPACE when marker is in [===].");
        for (int t = 0; t < loops; t++) {
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < barSize; i++) {
                if (i == marker) {
                    bar.append('|');
                } else if (i >= sweetStart && i < sweetStart + sweetSize) {
                    bar.append('=');
                } else {
                    bar.append('-');
                }
            }
            System.out.print("\r" + bar);

            Character key = input.isInstantMode() ? input.pollKeyNonBlocking() : null;
            if (key != null && (key == ' ' || key == '\n')) {
                System.out.println();
                int dist = distanceToSweet(marker, sweetStart, sweetSize);
                return Math.max(0, 100 - dist * 20);
            }

            sleep(35);
            marker += dir;
            if (marker == barSize - 1 || marker == 0) {
                dir *= -1;
            }
        }
        System.out.println();

        if (!input.isInstantMode()) {
            System.out.print("Press Enter to stop marker (fallback): ");
            input.readLineBlocking();
            int dist = distanceToSweet(marker, sweetStart, sweetSize);
            return Math.max(0, 100 - dist * 20);
        }
        return 0;
    }

    private int distanceToSweet(int marker, int start, int size) {
        int end = start + size - 1;
        if (marker >= start && marker <= end) {
            return 0;
        }
        if (marker < start) {
            return start - marker;
        }
        return marker - end;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
