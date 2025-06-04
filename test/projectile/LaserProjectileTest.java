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
import projectile.behavior.LaserBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static asserts.TestAsserts.assertProjectilesEquals;
import static org.junit.jupiter.api.Assertions.*;

class LaserProjectileTest {
    private final Position position = new Position(100, 100);
    private final Field field = new Field();
    private final int damage = 20;
    private final int range = 30;
    private final long activeTime = TimeUnit.SECONDS.toMillis(3);
    private final long damageCooldown = TimeUnit.MILLISECONDS.toMillis(500);

    Direction mockDirection = Direction.NORTH;
    HitboxParameters hitboxParameters = new HitboxParameters(10, 10, 0);

    @Test
    void cloneLaserProjectileWithSameCharacteristics() {
        LaserBehavior behavior = new LaserBehavior();
        LaserProjectile typicalProjectile = new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        );
        Position newPosition = new Position(34, 34);
        Direction newDirection = Direction.WEST;

        LaserBehavior expectedBehavior = new LaserBehavior();
        HitboxParameters expectedHitboxParameters = new HitboxParameters(
                hitboxParameters.width(),
                hitboxParameters.height(),
                newDirection.toRadians()
        );
        LaserProjectile expectedProjectile = new LaserProjectile(
                expectedHitboxParameters,
                damage,
                range,
                newPosition,
                expectedBehavior,
                field,
                newDirection,
                activeTime,
                damageCooldown
        );

        LaserProjectile actualProjectile = typicalProjectile.clone(newPosition, newDirection);

