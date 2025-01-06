package src;

import java.util.Random;
import java.util.Scanner;

public class Fighting {

static class Player {
    private int health;

    public Player(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health -= damage;
    }
}

    public static void main(String[] args) {
        Player player = new Player(100);
        Room room = new Room();

        Zombie[] zombies = {
            new Zombie("Walker", 50),
            new Zombie("Runner", 60),
            new Zombie("Crawler", 40),
            new Zombie("Brute", 80)
        };

        if (room.playerEntersNewRoom()) {
            Random random = new Random();
            if (random.nextInt(10) == 0) {
                Zombie zombie = zombies[random.nextInt(zombies.length)];
                System.out.println("A " + zombie.getName() + " has spawned!");
                miniGame(player, zombie);
            } else {
                System.out.println("No zombies spawned this time.");
            }
        }
    }

    public static void miniGame(Player player, Zombie zombie) {
        try (Scanner scanner = new Scanner(System.in)) {
            String sentence = "Type this sentence correctly!";
            System.out.println("Quick! Type the following sentence in under 10 seconds:");
            System.out.println(sentence);

            long startTime = System.currentTimeMillis();
            String userInput = scanner.nextLine();
            long endTime = System.currentTimeMillis();

            if (userInput.equals(sentence) && (endTime - startTime) < 10000) {
                zombie.takeDamage(20);
                System.out.println("You dealt 20 damage to the " + zombie.getName() + "!");
            } else {
                player.takeDamage(20);
                System.out.println("You took 20 damage!");
            }

            System.out.println("Player health: " + player.getHealth());
            System.out.println(zombie.getName() + " health: " + zombie.getHealth());
        }
    }
}

class Zombie {
    private String name;
    private int health;

    public Zombie(String name, int health) {
        this.name = name;
        this.health = health;
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
}
