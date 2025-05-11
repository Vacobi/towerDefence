package projectile;

import collision.HitboxParameters;
import core.Field;
import org.junit.jupiter.api.Test;
import projectile.behavior.HitOneTargetBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import static asserts.TestAsserts.assertMovingProjectilesEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlainProjectileTest {

    private final Position position = new Position(100, 100);
    private final Field field = new Field();
    private final int damage = 20;
    private final int range = 30;

    Direction mockDirection = Direction.NORTH;
    HitboxParameters hitboxParameters = new HitboxParameters(10, 10, 0);
    private final int speed = 10;
    HitOneTargetBehavior behavior = new HitOneTargetBehavior();
    LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

    @Test
    void clonePlainProjectileWithSameCharacteristics() {
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

        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                damage,
                range,
                newPosition,
                behavior,
                field,
                newDirection,
                movingStrategy
        );

        PlainProjectile actualProjectile = typicalProjectile.clone(newPosition, newDirection);

        assertMovingProjectilesEquals(expectedProjectile, actualProjectile);
        assertEquals(actualProjectile, actualProjectile.getBehavior().getProjectile());
        assertEquals(actualProjectile, actualProjectile.getMovingStrategy().getProjectile());
    }

    @Test
    void clonePlainProjectileWithOtherCharacteristics() {
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

        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                newDamage,
                newRange,
                position,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );

        PlainProjectile actualProjectile = typicalProjectile.clone(newDamage, newRange);

        assertMovingProjectilesEquals(expectedProjectile, actualProjectile);
        assertEquals(actualProjectile, actualProjectile.getBehavior().getProjectile());
        assertEquals(actualProjectile, actualProjectile.getMovingStrategy().getProjectile());
    }
}