package projectile.behavior;

import collision.HitboxParameters;
import core.Field;
import core.Wave;
import factory.MonsterFactory;
import factory.WaveFactory;
import monster.Monster;
import monster.MovingMonsterStrategy;
import monster.PlainRoadMoving;
import org.junit.jupiter.api.Test;
import projectile.ExplosiveProjectile;
import projectile.strategy.LinearMovingProjectileStrategy;
import testutils.ProjectileFactory;
import utils.Direction;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ExplosiveBehaviorTest {

    private final Path path = Paths.get("test", "road", "resources", "one_road_segment_one_road_cell.txt")
            .toAbsolutePath()
            .normalize();
    private final Field field = new Field(path.toString());

    private final MonsterFactory monsterFactory = new MonsterFactory();
    private final Position monsterPosition = new Position(100, 100);
    private final MovingMonsterStrategy strategy = new PlainRoadMoving(
            monsterPosition,
            field,
            1
    );
    private final int monsterFullHealth = 100;

    private final ProjectileFactory projectileFactory = new ProjectileFactory();
    private final WaveFactory waveFactory = new WaveFactory();

    @Test
    void collidesWithMonster() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy.clone()));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(monsterPosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


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
    void noCollisionWithMonster() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy.clone()));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        Position projectilePosition = new Position(1000, 1000);
        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(projectilePosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


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
    void explodeAndDamageMonsterInRadiusOfExplosion() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        Position collisionMonsterPosition = new Position(100, 100);
        MovingMonsterStrategy collisionMonsterStrategy = new PlainRoadMoving(
                collisionMonsterPosition,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionMonsterStrategy));

        ExplosiveProjectile expectedProjectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);

        Position positionCollidesOnlyAfterExplode = new Position(
                collisionMonsterPosition.getX() + ((expectedProjectile.getHitbox().getHitboxParameters().width() + expectedProjectile.getRadius()) / 2),
                collisionMonsterPosition.getY() + ((expectedProjectile.getHitbox().getHitboxParameters().height() + expectedProjectile.getRadius()) / 2)
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategy = new PlainRoadMoving(
                positionCollidesOnlyAfterExplode,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


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
        assertFalse(projectile.active());
    }

    @Test
    void explodeAndDamageSeveralMonstersInRadiusOfExplosion() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        Position collisionMonsterPosition = new Position(100, 100);
        MovingMonsterStrategy collisionMonsterStrategy = new PlainRoadMoving(
                collisionMonsterPosition,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionMonsterStrategy));

        ExplosiveProjectile expectedProjectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);

        Position positionCollidesOnlyAfterExplodeLeftTop = new Position(
                collisionMonsterPosition.getX() + ((expectedProjectile.getHitbox().getHitboxParameters().width() - expectedProjectile.getRadius()) / 2),
                collisionMonsterPosition.getY() + ((expectedProjectile.getHitbox().getHitboxParameters().height() + expectedProjectile.getRadius()) / 2)
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyLeftTop = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeLeftTop,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyLeftTop));

        Position positionCollidesOnlyAfterExplodeRightTop = new Position(
                collisionMonsterPosition.getX() + ((expectedProjectile.getHitbox().getHitboxParameters().width() + expectedProjectile.getRadius()) / 2),
                collisionMonsterPosition.getY() + ((expectedProjectile.getHitbox().getHitboxParameters().height() + expectedProjectile.getRadius()) / 2)
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyRightTop = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeRightTop,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyRightTop));

        Position positionCollidesOnlyAfterExplodeRightBottom = new Position(
                collisionMonsterPosition.getX() + ((expectedProjectile.getHitbox().getHitboxParameters().width() + expectedProjectile.getRadius()) / 2),
                collisionMonsterPosition.getY() + ((expectedProjectile.getHitbox().getHitboxParameters().height() - expectedProjectile.getRadius()) / 2)
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyRightBottom = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeRightBottom,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyRightBottom));

        Position positionCollidesOnlyAfterExplodeLeftBottom = new Position(
                collisionMonsterPosition.getX() + ((expectedProjectile.getHitbox().getHitboxParameters().width() - expectedProjectile.getRadius()) / 2),
                collisionMonsterPosition.getY() + ((expectedProjectile.getHitbox().getHitboxParameters().height() - expectedProjectile.getRadius()) / 2)
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyLeftBottom = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeLeftBottom,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyLeftBottom));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 5;
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
    void explodeAndDamageMonsterInBorderOfRadiusOfExplosion() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        Position collisionMonsterPosition = new Position(100, 100);
        MovingMonsterStrategy collisionMonsterStrategy = new PlainRoadMoving(
                collisionMonsterPosition,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionMonsterStrategy));

        ExplosiveProjectile expectedProjectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);

        Position positionCollidesOnlyAfterExplode = new Position(
                collisionMonsterPosition.getX() + expectedProjectile.getRadius() - 1,
                collisionMonsterPosition.getY() + expectedProjectile.getRadius() - 1
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategy = new PlainRoadMoving(
                positionCollidesOnlyAfterExplode,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


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
        assertFalse(projectile.active());
    }

    @Test
    void explodeAndDamageMonstersInEdgesOfRadiusOfExplosion() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        Position collisionMonsterPosition = new Position(100, 100);
        MovingMonsterStrategy collisionMonsterStrategy = new PlainRoadMoving(
                collisionMonsterPosition,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionMonsterStrategy));

        ExplosiveProjectile expectedProjectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);

        Position positionCollidesOnlyAfterExplodeLeftTop = new Position(
                collisionMonsterPosition.getX() - expectedProjectile.getRadius() + 1,
                collisionMonsterPosition.getY() + expectedProjectile.getRadius() - 1
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyLeftTop = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeLeftTop,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyLeftTop));

        Position positionCollidesOnlyAfterExplodeRightTop = new Position(
                collisionMonsterPosition.getX() + expectedProjectile.getRadius() - 1,
                collisionMonsterPosition.getY() + expectedProjectile.getRadius() - 1
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyRightTop = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeRightTop,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyRightTop));

        Position positionCollidesOnlyAfterExplodeRightBottom = new Position(
                collisionMonsterPosition.getX() + expectedProjectile.getRadius() - 1,
                collisionMonsterPosition.getY() - expectedProjectile.getRadius() + 1
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyRightBottom = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeRightBottom,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyRightBottom));

        Position positionCollidesOnlyAfterExplodeLeftBottom = new Position(
                collisionMonsterPosition.getX() - expectedProjectile.getRadius() + 1,
                collisionMonsterPosition.getY() - expectedProjectile.getRadius() + 1
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyLeftBottom = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeLeftBottom,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyLeftBottom));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 5;
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
    void monsterInRadiusOfExplosionButNotCollidesBeforeExplosion() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        Position projectilePosition = new Position(100, 100);
        ExplosiveProjectile expectedProjectile = projectileFactory.createExplosiveProjectile(projectilePosition, Direction.EAST, field);

        Position positionInRadiusOfExplosion = new Position(
                projectilePosition.getX() + ((expectedProjectile.getHitbox().getHitboxParameters().width() + expectedProjectile.getRadius()) / 2),
                projectilePosition.getY() + ((expectedProjectile.getHitbox().getHitboxParameters().height() + expectedProjectile.getRadius()) / 2)
        );
        MovingMonsterStrategy strategy = new PlainRoadMoving(
                positionInRadiusOfExplosion,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(strategy));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(projectilePosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


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
    void explodeAndSeveralMonstersInRadiusSeveralNot() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        Position collisionMonsterPosition = new Position(100, 100);
        MovingMonsterStrategy collisionMonsterStrategy = new PlainRoadMoving(
                collisionMonsterPosition,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionMonsterStrategy));

        ExplosiveProjectile expectedProjectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);

        Position positionCollidesOnlyAfterExplodeLeftTop = new Position(
                collisionMonsterPosition.getX() + ((expectedProjectile.getHitbox().getHitboxParameters().width() - expectedProjectile.getRadius()) / 2),
                collisionMonsterPosition.getY() + ((expectedProjectile.getHitbox().getHitboxParameters().height() + expectedProjectile.getRadius()) / 2)
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyLeftTop = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeLeftTop,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyLeftTop));

        Position positionCollidesOnlyAfterExplodeRightTop = new Position(
                collisionMonsterPosition.getX() + expectedProjectile.getRadius() * 2,
                collisionMonsterPosition.getY() + expectedProjectile.getRadius() * 2
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyRightTop = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeRightTop,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyRightTop));

        Position positionCollidesOnlyAfterExplodeRightBottom = new Position(
                collisionMonsterPosition.getX() + expectedProjectile.getRadius() * 2,
                collisionMonsterPosition.getY() - expectedProjectile.getRadius() * 2
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyRightBottom = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeRightBottom,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyRightBottom));

        Position positionCollidesOnlyAfterExplodeLeftBottom = new Position(
                collisionMonsterPosition.getX() + ((expectedProjectile.getHitbox().getHitboxParameters().width() - expectedProjectile.getRadius()) / 2),
                collisionMonsterPosition.getY() + ((expectedProjectile.getHitbox().getHitboxParameters().height() - expectedProjectile.getRadius()) / 2)
        );
        MovingMonsterStrategy collisionAfterExplodeMonsterStrategyLeftBottom = new PlainRoadMoving(
                positionCollidesOnlyAfterExplodeLeftBottom,
                field,
                1
        );
        monsterQueue.add(monsterFactory.createMonster(collisionAfterExplodeMonsterStrategyLeftBottom));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(collisionMonsterPosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 3;
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
    void applyEffectToDeactivatedProjectile() throws InterruptedException {
        Queue<Monster> monsterQueue = new LinkedList<>();

        monsterQueue.add(monsterFactory.createMonster(strategy.clone()));

        int monstersCount = monsterQueue.size();
        Wave wave = waveFactory.createWave(1, monsterQueue, 1, field);
        for (int i = 0; i < monstersCount; i++) {
            Thread.sleep(1);
            wave.spawnMonsters(System.currentTimeMillis());
        }
        field.setWave(wave);

        ExplosiveProjectile projectile = projectileFactory.createExplosiveProjectile(monsterPosition, Direction.EAST, field);
        ExplosiveBehavior behavior = (ExplosiveBehavior) projectile.getBehavior();


        int expectedDamagedMonstersCount = 0;
        int expectedHealthAfterHit = monsterFullHealth - projectile.getDamage();

        projectile.deactivate();
        assertThrows(IllegalStateException.class,
                () -> behavior.applyEffect(System.currentTimeMillis())
        );


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
    void setTwoProjectilesToExplosiveBehavior() {
        int speed = 1;
        int maxDistance = 100;
        int damage = 20;
        int width = 30;
        int height = 30;
        int radius = 100;

        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        ExplosiveBehavior behavior = new ExplosiveBehavior();

        HitboxParameters hitboxParameters = new HitboxParameters(
                width,
                height,
                0
        );
        Direction direction = Direction.EAST;
        Position position = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                position,
                behavior,
                field,
                direction,
                movingStrategy,
                radius
        );

        assertThrows(IllegalStateException.class, () ->  new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                position,
                behavior,
                field,
                direction,
                movingStrategy,
                radius
        ));
    }
}