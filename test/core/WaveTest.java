package core;

import events.WaveEvent;
import events.WaveListener;
import factory.MonsterFactory;
import factory.WaveFactory;
import monster.Monster;
import monster.MovingMonsterStrategy;
import monster.PlainRoadMoving;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class WaveTest implements WaveListener {
    private final Path path = Paths.get("test", "road", "resources", "one_road_segment_one_road_cell.txt")
            .toAbsolutePath()
            .normalize();
    private final Field field = new Field(path.toString());

    private final MonsterFactory monsterFactory = new MonsterFactory();
    private final Position monsterPosition = new Position(100, 100);
    private final MovingMonsterStrategy strategy = new PlainRoadMoving(monsterPosition, field, 100);
    private final MovingMonsterStrategy zeroSpeedStrategy = new PlainRoadMoving(monsterPosition, field, 0);
    private final int monsterFullHealth = 100;

    private final WaveFactory waveFactory = new WaveFactory();

    int monsterDeaths;
    int monsterReachedEnd;
    int waveEnd;
    @BeforeEach
    void setUp() {
        monsterDeaths = 0;
        monsterReachedEnd = 0;
        waveEnd = 0;
    }

    @Test
    void monsterDieWaveEnd() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(zeroSpeedStrategy.clone());
        monsterQueue.add(monster);
        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        wave.removeListener(field);
        wave.updateMonsters(System.currentTimeMillis()); // initial update

        int expectedAliveMonsters = monstersCount - 1;

        monster.applyDamage(monsterFullHealth);
        wave.updateMonsters(System.currentTimeMillis() + TimeUnit.MILLISECONDS.toMillis(1));
        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(1, monsterDeaths);
        assertEquals(0, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());
    }

    @Test
    void monsterReachedEndWaveEnd() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        wave.removeListener(field);
        wave.updateMonsters(System.currentTimeMillis()); // initial update

        int expectedAliveMonsters = monstersCount - 1;


        wave.updateMonsters(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(100));
        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(1, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());
    }

    @Test
    void severalMonstersAllReachedEnd() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();
        int MONSTERS_COUNT = 4;
        for (int i = 0; i < MONSTERS_COUNT; i++) {
            monsterQueue.add(monsterFactory.createMonster(strategy.clone()));
        }
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);
        for (int i = 0; i < MONSTERS_COUNT; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        wave.removeListener(field);
        wave.updateMonsters(System.currentTimeMillis()); // initial update

        int expectedAliveMonsters = 0;

        wave.updateMonsters(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(4, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());
    }

    @Override
    public void onMonsterDeath(WaveEvent event) {
        monsterDeaths++;
    }

    @Override
    public void onMonsterReachedEnd(WaveEvent event) {
        monsterReachedEnd++;
    }

    @Override
    public void onWaveEnd(WaveEvent event) {
        waveEnd++;
    }
}