package tower.utils;

import projectile.Projectile;
import tower.ShootingStrategy;
import utils.Direction;

import java.util.List;

public class TowerShootingCharacteristics {
    private final List<Direction> shotDirections;
    private final Class<? extends Projectile> projectileType;
    private final ShootingStrategy shootingStrategy;

    public TowerShootingCharacteristics(List<Direction> shotDirections,
                                        Class<? extends Projectile> projectileType,
                                        ShootingStrategy shootingStrategy) {
        this.shotDirections = shotDirections;
        this.projectileType = projectileType;
        this.shootingStrategy = shootingStrategy;
    }

    public List<Direction> shotDirections() {
        return shotDirections;
    }

    public Class<? extends Projectile> projectileType() {
        return projectileType;
    }

    public ShootingStrategy shootingStrategy() {
        return shootingStrategy;
    }
}
