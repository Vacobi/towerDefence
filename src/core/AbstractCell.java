package core;

import utils.CoordinatesConverter;
import utils.Position;

public abstract class AbstractCell {
    private final Position position;

    public AbstractCell(Position position) {
        this.position = position;
    }

    public Position position() {
        return position;
    }

    public Position getGlobalPosition() {
        return new Position(
                CoordinatesConverter.toGlobalCoordinate(position.getX()) + getSize() / 2,
                CoordinatesConverter.toGlobalCoordinate(position.getY()) + getSize() / 2
        );
    }

    public abstract boolean canPlaceTower();

    public static int getSize() {
        return 30;
    }
}
