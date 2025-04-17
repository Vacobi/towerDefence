package collision;

import utils.Position;

public class CollisionObject {
    private final Hitbox hitbox;

    public CollisionObject(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public Hitbox getHitbox() {
        return hitbox;
    }

    public boolean collidesWith(CollisionObject other) {
        return hitbox.intersects(other.hitbox);
    }

    public void updateHitboxPosition(Position newPosition) {
        hitbox.updateLeftTop(newPosition);
    }
}