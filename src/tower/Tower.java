package tower;

import core.Cell;
import core.Field;
import events.TowerListener;
import exception.TowerAlreadySetOnCell;
import projectile.Projectile;
import tower.utils.TowerGeographicalCharacteristics;
import tower.utils.TowerShootingCharacteristics;
import tower.utils.TowerUpgradablePart;

import java.util.*;

public class Tower {

    private Cell cell;
    private final Field field;

    private final ShootingStrategy shootingStrategy;

    private final Map<TowerUpgradableCharacteristic, Integer> characteristicLevels;
    private TowerCharacteristicsValues characteristicValues;
    private final int levelsUpgradeCount;

    public Tower(TowerGeographicalCharacteristics towerGeographicalCharacteristics,
                 TowerShootingCharacteristics towerShootingCharacteristics,
                 TowerUpgradablePart towerUpgradablePart) {
        this.cell = towerGeographicalCharacteristics.cell();
        this.field = towerGeographicalCharacteristics.field();

        this.shootingStrategy = towerShootingCharacteristics.shootingStrategy();

        levelsUpgradeCount = towerUpgradablePart.levelsCount();
        initializeCharacteristics(towerUpgradablePart.initialCharacteristicValues());

        characteristicLevels = new HashMap<>();
    }

    private void initializeCharacteristics(TowerCharacteristicsValues initialCharacteristicValues) {
        characteristicValues = new TowerCharacteristicsValues();

        for (TowerUpgradableCharacteristic characteristic : TowerUpgradableCharacteristic.values()) {
            characteristicLevels.put(characteristic, 1);
        }

        characteristicValues = initialCharacteristicValues;
    }

    public void upgrade(TowerUpgradableCharacteristic characteristic) {
        if (!characteristicLevels.containsKey(characteristic)) {
            throw new IllegalArgumentException("Unknown characteristic " + characteristic + " for this tower");
        }
        if (characteristicLevels.get(characteristic) >= levelsUpgradeCount) {
            throw new IllegalArgumentException("Tower characteristic " + characteristic + "  is already max level");
        }

        upgradeCharacteristic(characteristic);
    }

    private void upgradeCharacteristic(TowerUpgradableCharacteristic characteristic) {
        characteristicLevels.compute(
                TowerUpgradableCharacteristic.DAMAGE,
                (k, v) -> v + 1
        );

        // Here can be complex characteristic value calculating
        switch (characteristic) {
            case DAMAGE -> upgradeDamage();
            case RANGE -> upgradeRange();
            case SHOOTING_DELAY -> upgradeShootingDelay();
        }
    }

    private void upgradeDamage() {
        characteristicValues.setDamage(characteristicValues.getDamage() * 1.5);
    }

    private void upgradeRange() {
        characteristicValues.setRange(characteristicValues.getRange() * 1.5);
    }

    private void upgradeShootingDelay() {
        long reduced = characteristicValues.shootingDelay() / 2;

        characteristicValues.setShootingDelay(reduced);
    }

    public boolean canUpgrade(TowerUpgradableCharacteristic characteristic) {
        if (!characteristicLevels.containsKey(characteristic)) {
            return false;
        }

        return characteristicLevels.get(characteristic) < levelsUpgradeCount;
    }

    public Set<Projectile> shoot(long currentTick) {
        return shootingStrategy.shoot(currentTick);
    }

    //--/////////////////////////////////////////////////////////////////////////

    public void setCell(Cell cell) {
        if (this.cell != null) {
            throw new TowerAlreadySetOnCell(this);
        }

        this.cell = cell;
    }

    public Field getField() {
        return this.field;
    }

    public int getLevelsUpgradeCount() {
        return levelsUpgradeCount;
    }

    public TowerCharacteristicsValues getCharacteristicValues() {
        return characteristicValues;
    }

    public int levelOfCharacteristic(TowerUpgradableCharacteristic characteristic) {
        return characteristicLevels.get(characteristic);
    }

    //--/////////////////////////////////////////////////////////////////////////
}
