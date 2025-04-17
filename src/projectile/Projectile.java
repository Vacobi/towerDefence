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

    public Projectile(Hitbox hitbox, int damage, Position startPosition, Field field) {
        super(hitbox);

        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        this.damage = damage;

        this.active = true;
        this.position = startPosition;
        this.field = field;
    }

    public abstract void update(long currentTick);

    protected abstract void applyEffect(long currentTick);

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
}
