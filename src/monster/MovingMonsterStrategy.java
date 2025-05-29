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

    public abstract MovingMonsterStrategy clone();

    protected boolean monsterReachedEnd() {
        return reachedEnd;
    }

    public Long lastMovingTime() {
        return lastMovingTime;
    }

    protected void setLastMovingTime(Long lastMovingTime) {
        this.lastMovingTime = lastMovingTime;
    }

    public RoadSegment currentRoadSegment() {
        return currentRoadSegment;
    }

    protected void setCurrentRoadSegment(RoadSegment currentRoadSegment) {
        this.currentRoadSegment = currentRoadSegment;
    }

    protected int traveledInCurrentSegment() {
        return traveledInCurrentSegment;
    }

    protected void setTraveledInCurrentSegment(int traveledInCurrentSegment) {
        this.traveledInCurrentSegment = traveledInCurrentSegment;
    }

    public Position currentPosition() {
        return currentPosition;
    }

    protected void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    protected Field field() {
        return field;
    }

    protected int speed() {
        return speed;
    }

    protected void setMonsterReachedEnd(boolean reachedEnd) {
        this.reachedEnd = reachedEnd;
    }
}
