package monster;

import core.Field;
import road.RoadSegment;
import utils.Direction;
import utils.Position;

import java.util.Optional;

public class PlainRoadMoving extends MovingMonsterStrategy{

    private final int MILLIS_TO_SECONDS_COEFF = 1_000;

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

        Direction movingDirection = currentRoadSegment().getDirection();
        int delta = (int)(currentTick - lastMovingTime());
        int leftTravel = speed() * (delta / MILLIS_TO_SECONDS_COEFF);
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