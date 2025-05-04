package projectile;

import collision.Hitbox;
import core.Field;
import utils.Direction;
import utils.Position;


public class MovingProjectile extends DirectionalProjectile {
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

    @Override
    public MovingProjectile clone(int damage, int range) {
        return new MovingProjectile(
                strategy.clone(range),
                getHitbox(),
                damage,
                position(),
                getBehavior(),
                getField()
        );
    }

    @Override
    public MovingProjectile clone(Position position, Direction direction) {
        return new MovingProjectile(
                strategy.clone(direction),
                getHitbox(),
                getDamage(),
                position,
                getBehavior(),
                getField()
        );
    }
}