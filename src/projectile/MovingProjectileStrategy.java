package projectile;

import utils.Direction;

public interface MovingProjectileStrategy {
    void move(long currentTick);

    MovingProjectileStrategy clone(Direction direction);

    MovingProjectileStrategy clone();

    MovingProjectileStrategy clone(int range);
}
