package projectile;

import collision.CollisionObject;
import collision.HitboxParameters;
import core.Field;
import projectile.behavior.ProjectileBehavior;
import utils.Position;

public abstract class Projectile extends CollisionObject {
    private final int damage;
    private final int distance;

    private final Field field;
    private Position position;

    private boolean active;

    private final ProjectileBehavior projectileBehavior;

    public Projectile(
            HitboxParameters parameters,
            int damage,
            int distance,
            Position startPosition,
            ProjectileBehavior behavior,
            Field field
    ) {
        super(startPosition, parameters);

        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        this.damage = damage;

        if (distance <= 0) {
            throw new IllegalArgumentException("Max distance must be greater than 0");
        }
        this.distance = distance;

        this.active = true;

        this.projectileBehavior = behavior;
        this.projectileBehavior.setProjectile(this);

        this.field = field;
        this.position = startPosition;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void update(long currentTick) {
        if (!active) {
            throw new IllegalStateException("Projectile is not active");
        }

        projectileBehavior.applyEffect(currentTick);
    }

    public abstract Projectile clone(int damage, int range);

    public void deactivate() {
        active = false;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean active() {
        return active;
    }

    public Position position() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        updateHitboxPosition(position);
    }

    public int getDamage() {
        return damage;
    }

    public Field getField() {
        return field;
    }

    public ProjectileBehavior getBehavior() {
        return projectileBehavior;
    }

    public int getDistance() {
        return distance;
    }
}
