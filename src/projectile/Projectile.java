package projectile;

import collision.CollisionObject;
import collision.Hitbox;
import core.Field;
import utils.Position;

public abstract class Projectile extends CollisionObject {
    private final int damage;
    private final Field field;
    private boolean active;
    private Position position;

    private final ProjectileBehavior projectileBehavior;

    public Projectile(Hitbox hitbox, int damage, Position startPosition, ProjectileBehavior behavior, Field field) {
        super(hitbox);

        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        this.damage = damage;

        this.active = true;
        this.position = startPosition;
        this.projectileBehavior = behavior;
        this.field = field;

        updateHitboxPosition(startPosition);
    }

    public abstract void update(long currentTick);

    protected void applyEffect(long currentTick) {
        projectileBehavior.applyEffect(currentTick);
    }

    public boolean active() {
        return active;
    }

    // - ///////////////////////////////////////////////
    protected void setPosition(Position position) {
        this.position = position;
    }

    public Position position() {
        return position;
    }

    protected void deactivate() {
        active = false;
    }

    protected int getDamage() {
        return damage;
    }

    protected Field getField() {
        return field;
    }

    protected ProjectileBehavior getBehavior() {
        return projectileBehavior;
    }

    public abstract Projectile clone(int damage, int range);
}
