package projectile.strategy;

import projectile.MovingProjectile;
import utils.Position;

public class LinearMovingProjectileStrategy implements MovingProjectileStrategy {
    private long lastMoveTime;
    private int totalTraveledDistance;

    private final int speed;

    private MovingProjectile projectile;
    private final int MILLIS_TO_SECONDS_COEFF = 1_000;

    public LinearMovingProjectileStrategy(MovingProjectile projectile, int speed) {
        this.projectile = projectile;

        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be not less than 0");
        }
        this.speed = speed;

        this.lastMoveTime = System.currentTimeMillis();
        this.totalTraveledDistance = 0;
    }

    public LinearMovingProjectileStrategy(int speed) {
        this(null, speed);
    }

    @Override
    public void move(long currentTick) {
        if (projectile == null) {
            throw new IllegalStateException("No projectile found");
        }

        int moveDistance = (int) ((currentTick - lastMoveTime) / MILLIS_TO_SECONDS_COEFF) * speed;

        move(currentTick, moveDistance);
    }

    protected void move(long moveTime, int moveDistance) {
        if (totalTraveledDistance >= projectile.getDistance()) {
            projectile.deactivate();
        }

        if (!projectile.active()) {
            return;
        }

        int distanceToMove = Math.min(moveDistance, projectile.getDistance() - totalTraveledDistance);

        Position positionAfterMove = projectile.position().move(projectile.getDirection(), distanceToMove);
        projectile.setPosition(positionAfterMove);
        lastMoveTime = moveTime;
        totalTraveledDistance += distanceToMove;
    }

    @Override
    public void setProjectile(MovingProjectile projectile) {
        if (this.projectile != null) {
            throw new IllegalStateException("Projectile already set");
        }

        this.projectile = projectile;
    }

    @Override
    public MovingProjectile getProjectile() {
        return projectile;
    }

    @Override
    public MovingProjectileStrategy clone() {
        return new LinearMovingProjectileStrategy(speed);
    }

    public long getLastMoveTime() {
        return lastMoveTime;
    }

    protected int getTotalTraveledDistance() {
        return totalTraveledDistance;
    }
}
