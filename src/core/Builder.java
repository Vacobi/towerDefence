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

        if (!canBuildTower(towerFromCatalogue, cell, gold)) {
            return buildingResponse;
        }

        Optional<Integer> optionalBuildPrice = towersCatalogue.getPrice(towerFromCatalogue);
        int buildPrice = optionalBuildPrice.get();

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

    public boolean canBuildTower(Tower<? extends Projectile> tower, Cell cell, int gold) {
        if (!enoughGoldToBuild(tower, gold)) {
            return false;
        }

        return cell.canPlaceTower();
    }

    public boolean enoughGoldToBuild(Tower<? extends Projectile> tower, int gold) {
        Optional<Integer> optionalBuildPrice = towersCatalogue.getPrice(tower);
        if (optionalBuildPrice.isEmpty()) {
            return false;
        }

        int buildPrice = optionalBuildPrice.get();
        boolean enough = gold >= buildPrice;
        return enough;
    }
}
