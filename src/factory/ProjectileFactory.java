package factory;

import collision.Hitbox;
import core.Field;
import projectile.*;
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
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(null, defaultSpeed, defaultMaxDistance, direction);
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();

        Hitbox hitbox = new Hitbox(
                position.getX() - defaultWidth / 2,
                position.getY() + defaultHeight / 2,
                defaultWidth,
                defaultHeight,
                0);
        MovingProjectile projectile = new MovingProjectile(movingStrategy, hitbox, defaultDamage, position, behavior, field);

        movingStrategy.setProjectile(projectile);
        behavior.setProjectile(projectile);

        return projectile;
    }

}
