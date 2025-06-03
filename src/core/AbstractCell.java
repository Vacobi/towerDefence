package core;

import utils.CoordinatesConverter;
import utils.Position;

public abstract class AbstractCell {
    private final Position position;

    public AbstractCell(Position position) {
        this.position = position;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Position toGlobalPosition(Position position) {
        return new Position(
                CoordinatesConverter.toGlobalCoordinate(position.getX()) + getSize() / 2,
                CoordinatesConverter.toGlobalCoordinate(position.getY()) + getSize() / 2
        );
    }

    public Position getGlobalPosition() {
        return toGlobalPosition(position);
    }

    public abstract boolean canPlaceTower();

    public static int getSize() {
        return 40;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Position position() {
        return position;
    }
}
