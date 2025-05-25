package tower;

import collision.HitboxParameters;
import core.Cell;
import core.Field;
import org.junit.jupiter.api.Test;
import projectile.DirectionalProjectile;
import projectile.LaserProjectile;
import projectile.PlainProjectile;
import projectile.behavior.HitOneTargetBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static asserts.TestAsserts.assertProjectilesEquals;

class DirectionalShootingStrategyTest {

    private final Position position = new Position(100, 100);
    private final Field field = new Field();
    private final int levelsUpgradeCount = 3;
    private final int damage = 20;
    private final int range = 30;
    private final long delay = 70;
    private final TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(damage, range, delay);

    Direction mockDirection = Direction.NORTH;
    HitboxParameters hitboxParameters = new HitboxParameters(10, 10, 0);
    private final int speed = 10;

    @Test
    void instantlyShootAfterCreatingByPlainProjectileInOneDirection() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
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

        Direction direction = Direction.SOUTH;
        Cell cell = new Cell(position);
        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        long now = System.currentTimeMillis();
        new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(direction),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        HitboxParameters expectedHitboxParameters = new HitboxParameters(
                hitboxParameters.width(),
                hitboxParameters.height(),
                direction.toRadians()
        );
        PlainProjectile expectedSouthProjectile = new PlainProjectile(
                expectedHitboxParameters,
                damage,
                range,
                cell.getGlobalPosition(),
                behavior.clone(),
                field,
                direction,
                movingStrategy.clone()
        );
        List<DirectionalProjectile> expectedProjectiles = List.of(expectedSouthProjectile);

        List<DirectionalProjectile> actualProjectiles = shootingStrategy.shoot(now + TimeUnit.MILLISECONDS.toMillis(1));

        assertProjectilesEquals(expectedProjectiles, actualProjectiles);
    }

    @Test
    void instantlyShootAfterCreatingByLaserProjectileInOneDirection() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        long activeTime = 6000;
        long cooldown = 1000;
        LaserProjectile typicalProjectile = new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position,
                behavior,
                field,
                mockDirection,
                activeTime,
                cooldown
        );

        Direction direction = Direction.SOUTH;
        Cell cell = new Cell(position);
        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        long now = System.currentTimeMillis();
        Tower tower = new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(direction),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        HitboxParameters expectedHitboxParameters = new HitboxParameters(
                hitboxParameters.width(),
                hitboxParameters.height(),
                direction.toRadians()
        );
        LaserProjectile expectedSouthProjectile = new LaserProjectile(
                expectedHitboxParameters,
                damage,
                range,
                cell.getGlobalPosition(),
                behavior.clone(),
                field,
                direction,
                activeTime,
                cooldown
        );
        List<DirectionalProjectile> expectedProjectiles = List.of(expectedSouthProjectile);

        List<DirectionalProjectile> actualProjectiles = shootingStrategy.shoot(now + TimeUnit.MILLISECONDS.toMillis(1));

        assertProjectilesEquals(expectedProjectiles, actualProjectiles);
    }

    @Test
    void shootInFourDirections() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
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

        Set<Direction> directions = new LinkedHashSet<>();
        directions.addAll(Arrays.asList(Direction.values()));
        Cell cell = new Cell(position);
        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        long now = System.currentTimeMillis();
        Tower tower = new Tower(
                cell,
                field,
                shootingStrategy,
                directions.stream().toList(),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        List<DirectionalProjectile> expectedProjectiles = new LinkedList<>();
        for (Direction direction: directions) {
            HitboxParameters expectedHitboxParameters = new HitboxParameters(
                    hitboxParameters.width(),
                    hitboxParameters.height(),
                    direction.toRadians()
            );
            expectedProjectiles.add(new PlainProjectile(
                    expectedHitboxParameters,
                    damage,
                    range,
                    cell.getGlobalPosition(),
                    behavior.clone(),
                    field,
                    direction,
                    movingStrategy.clone()
            ));
        }

        List<DirectionalProjectile> actualProjectiles = shootingStrategy.shoot(now + TimeUnit.MILLISECONDS.toMillis(1));

        assertProjectilesEquals(expectedProjectiles, actualProjectiles);
    }
}
