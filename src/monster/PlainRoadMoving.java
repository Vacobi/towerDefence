package monster;

import core.Field;
import road.RoadSegment;
import utils.Direction;

import java.util.Optional;

public class PlainRoadMoving extends MovingMonsterStrategy{

    public PlainRoadMoving(Field field, int speed) {
        super(field, speed);
    }

    @Override
    public void moveMonster(long currentTick) {
        if (monsterReachedEnd()) {
            return;
        }

        Direction movingDirection = currentRoadSegment().getDirection();
        int delta = (int)(lastMovingTime() - currentTick);
        int leftTravel = speed() * delta;
        while (leftTravel > 0 && !monsterReachedEnd()) {
            int leftInThisRoadSegment = currentRoadSegment().getLength() - traveledInCurrentSegment();
            int traveledDistance = Math.min(leftTravel, leftInThisRoadSegment);
            setCurrentPosition(currentPosition().move(movingDirection, traveledDistance));
            setTraveledInCurrentSegment(traveledInCurrentSegment() + traveledDistance);
            switchRoadSegmentIfNecessary();
            leftTravel -= traveledDistance;
        }

        setLastMovingTime(currentTick);
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
}