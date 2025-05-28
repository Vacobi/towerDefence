package testutils;

import collision.HitboxParameters;
import core.Field;
import projectile.*;
import projectile.behavior.ExplosiveBehavior;
import projectile.behavior.HitOneTargetBehavior;
import projectile.behavior.LaserBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import java.util.concurrent.TimeUnit;

public class ProjectileFactory {

    public MovingProjectile createLinearMovingHitOneTargetProjectile(Position position, Direction direction, Field field) {
        int speed = 1;
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

    public LaserProjectile createLaserProjectile(Position position, Direction direction, Field field) {
        LaserBehavior behavior = new LaserBehavior();

        int length = 180;
        int damage = 20;
        int height = 30;
        HitboxParameters hitboxParameters = new HitboxParameters(length, height, direction.toRadians());
        long activeTime = TimeUnit.SECONDS.toMillis(3);
        long damageCooldown = TimeUnit.MILLISECONDS.toMillis(500);

        return new LaserProjectile(
                hitboxParameters,
                damage,
                length,
                position,
                behavior,
                field,
                direction,
                activeTime,
                damageCooldown
        );
    }

    public ExplosiveProjectile createExplosiveProjectile(Position position, Direction direction, Field field) {
        int speed = 1;
        int maxDistance = 100;
        int damage = 20;
        int width = 30;
        int height = 30;
        int radius = 100;

        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        ExplosiveBehavior behavior = new ExplosiveBehavior();

        HitboxParameters hitboxParameters = new HitboxParameters(
                width,
                height,
                0
        );
        ExplosiveProjectile projectile = new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                position,
                behavior,
                field,
                direction,
                movingStrategy,
                radius
        );

        return projectile;
    }
}
