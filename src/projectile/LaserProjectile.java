package projectile;

import collision.HitboxParameters;
import core.Field;
import projectile.behavior.ProjectileBehavior;
import utils.Direction;
import utils.Position;

public class LaserProjectile extends DirectionalProjectile{

    private final long activeTime;
    private final long damageCooldown;

    public LaserProjectile(
            HitboxParameters parameters,
            int damage,
            int length,
            Position startPosition,
            ProjectileBehavior behavior,
            Field field,
            Direction direction,
            long activeTime,
            long damageCooldown
    ) {
        super(parameters, damage, length, startPosition, behavior, field, direction);

        this.activeTime = activeTime;

        this.damageCooldown = damageCooldown;
    }

    @Override
    public LaserProjectile clone(int damage, int range) {
        ProjectileBehavior behavior = getBehavior().clone();

        LaserProjectile projectile = new LaserProjectile(
                getHitbox().getHitboxParameters(),
                damage,
                getDistance(),
                position(),
                behavior,
                getField(),
                getDirection(),
                activeTime,
                damageCooldown
        );

        behavior.setProjectile(projectile);

        return projectile;
    }

    @Override
    public LaserProjectile clone(Position position, Direction direction) {
        ProjectileBehavior behavior = getBehavior().clone();

        LaserProjectile projectile = new LaserProjectile(
                getHitbox().getHitboxParameters(),
                getDamage(),
                getDistance(),
                position,
                behavior,
                getField(),
                getDirection(),
                activeTime,
                damageCooldown
        );

        behavior.setProjectile(projectile);

        return projectile;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public long getDamageCooldown() {
        return damageCooldown;
    }
}
