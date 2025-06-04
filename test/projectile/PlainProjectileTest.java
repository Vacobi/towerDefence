package projectile;

import collision.HitboxParameters;
import core.Field;
import core.Wave;
import factory.MonsterFactory;
import factory.WaveFactory;
import monster.Monster;
import monster.MovingMonsterStrategy;
import monster.PlainRoadMoving;
import org.junit.jupiter.api.Test;
import projectile.behavior.HitOneTargetBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static asserts.TestAsserts.assertProjectilesEquals;
import static org.junit.jupiter.api.Assertions.*;

class PlainProjectileTest {

    private final Position position = new Position(100, 100);
    private final Field field = new Field();
    private final int damage = 20;
    private final int range = 30;

    Direction mockDirection = Direction.NORTH;
    HitboxParameters hitboxParameters = new HitboxParameters(10, 10, mockDirection.toRadians());
    private final int speed = 10;

    @Test
    void clonePlainProjectileWithSameCharacteristics() {
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        Position newPosition = new Position(34, 34);
        Direction newDirection = Direction.WEST;

        LinearMovingProjectileStrategy expectedMovingStrategy = new LinearMovingProjectileStrategy(speed);
        HitOneTargetBehavior expectedBehavior = new HitOneTargetBehavior();
        HitboxParameters expectedHitboxParameters = new HitboxParameters(
                hitboxParameters.width(),
                hitboxParameters.height(),
                newDirection.toRadians()
        );
        PlainProjectile expectedProjectile = new PlainProjectile(
                expectedHitboxParameters,
                damage,
                range,
                newPosition,
                expectedBehavior,
                field,
                newDirection,
                expectedMovingStrategy
        );

        PlainProjectile actualProjectile = typicalProjectile.clone(newPosition, newDirection);

        assertProjectilesEquals(expectedProjectile, actualProjectile);
        assertEquals(actualProjectile, actualProjectile.getBehavior().getProjectile());
        assertEquals(actualProjectile, actualProjectile.getMovingStrategy().getProjectile());
    }

    @Test
    void clonePlainProjectileWithOtherCharacteristics() {
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        int newDamage = 5673;
        int newRange = 19827;

        LinearMovingProjectileStrategy expectedMovingStrategy = new LinearMovingProjectileStrategy(speed);
        HitOneTargetBehavior expectedBehavior = new HitOneTargetBehavior();
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                newDamage,
                newRange,
                position,
                expectedBehavior,
                field,
                mockDirection,
                expectedMovingStrategy
        );

        PlainProjectile actualProjectile = typicalProjectile.clone(newDamage, newRange);

