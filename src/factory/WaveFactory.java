package factory;

import core.Field;
import monster.Monster;
import core.Wave;
import monster.MovingMonsterStrategy;
import monster.PlainRoadMoving;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class WaveFactory {
    private final long defaultDelay;
    private final int defaultMonstersCount;
    private final MonsterFactory monsterFactory;

    public WaveFactory() {
        defaultDelay = TimeUnit.SECONDS.toMillis(2);
        defaultMonstersCount = 20;
        monsterFactory = new MonsterFactory();
    }

    public Wave createWave(int number, Field field) {
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 20 * (number - number / 2));

        Queue<Monster> monstersToSpawn = monsterFactory.createMonsters(defaultMonstersCount * number / 2, strategy);

        Wave wave = new Wave(monstersToSpawn, defaultDelay / number, number);
        return wave;
    }
}
