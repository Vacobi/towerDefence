package projectile;

import collision.HitboxParameters;
import core.Field;
import projectile.behavior.ProjectileBehavior;
import utils.Direction;
import utils.Position;


public abstract class DirectionalProjectile extends Projectile {
    private Direction direction;

    public DirectionalProjectile(
            HitboxParameters parameters,
            int damage,
            int distance,
            Position startPosition,
            ProjectileBehavior behavior,
            Field field,
            Direction direction
    ) {
        super(parameters, damage, distance, startPosition, behavior, field);

        this.direction = direction;
    }

    public abstract DirectionalProjectile clone(Position position, Direction direction);

    public Direction getDirection() {
        return direction;
    }
}