        assertProjectilesEquals(expectedProjectile, actualProjectile);
        assertEquals(actualProjectile, actualProjectile.getBehavior().getProjectile());
        assertEquals(actualProjectile, actualProjectile.getMovingStrategy().getProjectile());
    }

    @Test
    void maxDistanceIsLessThanZero() {
        int speed = 1;
        int damage = 15;
        int maxDistance = -10;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertThrows(IllegalArgumentException.class, () -> new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                movingStrategy
        ));
    }

    @Test
    void maxDistanceIsZero() {
        int speed = 1;
        int damage = 15;
        int maxDistance = 0;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertThrows(IllegalArgumentException.class, () -> new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                movingStrategy
        ));
    }

    @Test
    void damageIsZero() {
        int speed = 1;
        int damage = 0;
        int maxDistance = 1;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertDoesNotThrow(() -> {
            new PlainProjectile(
                    hitboxParameters,
                    damage,
                    maxDistance,
                    startPosition,
                    behavior,
                    field,
                    direction,
                    movingStrategy
            );
        });
    }

    @Test
    void damageIsLessThanZero() {
        int speed = 1;
        int damage = -15;
        int maxDistance = 1;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertThrows(IllegalArgumentException.class, () -> new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                movingStrategy
        ));
    }

    @Test
    void updatingDeactivatedProjectile() {
        int speed = 1;
        int damage = 1;
        int maxDistance = 100;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        PlainProjectile projectile = new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                movingStrategy
        );
        projectile.deactivate();

        assertThrows(IllegalStateException.class, () -> projectile.update(System.currentTimeMillis()));
    }


    @Test
    void updateWithReachedEnd() {
        Field field = new Field();
        WaveFactory waveFactory = new WaveFactory();
        field.setWave(waveFactory.createWave(1, 1, field));

        Position position = new Position(34, 34);
        Direction direction = Direction.WEST;

        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        PlainProjectile projectile = new PlainProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                direction,
                movingStrategy
        );

        //--------------------------------------------------------------------------------------------------------------

        Position expectedPosition = position.move(direction, range);

        long tick = System.currentTimeMillis();
        projectile.update(tick);

        tick += TimeUnit.SECONDS.toMillis(50);

        //--------------------------------------------------------------------------------------------------------------

        projectile.update(tick);

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, projectile.position());
        assertTrue(projectile.active());
    }

    @Test
    void updateWithReachedEndAndDeactivates() {
        Field field = new Field();
        WaveFactory waveFactory = new WaveFactory();
        field.setWave(waveFactory.createWave(1, 1, field));

        Position position = new Position(34, 34);
        Direction direction = Direction.WEST;

        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        PlainProjectile projectile = new PlainProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                direction,
                movingStrategy
        );

        //--------------------------------------------------------------------------------------------------------------

        Position expectedPosition = position.move(direction, range);

        long tick = System.currentTimeMillis();
        projectile.update(tick);

        tick += TimeUnit.SECONDS.toMillis(50);

        //--------------------------------------------------------------------------------------------------------------

        projectile.update(tick);
        projectile.update(tick); // To deactivate

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, projectile.position());
        assertFalse(projectile.active());
    }

    @Test
    void hitAfterUpdate() {
        Field field = new Field();
        WaveFactory waveFactory = new WaveFactory();
        field.setWave(waveFactory.createWave(1, 1, field));

        long tick = System.currentTimeMillis();
        field.getWave().spawnMonsters(tick);
        Position position = field.getWave().getAliveMonsters().iterator().next().getPosition();
        Direction direction = Direction.WEST;

        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(0);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        PlainProjectile projectile = new PlainProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                direction,
                movingStrategy
        );

        //--------------------------------------------------------------------------------------------------------------

        Position expectedPosition = position;

        //--------------------------------------------------------------------------------------------------------------

        projectile.update(tick);

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, projectile.position());
        assertFalse(projectile.active());
    }

    @Test
    void collidesInTheEndOfTick() {
        Field field = new Field();
        MonsterFactory monsterFactory = new MonsterFactory();
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 1);
        Queue<Monster> monstersToSpawn = monsterFactory.createMonsters(1, strategy);
        Monster monster = monstersToSpawn.element();
        Wave wave = new Wave(monstersToSpawn, 10, 1);
        field.setWave(wave);

        long tick = System.currentTimeMillis();
        field.getWave().spawnMonsters(tick);

        Direction direction = Direction.WEST;
        Direction oppositeDirection = Direction.EAST;
        int maxDistance = 400;
        Position positionOfStart = monster.getPosition().move(oppositeDirection, maxDistance);

        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(10);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        PlainProjectile projectile = new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                positionOfStart,
                behavior,
                field,
                direction,
                movingStrategy
        );
        projectile.update(tick);

        //--------------------------------------------------------------------------------------------------------------

        Position expectedPosition = monster.getPosition();

        tick += TimeUnit.MINUTES.toMillis(5);

        //--------------------------------------------------------------------------------------------------------------

        projectile.update(tick);

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, projectile.position());
        assertTrue(projectile.active());
    }

    @Test
    void collidesInTheStartOfTick() {
        Field field = new Field();
        MonsterFactory monsterFactory = new MonsterFactory();
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 1);
        Queue<Monster> monstersToSpawn = monsterFactory.createMonsters(1, strategy);
        Monster monster = monstersToSpawn.element();
        Wave wave = new Wave(monstersToSpawn, 10, 1);
        field.setWave(wave);

        long tick = System.currentTimeMillis();
        field.getWave().spawnMonsters(tick);

        Direction direction = Direction.WEST;
        Direction oppositeDirection = Direction.EAST;
        int maxDistance = 400;
        Position positionOfStart = monster.getPosition().move(oppositeDirection, maxDistance);

        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(10);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        PlainProjectile projectile = new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                positionOfStart,
                behavior,
                field,
                direction,
                movingStrategy
        );
        projectile.update(tick);

        //--------------------------------------------------------------------------------------------------------------

        Position expectedPosition = monster.getPosition();

        tick += TimeUnit.MINUTES.toMillis(5);

        //--------------------------------------------------------------------------------------------------------------

        projectile.update(tick);
        projectile.update(tick);

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, projectile.position());
        assertFalse(projectile.active());
    }
}