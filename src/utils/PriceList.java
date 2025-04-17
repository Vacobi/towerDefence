package utils;

import tower.Tower;
import tower.TowerUpgradableCharacteristic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PriceList {
    private final Map<Class<? extends Tower>, Map<TowerUpgradableCharacteristic, List<Integer>>> upgradePrices;
    private final Map<Class<? extends Tower>,Integer> buildPrices;

    public PriceList() {
        buildPrices = new HashMap<>();
        buildPrices.put(Tower.class, 20);


        List<Integer> damageUpgradesPrices = List.of(10, 20, 30);
        List<Integer> delayUpgradesPrices = List.of(10, 20, 30);
        List<Integer> rangeUpgradesPrices = List.of(10, 20, 30);

        Map<TowerUpgradableCharacteristic, List<Integer>> towerUpgradePrices = new HashMap<>();
        towerUpgradePrices.put(TowerUpgradableCharacteristic.DAMAGE, damageUpgradesPrices);
        towerUpgradePrices.put(TowerUpgradableCharacteristic.RANGE, rangeUpgradesPrices);
        towerUpgradePrices.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, delayUpgradesPrices);

        Map<Class<? extends Tower>, Map<TowerUpgradableCharacteristic, List<Integer>>> upgradePrices = new HashMap<>();
        upgradePrices.put(Tower.class, towerUpgradePrices);


        this.upgradePrices = upgradePrices;
    }

    public Optional<Integer> getUpgradePrice(Class<? extends Tower> tower, TowerUpgradableCharacteristic upgrade, int level) {
        Map<TowerUpgradableCharacteristic, List<Integer>> upgradePricesOfSpecifiedTower =
                this.upgradePrices.get(tower);
        if (upgradePricesOfSpecifiedTower == null) {
            return Optional.empty();
        }

        List<Integer> characteristicUpgradePrices = upgradePricesOfSpecifiedTower.getOrDefault(upgrade, List.of());
        if (characteristicUpgradePrices == null) {
            return Optional.empty();
        }

        if (level < 1 || level > characteristicUpgradePrices.size()) {
            return Optional.empty();
        }

        return Optional.of(characteristicUpgradePrices.get(level - 1));
    }

    public Optional<Integer> getBuildPrice(Class<? extends Tower> type) {
        Integer buildPrice = this.buildPrices.get(type);

        if (buildPrice == null) {
            return Optional.empty();
        }
        return Optional.of(buildPrice);
    }
}