        assertProjectilesEquals(expectedProjectile, actualProjectile);
        assertEquals(actualProjectile, actualProjectile.getBehavior().getProjectile());
    }

    @Test
    void cloneLaserProjectileWithOtherCharacteristics() {
        LaserBehavior behavior = new LaserBehavior();
        LaserProjectile typicalProjectile = new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        );
        int newDamage = 5673;
        int newRange = 19827;

        LaserBehavior expectedBehavior = new LaserBehavior();
        HitboxParameters newHitboxParameters = new HitboxParameters(
                newRange, hitboxParameters.height(), mockDirection.toRadians()
        );
        LaserProjectile expectedProjectile = new LaserProjectile(
                newHitboxParameters,
                newDamage,
                newRange,
                position,
                expectedBehavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        );

        LaserProjectile actualProjectile = typicalProjectile.clone(newDamage, newRange);

        assertProjectilesEquals(expectedProjectile, actualProjectile);
        assertEquals(actualProjectile, actualProjectile.getBehavior().getProjectile());
    }

    @Test
    void maxDistanceIsLessThanZero() {
        int maxDistance = -10;
        LaserBehavior behavior = new LaserBehavior();

        assertThrows(IllegalArgumentException.class, () -> new LaserProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        ));
    }

    @Test
    void maxDistanceIsZero() {
        int maxDistance = 0;
        LaserBehavior behavior = new LaserBehavior();

        assertThrows(IllegalArgumentException.class, () -> new LaserProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        ));
    }

    @Test
    void damageIsZero() {
        int damage = 0;
        LaserBehavior behavior = new LaserBehavior();

        assertDoesNotThrow(() -> {
            new LaserProjectile(
                    hitboxParameters,
                    damage,
                    range,
                    position,
                    behavior,
                    field,
                    mockDirection,
                    activeTime,
                    damageCooldown
            );
        });
    }

    @Test
    void damageIsLessThanZero() {
        int damage = -15;
        LaserBehavior behavior = new LaserBehavior();

        assertThrows(IllegalArgumentException.class, () -> new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        ));
    }

    @Test
    void activeTimeLessThanZero() {
        LaserBehavior behavior = new LaserBehavior();
        long activeTime = -1;

        assertThrows(IllegalArgumentException.class, () -> new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        ));
    }

    @Test
    void activeTimeIsZero() {
        LaserBehavior behavior = new LaserBehavior();
        long activeTime = 0;

        assertThrows(IllegalArgumentException.class, () -> new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        ));
    }

    @Test
    void damageCooldownLessThanZero() {
        LaserBehavior behavior = new LaserBehavior();
        long damageCooldown = -1;

        assertThrows(IllegalArgumentException.class, () -> new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        ));
    }

    @Test
    void damageCooldownIsZero() {
        LaserBehavior behavior = new LaserBehavior();
        long damageCooldown = 0;

        LaserProjectile actualProjectile = assertDoesNotThrow(() -> new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                damageCooldown
        ));

        LaserProjectile expectedProjectile = new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior.clone(),
                field,
                mockDirection,
                activeTime,
                damageCooldown
        );

        assertProjectilesEquals(expectedProjectile, actualProjectile);
        assertEquals(actualProjectile, actualProjectile.getBehavior().getProjectile());
    }

    @Test
    void updatingDeactivatedProjectile() {
        int damage = 1;
        int maxDistance = 100;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        LaserBehavior behavior = new LaserBehavior();

        LaserProjectile projectile = new LaserProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                activeTime,
                damageCooldown
        );
        projectile.deactivate();

        assertThrows(IllegalStateException.class, () -> projectile.update(System.currentTimeMillis()));
    }


    @Test
    void updateWithPassActiveTime() {
        Field field = new Field();
        WaveFactory waveFactory = new WaveFactory();
        field.setWave(waveFactory.createWave(1, 1, field));

        Position position = new Position(34, 34);
        Direction direction = Direction.WEST;

        LaserBehavior behavior = new LaserBehavior();
        LaserProjectile projectile = new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                direction,
                activeTime,
                damageCooldown
        );

        //--------------------------------------------------------------------------------------------------------------

        Position expectedPosition = position;

        long tick = System.currentTimeMillis();
        projectile.update(tick);

        tick += TimeUnit.SECONDS.toMillis(50);

        //--------------------------------------------------------------------------------------------------------------

        projectile.update(tick);

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, projectile.position());
        assertFalse(projectile.active());
    }

    @Test
    void updateWithDoNotPassActiveTime() {
        Field field = new Field();
        WaveFactory waveFactory = new WaveFactory();
        field.setWave(waveFactory.createWave(1, 1, field));

        Position position = new Position(34, 34);
        Direction direction = Direction.WEST;

        LaserBehavior behavior = new LaserBehavior();
        LaserProjectile projectile = new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                direction,
                activeTime,
                damageCooldown
        );

        //--------------------------------------------------------------------------------------------------------------

        Position expectedPosition = position;

        long tick = System.currentTimeMillis();
        projectile.update(tick);

        tick += activeTime / 2;

        //--------------------------------------------------------------------------------------------------------------

        projectile.update(tick);

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, projectile.position());
        assertTrue(projectile.active());
    }

    @Test
    void hitAfterUpdate() {
        Field field = new Field();
        MonsterFactory monsterFactory = new MonsterFactory();
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 1);
        Queue<Monster> monstersToSpawn = monsterFactory.createMonsters(1, strategy);
        Monster monster = monstersToSpawn.element();
        int monsterHealthBeforeHit = monster.getHealth();

        Wave wave = new Wave(monstersToSpawn, 10, 1);
        field.setWave(wave);

        long tick = System.currentTimeMillis();
        Position position = monster.getPosition();
        Direction direction = Direction.WEST;

        LaserBehavior behavior = new LaserBehavior();
        LaserProjectile projectile = new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                direction,
                activeTime,
                damageCooldown
        );

        //--------------------------------------------------------------------------------------------------------------

        Position expectedPosition = position;

        //--------------------------------------------------------------------------------------------------------------

        projectile.update(tick);
        field.getWave().spawnMonsters(tick);
        tick += TimeUnit.MILLISECONDS.toMillis(50);

        projectile.update(tick);

        int monsterHealthAfterHit = monster.getHealth();

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, projectile.position());
        assertEquals(monsterHealthBeforeHit - projectile.getDamage(), monsterHealthAfterHit);
        assertTrue(projectile.active());
    }
}