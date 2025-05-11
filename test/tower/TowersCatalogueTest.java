package tower;

import core.Field;
import org.junit.jupiter.api.Test;
import projectile.LaserProjectile;
import projectile.MovingProjectile;
import projectile.PlainProjectile;
import projectile.Projectile;
import projectile.behavior.HitOneTargetBehavior;
import projectile.behavior.LaserBehavior;
import projectile.behavior.ProjectileBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import projectile.strategy.MovingProjectileStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TowersCatalogueTest {

    Field field = new Field();

    @Test
    void towersCatalogueContainsAllNecessaryTowers() {
        TowersCatalogue towersCatalogue = new TowersCatalogue(field);

        int expectedTowers = 2;

        int actualTowers = towersCatalogue.getAvailableTowersWithPrices().size();

        assertEquals(expectedTowers, actualTowers);
    }

    @Test
    void towersCatalogueContainsTowerWithPlainProjectile() {
        TowersCatalogue towersCatalogue = new TowersCatalogue(field);

        Class<? extends DirectionalShootingStrategy> expectedShootingStrategy = DirectionalShootingStrategy.class;
        Class<? extends MovingProjectile> expectedProjectile = PlainProjectile.class;
        Class<? extends MovingProjectileStrategy> expectedMovingStrategy = LinearMovingProjectileStrategy.class;
        Class<? extends ProjectileBehavior> expectedProjectileBehavior = HitOneTargetBehavior.class;
        int expectedPrice = 20;

        Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
        Tower<? extends Projectile> actualTowerWithThisProjectile = null;
        for (Tower<? extends Projectile> tower : towers.keySet()) {
            if (tower.getTypicalProjectile().getClass() == expectedProjectile) {
                actualTowerWithThisProjectile = tower;
            }
        }

        assertNotNull(actualTowerWithThisProjectile);
        assertEquals(expectedShootingStrategy, actualTowerWithThisProjectile.getStrategy().getClass());
        assertEquals(expectedProjectile, actualTowerWithThisProjectile.getTypicalProjectile().getClass());
        assertEquals(expectedMovingStrategy, ((MovingProjectile)actualTowerWithThisProjectile.getTypicalProjectile()).getMovingStrategy().getClass());
        assertEquals(expectedProjectileBehavior, actualTowerWithThisProjectile.getTypicalProjectile().getBehavior().getClass());
        assertTrue(towersCatalogue.getPrice(actualTowerWithThisProjectile).isPresent());
        assertEquals(expectedPrice, towersCatalogue.getPrice(actualTowerWithThisProjectile).get());
    }

    @Test
    void towersCatalogueContainsTowerWithLaserProjectile() {
        TowersCatalogue towersCatalogue = new TowersCatalogue(field);

        Class<? extends DirectionalShootingStrategy> expectedShootingStrategy = DirectionalShootingStrategy.class;
        Class<? extends Projectile> expectedProjectile = LaserProjectile.class;
        Class<? extends ProjectileBehavior> expectedProjectileBehavior = LaserBehavior.class;
        int expectedPrice = 30;

        Map<Tower<? extends Projectile>, Integer> towers = towersCatalogue.getAvailableTowersWithPrices();
        Tower<? extends Projectile> actualTowerWithThisProjectile = null;
        for (Tower<? extends Projectile> tower : towers.keySet()) {
            if (tower.getTypicalProjectile().getClass() == expectedProjectile) {
                actualTowerWithThisProjectile = tower;
            }
        }

        assertNotNull(actualTowerWithThisProjectile);
        assertEquals(expectedShootingStrategy, actualTowerWithThisProjectile.getStrategy().getClass());
        assertEquals(expectedProjectile, actualTowerWithThisProjectile.getTypicalProjectile().getClass());
        assertEquals(expectedProjectileBehavior, actualTowerWithThisProjectile.getTypicalProjectile().getBehavior().getClass());
        assertTrue(towersCatalogue.getPrice(actualTowerWithThisProjectile).isPresent());
        assertEquals(expectedPrice, towersCatalogue.getPrice(actualTowerWithThisProjectile).get());
    }

    @Test
    void totalListsOfDamageUpgradePrices() {
        TowersCatalogue towersCatalogue = new TowersCatalogue(field);
        TowerUpgradableCharacteristic characteristic = TowerUpgradableCharacteristic.DAMAGE;

        List<Integer> expectedPrices = new ArrayList<>();
        expectedPrices.add(0);
        expectedPrices.add(20);
        expectedPrices.add(30);

        List<Integer> actualPrices = towersCatalogue.getCharacteristicUpgradePrices(characteristic);

        assertEquals(expectedPrices, actualPrices);
    }

    @Test
    void totalListsOfRangeUpgradePrices() {
        TowersCatalogue towersCatalogue = new TowersCatalogue(field);
        TowerUpgradableCharacteristic characteristic = TowerUpgradableCharacteristic.RANGE;

        List<Integer> expectedPrices = new ArrayList<>();
        expectedPrices.add(0);
        expectedPrices.add(20);
        expectedPrices.add(30);

        List<Integer> actualPrices = towersCatalogue.getCharacteristicUpgradePrices(characteristic);

        assertEquals(expectedPrices, actualPrices);
    }

    @Test
    void totalListsOfDelayUpgradePrices() {
        TowersCatalogue towersCatalogue = new TowersCatalogue(field);
        TowerUpgradableCharacteristic characteristic = TowerUpgradableCharacteristic.SHOOTING_DELAY;

        List<Integer> expectedPrices = new ArrayList<>();
        expectedPrices.add(0);
        expectedPrices.add(20);
        expectedPrices.add(30);

        List<Integer> actualPrices = towersCatalogue.getCharacteristicUpgradePrices(characteristic);

        assertEquals(expectedPrices, actualPrices);
    }

    @Test
    void getPriceOfUpgradeOfExistingLevelCharacteristic() {
        TowersCatalogue towersCatalogue = new TowersCatalogue(field);
        TowerUpgradableCharacteristic characteristic = TowerUpgradableCharacteristic.DAMAGE;
        int level = 3;

        int expectedPrice = 30;

        Optional<Integer> actualPrice = towersCatalogue.getUpgradePrice(characteristic, level);

        assertTrue(actualPrice.isPresent());
        assertEquals(expectedPrice, actualPrice.get());
    }

    @Test
    void getPriceOfUpgradeOfNotExistingLevelCharacteristic() {
        TowersCatalogue towersCatalogue = new TowersCatalogue(field);
        TowerUpgradableCharacteristic characteristic = TowerUpgradableCharacteristic.DAMAGE;
        int level = 4;

        Optional<Integer> actualPrice = towersCatalogue.getUpgradePrice(characteristic, level);

        assertTrue(actualPrice.isEmpty());
    }
}