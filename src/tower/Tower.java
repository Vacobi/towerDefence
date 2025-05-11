package tower;

import core.Cell;
import core.Field;
import projectile.Projectile;
import utils.Direction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Tower<T extends Projectile> {

    private final Cell cell;
    private final Field field;

    private final ShootingStrategy<T> shootingStrategy;
    private final List<Direction> shotDirections;
    private T typicalProjectile;

    private final Map<TowerUpgradableCharacteristic, Integer> characteristicLevels;
    private final TowerCharacteristicsValues actualCharacteristicValues;
    private final TowerCharacteristicsValues initialCharacteristicValues;

    private final int levelsUpgradeCount;

    public Tower(
            Cell cell,
            Field field,
            ShootingStrategy<T> shootingStrategy,
            List<Direction> shotDirections,
            int levelsUpgradeCount,
            TowerCharacteristicsValues characteristicValues,
            T typicalProjectile
    ) {
        this.cell = cell;
        this.field = field;

        this.shotDirections = shotDirections;

        if (typicalProjectile.getDamage() != characteristicValues.getDamage() ||
            typicalProjectile.getDistance() != characteristicValues.getRange()) {

            this.typicalProjectile = (T) typicalProjectile.clone(characteristicValues.getDamage(), characteristicValues.getRange());
        } else {
            this.typicalProjectile = typicalProjectile;
        }

        this.levelsUpgradeCount = levelsUpgradeCount;
        this.characteristicLevels = new HashMap<>();
        initializeCharacteristicLevels();

        this.actualCharacteristicValues = characteristicValues;
        this.initialCharacteristicValues = characteristicValues;

        this.shootingStrategy = shootingStrategy;
        shootingStrategy.setTower(this);
    }

    protected void initializeCharacteristicLevels() {
        for (TowerUpgradableCharacteristic characteristic : TowerUpgradableCharacteristic.values()) {
            characteristicLevels.put(characteristic, 1);
        }
    }

    public List<T> shoot(long currentTick) {
        return shootingStrategy.shoot(currentTick);
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
        actualCharacteristicValues.setDamage((int) (actualCharacteristicValues.getDamage() * 1.5));
        this.typicalProjectile = (T) typicalProjectile.clone(actualCharacteristicValues.getDamage(), actualCharacteristicValues.getRange());
    }

    private void upgradeRange() {
        actualCharacteristicValues.setRange((int) (actualCharacteristicValues.getRange() * 1.5));
        this.typicalProjectile = (T) typicalProjectile.clone(actualCharacteristicValues.getDamage(), actualCharacteristicValues.getRange());
    }

    private void upgradeShootingDelay() {
        long reduced = (long) (actualCharacteristicValues.shootingDelay() / 1.5);

        actualCharacteristicValues.setShootingDelay(reduced);
    }

    public boolean canUpgrade(TowerUpgradableCharacteristic characteristic) {
        if (!characteristicLevels.containsKey(characteristic)) {
            return false;
        }

        return characteristicLevels.get(characteristic) < levelsUpgradeCount;
    }

    public Tower<T> clone(Cell cell) {
        return new Tower<> (
                cell,
                this.field,
                this.shootingStrategy.clone(),
                this.shotDirections,
                this.levelsUpgradeCount,
                this.initialCharacteristicValues,
                (T) this.typicalProjectile.clone(initialCharacteristicValues.getDamage(), initialCharacteristicValues.getRange())
                );
    }

    public Optional<Integer> getLevelOfCharacteristic(TowerUpgradableCharacteristic characteristic) {
        return characteristicLevels.containsKey(characteristic) ? Optional.of(characteristicLevels.get(characteristic)) : Optional.empty();
    }

    //--/////////////////////////////////////////////////////////////////////////

    public ShootingStrategy<T> getStrategy() {
        return shootingStrategy;
    }

    public Cell getCell() {
        return cell;
    }

    public Field getField() {
        return field;
    }

    public Map<TowerUpgradableCharacteristic, Integer> getCharacteristicLevels() {
        return characteristicLevels;
    }

    public int getLevelsUpgradeCount() {
        return levelsUpgradeCount;
    }

    public TowerCharacteristicsValues characteristicValues() {
        return actualCharacteristicValues;
    }

    public List<Direction> getShotDirections() {
        return shotDirections;
    }

    public T getTypicalProjectile() {
        return typicalProjectile;
    }
}
