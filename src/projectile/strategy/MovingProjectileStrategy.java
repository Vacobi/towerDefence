package projectile.strategy;

import projectile.MovingProjectile;

public interface MovingProjectileStrategy {
    void move(long currentTick);

    void setProjectile(MovingProjectile projectile);

    MovingProjectile getProjectile();

    MovingProjectileStrategy clone();
}
