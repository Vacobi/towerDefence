package tower;

import projectile.Projectile;

import java.util.Set;

public interface ShootingStrategy {
    Set<Projectile> shoot(long currentTick);
}
