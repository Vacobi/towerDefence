package projectile;

import collision.HitboxParameters;
import core.Field;
import org.junit.jupiter.api.Test;
import projectile.behavior.LaserBehavior;
import utils.Direction;
import utils.Position;

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
}