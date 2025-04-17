package tower.utils;

import core.Cell;
import core.Field;

public class TowerGeographicalCharacteristics {
    private final Cell cell;
    private final Field field;

    public TowerGeographicalCharacteristics(Cell cell, Field field) {
        this.cell = cell;
        this.field = field;
    }

    public Cell cell() {
        return cell;
    }

    public Field field() {
        return field;
    }
}
