package testutils;

import collision.HitboxParameters;
import core.Field;
import projectile.*;
import projectile.behavior.HitOneTargetBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

public class ProjectileFactory {

    public MovingProjectile createLinearMovingHitOneTargetProjectile(Position position, Direction direction, Field field) {
        int speed = 20;
        int maxDistance = 100;
        int damage = 20;
        int width = 10;
        int height = 10;

        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();

        HitboxParameters hitboxParameters = new HitboxParameters(
                width,
                height,
                0
        );
        MovingProjectile projectile = new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                position,
                behavior,
                field,
                direction,
                movingStrategy
        );

        return projectile;
    }

}
