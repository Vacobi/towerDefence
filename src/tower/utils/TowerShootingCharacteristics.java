package tower.utils;

import projectile.Projectile;
import tower.ShootingStrategy;
import utils.Direction;

import java.util.List;

public class TowerShootingCharacteristics {
    private final List<Direction> shotDirections;
    private final ShootingStrategy shootingStrategy;

    public TowerShootingCharacteristics(List<Direction> shotDirections,
                                        ShootingStrategy shootingStrategy) {
        this.shotDirections = shotDirections;
        this.shootingStrategy = shootingStrategy;
    }

    public List<Direction> shotDirections() {
        return shotDirections;
    }

    public ShootingStrategy shootingStrategy() {
        return shootingStrategy;
    }
}
