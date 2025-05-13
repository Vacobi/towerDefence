package asserts;

import collision.Hitbox;
import collision.HitboxParameters;
import core.Cell;
import projectile.DirectionalProjectile;
import projectile.MovingProjectile;
import projectile.Projectile;
import road.RoadSegment;
import tower.Tower;
import tower.TowerCharacteristicsValues;
import tower.TowerUpgradableCharacteristic;
import utils.BuildingResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

    public static void assertTowersEquals(Tower expected, Tower actual) {
        assertTowerCharacteristicsEquals(expected.getCharacteristicValues(), actual.getCharacteristicValues());
        assertCellsEquals(expected.getCell(), actual.getCell());
        assertEquals(expected.getField(), actual.getField());
        assertEquals(expected.getStrategy().getClass(), actual.getStrategy().getClass());
        assertEquals(expected.getShotDirections(), actual.getShotDirections());
        assertEquals(expected.getLevelsUpgradeCount(), actual.getLevelsUpgradeCount());
        assertProjectilesEquals(expected.getTypicalProjectile(), actual.getTypicalProjectile());
        assertEquals(expected.getCharacteristicLevels(), actual.getCharacteristicLevels());
    }

    public static void assertTowerCharacteristicsEquals(TowerCharacteristicsValues expected, TowerCharacteristicsValues actual) {
        assertEquals(expected.getDamage(), actual.getDamage());
        assertEquals(expected.getRange(), actual.getRange());
        assertEquals(expected.shootingDelay(), actual.shootingDelay());
    }

    public static void assertTowersAfterUpgradeEquals(
            Tower expectedTower,
            Tower actualTower,
            TowerCharacteristicsValues expectedCharacteristics,
            Map<TowerUpgradableCharacteristic, Integer> characteristicLevels
    ) {
        assertCellsEquals(expectedTower.getCell(), actualTower.getCell());
        assertEquals(expectedTower.getField(), actualTower.getField());
        assertEquals(expectedTower.getStrategy().getClass(), actualTower.getStrategy().getClass());
        assertEquals(expectedTower.getShotDirections(), actualTower.getShotDirections());
        assertEquals(expectedTower.getLevelsUpgradeCount(), actualTower.getLevelsUpgradeCount());
        assertProjectilesEquals(
                expectedTower.getTypicalProjectile().clone(expectedCharacteristics.getDamage(), expectedCharacteristics.getRange()),
                actualTower.getTypicalProjectile()
        );
        assertEquals(characteristicLevels, actualTower.getCharacteristicLevels());
        assertTowerCharacteristicsEquals(expectedCharacteristics, actualTower.getCharacteristicValues());
    }

    public static void assertCellsEquals(Cell expected, Cell actual) {
        assertEquals(expected.canPlaceTower(), actual.canPlaceTower());
        assertEquals(expected.position(), actual.position());
    }

    public static void assertBuildingResponsesEquals(BuildingResponse expected, BuildingResponse actual) {
        assertEquals(expected.change(), actual.change());
        assertEquals(expected.built(), actual.built());
    }
}
