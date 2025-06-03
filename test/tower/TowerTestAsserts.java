package tower;

import java.util.Map;

import static asserts.TestAsserts.assertCellsEquals;
import static asserts.TestAsserts.assertProjectilesEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TowerTestAsserts {
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
}
