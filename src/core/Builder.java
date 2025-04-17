package core;

import factory.TowerFactory;
import tower.Tower;
import tower.TowerUpgradableCharacteristic;
import utils.BuildingResponse;
import utils.PriceList;

import java.util.Optional;

public class Builder {
    private final PriceList priceList;
    private final Field field;

    TowerFactory towerFactory;

    public Builder(Field field) {
        priceList = new PriceList();
        this.field = field;
        towerFactory = new TowerFactory();
    }

    public BuildingResponse buildTower(Class<? extends Tower> towerType, Cell cell, int gold) {
        BuildingResponse buildingResponse = new BuildingResponse(gold, false);

        Optional<Integer> optionalBuildPrice = priceList.getBuildPrice(towerType);
        if (optionalBuildPrice.isEmpty()) {
            return buildingResponse;
        }
        int buildPrice = optionalBuildPrice.get();

        if (buildPrice > gold) {
            return buildingResponse;
        }

        if (!cell.canPlaceTower()) {
            return buildingResponse;
        }


        Tower tower = towerFactory.createTower(cell, field);
        cell.setTower(tower);
        return new BuildingResponse(gold - buildPrice, true);
    }

    public BuildingResponse upgradeTower(Tower tower, TowerUpgradableCharacteristic towerUpgrade, int gold) {
        BuildingResponse buildingResponse = new BuildingResponse(gold, false);

        if (!tower.canUpgrade(towerUpgrade)) {
            return buildingResponse;
        }

        int nextLevel = tower.levelOfCharacteristic(towerUpgrade) + 1;
        Optional<Integer> optionalUpgradePrice = priceList.getUpgradePrice(tower.getClass(), towerUpgrade, nextLevel);
        if (optionalUpgradePrice.isEmpty()) {
            return buildingResponse;
        }
        int upgradePrice = optionalUpgradePrice.get();

        if (upgradePrice > gold) {
            return buildingResponse;
        }

        tower.upgrade(towerUpgrade);
        return new BuildingResponse(gold - upgradePrice, true);
    }
}
