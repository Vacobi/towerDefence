package projectile;

import core.Field;
import factory.ProjectileFactory;
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

    private final ProjectileFactory projectileFactory = new ProjectileFactory();

    private final Position position = new Position(10, 10);
    private final int projectileSpeed = 10;
    private final int projectileMaxDistance = 100;

    private final int MILLIS_TO_SECONDS_COEFF = 1000;

    @Test
    void movePartOfDistance() {
        Direction direction = Direction.NORTH;
        MovingProjectile projectile = projectileFactory.createMovingProjectile(position, direction, field);
        LinearMovingProjectileStrategy strategy = new LinearMovingProjectileStrategy(projectile, projectileSpeed, projectileMaxDistance, direction);
        long tick = strategy.getLastMoveTime() + TimeUnit.SECONDS.toMillis(2);

        int expectedTotalTraveled = (int) (TimeUnit.SECONDS.toMillis(2) / MILLIS_TO_SECONDS_COEFF) * projectileSpeed;
        Position expectedPosition = projectile.position().move(direction, expectedTotalTraveled);

        strategy.move(tick);

        assertEquals(expectedPosition, projectile.position());
        assertEquals(expectedTotalTraveled, strategy.getTotalTraveledDistance());
        assertTrue(projectile.active());
    }
}