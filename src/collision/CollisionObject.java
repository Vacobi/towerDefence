package collision;

import utils.Position;

public class CollisionObject {

    private Hitbox hitbox;

    public CollisionObject(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public CollisionObject(Position center, HitboxParameters parameters) {
        this.hitbox = new Hitbox(center, parameters);
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean collidesWith(CollisionObject other) {
        return hitbox.intersects(other.hitbox);
    }

    public void updateHitboxPosition(Position newPosition) {
        hitbox.updateCenter(newPosition);
    }

    public void updateHitboxParameters(HitboxParameters newHitboxParameters) {
        hitbox = new Hitbox(hitbox.getCenter(), newHitboxParameters);
    }

    //------------------------------------------------------------------------------------------------------------------

    public Hitbox getHitbox() {
        return hitbox;
    }
}
