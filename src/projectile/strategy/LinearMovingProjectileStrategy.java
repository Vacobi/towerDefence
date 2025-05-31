package projectile.strategy;

import projectile.MovingProjectile;
import utils.Position;

public class LinearMovingProjectileStrategy implements MovingProjectileStrategy {
    private long lastMoveTime;
    private int totalTraveledDistance;

    private final int speed;
    private static final int SPEED_COEFF = 25;

    private MovingProjectile projectile;

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

        int moveDistance = (int) ((currentTick - lastMoveTime) / SPEED_COEFF) * speed;

        move(currentTick, moveDistance);
    }

    protected void move(long moveTime, int moveDistance) {
        if (projectileMovedAllDistance()) {
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

    private boolean projectileMovedAllDistance() {
        return totalTraveledDistance >= projectile.getDistance();
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

    public static int getSpeedCoeff() {
        return SPEED_COEFF;
    }
}
