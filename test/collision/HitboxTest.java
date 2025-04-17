package collision;

import org.junit.jupiter.api.Test;
import utils.Position;

import static org.junit.jupiter.api.Assertions.*;

class HitboxTest {
    @Test
    void sameHitboxes() {
        Hitbox hitbox = new Hitbox(200, 100, 100, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(200, 100, 100, 20, Math.toRadians(0));

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void collidesOnlyOnePoint() {
        Hitbox hitbox = new Hitbox(200, 100, 100, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(50, 80, 150, 20, Math.toRadians(0));

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void collidesOnlyOneEdge() {
        Hitbox hitbox = new Hitbox(200, 100, 100, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(50, 100, 150, 20, Math.toRadians(0));

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void collidesOnlyOneLine() {
        Hitbox hitbox = new Hitbox(200, 100, 100, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(51, 100, 150, 20, Math.toRadians(0));

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void circumscribedRectangle() {
        Hitbox hitbox = new Hitbox(100, 500, 500, 400, 0);
        Hitbox other = new Hitbox(200, 600, 300, 200, 0);

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void inscribedRectangle() {
        Hitbox hitbox = new Hitbox(200, 600, 300, 200, 0);
        Hitbox other = new Hitbox(100, 500, 500, 400, 0);

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void collidesByOneEdgeInRotated() {
        Hitbox hitbox = new Hitbox(700, 100, 400, 200, Math.toRadians(90));
        Hitbox other = new Hitbox(100, 200, 400, 200, Math.toRadians(0));

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void collidesByOneLineInRotated() {
        Hitbox hitbox = new Hitbox(700, 100, 400, 200, Math.toRadians(90));
        Hitbox other = new Hitbox(101, 200, 400, 200, Math.toRadians(0));

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void collidesByOnePointByRotated() {
        Hitbox hitbox = new Hitbox(200, 100, 400, 200, Math.toRadians(0));
        Hitbox other = new Hitbox(600, 300, 400, 200, Math.toRadians(45));

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void notCollidesAfterRotate() {
        Hitbox hitbox = new Hitbox(150, 150, 100, 60, Math.toRadians(135));
        Hitbox other = new Hitbox(180, 200, 120, 80, Math.toRadians(0));

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void collidesOnlyAfterRotate() {
        Hitbox hitbox = new Hitbox(150, 150, 100, 60, Math.toRadians(0));
        Hitbox other = new Hitbox(260, 180, 120, 80, Math.toRadians(45));

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void moveHitbox() {
        Hitbox hitbox = new Hitbox(150, 150, 100, 60, Math.toRadians(30));

        Position newPosition = new Position(100, 100);
        hitbox.updateLeftTop(newPosition);

        assertEquals(newPosition, hitbox.getLeftTop());
    }
}