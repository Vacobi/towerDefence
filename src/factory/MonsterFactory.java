package factory;

import collision.Hitbox;
import monster.Monster;
import monster.MovingMonsterStrategy;

import java.util.LinkedList;
import java.util.Queue;

public class MonsterFactory {

    private final int defaultHitboxWidth;
    private final int defaultHitboxHeight;
    private final int defaultHealth;

    public MonsterFactory() {
        defaultHitboxWidth = 10;
        defaultHitboxHeight = 10;
        defaultHealth = 100;
    }

    public Monster createMonster(MovingMonsterStrategy strategy) {
        Hitbox hitbox = new Hitbox(
                strategy.currentPosition().getX() - defaultHitboxWidth / 2,
                strategy.currentPosition().getY() + defaultHitboxHeight / 2,
                defaultHitboxWidth,
                defaultHitboxHeight,
                0);
        return new Monster(hitbox, strategy, defaultHealth);
    }

    public Queue<Monster> createMonsters(int count, MovingMonsterStrategy strategy) {
        Queue<Monster> monsters = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            monsters.add(createMonster(strategy));
        }
        return monsters;
    }
}
