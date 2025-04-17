package projectile;

import collision.Hitbox;
import core.Field;
import monster.Monster;
import utils.Direction;
import utils.Position;

public class LinearMovingProjectile extends MovingProjectile {
    public LinearMovingProjectile(int speed, int maxDistance, Direction direction, Hitbox hitbox, int damage, Position startPosition, Field field) {
        super(speed, maxDistance, direction, hitbox, damage, startPosition, field);
    }

    @Override
    public void applyEffect(long currentTick) {
        for (Monster monster : getField().getWave().getAliveMonsters()) {
            if (collidesWith(monster)) {
                monster.applyDamage(getDamage());
                deactivate();
                return;
            }
        }
    }

    @Override
    protected void move(long currentTick) {
        int moveDistance = (int)(currentTick - lastMoveTime()) * speed() / 10;

        move(currentTick, moveDistance);
    }

    protected void move(long moveTime, int moveDistance) {
        if (totalTraveledDistance() >= maxDistance()) {
            deactivate();
        }

        if (!active()) {
            return;
        }

        int distanceToMove = Math.min(moveDistance, maxDistance() - totalTraveledDistance());

        Position positionAfterMove = position().move(direction(), distanceToMove);
        setPosition(positionAfterMove);
        setLastMoveTime(moveTime);
        setTotalTraveledDistance(totalTraveledDistance() + distanceToMove);
    }
}