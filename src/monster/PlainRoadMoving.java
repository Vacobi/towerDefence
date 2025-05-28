package monster;

import core.Field;
import road.RoadSegment;
import utils.Direction;
import utils.Position;

import java.util.Optional;

public class PlainRoadMoving extends MovingMonsterStrategy{

    private static final int SPEED_COEFF = 25;

    public PlainRoadMoving(Field field, int speed) {
        super(field.getRoad().getNextRoadSegment(null).get().getStart(), field, speed);
        switchRoadSegmentIfNecessary();
    }

    public PlainRoadMoving(Position position, Field field, int speed) {
        super(position, field, speed);
        switchRoadSegmentIfNecessary();
    }

    @Override
    public void moveMonster(long currentTick) {
        if (monsterReachedEnd()) {
            return;
        }

        if (lastMovingTime() == null) {
            setLastMovingTime(currentTick);
            return;
        }

        int delta = (int)(currentTick - lastMovingTime());
        int leftTravelOnThisTick = speed() * (delta / SPEED_COEFF);

        while (leftTravelOnThisTick > 0 && !monsterReachedEnd()) {
            Direction movingDirection = currentRoadSegment().getDirection();

            int leftInThisRoadSegment = currentRoadSegment().getLength() - traveledInCurrentSegment();
            int traveledDistance = Math.min(leftTravelOnThisTick, leftInThisRoadSegment);

            setCurrentPosition(currentPosition().move(movingDirection, traveledDistance));
            setTraveledInCurrentSegment(traveledInCurrentSegment() + traveledDistance);
            switchRoadSegmentIfNecessary();

            leftTravelOnThisTick -= traveledDistance;

            setLastMovingTime(currentTick);
        }
    }

    @Override
    public MovingMonsterStrategy clone() {
        return new PlainRoadMoving(currentPosition(), field(), speed());
    }

    private void switchRoadSegmentIfNecessary() {
        if (monsterReachedEnd()) {
            return;
        }

        if (currentRoadSegment() == null || currentRoadSegment().reachedEnd(traveledInCurrentSegment())) {
            Optional<RoadSegment> optionalRoadSegment = field().getRoad().getNextRoadSegment(currentRoadSegment());
            if (optionalRoadSegment.isPresent()) {
                setCurrentRoadSegment(optionalRoadSegment.get());
                setTraveledInCurrentSegment(0);
            } else {
                setMonsterReachedEnd(true);
            }
        }
    }

    public static int getSpeedCoeff() {
        return SPEED_COEFF;
    }
}