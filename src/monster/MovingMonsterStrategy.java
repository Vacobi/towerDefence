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

    private Monster monster;

    public MovingMonsterStrategy(Position initialPosition, Field field, int speed) {
        this.currentPosition = initialPosition;
        this.speed = speed;
        this.field = field;

        currentRoadSegment = null;
        traveledInCurrentSegment = 0;
        reachedEnd = false;
    }

    //------------------------------------------------------------------------------------------------------------------

    public final void moveMonster(long currentTick) {
        if (monster == null) {
            throw new IllegalStateException("Monster has not been set");
        }

        move(currentTick);
    }

    public abstract void move(long currentTick);

    public abstract MovingMonsterStrategy clone();

    //------------------------------------------------------------------------------------------------------------------

    public void setMonster(Monster monster) {
        if (this.monster != null) {
            throw new IllegalStateException("Monster is already set");
        }

        this.monster = monster;
    }

    public Monster getMonster() {
        return monster;
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

    public Position currentPosition() {
        return currentPosition;
    }

    protected void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    protected boolean monsterReachedEnd() {
        return reachedEnd;
    }

    protected int traveledInCurrentSegment() {
        return traveledInCurrentSegment;
    }

    protected void setTraveledInCurrentSegment(int traveledInCurrentSegment) {
        this.traveledInCurrentSegment = traveledInCurrentSegment;
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
