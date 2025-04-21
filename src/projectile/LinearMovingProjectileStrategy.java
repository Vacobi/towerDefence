package projectile;

import utils.Direction;
import utils.Position;

public class LinearMovingProjectileStrategy implements MovingProjectileStrategy{
    private long lastMoveTime;
    private int totalTraveledDistance;

    private final int speed;
    private final int maxDistance;
    private final Direction direction;

    private MovingProjectile projectile;

    public LinearMovingProjectileStrategy(MovingProjectile projectile, int speed, int maxDistance, Direction direction) {
        this.projectile = projectile;

        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be not less than 0");
        }
        this.speed = speed;

        if (maxDistance <= 0) {
            throw new IllegalArgumentException("Max distance must be greater than 0");
        }
        this.maxDistance = maxDistance;

        if (direction == null) {
            throw new IllegalArgumentException("Direction must be not null");
        }
        this.direction = direction;

        this.lastMoveTime = System.currentTimeMillis();
        this.totalTraveledDistance = 0;
    }

    @Override
    public void move(long currentTick) {
        int moveDistance = (int)(currentTick - lastMoveTime) * speed / 10;

        move(currentTick, moveDistance);
    }

    protected void move(long moveTime, int moveDistance) {
        if (totalTraveledDistance >= maxDistance) {
            projectile.deactivate();
        }

        if (!projectile.active()) {
            return;
        }

        int distanceToMove = Math.min(moveDistance, maxDistance - totalTraveledDistance);

        Position positionAfterMove = projectile.position().move(direction, distanceToMove);
        projectile.setPosition(positionAfterMove);
        lastMoveTime = moveTime;
        totalTraveledDistance += distanceToMove;
    }
}
