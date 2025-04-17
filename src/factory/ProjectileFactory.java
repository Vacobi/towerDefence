package factory;

import core.Field;
import projectile.Projectile;
import utils.Direction;
import utils.Position;

public abstract class ProjectileFactory {
    public abstract Projectile createProjectile(Position position, Direction direction, Field field);
}
