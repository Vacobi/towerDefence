package road;

import utils.Direction;
import utils.Position;

public class RoadSegment {
    private final Position start;
    private final Direction direction;
    private final int length;

    public RoadSegment(Position start, Direction direction, int length) {
        this.start = start;
        this.direction = direction;
        this.length = length;
    }

    public Position getStart() {
        return start;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getLength() {
        return length;
    }

    public boolean reachedEnd(int moved) {
        return length >= moved;
    }
}
