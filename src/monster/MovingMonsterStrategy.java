package monster;

import core.Field;
import road.RoadSegment;
import utils.Position;

public abstract class MovingMonsterStrategy {

    private Long lastMovingTime;
    private RoadSegment currentRoadSegment;
    private int traveledInCurrentSegment;
    private Position currentPosition;
    private final Field field;
    private final int speed;
    private boolean reachedEnd;

    public MovingMonsterStrategy(Position initialPosition, Field field, int speed) {
        this.currentPosition = initialPosition;
        this.speed = speed;
        this.field = field;

        currentRoadSegment = null;
        traveledInCurrentSegment = 0;
        reachedEnd = false;
    }

    public abstract void moveMonster(long currentTick);

    public boolean monsterReachedEnd() {
        return reachedEnd;
    }

    public Long lastMovingTime() {
        return lastMovingTime;
    }

    public void setLastMovingTime(Long lastMovingTime) {
        this.lastMovingTime = lastMovingTime;
    }

    public RoadSegment currentRoadSegment() {
        return currentRoadSegment;
    }

    public void setCurrentRoadSegment(RoadSegment currentRoadSegment) {
        this.currentRoadSegment = currentRoadSegment;
    }

    public int traveledInCurrentSegment() {
        return traveledInCurrentSegment;
    }

    public void setTraveledInCurrentSegment(int traveledInCurrentSegment) {
        this.traveledInCurrentSegment = traveledInCurrentSegment;
    }

    public Position currentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Field field() {
        return field;
    }

    public int speed() {
        return speed;
    }

    public void setMonsterReachedEnd(boolean reachedEnd) {
        this.reachedEnd = reachedEnd;
    }

    public abstract MovingMonsterStrategy clone();
}
