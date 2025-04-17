package projectile;

import collision.CollisionObject;
import collision.Hitbox;

public abstract class Projectile extends CollisionObject {

    public Projectile(Hitbox hitbox) {
        super(hitbox);
    }
}
