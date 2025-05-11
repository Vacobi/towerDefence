package factory;

import collision.HitboxParameters;
import core.Field;
import projectile.*;
import projectile.behavior.HitOneTargetBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

public class ProjectileFactory {
    private final int defaultSpeed;
    private final int defaultMaxDistance;
    private final int defaultDamage;
    private final int defaultWidth;
    private final int defaultHeight;

    public ProjectileFactory() {
        defaultSpeed = 20;
        defaultMaxDistance = 100;
        defaultDamage = 20;
        defaultWidth = 10;
        defaultHeight = 10;
    }

    public MovingProjectile createLinearMovingHitOneTargetProjectile(Position position, Direction direction, Field field) {
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(defaultSpeed);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();

        HitboxParameters hitboxParameters = new HitboxParameters(
                defaultWidth,
                defaultHeight,
                0
        );
        MovingProjectile projectile = new PlainProjectile(
                hitboxParameters,
                defaultDamage,
                defaultMaxDistance,
                position,
                behavior,
                field,
                direction,
                movingStrategy
        );

        movingStrategy.setProjectile(projectile);
        behavior.setProjectile(projectile);

        return projectile;
    }

}
