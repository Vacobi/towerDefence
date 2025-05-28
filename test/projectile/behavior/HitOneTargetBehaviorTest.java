package projectile.behavior;

import core.Field;
import core.Wave;
import factory.MonsterFactory;
import testutils.ProjectileFactory;
import factory.WaveFactory;
import monster.Monster;
import monster.MovingMonsterStrategy;
import monster.PlainRoadMoving;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import projectile.Projectile;
import utils.Direction;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class HitOneTargetBehaviorTest {
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

    @Nested
    class HitMonsterTest {

        @Test
        void collidesWithMonster() {
            Monster monster = monsterFactory.createMonster(strategy);
            Projectile projectile = projectileFactory.createLinearMovingHitOneTargetProjectile(monster.getPosition(), Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();

            int expectedHealth = monster.getHealth() - projectile.getDamage();

            behavior.hitMonster(monster);
            int actualHealth = monster.getHealth();

            assertEquals(expectedHealth, actualHealth);
            assertFalse(projectile.active());
        }

        @Test
        void notCollidesWithMonster() {
            MovingMonsterStrategy notCollidingStrategy = new PlainRoadMoving(new Position(0, 0), field, 10);
            Monster monster = monsterFactory.createMonster(notCollidingStrategy);
            Projectile projectile = projectileFactory.createLinearMovingHitOneTargetProjectile(new Position(100, 100), Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();

            int expectedHealth = monster.getHealth();

            behavior.hitMonster(monster);
            int actualHealth = monster.getHealth();

            assertEquals(expectedHealth, actualHealth);
            assertTrue(projectile.active());
        }

        @Test
        void monsterDies() {
            Monster monster = monsterFactory.createMonster(strategy);
            Projectile projectile = projectileFactory.createLinearMovingHitOneTargetProjectile(monster.getPosition(), Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();

            int expectedHealth = 0;

            monster.applyDamage(monster.getHealth() - projectile.getDamage());
            behavior.hitMonster(monster);
            int actualHealth = monster.getHealth();

            assertEquals(expectedHealth, actualHealth);
            assertFalse(projectile.active());
        }
    }

    @Nested
    class ApplyEffectTest {
        WaveFactory waveFactory = new WaveFactory();

        @Test
        void onlyOneMonsterAndCollision() throws InterruptedException {
            Queue<Monster> monsterQueue = new LinkedList<>();
            monsterQueue.add(monsterFactory.createMonster(strategy));
            int monstersCount = monsterQueue.size();
            Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
            for (int i = 0; i < monstersCount; i++) {
                Thread.sleep(1);
                wave.spawnMonsters(System.currentTimeMillis());
            }
            field.setWave(wave);
            Projectile projectile = projectileFactory.createLinearMovingHitOneTargetProjectile(monsterPosition, Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();


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
            assertFalse(projectile.active());
        }

        @Test
        void severalMonstersAndCollisionWithEach() throws InterruptedException {
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
            Projectile projectile = projectileFactory.createLinearMovingHitOneTargetProjectile(monsterPosition, Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();


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
            assertFalse(projectile.active());
        }

        @Test
        void severalMonstersAndCollisionWithOne() throws InterruptedException {
            Position positionWithoutCollision = new Position(1000,  1000);
            MovingMonsterStrategy strategyWithoutCollision = new PlainRoadMoving(positionWithoutCollision, field, 10);
            Monster collisionedMonster = monsterFactory.createMonster(strategy);
            Queue<Monster> monsterQueue = new LinkedList<>();
            monsterQueue.add(monsterFactory.createMonster(strategyWithoutCollision));
            monsterQueue.add(monsterFactory.createMonster(strategyWithoutCollision));
            monsterQueue.add(collisionedMonster);
            monsterQueue.add(monsterFactory.createMonster(strategyWithoutCollision));
            int monstersCount = monsterQueue.size();
            Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
            for (int i = 0; i < monstersCount; i++) {
                Thread.sleep(2);
                wave.spawnMonsters(System.currentTimeMillis());
            }
            field.setWave(wave);
            Projectile projectile = projectileFactory.createLinearMovingHitOneTargetProjectile(monsterPosition, Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();


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
            assertEquals(expectedHealthAfterHit, collisionedMonster.getHealth());
            assertFalse(projectile.active());
        }

        @Test
        void severalMonstersAndNoCollision() throws InterruptedException {
            Position positionWithoutCollision = new Position(1000,  1000);
            MovingMonsterStrategy strategyWithoutCollision = new PlainRoadMoving(positionWithoutCollision, field, 10);
            Queue<Monster> monsterQueue = new LinkedList<>();
            monsterQueue.add(monsterFactory.createMonster(strategyWithoutCollision));
            monsterQueue.add(monsterFactory.createMonster(strategyWithoutCollision));
            monsterQueue.add(monsterFactory.createMonster(strategyWithoutCollision));
            monsterQueue.add(monsterFactory.createMonster(strategyWithoutCollision));
            int monstersCount = monsterQueue.size();
            Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
            for (int i = 0; i < monstersCount; i++) {
                Thread.sleep(2);
                wave.spawnMonsters(System.currentTimeMillis());
            }
            field.setWave(wave);
            Position projectilePosition = new Position(100, 100);
            Projectile projectile = projectileFactory.createLinearMovingHitOneTargetProjectile(projectilePosition, Direction.NORTH, field);
            HitOneTargetBehavior behavior = (HitOneTargetBehavior) projectile.getBehavior();


            int expectedDamagedMonstersCount = 0;


            behavior.applyEffect(System.currentTimeMillis());
            AtomicInteger actualDamagedMonsters = new AtomicInteger();


            wave.getAliveMonsters().forEach((Monster m) -> {
                if (m.getHealth() != monsterFullHealth) {
                    actualDamagedMonsters.getAndIncrement();
                }
            });
            assertEquals(expectedDamagedMonstersCount, actualDamagedMonsters.get());
            assertTrue(projectile.active());
        }
    }
}