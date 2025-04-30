package tower;

import core.Field;
import factory.ProjectileFactory;
import projectile.Projectile;
import utils.Direction;
import utils.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlainShootingStrategy implements ShootingStrategy{

    private Long lastShootTime;
    private long shootingDelay;
    private Position position;
    private List<Direction> shotDirections;
    private final Field field;

    private final ProjectileFactory projectileFactory;

    public PlainShootingStrategy(long shootingDelay,
                                 Position position,
                                 List<Direction> shotDirections,
                                 Field field) {
        this.shootingDelay = shootingDelay;
        this.position = position;
        this.shotDirections = shotDirections;
        this.field = field;

        projectileFactory = new ProjectileFactory();
        lastShootTime = System.currentTimeMillis();
    }

    @Override
    public Set<Projectile> shoot(long currentTick) {

        if (currentTick - lastShootTime < shootingDelay) {
            return Set.of();
        }

        Set<Projectile> projectiles = new HashSet<>();
        Position startPosition = position;
        shotDirections.forEach((Direction direction) -> {
            projectiles.add(projectileFactory.createLinearMovingHitOneTargetProjectile(startPosition, direction, field));
        });

        return projectiles;
    }
}
