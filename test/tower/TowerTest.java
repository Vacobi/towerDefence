package tower;

import collision.HitboxParameters;
import core.AbstractCell;
import core.Cell;
import core.Field;
import org.junit.jupiter.api.Test;
import projectile.PlainProjectile;
import projectile.behavior.HitOneTargetBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static tower.TowerTestAsserts.assertTowersAfterUpgradeEquals;
import static tower.TowerTestAsserts.assertTowersEquals;

class TowerTest {
    private final Position cellPosition = new Position(100, 100);
    private final Position projectilePosition = AbstractCell.toGlobalPosition(cellPosition);
    private final Field field = new Field();
    private final int levelsUpgradeCount = 3;

    private final Direction mockDirection = Direction.NORTH;
    private final HitboxParameters hitboxParameters = new HitboxParameters(10, 10, 0);
    private final int speed = 10;

    @Test
    void characteristicsOfTowerAreDifferentFromProjectile() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        int projectileDamage = 20;
        int projectileRange = 30;
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                projectileDamage,
                projectileRange,
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        Cell cell = new Cell(cellPosition);

        DirectionalShootingStrategy expectedShootingStrategy = new DirectionalShootingStrategy();
        Cell expectedCell = new Cell(cellPosition);
        int damageInCharacteristics = 51;
        int rangeInCharacteristics = 812;
        long delayInCharacteristics = 192;
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                damageInCharacteristics,
                rangeInCharacteristics,
                expectedCell.getGlobalPosition(),
                behavior.clone(),
                field,
                mockDirection,
                movingStrategy.clone()
        );
        TowerCharacteristicsValues expectedCharacteristics = new TowerCharacteristicsValues(
                damageInCharacteristics,
                rangeInCharacteristics,
                delayInCharacteristics
        );
        Tower expectedTower = new Tower(
                expectedCell,
                field,
                expectedShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                expectedCharacteristics,
                expectedProjectile
        );

        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        Tower actualTower = new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                expectedCharacteristics,
                typicalProjectile
        );

        assertTowersEquals(expectedTower, actualTower);
    }

    @Test
    void upgradeDamage() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        Cell cell = new Cell(cellPosition);

        DirectionalShootingStrategy expectedShootingStrategy = new DirectionalShootingStrategy();
        Cell expectedCell = new Cell(cellPosition);
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                expectedCell.getGlobalPosition(),
                behavior.clone(),
                field,
                mockDirection,
                movingStrategy.clone()
        );
        TowerCharacteristicsValues expectedCharacteristicsValues = new TowerCharacteristicsValues(
                (int) (characteristics.getDamage() * 1.5),
                characteristics.getRange(),
                characteristics.shootingDelay()
        );
        Tower expectedTower = new Tower(
                expectedCell,
                field,
                expectedShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                expectedProjectile
        );
        Map<TowerUpgradableCharacteristic, Integer> expectedCharacteristicLevels = new HashMap<>();
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.DAMAGE, 2);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.RANGE, 1);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, 1);

        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        Tower actualTower = new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        actualTower.upgrade(TowerUpgradableCharacteristic.DAMAGE);

        assertTowersAfterUpgradeEquals(expectedTower, actualTower, expectedCharacteristicsValues, expectedCharacteristicLevels);
    }

    @Test
    void upgradeRange() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        Cell cell = new Cell(cellPosition);

        DirectionalShootingStrategy expectedShootingStrategy = new DirectionalShootingStrategy();
        Cell expectedCell = new Cell(cellPosition);
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                expectedCell.getGlobalPosition(),
                behavior.clone(),
                field,
                mockDirection,
                movingStrategy.clone()
        );
        TowerCharacteristicsValues expectedCharacteristicsValues = new TowerCharacteristicsValues(
                characteristics.getDamage(),
                (int) (characteristics.getRange() * 1.5),
                characteristics.shootingDelay()
        );
        Tower expectedTower = new Tower(
                expectedCell,
                field,
                expectedShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                expectedProjectile
        );
        Map<TowerUpgradableCharacteristic, Integer> expectedCharacteristicLevels = new HashMap<>();
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.DAMAGE, 1);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.RANGE, 2);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, 1);

        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        Tower actualTower = new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        actualTower.upgrade(TowerUpgradableCharacteristic.RANGE);

        assertTowersAfterUpgradeEquals(expectedTower, actualTower, expectedCharacteristicsValues, expectedCharacteristicLevels);
    }

    @Test
    void upgradeDelay() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        Cell cell = new Cell(cellPosition);

        DirectionalShootingStrategy expectedShootingStrategy = new DirectionalShootingStrategy();
        Cell expectedCell = new Cell(cellPosition);
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                expectedCell.getGlobalPosition(),
                behavior.clone(),
                field,
                mockDirection,
                movingStrategy.clone()
        );
        TowerCharacteristicsValues expectedCharacteristicsValues = new TowerCharacteristicsValues(
                characteristics.getDamage(),
                characteristics.getRange(),
                (long) (characteristics.shootingDelay() / 1.5)
        );
        Tower expectedTower = new Tower(
                expectedCell,
                field,
                expectedShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                expectedProjectile
        );
        Map<TowerUpgradableCharacteristic, Integer> expectedCharacteristicLevels = new HashMap<>();
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.DAMAGE, 1);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.RANGE, 1);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, 2);

        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        Tower actualTower = new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        actualTower.upgrade(TowerUpgradableCharacteristic.SHOOTING_DELAY);

        assertTowersAfterUpgradeEquals(expectedTower, actualTower, expectedCharacteristicsValues, expectedCharacteristicLevels);
    }

    @Test
    void upgradeToMaxLevel() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        Cell cell = new Cell(cellPosition);

        DirectionalShootingStrategy expectedShootingStrategy = new DirectionalShootingStrategy();
        Cell expectedCell = new Cell(cellPosition);
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                expectedCell.getGlobalPosition(),
                behavior.clone(),
                field,
                mockDirection,
                movingStrategy.clone()
        );
        TowerCharacteristicsValues expectedCharacteristicsValues = new TowerCharacteristicsValues(
                (int) (characteristics.getDamage() * 1.5 * 1.5),
                characteristics.getRange(),
                characteristics.shootingDelay()
        );
        Tower expectedTower = new Tower(
                expectedCell,
                field,
                expectedShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                expectedProjectile
        );
        Map<TowerUpgradableCharacteristic, Integer> expectedCharacteristicLevels = new HashMap<>();
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.DAMAGE, 3);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.RANGE, 1);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, 1);

        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        Tower actualTower = new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        actualTower.upgrade(TowerUpgradableCharacteristic.DAMAGE);
        actualTower.upgrade(TowerUpgradableCharacteristic.DAMAGE);

        assertTowersAfterUpgradeEquals(expectedTower, actualTower, expectedCharacteristicsValues, expectedCharacteristicLevels);
    }

    @Test
    void upgradeAllCharacteristicsToMaxLevel() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        Cell cell = new Cell(cellPosition);

        DirectionalShootingStrategy expectedShootingStrategy = new DirectionalShootingStrategy();
        Cell expectedCell = new Cell(cellPosition);
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                expectedCell.getGlobalPosition(),
                behavior.clone(),
                field,
                mockDirection,
                movingStrategy.clone()
        );
        TowerCharacteristicsValues expectedCharacteristicsValues = new TowerCharacteristicsValues(
                (int) (characteristics.getDamage() * 1.5 * 1.5),
                (int) (characteristics.getRange() * 1.5 * 1.5),
                (long) (characteristics.shootingDelay() / 1.5 / 1.5)
        );
        Tower expectedTower = new Tower(
                expectedCell,
                field,
                expectedShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                expectedProjectile
        );
        Map<TowerUpgradableCharacteristic, Integer> expectedCharacteristicLevels = new HashMap<>();
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.DAMAGE, 3);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.RANGE, 3);
        expectedCharacteristicLevels.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, 3);

        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        Tower actualTower = new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        actualTower.upgrade(TowerUpgradableCharacteristic.DAMAGE);
        actualTower.upgrade(TowerUpgradableCharacteristic.DAMAGE);
        actualTower.upgrade(TowerUpgradableCharacteristic.RANGE);
        actualTower.upgrade(TowerUpgradableCharacteristic.RANGE);
        actualTower.upgrade(TowerUpgradableCharacteristic.SHOOTING_DELAY);
        actualTower.upgrade(TowerUpgradableCharacteristic.SHOOTING_DELAY);

        assertTowersAfterUpgradeEquals(expectedTower, actualTower, expectedCharacteristicsValues, expectedCharacteristicLevels);
    }

    @Test
    void upgradeHigherThanMaxLevel() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );

        Cell cell = new Cell(cellPosition);

        DirectionalShootingStrategy shootingStrategy = new DirectionalShootingStrategy();
        Tower actualTower = new Tower(
                cell,
                field,
                shootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        actualTower.upgrade(TowerUpgradableCharacteristic.DAMAGE);
        actualTower.upgrade(TowerUpgradableCharacteristic.DAMAGE);
        assertThrows(IllegalArgumentException.class, () -> actualTower.upgrade(TowerUpgradableCharacteristic.DAMAGE));
    }

    @Test
    void cloneTower() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        DirectionalShootingStrategy typicalShootingStrategy = new DirectionalShootingStrategy();
        Cell typicalCell = new Cell(cellPosition);
        Tower typicalTower = new Tower(
                typicalCell,
                field,
                typicalShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        DirectionalShootingStrategy expectedShootingStrategy = new DirectionalShootingStrategy();
        Cell expectedCell = new Cell(cellPosition);
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                expectedCell.getGlobalPosition(),
                behavior.clone(),
                field,
                mockDirection,
                movingStrategy.clone()
        );
        Tower expectedTower = new Tower(
                expectedCell,
                field,
                expectedShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                expectedProjectile
        );

        Tower actualTower = typicalTower.clone(expectedCell);

        assertTowersEquals(expectedTower, actualTower);
    }

    @Test
    void cloneTowerAfterUpgrade() {
        HitOneTargetBehavior behavior = new HitOneTargetBehavior();
        LinearMovingProjectileStrategy movingStrategy = new LinearMovingProjectileStrategy(speed);
        TowerCharacteristicsValues characteristics = new TowerCharacteristicsValues(20, 30, 40);
        PlainProjectile typicalProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                projectilePosition,
                behavior,
                field,
                mockDirection,
                movingStrategy
        );
        DirectionalShootingStrategy typicalShootingStrategy = new DirectionalShootingStrategy();
        Cell typicalCell = new Cell(cellPosition);
        Tower typicalTower = new Tower(
                typicalCell,
                field,
                typicalShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                characteristics,
                typicalProjectile
        );

        DirectionalShootingStrategy expectedShootingStrategy = new DirectionalShootingStrategy();
        Cell expectedCell = new Cell(cellPosition);
        PlainProjectile expectedProjectile = new PlainProjectile(
                hitboxParameters,
                characteristics.getDamage(),
                characteristics.getRange(),
                expectedCell.getGlobalPosition(),
                behavior.clone(),
                field,
                mockDirection,
                movingStrategy.clone()
        );
        TowerCharacteristicsValues expectedCharacteristicsValues = new TowerCharacteristicsValues(20, 30, 40);
        Tower expectedTower = new Tower(
                expectedCell,
                field,
                expectedShootingStrategy,
                List.of(mockDirection),
                levelsUpgradeCount,
                expectedCharacteristicsValues,
                expectedProjectile
        );

        typicalTower.upgrade(TowerUpgradableCharacteristic.DAMAGE);
        typicalTower.upgrade(TowerUpgradableCharacteristic.RANGE);
        typicalTower.upgrade(TowerUpgradableCharacteristic.SHOOTING_DELAY);
        Tower actualTower = typicalTower.clone(expectedCell);

        assertTowersEquals(expectedTower, actualTower);
    }
}