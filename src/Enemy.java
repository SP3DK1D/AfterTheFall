package src;

/** Enemy placed on the map with simple AI and combat stats. */
public class Enemy {
    private final String name;
    private int hp;
    private final int attack;
    private final int xpReward;
    private final int scrapReward;
    private Position position;
    private final int detectionRadius;

    public Enemy(String name, int hp, int attack, int xpReward, int scrapReward, Position position, int detectionRadius) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.xpReward = xpReward;
        this.scrapReward = scrapReward;
        this.position = position;
        this.detectionRadius = detectionRadius;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getAttack() { return attack; }
    public int getXpReward() { return xpReward; }
    public int getScrapReward() { return scrapReward; }
    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }
    public int getDetectionRadius() { return detectionRadius; }

    public void takeDamage(int amount) { hp -= amount; }
    public boolean isAlive() { return hp > 0; }
}
