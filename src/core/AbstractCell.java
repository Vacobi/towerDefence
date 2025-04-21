package core;

import utils.Position;

public abstract class AbstractCell {
    private final Position position;

    public AbstractCell(Position position) {
        this.position = position;
    }

    public Position position() {
        return position;
    }

    public abstract boolean canPlaceTower();
}
