package tower;

import collision.HitboxParameters;
import core.AbstractCell;
import core.Cell;
import core.Field;
import projectile.ExplosiveProjectile;
import projectile.LaserProjectile;
import projectile.PlainProjectile;
import projectile.Projectile;
import projectile.behavior.ExplosiveBehavior;
import projectile.behavior.HitOneTargetBehavior;
import projectile.behavior.LaserBehavior;
import projectile.behavior.ProjectileBehavior;
import projectile.strategy.LinearMovingProjectileStrategy;
import projectile.strategy.MovingProjectileStrategy;
import projectile.strategy.RandomMovingProjectileStrategy;
import utils.Direction;
import utils.Position;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TowersCatalogue {

    private static Map<Tower<? extends Projectile>, Integer> availableTowersWithPrices;
    private static Map<Tower<? extends Projectile>, String> namesOfTowers;

    private static Map<TowerUpgradableCharacteristic, String> upgradeNames;
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

        namesOfTowers = new HashMap<>();
        availableTowersWithPrices = new LinkedHashMap<>();
        initializeTowersWithPrices(field);

        upgradeNames = new HashMap<>();
        initializeUpgradeNames();
        upgradePrices = new HashMap<>();
        initializeUpgradePrices();
    }

    private void initializeShootingStrategies() {
        DirectionalShootingStrategy dss = new DirectionalShootingStrategy();

        towerShootingStrategies.put(dss.getClass(), dss);
    }

    private void initializeMovingStrategies() {
        int defaultSpeed = 10;

        LinearMovingProjectileStrategy lmps = new LinearMovingProjectileStrategy(defaultSpeed);
        RandomMovingProjectileStrategy rmps = new RandomMovingProjectileStrategy(defaultSpeed);

        movingProjectileStrategies.put(lmps.getClass(), lmps);
        movingProjectileStrategies.put(rmps.getClass(), rmps);
    }

    private void initializeProjectileBehaviors() {
        HitOneTargetBehavior hotb = new HitOneTargetBehavior();
        LaserBehavior lb = new LaserBehavior();
        ExplosiveBehavior eb = new ExplosiveBehavior();

        projectileBehaviors.put(hotb.getClass(), hotb);
        projectileBehaviors.put(lb.getClass(), lb);
        projectileBehaviors.put(eb.getClass(), eb);
    }

    private void initializeTowersWithPrices(Field field) {
        addTowerWithPlainProjectile(field);
        addTowerWithLaser(field);
        addTowerWithExplosiveProjectile(field);
        addTowerWithRandomProjectile(field);
        addTowerWithRandomExplosiveProjectile(field);
    }

    private void initializeUpgradePrices() {
        List<Integer> damageUpgradesPrices = List.of(0, 20, 30);
        List<Integer> delayUpgradesPrices = List.of(0, 20, 30);
        List<Integer> rangeUpgradesPrices = List.of(0, 20, 30);

        upgradePrices.put(TowerUpgradableCharacteristic.DAMAGE, damageUpgradesPrices);
        upgradePrices.put(TowerUpgradableCharacteristic.RANGE, rangeUpgradesPrices);
        upgradePrices.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, delayUpgradesPrices);
    }

    private static void initializeUpgradeNames() {
        upgradeNames.put(TowerUpgradableCharacteristic.SHOOTING_DELAY, "Задержка выстрела");
        upgradeNames.put(TowerUpgradableCharacteristic.RANGE, "Расстояние выстрела");
        upgradeNames.put(TowerUpgradableCharacteristic.DAMAGE, "Урон");
    }

    //------------------------------------------------------------------------------------------------------------------

    private void addTowerWithPlainProjectile(Field field) {
        int maxDistance =  (int) (AbstractCell.getSize() * 2.5);
        int damage = 50;
        Position mockPosition = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Direction mockDirection = Direction.NORTH;
        int width = (int) (AbstractCell.getSize() * 0.4);
        int height = (int) (AbstractCell.getSize() * 0.4);
        HitboxParameters hitboxParameters = new HitboxParameters(width, height, 0);
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
        namesOfTowers.put(tower, "Башня с обычным снарядом");
    }

    private void addTowerWithLaser(Field field) {
        int length = (int) (AbstractCell.getSize() * 2.9);
        int damage = 20;
        Position mockPosition = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Direction mockDirection = Direction.NORTH;
        int height = (int) (AbstractCell.getSize() * 0.2);
        HitboxParameters hitboxParameters = new HitboxParameters(length, height, 0);
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
        namesOfTowers.put(tower, "Башня с Лазером");
    }

    private void addTowerWithExplosiveProjectile(Field field) {
        int maxDistance =  (int) (AbstractCell.getSize() * 2.5);
        int damage = 100;
        Position mockPosition = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Direction mockDirection = Direction.NORTH;
        int width = (int) (AbstractCell.getSize() * 0.3);
        int height = (int) (AbstractCell.getSize() * 0.3);
        HitboxParameters hitboxParameters = new HitboxParameters(width, height, 0);
        long shootingDelay = TimeUnit.SECONDS.toMillis(3);
        int radius = (int) (AbstractCell.getSize() * 1.5);

        ExplosiveProjectile plainProjectile = new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                mockPosition,
                projectileBehaviors.get(ExplosiveBehavior.class).clone(),
                field,
                mockDirection,
                movingProjectileStrategies.get(LinearMovingProjectileStrategy.class).clone(),
                radius
        );

        Cell cell = new Cell(mockPosition);
        Tower<ExplosiveProjectile> tower = new Tower<ExplosiveProjectile>(
                cell,
                field,
                towerShootingStrategies.get(DirectionalShootingStrategy.class).clone(),
                shotDirections,
                levelsCount,
                new TowerCharacteristicsValues(damage, maxDistance, shootingDelay),
                plainProjectile
        );

        int price = 50;
        availableTowersWithPrices.put(tower, price);
        namesOfTowers.put(tower, "Башня с Бомбами");
    }

    private void addTowerWithRandomProjectile(Field field) {
        int maxDistance =  (int) (AbstractCell.getSize() * 2.5);
        int damage = 100;
        Position mockPosition = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Direction mockDirection = Direction.NORTH;
        int width = (int) (AbstractCell.getSize() * 0.4);
        int height = (int) (AbstractCell.getSize() * 0.4);
        HitboxParameters hitboxParameters = new HitboxParameters(width, height, 0);
        long shootingDelay = TimeUnit.SECONDS.toMillis(3);

        PlainProjectile plainProjectile = new PlainProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                mockPosition,
                projectileBehaviors.get(HitOneTargetBehavior.class).clone(),
                field,
                mockDirection,
                movingProjectileStrategies.get(RandomMovingProjectileStrategy.class).clone()
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

        int price = 10;
        availableTowersWithPrices.put(tower, price);
        namesOfTowers.put(tower, "Башня с рандомным снарядом. Сам не знаю, как это работает");
    }

    private void addTowerWithRandomExplosiveProjectile(Field field) {
        int maxDistance =  (int) (AbstractCell.getSize() * 2.5);
        int damage = 100;
        Position mockPosition = new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Direction mockDirection = Direction.NORTH;
        int width = (int) (AbstractCell.getSize() * 0.3);
        int height = (int) (AbstractCell.getSize() * 0.3);
        HitboxParameters hitboxParameters = new HitboxParameters(width, height, 0);
        long shootingDelay = TimeUnit.SECONDS.toMillis(3);
        int radius = (int) (AbstractCell.getSize() * 1.5);

        ExplosiveProjectile plainProjectile = new ExplosiveProjectile(
                hitboxParameters,
                damage,
                maxDistance,
                mockPosition,
                projectileBehaviors.get(ExplosiveBehavior.class).clone(),
                field,
                mockDirection,
                movingProjectileStrategies.get(RandomMovingProjectileStrategy.class).clone(),
                radius
        );

        Cell cell = new Cell(mockPosition);
        Tower<ExplosiveProjectile> tower = new Tower<ExplosiveProjectile>(
                cell,
                field,
                towerShootingStrategies.get(DirectionalShootingStrategy.class).clone(),
                shotDirections,
                levelsCount,
                new TowerCharacteristicsValues(damage, maxDistance, shootingDelay),
                plainProjectile
        );

        int price = 40;
        availableTowersWithPrices.put(tower, price);
        namesOfTowers.put(tower, "Башня с взрывающимся рандомным снарядом");
    }

    //------------------------------------------------------------------------------------------------------------------

    public Optional<Integer> getUpgradePrice(TowerUpgradableCharacteristic characteristic, int upgradeLevel) {
        if (upgradePrices.get(characteristic).size() < upgradeLevel) {
            return Optional.empty();
        }

        int indexOfLevel = upgradeLevel - 1;
        Integer upgradePrice = upgradePrices.get(characteristic).get(indexOfLevel);

        return upgradePrice == null ? Optional.empty() : Optional.of(upgradePrice);
    }

    public Map<TowerUpgradableCharacteristic, List<Integer>> getUpgradePrices() {
        return new HashMap<>(upgradePrices);
    }

    public List<Integer> getCharacteristicUpgradePrices(TowerUpgradableCharacteristic characteristic) {
        return new ArrayList<>(upgradePrices.get(characteristic));
    }

    public Map<Tower<? extends Projectile>, Integer> getAvailableTowersWithPrices() {
        return new LinkedHashMap<>(availableTowersWithPrices);
    }

    public Optional<Integer> getPrice(Tower<? extends Projectile> tower) {
        Integer buildPrice = availableTowersWithPrices.get(tower);

        return buildPrice == null ? Optional.empty() : Optional.of(buildPrice);
    }

    public String getNameOfTower(Tower<? extends Projectile> tower) {
        return namesOfTowers.get(tower);
    }

    public static String getNameOfUpgrade(TowerUpgradableCharacteristic characteristic) {
        return upgradeNames.get(characteristic);
    }
}
