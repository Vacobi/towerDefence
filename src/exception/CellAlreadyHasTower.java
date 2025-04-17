package exception;

import core.Cell;

public class CellAlreadyHasTower extends RuntimeException {
    public CellAlreadyHasTower(Cell cell) {
        super("Cell " + cell.toString() + " already has tower " + cell.getTower().toString());
    }
}
