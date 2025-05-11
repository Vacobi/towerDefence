package collision;

import utils.Position;

public class CollisionObject {
    private Hitbox hitbox;

    public CollisionObject(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public CollisionObject(Position leftTop, HitboxParameters parameters) {
        this.hitbox = new Hitbox(leftTop, parameters);
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

    public void updateHitboxParameters(HitboxParameters newHitboxParameters) {
        hitbox = new Hitbox(hitbox.getLeftTop(), newHitboxParameters);
    }
}
