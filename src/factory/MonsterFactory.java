package factory;

import collision.HitboxParameters;
import core.AbstractCell;
import monster.Monster;
import monster.MovingMonsterStrategy;

import java.util.LinkedList;
import java.util.Queue;

public class MonsterFactory {

    private final HitboxParameters defaultHitboxParameters;
    private final int defaultHealth;

    public MonsterFactory() {
        int width = (int) (AbstractCell.getSize() * 0.7);
        int height = (int) (AbstractCell.getSize() * 0.7);
        defaultHitboxParameters = new HitboxParameters(width, height, Math.toRadians(0));
        defaultHealth = 100;
    }

    //------------------------------------------------------------------------------------------------------------------

    public Monster createMonster(MovingMonsterStrategy strategy) {
        return new Monster(defaultHitboxParameters, strategy, defaultHealth);
    }

    public Queue<Monster> createMonsters(int count, MovingMonsterStrategy strategy) {
        Queue<Monster> monsters = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            monsters.add(createMonster(strategy.clone()));
        }
        return monsters;
    }
}
