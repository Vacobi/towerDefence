package projectile;

import collision.Hitbox;
import core.Field;
import utils.Direction;
import utils.Position;


public abstract class MovingProjectile extends Projectile {

    private int speed;
    private final int maxDistance;
    private final Direction direction;

    private long lastMoveTime;
    private int totalTraveledDistance;

    public MovingProjectile(int speed, int maxDistance, Direction direction, Hitbox hitbox, int damage, Position startPosition, Field field) {
        super(hitbox, damage, startPosition, field);

        if (speed < 0) {
            throw new IllegalArgumentException("Speed must be not less than 0");
        }
        this.speed = speed;

        if (maxDistance <= 0) {
            throw new IllegalArgumentException("Max distance must be greater than 0");
        }
        this.maxDistance = maxDistance;

        this.direction = direction;

        this.lastMoveTime = System.currentTimeMillis();
        this.totalTraveledDistance = 0;
    }

    @Override
    public void update(long currentTick) {
        move(currentTick);

        applyEffect(currentTick);
    }

    protected abstract void move(long currentTick);

    // - ////////////////////////////////////////////////////////////////////////////////////
    protected void setLastMoveTime(Long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    protected int totalTraveledDistance() {
        return totalTraveledDistance;
    }

    protected long lastMoveTime() {
        return lastMoveTime;
    }

    public Direction direction() {
        return direction;
    }

    public int speed() {
        return speed;
    }

    public int maxDistance() {
        return maxDistance;
    }

    public void setLastMoveTime(long lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

    public void setTotalTraveledDistance(int totalTraveledDistance) {
        this.totalTraveledDistance = totalTraveledDistance;
    }
}