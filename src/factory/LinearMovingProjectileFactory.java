package factory;

import collision.Hitbox;
import core.Field;
import projectile.LinearMovingProjectile;
import projectile.Projectile;
import utils.Direction;
import utils.Position;

public class LinearMovingProjectileFactory extends ProjectileFactory {

    private final int defaultSpeed;
    private final int defaultMaxDistance;
    private final Hitbox defaultHitbox;
    private final int defaultDamage;

    public LinearMovingProjectileFactory() {
        defaultSpeed = 20;
        defaultMaxDistance = 100;
        defaultHitbox = new Hitbox(0, 0, 1, 1, 0);
        defaultDamage = 20;
    }

    @Override
    public Projectile createProjectile(Position position, Direction direction, Field field) {
        return new LinearMovingProjectile(
                defaultSpeed,
                defaultMaxDistance,
                direction,
                defaultHitbox,
                defaultDamage,
                position,
                field
        );
    }
}
