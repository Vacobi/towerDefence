package core;

import projectile.Projectile;
import tower.TowersCatalogue;
import tower.Tower;
import tower.TowerUpgradableCharacteristic;
import utils.BuildingResponse;

import java.util.Optional;

public class Builder {
    private final TowersCatalogue towersCatalogue;

    public Builder(Field field) {

        towersCatalogue = new TowersCatalogue(field);
    }

    public BuildingResponse buildTower(Tower<? extends Projectile> towerFromCatalogue, Cell cell, int gold) {
        BuildingResponse buildingResponse = new BuildingResponse(gold, false);

        Optional<Integer> optionalBuildPrice = towersCatalogue.getPrice(towerFromCatalogue);
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

        Tower tower = towerFromCatalogue.clone(cell);
        cell.setTower(tower);
        return new BuildingResponse(gold - buildPrice, true);
    }

    public BuildingResponse upgradeTower(Tower<? extends Projectile> tower, TowerUpgradableCharacteristic towerUpgrade, int gold) {
        BuildingResponse buildingResponse = new BuildingResponse(gold, false);

        if (!tower.canUpgrade(towerUpgrade)) {
            return buildingResponse;
        }

        int nextLevel = tower.getLevelOfCharacteristic(towerUpgrade).get() + 1;
        Optional<Integer> optionalUpgradePrice = towersCatalogue.getUpgradePrice(towerUpgrade, nextLevel);
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

    public TowersCatalogue getTowersCatalogue() {
        return towersCatalogue;
    }
}
