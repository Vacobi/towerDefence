package projectile.strategy;

import collision.HitboxParameters;
import core.Field;
import projectile.PlainProjectile;
import projectile.behavior.HitOneTargetBehavior;
import org.junit.jupiter.api.Test;
import utils.Direction;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class LinearMovingProjectileStrategyTest {
    private final Path path = Paths.get("test", "road", "resources", "one_road_segment_one_road_cell.txt")
            .toAbsolutePath()
            .normalize();
    private final Field field = new Field(path.toString());

    HitboxParameters hitboxParameters = new HitboxParameters(10, 10, 0);

    private final int SPEED_COEFF = LinearMovingProjectileStrategy.getSpeedCoeff();


    @Test
    void movePartOfDistance() {
        int speed = 1;
        int damage = 15;
        int maxDistance = Integer.MAX_VALUE;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(10, 10);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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
        long delta = TimeUnit.SECONDS.toMillis(2);
        long movingTime = now + delta;

        int expectedTotalTraveled = (int) (delta / SPEED_COEFF) * speed;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);
        boolean expActive = true;

        movingStrategy.move(movingTime);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expActive, projectile.active());
        assertEquals(expectedTotalTraveled, movingStrategy.getTotalTraveledDistance());
        assertEquals(movingTime, movingStrategy.getLastMoveTime());
    }

    @Test
    void speedIsZero() {
        int speed = 0;
        int damage = 15;
        int maxDistance = 10;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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

        long movingTime = now + TimeUnit.SECONDS.toMillis(1);
        movingStrategy.move(movingTime);

        Position expPosition = new Position(1, 1);
        Position actualPosition = projectile.position();
        assertEquals(expPosition, actualPosition);
    }

    @Test
    void speedIsNegative() {
        int speed = -5;

        assertThrows(IllegalArgumentException.class, () -> {
            new LinearMovingProjectileStrategy(speed);
        });
    }

    @Test
    void speedIsPositive() {
        int speed = 5;
        int damage = 15;
        int maxDistance = Integer.MAX_VALUE;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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
        long delta = TimeUnit.SECONDS.toMillis(1);
        long movingTime = now + delta;

        int expectedTotalTraveled = (int) (delta / SPEED_COEFF) * speed;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);
        boolean expActive = true;

        movingStrategy.move(movingTime);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expActive, projectile.active());
        assertEquals(expectedTotalTraveled, movingStrategy.getTotalTraveledDistance());
        assertEquals(movingTime, movingStrategy.getLastMoveTime());
    }

    @Test
    void directionIsWest() {
        int speed = 1;
        int damage = 15;
        int maxDistance = Integer.MAX_VALUE;
        Direction direction = Direction.WEST;
        Position startPosition = new Position(10, 10);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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
        long delta = TimeUnit.SECONDS.toMillis(2);
        long movingTime = now + delta;

        int expectedTotalTraveled = (int) (delta / SPEED_COEFF) * speed;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);
        boolean expActive = true;

        movingStrategy.move(movingTime);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expActive, projectile.active());
        assertEquals(expectedTotalTraveled, movingStrategy.getTotalTraveledDistance());
        assertEquals(movingTime, movingStrategy.getLastMoveTime());
    }

    @Test
    void directionIsSouth() {
        int speed = 1;
        int damage = 15;
        int maxDistance = Integer.MAX_VALUE;
        Direction direction = Direction.SOUTH;
        Position startPosition = new Position(10, 10);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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
        long delta = TimeUnit.SECONDS.toMillis(2);
        long movingTime = now + delta;

        int expectedTotalTraveled = (int) (delta / SPEED_COEFF) * speed;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);
        boolean expActive = true;

        movingStrategy.move(movingTime);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expActive, projectile.active());
        assertEquals(expectedTotalTraveled, movingStrategy.getTotalTraveledDistance());
        assertEquals(movingTime, movingStrategy.getLastMoveTime());
    }

    @Test
    void directionIsEast() {
        int speed = 1;
        int damage = 15;
        int maxDistance = Integer.MAX_VALUE;
        Direction direction = Direction.EAST;
        Position startPosition = new Position(10, 10);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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
        long delta = TimeUnit.SECONDS.toMillis(2);
        long movingTime = now + delta;

        int expectedTotalTraveled = (int) (delta / SPEED_COEFF) * speed;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);
        boolean expActive = true;

        movingStrategy.move(movingTime);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expActive, projectile.active());
        assertEquals(expectedTotalTraveled, movingStrategy.getTotalTraveledDistance());
        assertEquals(movingTime, movingStrategy.getLastMoveTime());
    }

    @Test
    void traveledMaxDistance() {
        int speed = 1;
        int damage = 15;
        long delta = TimeUnit.SECONDS.toMillis(10);
        int maxDistance = (int) (delta / SPEED_COEFF) * speed;
        Direction direction = Direction.EAST;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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
        long movingTime = now + delta;

        int expectedTotalTraveled = (int) (delta / SPEED_COEFF) * speed;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);
        boolean expActive = true;

        movingStrategy.move(movingTime);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expActive, projectile.active());
        assertEquals(expectedTotalTraveled, movingStrategy.getTotalTraveledDistance());
        assertEquals(movingTime, movingStrategy.getLastMoveTime());
    }

    @Test
    void movingAfterReachedMaxDistance() {
        int speed = 1;
        int damage = 15;
        long delta = TimeUnit.SECONDS.toMillis(10);
        int maxDistance = (int) (delta / SPEED_COEFF) * speed;
        Direction direction = Direction.NORTH;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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
        long movingTime = now + delta;

        int expectedTotalTraveled = (int) (delta / SPEED_COEFF) * speed;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);
        boolean expActive = false;

        movingStrategy.move(movingTime);
        long extraMovingTime = now + delta + TimeUnit.SECONDS.toMillis(1);
        movingStrategy.move(extraMovingTime);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expActive, projectile.active());
        assertEquals(expectedTotalTraveled, movingStrategy.getTotalTraveledDistance());
        assertEquals(movingTime, movingStrategy.getLastMoveTime());
    }

    @Test
    void traveledMoreThanMaxDistance() {
        int speed = 10;
        int damage = 15;
        int maxDistance = 10;
        Direction direction = Direction.EAST;
        Position startPosition = new Position(1, 1);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        long now = System.currentTimeMillis();
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
        long delta = TimeUnit.SECONDS.toMillis(10);
        long movingTime = now + delta;

        int expectedTotalTraveled = 10;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);
        boolean expActive = true;

        movingStrategy.move(movingTime);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expActive, projectile.active());
        assertEquals(expectedTotalTraveled, movingStrategy.getTotalTraveledDistance());
        assertEquals(movingTime, movingStrategy.getLastMoveTime());
    }
}