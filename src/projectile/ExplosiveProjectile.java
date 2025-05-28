package projectile;

import collision.HitboxParameters;
import core.Field;
import projectile.behavior.ProjectileBehavior;
import projectile.strategy.MovingProjectileStrategy;
import utils.Direction;
import utils.Position;

public class ExplosiveProjectile extends MovingProjectile {

    private final int radius;
    private boolean exploded;

    public ExplosiveProjectile(
            HitboxParameters parameters,
            int damage,
            int distance,
            Position position,
            ProjectileBehavior<ExplosiveProjectile> behavior,
            Field field,
            Direction direction,
            MovingProjectileStrategy strategy,
            int radius
    ) {
        super(parameters, damage, distance, position, behavior, field, direction, strategy);

        if (radius <= 0) {
            throw new IllegalArgumentException("Radius of explosion must be positive");
        }
        this.radius = radius;

        this.exploded = false;
    }

    @Override
    public ExplosiveProjectile clone(Position position, Direction direction) {

        HitboxParameters old = getHitbox().getHitboxParameters();
        HitboxParameters hitboxParameters = new HitboxParameters(old.width(), old.height(), direction.toRadians());

        ExplosiveProjectile projectile = new ExplosiveProjectile(
                hitboxParameters,
                getDamage(),
                getDistance(),
                position,
                getBehavior().clone(),
                getField(),
                direction,
                getMovingStrategy().clone(),
                radius
        );

        return projectile;
    }

    @Override
    public ExplosiveProjectile clone(int damage, int range) {

        HitboxParameters old = getHitbox().getHitboxParameters();
        HitboxParameters hitboxParameters = new HitboxParameters(old.width(), old.height(), getDirection().toRadians());

        ExplosiveProjectile projectile = new ExplosiveProjectile(
                hitboxParameters,
                damage,
                range,
                position(),
                getBehavior().clone(),
                getField(),
                getDirection(),
                getMovingStrategy().clone(),
                radius
        );

        return projectile;
    }

    public int getRadius() {
        return radius;
    }

    public void setExploded() {
        this.exploded = true;
    }

    public boolean isExploded() {
        return exploded;
    }
}