package asserts;

import collision.Hitbox;
import collision.HitboxParameters;
import projectile.DirectionalProjectile;
import projectile.MovingProjectile;
import projectile.Projectile;
import road.RoadSegment;

import java.util.List;

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

    public static void assertProjectilesEquals(List<? extends Projectile> expectedProjectile, List<? extends Projectile> actualProjectile) {
        for (int i = 0; i < expectedProjectile.size(); i++) {
            TestAsserts.assertProjectilesEquals(expectedProjectile.get(i), actualProjectile.get(i));
        }
    }

    public static void assertProjectilesEquals(Projectile expectedProjectile, Projectile actualProjectile) {
        assertEquals(expectedProjectile.getClass(), actualProjectile.getClass());

        assertHitboxesEquals(expectedProjectile.getHitbox(), actualProjectile.getHitbox());

        assertEquals(expectedProjectile.getDamage(), actualProjectile.getDamage());
        assertEquals(expectedProjectile.getDistance(), actualProjectile.getDistance());

        assertEquals(expectedProjectile.position(), actualProjectile.position());

        assertEquals(expectedProjectile.getBehavior().getClass(), actualProjectile.getBehavior().getClass());
        assertNotEquals(expectedProjectile.getBehavior(), actualProjectile.getBehavior());

        assertEquals(expectedProjectile.getField(), actualProjectile.getField());

        if (expectedProjectile instanceof DirectionalProjectile expectedDirectionalProjectile) {
            DirectionalProjectile actualDirectionalProjectile = (DirectionalProjectile) actualProjectile;
            assertEquals(expectedDirectionalProjectile.getDirection(), actualDirectionalProjectile.getDirection());
        }

        if (expectedProjectile instanceof MovingProjectile expectedMovingProjectile) {
            if (actualProjectile instanceof MovingProjectile actualMovingProjectile) {
                assertEquals(expectedMovingProjectile.getMovingStrategy().getClass(), actualMovingProjectile.getMovingStrategy().getClass());
                assertNotEquals(expectedMovingProjectile.getMovingStrategy(), actualMovingProjectile.getMovingStrategy());
            }
            MovingProjectile actualMovingProjectile = (MovingProjectile) actualProjectile;
            assertEquals(expectedMovingProjectile.getMovingStrategy().getClass(), actualMovingProjectile.getMovingStrategy().getClass());
            assertNotEquals(expectedMovingProjectile.getMovingStrategy(), actualMovingProjectile.getMovingStrategy());
        }
    }

    public static void assertRoadSegmentsEquals(RoadSegment expected, RoadSegment actual) {
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getDirection(), actual.getDirection());
        assertEquals(expected.getLength(), actual.getLength());
    }
}
