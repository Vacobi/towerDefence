package projectile.strategy;

import projectile.MovingProjectile;
import utils.Direction;
import utils.Position;

import java.util.Random;

public class RandomMovingProjectileStrategy implements MovingProjectileStrategy {

    private long lastMoveTime;
    private int totalTraveledDistance;

    private final int speed;
    private static final int SPEED_COEFF = 250;

    private MovingProjectile projectile;

    public RandomMovingProjectileStrategy(MovingProjectile projectile, int speed) {

        this.projectile = projectile;

        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be not less than 0");
        }
        this.speed = speed;

        this.lastMoveTime = System.currentTimeMillis();
        this.totalTraveledDistance = 0;
    }

    public RandomMovingProjectileStrategy(int speed) {
        this(null, speed);
    }

    @Override
    public void move(long currentTick) {
        if (projectile == null) {
            throw new IllegalStateException("No projectile found");
        }

        int delta = (int)(currentTick - lastMoveTime);
        int moveDistance = (delta * speed) / SPEED_COEFF;

        move(currentTick, moveDistance);
    }

    protected void move(long moveTime, int moveDistance) {
        if (projectileMovedAllDistance()) {
            projectile.deactivate();
        }

        if (!projectile.active()) {
            return;
        }

        if (moveDistance == 0) {
            return;
        }

        chooseDirection();

        int distanceToMove = Math.min(moveDistance, projectile.getDistance() - totalTraveledDistance);

        Position positionAfterMove = projectile.position().move(projectile.getDirection(), distanceToMove);
        projectile.setPosition(positionAfterMove);
        lastMoveTime = moveTime;
        totalTraveledDistance += distanceToMove;
    }

    private boolean projectileMovedAllDistance() {
        return totalTraveledDistance >= projectile.getDistance();
    }

    private void chooseDirection() {
        Random random = new Random();
        if (Math.random() < (double) totalTraveledDistance / projectile.getDistance() * 0.1) {
            int directionIndex = random.nextInt(4);
            Direction newDirection = Direction.values()[directionIndex];
            projectile.setDirection(newDirection);
        }
    }

    @Override
    public RandomMovingProjectileStrategy clone() {
        return new RandomMovingProjectileStrategy(speed);
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
}
