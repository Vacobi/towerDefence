package projectile;

import collision.Hitbox;
import core.Field;
import monster.Monster;
import utils.Position;

@Deprecated
public class LinearMovingProjectile extends MovingProjectile {
//    public LinearMovingProjectile(int speed, int maxDistance, Direction direction, Hitbox hitbox, int damage, Position startPosition, Field field) {
//        super(new LinearMovingProjectileStrategy(this, speed, maxDistance, direction), hitbox, damage, startPosition, field);
//    }

    public LinearMovingProjectile(MovingProjectileStrategy strategy, Hitbox hitbox, int damage, Position startPosition, Field field) {
        super(strategy, hitbox, damage, startPosition, field);
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