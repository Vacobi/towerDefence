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
import java.util.List;
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
    void monsterDieWaveEnd() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(zeroSpeedStrategy.clone());
        monsterQueue.add(monster);

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);

        int monstersCount = monsterQueue.size();
        long lastSpawnTime = System.currentTimeMillis();
        for (int i = 0; i < monstersCount; i++) {
            lastSpawnTime += TimeUnit.MILLISECONDS.toMillis(1);
            wave.spawnMonsters(lastSpawnTime);
        }
        wave.removeListener(field);

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
    void monsterReachedEndWaveEnd() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);

        int monstersCount = monsterQueue.size();
        long lastSpawnTime = System.currentTimeMillis();
        for (int i = 0; i < monstersCount; i++) {
            lastSpawnTime += TimeUnit.MILLISECONDS.toMillis(1);
            wave.spawnMonsters(lastSpawnTime);
        }
        wave.removeListener(field);

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
    void severalMonstersAllReachedEnd() {
        int monstersCount = 4;
        Queue<Monster> monsterQueue = new LinkedList<>();
        for (int i = 0; i < monstersCount; i++) {
            monsterQueue.add(monsterFactory.createMonster(strategy.clone()));
        }

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);

        long lastSpawnTime = System.currentTimeMillis();
        for (int i = 0; i < monstersCount; i++) {
            lastSpawnTime += TimeUnit.MILLISECONDS.toMillis(1);
            wave.spawnMonsters(lastSpawnTime);
        }
        wave.removeListener(field);

        int expectedAliveMonsters = 0;

        wave.updateMonsters(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(4, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());
    }

    @Test
    void severalMonstersSeveralDieSeveralReachedEnd(){
        Queue<Monster> monsterQueue = new LinkedList<>();
        int monstersCount = 5;

        List<Monster> monsters = new LinkedList<>();
        for (int i = 0; i < monstersCount; i++) {
            Monster monster = monsterFactory.createMonster(strategy.clone());
            monsters.add(monster);
            monsterQueue.add(monster);
        }

        Wave wave = waveFactory.createWave(1, monsterQueue, 1L, field);
        wave.addListener(this);
        field.setWave(wave);

        long lastSpawnTime = System.currentTimeMillis();
        for (int i = 0; i < monstersCount; i++) {
            lastSpawnTime += TimeUnit.MILLISECONDS.toMillis(1);
            wave.spawnMonsters(lastSpawnTime);
        }
        wave.removeListener(field);

        monsters.get(0).applyDamage(monsterFullHealth);
        monsters.get(2).applyDamage(monsterFullHealth);

        int expectedAliveMonsters = 0;

        wave.updateMonsters(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(2, monsterDeaths);
        assertEquals(3, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());
    }

    @Test
    void spawnInDelay() {
        int monstersCount = 2;
        Queue<Monster> monsterQueue = new LinkedList<>();
        for (int i = 0; i < monstersCount; i++) {
            monsterQueue.add(monsterFactory.createMonster(strategy.clone()));
        }

        Wave wave = waveFactory.createWave(1, monsterQueue, 1000L, field);
        wave.addListener(this);
        field.setWave(wave);
        wave.removeListener(field);

        int expectedAliveMonsters = 2;

        long lastSpawnTime = System.currentTimeMillis();
        wave.spawnMonsters(lastSpawnTime);
        lastSpawnTime += 1000L;
        wave.spawnMonsters(lastSpawnTime);

        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(0, monsterReachedEnd);
        assertEquals(0, waveEnd);
        assertFalse(wave.hasEnded());
    }

    @Test
    void spawnBeforeDelayPassed() {
        int monstersCount = 2;
        Queue<Monster> monsterQueue = new LinkedList<>();
        for (int i = 0; i < monstersCount; i++) {
            monsterQueue.add(monsterFactory.createMonster(strategy.clone()));
        }

        Wave wave = waveFactory.createWave(1, monsterQueue, 1000L, field);
        wave.addListener(this);
        field.setWave(wave);
        wave.removeListener(field);

        int expectedAliveMonsters = 1;

        long lastSpawnTime = System.currentTimeMillis();
        wave.spawnMonsters(lastSpawnTime);
        lastSpawnTime += 999L;
        wave.spawnMonsters(lastSpawnTime);

        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(0, monsterReachedEnd);
        assertEquals(0, waveEnd);
        assertFalse(wave.hasEnded());
    }

    @Test
    void updateMonstersAfterWaveEnd() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        int monstersCount = monsterQueue.size();

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);

        long lastSpawnTime = System.currentTimeMillis();
        for (int i = 0; i < monstersCount; i++) {
            lastSpawnTime += TimeUnit.MILLISECONDS.toMillis(1);
            wave.spawnMonsters(lastSpawnTime);
        }

        wave.removeListener(field);
        wave.updateMonsters(System.currentTimeMillis());

        //--------------------------------------------------------------------------------------------------------------

        int expectedAliveMonsters = monstersCount - 1;

        //--------------------------------------------------------------------------------------------------------------

        wave.updateMonsters(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(100));
        int actualAliveMonsters = wave.getAliveMonsters().size();

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(1, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());


        //--------------------------------------------------------------------------------------------------------------

        assertDoesNotThrow(() -> wave.updateMonsters(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(100)));
        actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(1, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());
    }

    @Test
    void spawnMonstersAfterEndOfQueue() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        int monstersCount = monsterQueue.size();

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);

        long lastSpawnTime = System.currentTimeMillis();
        for (int i = 0; i < monstersCount; i++) {
            lastSpawnTime += TimeUnit.MILLISECONDS.toMillis(1);
            wave.spawnMonsters(lastSpawnTime);
        }

        wave.removeListener(field);

        //--------------------------------------------------------------------------------------------------------------

        int expectedAliveMonsters = monstersCount;

        //--------------------------------------------------------------------------------------------------------------

        long lastUpdateTime = lastSpawnTime;
        final long spawnTime = lastUpdateTime + TimeUnit.MINUTES.toMillis(100);
        assertDoesNotThrow(() -> wave.spawnMonsters(spawnTime));
        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(0, monsterReachedEnd);
        assertEquals(0, waveEnd);
        assertFalse(wave.hasEnded());
    }

    @Test
    void spawnMonstersAfterWaveEnd() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        int monstersCount = monsterQueue.size();

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);

        long lastSpawnTime = System.currentTimeMillis();
        for (int i = 0; i < monstersCount; i++) {
            lastSpawnTime += TimeUnit.MILLISECONDS.toMillis(1);
            wave.spawnMonsters(lastSpawnTime);
        }

        wave.removeListener(field);

        //--------------------------------------------------------------------------------------------------------------

        int expectedAliveMonsters = monstersCount - 1;

        //--------------------------------------------------------------------------------------------------------------

        long lastUpdateTime = lastSpawnTime;
        lastUpdateTime += TimeUnit.MINUTES.toMillis(100);
        wave.spawnMonsters(lastUpdateTime);
        lastUpdateTime += TimeUnit.MINUTES.toMillis(100);
        wave.updateMonsters(lastUpdateTime);
        int actualAliveMonsters = wave.getAliveMonsters().size();

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(1, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());

        //--------------------------------------------------------------------------------------------------------------

        final long spawnTime = lastUpdateTime + TimeUnit.MINUTES.toMillis(100);
        assertDoesNotThrow(() -> wave.spawnMonsters(spawnTime));
        actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(1, monsterReachedEnd);
        assertEquals(1, waveEnd);
        assertTrue(wave.hasEnded());
    }

    @Test
    void updateMonstersWhenAliveMonstersEmpty() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        int monstersCount = monsterQueue.size();

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        wave.addListener(this);
        field.setWave(wave);

        wave.removeListener(field);

        //--------------------------------------------------------------------------------------------------------------

        int expectedAliveMonsters = monstersCount - 1;

        //--------------------------------------------------------------------------------------------------------------

        assertDoesNotThrow(() -> wave.updateMonsters(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(100)));
        int actualAliveMonsters = wave.getAliveMonsters().size();

        assertEquals(expectedAliveMonsters, actualAliveMonsters);
        assertEquals(0, monsterDeaths);
        assertEquals(0, monsterReachedEnd);
        assertEquals(0, waveEnd);
        assertFalse(wave.hasEnded());
    }

    @Test
    void zeroMonsters() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        assertThrows(IllegalArgumentException.class, () -> waveFactory.createWave(1, monsterQueue, 1, field));
    }

    @Test
    void delayIsZero() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        assertDoesNotThrow(() -> waveFactory.createWave(1, monsterQueue, 0, field));
    }

    @Test
    void delayIsNegative() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        assertThrows(IllegalArgumentException.class, () -> waveFactory.createWave(1, monsterQueue, -1, field));
    }

    @Test
    void numberIsNegative() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        assertThrows(IllegalArgumentException.class, () -> waveFactory.createWave(-1, monsterQueue, 1, field));
    }

    @Test
    void numberIsZero() {
        Queue<Monster> monsterQueue = new LinkedList<>();
        Monster monster = monsterFactory.createMonster(strategy.clone());
        monsterQueue.add(monster);
        assertThrows(IllegalArgumentException.class, () -> waveFactory.createWave(0, monsterQueue, 1, field));
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