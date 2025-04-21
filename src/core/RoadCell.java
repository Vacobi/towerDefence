package core;

import utils.Position;

public class RoadCell extends AbstractCell{

    public RoadCell(Position position) {
        super(position);
    }

    @Override
    public boolean canPlaceTower() {
        return false;
    }
}
