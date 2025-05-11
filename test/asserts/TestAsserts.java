package asserts;

import collision.Hitbox;
import collision.HitboxParameters;
import projectile.MovingProjectile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestAsserts {
    public static void assertHitboxesEquals(Hitbox expected, Hitbox actual) {
        assertHitboxParametersEquals(expected.getHitboxParameters(), actual.getHitboxParameters());

        assertEquals(expected.getLeftTop(), actual.getLeftTop());
    }

    private static void assertHitboxParametersEquals(HitboxParameters expected, HitboxParameters actual) {
        assertEquals(expected.width(), actual.width());
        assertEquals(expected.height(), actual.height());
        assertEquals(expected.angle(), actual.angle());
    }

    public static void assertMovingProjectilesEquals(MovingProjectile expectedProjectile, MovingProjectile actualProjectile) {
        assertHitboxesEquals(expectedProjectile.getHitbox(), actualProjectile.getHitbox());

        assertEquals(expectedProjectile.getDamage(), actualProjectile.getDamage());
        assertEquals(expectedProjectile.getDistance(), actualProjectile.getDistance());

        assertEquals(expectedProjectile.position(), actualProjectile.position());

        assertEquals(expectedProjectile.getBehavior().getClass(), actualProjectile.getBehavior().getClass());
        assertNotEquals(expectedProjectile.getBehavior(), actualProjectile.getBehavior());

        assertEquals(expectedProjectile.getField(), actualProjectile.getField());

        assertEquals(expectedProjectile.getDirection(), actualProjectile.getDirection());

        assertEquals(expectedProjectile.getMovingStrategy().getClass(), actualProjectile.getMovingStrategy().getClass());
        assertNotEquals(expectedProjectile.getMovingStrategy(), actualProjectile.getMovingStrategy());
    }
}
