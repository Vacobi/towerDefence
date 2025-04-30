package projectile;

import collision.Hitbox;
import core.Field;
import utils.Direction;
import utils.Position;

public abstract class DirectionalProjectile extends Projectile {
    public DirectionalProjectile(Hitbox hitbox, int damage, Position startPosition, ProjectileBehavior behavior, Field field) {
        super(hitbox, damage, startPosition, behavior, field);
    }

    public abstract Projectile clone(Position position, Direction direction);
}
