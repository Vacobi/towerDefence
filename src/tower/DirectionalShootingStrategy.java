package tower;

import projectile.DirectionalProjectile;
import utils.Direction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DirectionalShootingStrategy implements ShootingStrategy<DirectionalProjectile>{
    private Long lastShootTime;

    private Tower<DirectionalProjectile> tower;

    public DirectionalShootingStrategy(Tower<DirectionalProjectile> tower) {
        this.tower = tower;
        lastShootTime = System.currentTimeMillis();
    }

    public DirectionalShootingStrategy() {
        this(null);
    }

    @Override
    public void setTower(Tower<DirectionalProjectile> tower) {
        if (this.tower != null) {
            throw new IllegalArgumentException("Tower already assigned to this shooting strategy");
        }

        this.tower = tower;
        lastShootTime -= tower.getCharacteristicValues().shootingDelay();
    }

    @Override
    public List<DirectionalProjectile> shoot(long currentTick) {
        if (currentTick - lastShootTime < tower.getCharacteristicValues().shootingDelay()) {
            return Collections.emptyList();
        }

        if (tower == null) {
            throw new IllegalStateException("Tower has not been set");
        }

        List<DirectionalProjectile> projectiles = new LinkedList<>();
        for (Direction direction : tower.getShotDirections()) {
            DirectionalProjectile projectile = tower.getTypicalProjectile().clone(
                    tower.getCell().getGlobalPosition(),
                    direction
            );
            projectiles.add(projectile);
        }

        lastShootTime = currentTick;
        return projectiles;
    }

    @Override
    public ShootingStrategy<DirectionalProjectile> clone() {
        return new DirectionalShootingStrategy();
    }
}
