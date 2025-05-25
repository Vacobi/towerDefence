package projectile;

import collision.HitboxParameters;
import core.Field;
import projectile.behavior.ProjectileBehavior;
import projectile.strategy.MovingProjectileStrategy;
import utils.Direction;
import utils.Position;

public class PlainProjectile extends MovingProjectile {

    public PlainProjectile(
            HitboxParameters parameters,
            int damage,
            int distance,
            Position startPosition,
            ProjectileBehavior behavior,
            Field field,
            Direction direction,
            MovingProjectileStrategy strategy
    ) {
        super(parameters, damage, distance, startPosition, behavior, field, direction, strategy);
    }

    @Override
    public PlainProjectile clone(int damage, int range) {

        PlainProjectile projectile = new PlainProjectile(
                getHitbox().getHitboxParameters(),
                damage,
                range,
                position(),
                getBehavior().clone(),
                getField(),
                getDirection(),
                getMovingStrategy().clone()
        );

        return projectile;
    }

    @Override
    public PlainProjectile clone(Position position, Direction direction) {
        ProjectileBehavior behavior = getBehavior().clone();

        HitboxParameters old = getHitbox().getHitboxParameters();
        HitboxParameters hitboxParameters = new HitboxParameters(old.width(), old.height(), direction.toRadians());

        PlainProjectile projectile = new PlainProjectile(
                hitboxParameters,
                getDamage(),
                getDistance(),
                position,
                behavior,
                getField(),
                direction,
                getMovingStrategy().clone()
        );

        return projectile;
    }
}
