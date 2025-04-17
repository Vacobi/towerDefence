package core;

import exception.CellAlreadyHasTower;
import tower.Tower;
import utils.Position;

public class Cell {
    private final Position position;
    private Tower tower;

    public Cell(Position position) {
        this.position = position;
    }

    public Cell(Position position, Tower tower) {
        this.position = position;
    }

    public Position position() {
        return position;
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
}
