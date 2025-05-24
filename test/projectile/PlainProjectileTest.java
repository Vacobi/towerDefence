package projectile;

import collision.HitboxParameters;
import core.Field;
import org.junit.jupiter.api.Test;
import projectile.behavior.HitOneTargetBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import static asserts.TestAsserts.assertProjectilesEquals;
import static org.junit.jupiter.api.Assertions.*;

class PlainProjectileTest {

    private final Position position = new Position(100, 100);
    private final Field field = new Field();
    private final int damage = 20;
    private final int range = 30;

    Direction mockDirection = Direction.NORTH;
    HitboxParameters hitboxParameters = new HitboxParameters(10, 10, 0);
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
        int speed = 0;
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
        int speed = 0;
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
        int speed = 0;
        int damage = 15;
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
        int speed = 0;
        int damage = -15;
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
}