package core;

import exception.CellAlreadyHasTower;
import tower.Tower;
import utils.Position;

public class Cell extends AbstractCell implements Cloneable {
    private Tower tower;

    public Cell(Position position) {
        super(position);
    }

    public Cell(Position position, Tower tower) {
        super(position);
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        if (this.tower != null) {
            throw new CellAlreadyHasTower(this);
        }

        this.tower = tower;
    }

    public boolean canPlaceTower() {
        return tower == null;
    }

    @Override
    public Cell clone() {
        return new Cell(position());
    }
}
