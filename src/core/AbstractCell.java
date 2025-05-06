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
                CoordinatesConverter.xToGlobalCoordinate(position.getX()),
                CoordinatesConverter.yToGlobalCoordinate(position.getY())
        );
    }

    public abstract boolean canPlaceTower();
}
