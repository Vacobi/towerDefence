package projectile;

import collision.HitboxParameters;
import core.Field;
import org.junit.jupiter.api.Test;
import projectile.behavior.ExplosiveBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import static asserts.TestAsserts.assertProjectilesEquals;
import static org.junit.jupiter.api.Assertions.*;

class ExplosiveProjectileTest {

    private final Position position = new Position(100, 100);
    private final Field field = new Field();
    private final int damage = 20;
    private final int maxDistance = 30;
    private final int radius = 100;

    Direction direction = Direction.NORTH;
    HitboxParameters hitboxParameters = new HitboxParameters(10, 10, direction.toRadians());
    private final int speed = 10;

    @Test
    void cloneExplosiveProjectileWithSameCharacteristics() {
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        ExplosiveProjectile typicalProjectile = new ExplosiveProjectile(
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
        Position newPosition = new Position(34, 34);
        Direction newDirection = Direction.WEST;

        LinearMovingProjectileStrategy expectedMovingStrategy = new LinearMovingProjectileStrategy(speed);
        ExplosiveBehavior expectedBehavior = new ExplosiveBehavior();
        HitboxParameters expectedHitboxParameters = new HitboxParameters(
                hitboxParameters.width(),
                hitboxParameters.height(),
                newDirection.toRadians()
        );
        ExplosiveProjectile expectedProjectile = new ExplosiveProjectile(
                expectedHitboxParameters,
                damage,
                maxDistance,
                newPosition,
                expectedBehavior,
                field,
                newDirection,
                expectedMovingStrategy,
                radius
        );

        ExplosiveProjectile actualProjectile = typicalProjectile.clone(newPosition, newDirection);

        assertProjectilesEquals(expectedProjectile, actualProjectile);
        assertEquals(actualProjectile, actualProjectile.getBehavior().getProjectile());
        assertEquals(actualProjectile, actualProjectile.getMovingStrategy().getProjectile());
    }

    @Test
    void cloneExplosiveProjectileWithOtherCharacteristics() {
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        ExplosiveProjectile typicalProjectile = new ExplosiveProjectile(
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
        int newDamage = 5673;
        int newRange = 19827;

        LinearMovingProjectileStrategy expectedMovingStrategy = new LinearMovingProjectileStrategy(speed);
        ExplosiveBehavior expectedBehavior = new ExplosiveBehavior();
        ExplosiveProjectile expectedProjectile = new ExplosiveProjectile(
                hitboxParameters,
                newDamage,
                newRange,
                position,
                expectedBehavior,
                field,
                direction,
                expectedMovingStrategy,
                radius
        );

        ExplosiveProjectile actualProjectile = typicalProjectile.clone(newDamage, newRange);

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
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertThrows(IllegalArgumentException.class, () -> new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                movingStrategy,
                radius
        ));
    }

    @Test
    void maxDistanceIsZero() {
        int speed = 1;
        int damage = 15;
        int maxDistance = 0;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertThrows(IllegalArgumentException.class, () -> new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                movingStrategy,
                radius
        ));
    }

    @Test
    void damageIsZero() {
        int speed = 1;
        int damage = 0;
        int maxDistance = 1;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertDoesNotThrow(() -> {
            new ExplosiveProjectile(
                    hitboxParameters,
                    damage,
                    maxDistance,
                    startPosition,
                    behavior,
                    field,
                    direction,
                    movingStrategy,
                    radius
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
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertThrows(IllegalArgumentException.class, () -> new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                movingStrategy,
                radius
        ));
    }

    @Test
    void updatingDeactivatedProjectile() {
        int speed = 1;
        int damage = 1;
        int maxDistance = 100;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        ExplosiveProjectile projectile = new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                startPosition,
                behavior,
                field,
                direction,
                movingStrategy,
                radius
        );
        projectile.deactivate();

        assertThrows(IllegalStateException.class, () -> projectile.update(System.currentTimeMillis()));
    }

    @Test
    void radiusIsLessThanZero() {
        int radius = -1;
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertThrows(IllegalArgumentException.class, () -> new ExplosiveProjectile(
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

    @Test
    void radiusIsZero() {
        int radius = 0;
        ExplosiveBehavior behavior = new ExplosiveBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);

        assertThrows(IllegalArgumentException.class, () -> new ExplosiveProjectile(
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