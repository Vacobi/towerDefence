package projectile.behavior;

import projectile.Projectile;

public interface ProjectileBehavior<T extends Projectile> extends Cloneable {
    void applyEffect(long currentTick);

    ProjectileBehavior<T> clone();

    void setProjectile(T projectile);

    T getProjectile();
}
