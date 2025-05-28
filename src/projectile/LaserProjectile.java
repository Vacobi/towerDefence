package projectile;

import collision.HitboxParameters;
import core.Field;
import projectile.behavior.LaserBehavior;
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
            ProjectileBehavior<LaserProjectile> behavior,
            Field field,
            Direction direction,
            long activeTime,
            long damageCooldown
    ) {
        super(parameters, damage, length, startPosition, behavior, field, direction);

        if (activeTime <= 0) {
            throw new IllegalArgumentException("Active time must be positive");
        }
        this.activeTime = activeTime;

        if (damageCooldown < 0) {
            throw new IllegalArgumentException("Damage cooldown must non negative");
        }
        this.damageCooldown = damageCooldown;
    }

    @Override
    public LaserProjectile clone(int damage, int range) {

        HitboxParameters old = getHitbox().getHitboxParameters();
        HitboxParameters hitboxParameters = new HitboxParameters(range, old.height(), getDirection().toRadians());

        LaserProjectile projectile = new LaserProjectile(
                hitboxParameters,
                damage,
                range,
                position(),
                getBehavior().clone(),
                getField(),
                getDirection(),
                activeTime,
                damageCooldown
        );

        return projectile;
    }

    @Override
    public LaserProjectile clone(Position position, Direction direction) {
        ProjectileBehavior behavior = getBehavior().clone();

        HitboxParameters old = getHitbox().getHitboxParameters();
        HitboxParameters hitboxParameters = new HitboxParameters(old.width(), old.height(), direction.toRadians());

        LaserProjectile projectile = new LaserProjectile(
                hitboxParameters,
                getDamage(),
                getDistance(),
                position,
                behavior,
                getField(),
                direction,
                activeTime,
                damageCooldown
        );

        return projectile;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public long getDamageCooldown() {
        return damageCooldown;
    }
}
