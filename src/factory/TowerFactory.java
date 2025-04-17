package factory;

import core.Cell;
import core.Field;
import projectile.LinearMovingProjectile;
import projectile.Projectile;
import tower.*;
import tower.utils.TowerGeographicalCharacteristics;
import tower.utils.TowerShootingCharacteristics;
import tower.utils.TowerUpgradablePart;
import utils.Direction;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TowerFactory {
    private final List<Direction> shotDirections;
    private final Class<? extends Projectile> projectileType;
    private final TowerCharacteristicsValues initialCharacteristicValues;
    private final int levelsCount;
    private final Class<? extends Tower> towerType = Tower.class;

    public TowerFactory() {
        shotDirections = Arrays.stream(Direction.values()).toList();
        projectileType = LinearMovingProjectile.class;
        initialCharacteristicValues = new TowerCharacteristicsValues(
                20.0,
                100.0,
                TimeUnit.SECONDS.toMillis(3)
        );
        levelsCount = 3;
    }

    public Tower createTower(Cell cell, Field field) {
        ShootingStrategy strategy = new PlainShootingStrategy(
                initialCharacteristicValues.shootingDelay(),
                cell.position(),
                shotDirections,
                field
        );
        TowerGeographicalCharacteristics tgc = new TowerGeographicalCharacteristics(cell, field);
        TowerShootingCharacteristics tsc = new TowerShootingCharacteristics(shotDirections, projectileType, strategy);
        TowerUpgradablePart tup = new TowerUpgradablePart(initialCharacteristicValues, levelsCount);

        return new Tower(
                tgc,
                tsc,
                tup
        );
    }
}
