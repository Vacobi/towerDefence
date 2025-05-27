package collision;

import org.junit.jupiter.api.Test;
import utils.Position;

import static org.junit.jupiter.api.Assertions.*;

class HitboxTest {
    @Test
    void sameHitboxes() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(200, 100, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(200, 100, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void collidesOnlyOnePoint() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(200, 100, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(150, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(75, 80, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void collidesOnlyOneEdge() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(200, 100, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(150, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(75, 100, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void collidesOnlyOneLine() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(200, 100, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(150, 20, Math.toRadians(0));
        Hitbox other = new Hitbox(76, 100, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void circumscribedRectangle() {
        HitboxParameters hitboxParameters = new HitboxParameters(500, 400, 0);
        Hitbox hitbox = new Hitbox(100, 500, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(300, 200, 0);
        Hitbox other = new Hitbox(200, 600, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void inscribedRectangle() {
        HitboxParameters hitboxParameters = new HitboxParameters(300, 200, 0);
        Hitbox hitbox = new Hitbox(500, 600, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(500, 400, 0);
        Hitbox other = new Hitbox(500, 600, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void collidesByOneEdgeInRotated() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(90, 600, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(100, 20, Math.toRadians(180));
        Hitbox other = new Hitbox(190, 600, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void collidesByOneLineInRotated() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(90, 600, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(100, 20, Math.toRadians(180));
        Hitbox other = new Hitbox(189, 600, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void collidesByOnePointByRotated() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 20, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(90, 600, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(100, 20, Math.toRadians(180));
        Hitbox other = new Hitbox(190, 580, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void notCollidesAfterRotate() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 60, Math.toRadians(135));
        Hitbox hitbox = new Hitbox(150, 150, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(120, 80, Math.toRadians(0));
        Hitbox other = new Hitbox(250, 200, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertFalse(actualCollides);
    }

    @Test
    void collidesOnlyAfterRotate() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 60, Math.toRadians(0));
        Hitbox hitbox = new Hitbox(150, 150, hitboxParameters);
        HitboxParameters hitboxParametersOther = new HitboxParameters(120, 80, Math.toRadians(30));
        Hitbox other = new Hitbox(180, 230, hitboxParametersOther);

        boolean actualCollides = hitbox.intersects(other);

        assertTrue(actualCollides);
    }

    @Test
    void moveHitbox() {
        HitboxParameters hitboxParameters = new HitboxParameters(100, 60, Math.toRadians(30));
        Hitbox hitbox = new Hitbox(150, 150, hitboxParameters);

        Position newPosition = new Position(100, 100);
        hitbox.updateCenter(newPosition);

        assertEquals(newPosition, hitbox.getCenter());
    }
}