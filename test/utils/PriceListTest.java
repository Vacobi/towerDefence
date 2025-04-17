package utils;

import org.junit.jupiter.api.Test;
import tower.Tower;
import tower.TowerUpgradableCharacteristic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PriceListTest {
    private final PriceList priceList = new PriceList();

    @Test
    void getDefaultTowerBuildPrice() {

        Optional<Integer> actualPrice = priceList.getBuildPrice(Tower.class);

        assertTrue(actualPrice.isPresent());
        int actualBuildPrice = actualPrice.get();
        int expPrice = 20;
        assertEquals(expPrice, actualBuildPrice);
    }

    @Test
    void getDefaultTowerRangeUpdatePrice() {

        Optional<Integer> actualPriceFirstLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.RANGE, 1);
        Optional<Integer> actualPriceSecondLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.RANGE, 2);
        Optional<Integer> actualPriceThirdLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.RANGE, 3);


        assertTrue(actualPriceFirstLevel.isPresent());
        int actualUpgradePriceFirstLevel = actualPriceFirstLevel.get();
        int expPriceFirstLevel = 10;
        assertEquals(expPriceFirstLevel, actualUpgradePriceFirstLevel);

        assertTrue(actualPriceSecondLevel.isPresent());
        int actualUpgradePriceSecondLevel = actualPriceSecondLevel.get();
        int expPriceSecondLevel = 20;
        assertEquals(expPriceSecondLevel, actualUpgradePriceSecondLevel);

        assertTrue(actualPriceThirdLevel.isPresent());
        int actualUpgradePriceThirdLevel = actualPriceThirdLevel.get();
        int expPriceThirdLevel = 30;
        assertEquals(expPriceThirdLevel, actualUpgradePriceThirdLevel);
    }

    @Test
    void getDefaultTowerDamageUpdatePrice() {

        Optional<Integer> actualPriceFirstLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.DAMAGE, 1);
        Optional<Integer> actualPriceSecondLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.DAMAGE, 2);
        Optional<Integer> actualPriceThirdLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.DAMAGE, 3);


        assertTrue(actualPriceFirstLevel.isPresent());
        int actualUpgradePriceFirstLevel = actualPriceFirstLevel.get();
        int expPriceFirstLevel = 10;
        assertEquals(expPriceFirstLevel, actualUpgradePriceFirstLevel);

        assertTrue(actualPriceSecondLevel.isPresent());
        int actualUpgradePriceSecondLevel = actualPriceSecondLevel.get();
        int expPriceSecondLevel = 20;
        assertEquals(expPriceSecondLevel, actualUpgradePriceSecondLevel);

        assertTrue(actualPriceThirdLevel.isPresent());
        int actualUpgradePriceThirdLevel = actualPriceThirdLevel.get();
        int expPriceThirdLevel = 30;
        assertEquals(expPriceThirdLevel, actualUpgradePriceThirdLevel);
    }

    @Test
    void getDefaultTowerDelayUpdatePrice() {

        Optional<Integer> actualPriceFirstLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.SHOOTING_DELAY, 1);
        Optional<Integer> actualPriceSecondLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.SHOOTING_DELAY, 2);
        Optional<Integer> actualPriceThirdLevel = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.SHOOTING_DELAY, 3);


        assertTrue(actualPriceFirstLevel.isPresent());
        int actualUpgradePriceFirstLevel = actualPriceFirstLevel.get();
        int expPriceFirstLevel = 10;
        assertEquals(expPriceFirstLevel, actualUpgradePriceFirstLevel);

        assertTrue(actualPriceSecondLevel.isPresent());
        int actualUpgradePriceSecondLevel = actualPriceSecondLevel.get();
        int expPriceSecondLevel = 20;
        assertEquals(expPriceSecondLevel, actualUpgradePriceSecondLevel);

        assertTrue(actualPriceThirdLevel.isPresent());
        int actualUpgradePriceThirdLevel = actualPriceThirdLevel.get();
        int expPriceThirdLevel = 30;
        assertEquals(expPriceThirdLevel, actualUpgradePriceThirdLevel);
    }

    @Test
    void getNegativeLevelUpgrade() {
        Optional<Integer> actualPrice = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.SHOOTING_DELAY, -1);

        assertTrue(actualPrice.isEmpty());
    }

    @Test
    void getZeroLevelUpgrade() {
        Optional<Integer> actualPrice = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.SHOOTING_DELAY, 0);

        assertTrue(actualPrice.isEmpty());
    }

    @Test
    void getMoreThanMaxLevelUpgrade() {
        Optional<Integer> actualPrice = priceList.getUpgradePrice(Tower.class, TowerUpgradableCharacteristic.SHOOTING_DELAY, 4);

        assertTrue(actualPrice.isEmpty());
    }
}