package tower;

import projectile.Projectile;

import java.util.List;

public interface ShootingStrategy<T extends Projectile> {
    List<T> shoot(long currentTick);

    ShootingStrategy<T> clone();

    void setTower(Tower<T> tower);
}
