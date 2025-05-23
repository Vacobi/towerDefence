package tower;

import collision.HitboxParameters;
import core.Cell;
import core.Field;
import projectile.LaserProjectile;
import projectile.PlainProjectile;
import projectile.Projectile;
import projectile.behavior.HitOneTargetBehavior;
import projectile.behavior.LaserBehavior;
import projectile.behavior.ProjectileBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import projectile.strategy.MovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TowersCatalogue {

    private static Map<Tower<? extends Projectile>, Integer> availableTowersWithPrices;

    private static Map<TowerUpgradableCharacteristic, List<Integer>> upgradePrices;

    private static Map<Class<? extends ShootingStrategy>, ShootingStrategy> towerShootingStrategies;
    private final List<Direction> shotDirections;
    private final int levelsCount;

    private static Map<Class<? extends MovingProjectileStrategy>, MovingProjectileStrategy> movingProjectileStrategies;
    private static Map<Class<? extends ProjectileBehavior>, ProjectileBehavior> projectileBehaviors;

    public TowersCatalogue(Field field) {
        shotDirections = Arrays.stream(Direction.values()).toList();
        levelsCount = 3;

        towerShootingStrategies = new HashMap<>();
        initializeShootingStrategies();

        movingProjectileStrategies = new HashMap<>();
        initializeMovingStrategies();

        projectileBehaviors = new HashMap<>();
        initializeProjectileBehaviors();

        availableTowersWithPrices = new HashMap<>();
        initializeTowersWithPrices(field);

        upgradePrices = new HashMap<>();
        initializeUpgradePrices();
    }

    private void initializeShootingStrategies() {
        DirectionalShootingStrategy dss = new DirectionalShootingStrategy();

        towerShootingStrategies.put(dss.getClass(), dss);
    }

    private void initializeMovingStrategies() {
        int defaultSpeed = 20;

        LinearMovingProjectileStrategy lmps = new LinearMovingProjectileStrategy(defaultSpeed);

        movingProjectileStrategies.put(lmps.getClass(), lmps);
    }

    private void initializeProjectileBehaviors() {
        HitOneTargetBehavior hotb = new HitOneTargetBehavior();
        LaserBehavior lb = new LaserBehavior();

        projectileBehaviors.put(hotb.getClass(), hotb);
        projectileBehaviors.put(lb.getClass(), lb);
    }

    private void initializeTowersWithPrices(Field field) {
        addTowerWithPlainProjectile(field);
        addTowerWithLaser(field);
    }

    private void addTowerWithPlainProjectile(Field field) {
        int maxDistance = 100;
        int damage = 20;
        Position mockPosition = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Direction mockDirection = Direction.NORTH;
        HitboxParameters hitboxParameters = new HitboxParameters(10, 10, 0);
        long shootingDelay = TimeUnit.SECONDS.toMillis(3);

        PlainProjectile plainProjectile = new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                mockPosition,
                projectileBehaviors.get(HitOneTargetBehavior.class).clone(),
                field,
                mockDirection,
                movingProjectileStrategies.get(LinearMovingProjectileStrategy.class).clone()
        );

        Cell cell = new Cell(mockPosition);
        Tower<PlainProjectile> tower = new Tower<PlainProjectile>(
                cell,
                field,
                towerShootingStrategies.get(DirectionalShootingStrategy.class).clone(),
                shotDirections,
                levelsCount,
                new TowerCharacteristicsValues(damage, maxDistance, shootingDelay),
                plainProjectile
        );

        int price = 20;
        availableTowersWithPrices.put(tower, price);
    }

    private void addTowerWithLaser(Field field) {
        int length = 40;
        int damage = 10;
        Position mockPosition = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Direction mockDirection = Direction.NORTH;
        HitboxParameters hitboxParameters = new HitboxParameters(length, 10, 0);
        long activeTime = TimeUnit.SECONDS.toMillis(3);
        long damageCooldown = TimeUnit.MILLISECONDS.toMillis(500);
        long shootingDelay = TimeUnit.SECONDS.toMillis(5);

        LaserProjectile laserProjectile = new LaserProjectile(
                hitboxParameters,
                damage,
                length,
                mockPosition,
                projectileBehaviors.get(LaserBehavior.class),
                field,
                mockDirection,
                activeTime,
                damageCooldown
        );

        Cell cell = new Cell(mockPosition);
        Tower<LaserProjectile> tower = new Tower<LaserProjectile>(
                cell,
                field,
                towerShootingStrategies.get(DirectionalShootingStrategy.class).clone(),
                shotDirections,
                levelsCount,
                new TowerCharacteristicsValues(damage, length, shootingDelay),
                laserProjectile
        );

        int price = 30;
        availableTowersWithPrices.put(tower, price);
    }

    public Map<Tower<? extends Projectile>, Integer> getAvailableTowersWithPrices() {
        return availableTowersWithPrices;
    }

    public Optional<Integer> getPrice(Tower<? extends Projectile> tower) {
        Integer buildPrice = availableTowersWithPrices.get(tower);

        return buildPrice == null ? Optional.empty() : Optional.of(buildPrice);
    }

    private void initializeUpgradePrices() {
        List<Integer> damageUpgradesPrices = List.of(0, 20, 30);
        List<Integer> delayUpgradesPrices = List.of(0, 20, 30);
        List<Integer> rangeUpgradesPrices = List.of(0, 20, 30);

        upgradePrices.put(TowerUpgradableCharacteristic.DAMAGE, damageUpgradesPrices);
        upgradePrices.put(TowerUpgradableCharacteristic.RANGE, rangeUpgradesPrices);
        upgradePrices.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, delayUpgradesPrices);
    }

    public Optional<Integer> getUpgradePrice(TowerUpgradableCharacteristic characteristic, int upgradeLevel) {
        if (upgradePrices.get(characteristic).size() < upgradeLevel) {
            return Optional.empty();
        }

        int indexOfLevel = upgradeLevel - 1;
        Integer upgradePrice = upgradePrices.get(characteristic).get(indexOfLevel);

        return upgradePrice == null ? Optional.empty() : Optional.of(upgradePrice);
    }

    public Map<TowerUpgradableCharacteristic, List<Integer>> getUpgradePrices() {
        return upgradePrices;
    }

    public List<Integer> getCharacteristicUpgradePrices(TowerUpgradableCharacteristic characteristic) {
        return upgradePrices.get(characteristic);
    }
}
