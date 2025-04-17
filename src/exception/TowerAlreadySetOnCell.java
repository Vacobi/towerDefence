package exception;

import tower.Tower;

public class TowerAlreadySetOnCell extends RuntimeException {
    public TowerAlreadySetOnCell(Tower tower) {
        super("Tower " + tower + " already set on cell");
    }
}
