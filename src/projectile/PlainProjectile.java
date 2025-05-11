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
        ProjectileBehavior behavior = getBehavior().clone();
        MovingProjectileStrategy movingStrategy = getMovingStrategy().clone();

        PlainProjectile projectile = new PlainProjectile(
                getHitbox().getHitboxParameters(),
                damage,
                range,
                position(),
                behavior,
                getField(),
                getDirection(),
                movingStrategy
        );

        behavior.setProjectile(projectile);
        movingStrategy.setProjectile(projectile);

        return projectile;
    }

    @Override
    public PlainProjectile clone(Position position, Direction direction) {
        ProjectileBehavior behavior = getBehavior().clone();
        MovingProjectileStrategy movingStrategy = getMovingStrategy().clone();

        PlainProjectile projectile = new PlainProjectile(
                getHitbox().getHitboxParameters(),
                getDamage(),
                getDistance(),
                position,
                behavior,
                getField(),
                direction,
                movingStrategy
        );

        behavior.setProjectile(projectile);
        movingStrategy.setProjectile(projectile);

        return projectile;
    }
}
