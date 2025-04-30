package projectile;

import utils.Direction;

public interface MovingProjectileStrategy {
    void move(long currentTick);

    MovingProjectileStrategy clone(Direction direction);
}
