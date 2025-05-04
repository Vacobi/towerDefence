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
    private final int MILLIS_TO_SECONDS_COEFF = 1_000;

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

    public void setProjectile(MovingProjectile projectile) {
        if (this.projectile != null) {
            throw new IllegalStateException("Projectile already set");
        }

        this.projectile = projectile;
    }

    @Override
    public void move(long currentTick) {
        int moveDistance = (int) ((currentTick - lastMoveTime) / MILLIS_TO_SECONDS_COEFF) * speed;

        move(currentTick, moveDistance);
    }

    @Override
    public MovingProjectileStrategy clone(int range) {
        return new LinearMovingProjectileStrategy(projectile, speed, range, direction);
    }

    @Override
    public MovingProjectileStrategy clone() {
        return new LinearMovingProjectileStrategy(projectile, speed, maxDistance, direction);
    }

    @Override
    public MovingProjectileStrategy clone(Direction direction) {
        return new LinearMovingProjectileStrategy(projectile, speed, maxDistance, direction);
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

    protected long getLastMoveTime() {
        return lastMoveTime;
    }

    protected int getTotalTraveledDistance() {
        return totalTraveledDistance;
    }
}
