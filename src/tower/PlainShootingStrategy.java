package tower;

import projectile.DirectionalProjectile;
import projectile.Projectile;
import utils.Direction;
import utils.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlainShootingStrategy implements ShootingStrategy{

    private Long lastShootTime;
    private long shootingDelay;
    private final Position position;
    private final List<Direction> shotDirections;

    private final DirectionalProjectile projectile;

    public PlainShootingStrategy(DirectionalProjectile projectile,
                                 long shootingDelay,
                                 Position position,
                                 List<Direction> shotDirections) {
        this.shootingDelay = shootingDelay;
        this.position = position;
        this.shotDirections = shotDirections;

        this.projectile = projectile;
        lastShootTime = System.currentTimeMillis() - shootingDelay;
    }

    @Override
    public Set<Projectile> shoot(long currentTick) {

        if (currentTick - lastShootTime < shootingDelay) {
            return Set.of();
        }

        Set<Projectile> projectiles = new HashSet<>();
        shotDirections.forEach((Direction direction) -> {
            projectiles.add(projectile.clone(position, direction));
        });

        return projectiles;
    }
}
