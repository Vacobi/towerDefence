package projectile.behavior;

import core.Field;
import core.Wave;
import factory.MonsterFactory;
import factory.WaveFactory;
import monster.Monster;
import monster.MovingMonsterStrategy;
import monster.PlainRoadMoving;
import org.junit.jupiter.api.Test;
import projectile.LaserProjectile;
import testutils.ProjectileFactory;
import utils.Direction;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class LaserBehaviorTest {

    private final Path path = Paths.get("test", "road", "resources", "one_road_segment_one_road_cell.txt")
            .toAbsolutePath()
            .normalize();
    private final Field field = new Field(path.toString());

    private final MonsterFactory monsterFactory = new MonsterFactory();
    private final Position monsterPosition = new Position(100, 100);
    private final MovingMonsterStrategy strategy = new PlainRoadMoving(
            monsterPosition,
            field,
            10
    );
    private final int monsterFullHealth = 100;

    private final ProjectileFactory projectileFactory = new ProjectileFactory();
    private final WaveFactory waveFactory = new WaveFactory();

    @Test
    void collidesWithMonster() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 1;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        behavior.applyEffect(System.currentTimeMillis());


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void noCollisionWithMonster() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        Position laserPosition = new Position(1000, 1000);
        LaserProjectile projectile = projectileFactory.createLaserProjectile(laserPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 0;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        behavior.applyEffect(System.currentTimeMillis());


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void collidesWithAllMonstersInTheSamePosition() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));
        monsterQueue.add(monsterFactory.createMonster(strategy));
        monsterQueue.add(monsterFactory.createMonster(strategy));
        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = monstersCount;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        behavior.applyEffect(System.currentTimeMillis());


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void collidesWithAllMonstersInDifferentPositions() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();
        int monsterHitboxWidth = monsterFactory.createMonster(strategy).getHitbox().getHitboxParameters().width();

        PlainRoadMoving strategyOfFirst = new PlainRoadMoving(new Position(100, 100), field, 10);
        monsterQueue.add(monsterFactory.createMonster(strategyOfFirst));

        PlainRoadMoving strategyOfSecond = new PlainRoadMoving(new Position(100 + monsterHitboxWidth, 100), field, 10);
        monsterQueue.add(monsterFactory.createMonster(strategyOfSecond));

        PlainRoadMoving strategyOfThird = new PlainRoadMoving(new Position(100 + monsterHitboxWidth * 2, 100), field, 10);
        monsterQueue.add(monsterFactory.createMonster(strategyOfThird));

        PlainRoadMoving strategyOfFourth = new PlainRoadMoving(new Position(100 + monsterHitboxWidth * 3, 100), field, 10);
        monsterQueue.add(monsterFactory.createMonster(strategyOfFourth));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        Position laserPosition = new Position(100, 100);
        LaserProjectile projectile = projectileFactory.createLaserProjectile(laserPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = monstersCount;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        behavior.applyEffect(System.currentTimeMillis());


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void collidesWithSeveralMonsters() throws InterruptedException {
        Position laserPosition = new Position(100, 100);
        LaserProjectile projectile = projectileFactory.createLaserProjectile(laserPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();

        Queue<Monster> monsterQueue = new LinkedList<>();
        int monsterHitboxWidth = monsterFactory.createMonster(strategy).getHitbox().getHitboxParameters().width();

        PlainRoadMoving strategyOfFirst = new PlainRoadMoving(new Position(100 + projectile.getDistance() * 2, 100), field, 10);
        monsterQueue.add(monsterFactory.createMonster(strategyOfFirst));

        PlainRoadMoving strategyOfSecond = new PlainRoadMoving(new Position(100 + monsterHitboxWidth, 100), field, 10);
        monsterQueue.add(monsterFactory.createMonster(strategyOfSecond));

        PlainRoadMoving strategyOfThird = new PlainRoadMoving(new Position(100 + projectile.getDistance() * 2, 100), field, 10);
        monsterQueue.add(monsterFactory.createMonster(strategyOfThird));

        PlainRoadMoving strategyOfFourth = new PlainRoadMoving(new Position(100 + monsterHitboxWidth * 2, 100), field, 10);
        monsterQueue.add(monsterFactory.createMonster(strategyOfFourth));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);


        int expectedDamagedMonstersCount = 2;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        behavior.applyEffect(System.currentTimeMillis());


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    // Тесты на время удара
    @Test
    void collidesWithSameMonsterInCooldown() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 1;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        long now = System.currentTimeMillis();
        behavior.applyEffect(now);
        behavior.applyEffect(now + projectile.getDamageCooldown() / 2);


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void collidesWithSameMonsterInBorderOfCooldown() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 1;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        long now = System.currentTimeMillis();
        behavior.applyEffect(now);
        behavior.applyEffect(now + projectile.getDamageCooldown());


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void collidesWithSameMonsterBeforeAndAfterCooldown() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 1;
        int expectedHealthAfterHit = monsterFullHealth - 2 * projectile.getDamage();


        long now = System.currentTimeMillis();
        behavior.applyEffect(now);
        behavior.applyEffect(now + projectile.getDamageCooldown() + 1);


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void collidesWithMonsterOnTheBorderOfActiveTime() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 1;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        behavior.applyEffect(behavior.getStartTime() + projectile.getActiveTime());


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void collidesWithMonsterAfterActiveTime() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 1;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        long now = System.currentTimeMillis();
        behavior.applyEffect(behavior.getStartTime() + projectile.getActiveTime() + 1);
        behavior.applyEffect(now + projectile.getDamageCooldown() + 1);


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertFalse(projectile.active());
    }

    @Test
    void collidesOtherMonsterBeforeCooldownOfPrevious() {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));
        monsterQueue.add(monsterFactory.createMonster(strategy));

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 2;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();


        long now = System.currentTimeMillis();
        wave.spawnMonsters(System.currentTimeMillis());
        behavior.applyEffect(now);
        wave.spawnMonsters(System.currentTimeMillis());
        behavior.applyEffect(now + 1);


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertTrue(projectile.active());
    }

    @Test
    void collidesWithMonsterAfterDeactivate() {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy));

        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        field.setWave(wave);

        LaserProjectile projectile = projectileFactory.createLaserProjectile(monsterPosition, Direction.EAST, field);
        LaserBehavior behavior = (LaserBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 0;
        int expectedHealthAfterHit = monsterFullHealth;


        long now = System.currentTimeMillis();
        behavior.applyEffect(now);
        wave.spawnMonsters(System.currentTimeMillis());
        behavior.applyEffect(now + projectile.getActiveTime() + 1);


        AtomicInteger actualDamagedMonsters = new AtomicInteger();
        wave.getAliveMonsters().forEach((Monster m) -> {
            if (m.getHealth() != monsterFullHealth) {
                actualDamagedMonsters.getAndIncrement();
                assertEquals(expectedHealthAfterHit, m.getHealth());
            }
        });
        assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
        assertFalse(projectile.active());
    }
}