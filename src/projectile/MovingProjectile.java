package projectile;

import collision.Hitbox;
import core.Field;
import utils.Position;


public class MovingProjectile extends Projectile {
    private final MovingProjectileStrategy strategy;

    public MovingProjectile(MovingProjectileStrategy strategy, Hitbox hitbox, int damage, Position startPosition, ProjectileBehavior behavior, Field field) {
        super(hitbox, damage, startPosition, behavior, field);

        this.strategy = strategy;
    }

    @Override
    public void update(long currentTick) {
        strategy.move(currentTick);

        applyEffect(currentTick);
    }
}