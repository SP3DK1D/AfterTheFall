package src;

/** Enemy placed on the map. */
public class Enemy {
    private final String name;
    private int hp;
    private final int attack;
    private final int xpReward;
    private final int scrapReward;
    private final Position position;

    public Enemy(String name, int hp, int attack, int xpReward, int scrapReward, Position position) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.xpReward = xpReward;
        this.scrapReward = scrapReward;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public int getAttack() {
        return attack;
    }

    public int getXpReward() {
        return xpReward;
    }

    public int getScrapReward() {
        return scrapReward;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
