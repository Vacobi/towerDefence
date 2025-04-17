package factory;

import collision.Hitbox;
import monster.Monster;
import monster.MovingMonsterStrategy;

import java.util.LinkedList;
import java.util.Queue;

public class MonsterFactory {

    private final Hitbox defaultHitbox;
    private final int defaultHealth;

    public MonsterFactory() {
        defaultHitbox = new Hitbox(0, 0, 1, 1, 0);
        defaultHealth = 100;
    }

    public Monster createMonster(MovingMonsterStrategy strategy) {
        return new Monster(defaultHitbox, strategy, defaultHealth);
    }

    public Queue<Monster> createMonsters(int count, MovingMonsterStrategy strategy) {
        Queue<Monster> monsters = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            monsters.add(createMonster(strategy));
        }
        return monsters;
    }
}
