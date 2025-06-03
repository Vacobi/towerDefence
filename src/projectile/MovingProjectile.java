package projectile;

import collision.HitboxParameters;
import core.Field;
import projectile.behavior.ProjectileBehavior;
import projectile.strategy.MovingProjectileStrategy;
import utils.Direction;
import utils.Position;

public abstract class MovingProjectile extends DirectionalProjectile {
    private final MovingProjectileStrategy strategy;

    public MovingProjectile(
            HitboxParameters parameters,
            int damage,
            int distance,
            Position startPosition,
            ProjectileBehavior behavior,
            Field field,
            Direction direction,
            MovingProjectileStrategy strategy
    ) {
        super(parameters, damage, distance, startPosition, behavior, field, direction);

        this.strategy = strategy;
        strategy.setProjectile(this);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void update(long currentTick) {
        super.update(currentTick);

        strategy.move(currentTick);
    }

    //------------------------------------------------------------------------------------------------------------------

    public MovingProjectileStrategy getMovingStrategy() {
        return strategy;
    }
}