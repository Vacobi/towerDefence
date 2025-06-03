package projectile.strategy;

import projectile.MovingProjectile;

public interface MovingProjectileStrategy {
    void move(long currentTick);

    MovingProjectileStrategy clone();

    void setProjectile(MovingProjectile projectile);

    MovingProjectile getProjectile();
}
