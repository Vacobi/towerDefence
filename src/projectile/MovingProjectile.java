package projectile;

import collision.Hitbox;
import core.Field;
import monster.Monster;
import utils.Position;


public abstract class MovingProjectile extends Projectile {
    private final MovingProjectileStrategy strategy;

    public MovingProjectile(MovingProjectileStrategy strategy, Hitbox hitbox, int damage, Position startPosition, Field field) {
        super(hitbox, damage, startPosition, field);

        this.strategy = strategy;
    }

    @Override
    public void update(long currentTick) {
        strategy.move(currentTick);

        applyEffect(currentTick);
    }

    @Override
    public void applyEffect(long currentTick) {
        for (Monster monster : getField().getWave().getAliveMonsters()) {
            if (collidesWith(monster)) {
                monster.applyDamage(getDamage());
                deactivate();
                return;
            }
        }
    }
}