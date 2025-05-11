package collision;

import org.junit.jupiter.api.Test;
import utils.Position;

import static org.junit.jupiter.api.Assertions.*;

class CollisionObjectTest {
    @Test
    void objectsCollides() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(200, 100, hitboxParameters);
        CollisionObject collisionObject = new CollisionObject(hitbox);
        HitboxParameters hitboxParametersOther = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(250, 100, hitboxParametersOther);
        CollisionObject otherCollisionObject = new CollisionObject(other);

        boolean actualCollides = collisionObject.collidesWith(otherCollisionObject);

        assertTrue(actualCollides);
    }

    @Test
    void objectsNotCollides() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(200, 100, hitboxParameters);
        CollisionObject collisionObject = new CollisionObject(hitbox);
        HitboxParameters hitboxParametersOther = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(350, 100, hitboxParameters);
        CollisionObject otherCollisionObject = new CollisionObject(other);

        boolean actualCollides = collisionObject.collidesWith(otherCollisionObject);

        assertFalse(actualCollides);
    }

    @Test
    void moveObject() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(200, 100, hitboxParameters);
        CollisionObject collisionObject = new CollisionObject(hitbox);

        Position newPosition = new Position(90, 80);
        collisionObject.updateHitboxPosition(newPosition);

        assertEquals(newPosition, collisionObject.getHitbox().getLeftTop());
    }
}
