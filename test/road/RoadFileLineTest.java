package road;

import exception.WrongFileArgumentValue;
import exception.WrongFileFormat;
import org.junit.jupiter.api.Test;
import utils.Direction;

import static org.junit.jupiter.api.Assertions.*;

class RoadFileLineTest {

    @Test
    void correctLineLengthOneCell() {
        String line = "0 2 1 NORTH";

        int expectedX = 0;
        int expectedY = 2;
        int expectedCellsCount = 1;
        Direction expectedDirection = Direction.NORTH;

        RoadFileLine actualRfl = new RoadFileLine(line);

        assertEquals(expectedX, actualRfl.x);
        assertEquals(expectedY, actualRfl.y);
        assertEquals(expectedCellsCount, actualRfl.cellsCount);
        assertEquals(expectedDirection, actualRfl.direction);
    }

    @Test
    void correctLineLengthSeveralCells() {
        String line = "0 2 5 NORTH";

        int expectedX = 0;
        int expectedY = 2;
        int expectedCellsCount = 5;
        Direction expectedDirection = Direction.NORTH;

        RoadFileLine actualRfl = new RoadFileLine(line);

        assertEquals(expectedX, actualRfl.x);
        assertEquals(expectedY, actualRfl.y);
        assertEquals(expectedCellsCount, actualRfl.cellsCount);
        assertEquals(expectedDirection, actualRfl.direction);
    }

    @Test
    void cellsLengthIsZero() {
        String line = "0 2 0 NORTH";

        int expectedX = 0;
        int expectedY = 2;
        int expectedCellsCount = 0;
        Direction expectedDirection = Direction.NORTH;

        RoadFileLine actualRfl = new RoadFileLine(line);

        assertEquals(expectedX, actualRfl.x);
        assertEquals(expectedY, actualRfl.y);
        assertEquals(expectedCellsCount, actualRfl.cellsCount);
        assertEquals(expectedDirection, actualRfl.direction);
    }

    @Test
    void negativeCellsLength() {
        String line = "0 2 -1 NORTH";

        assertThrows(WrongFileArgumentValue.class, () -> new RoadFileLine(line));
    }

    @Test
    void nonExistingDirection() {
        String line = "0 2 1 ABC";

        assertThrows(WrongFileArgumentValue.class, () -> new RoadFileLine(line));
    }

    @Test
    void lessArgumentsThanRequired() {
        String line = "0 1 NORTH";

        assertThrows(WrongFileFormat.class, () -> new RoadFileLine(line));
    }

    @Test
    void xIsStringInsteadOfInt() {
        String line = "abc 2 1 NORTH";

        assertThrows(WrongFileFormat.class, () -> new RoadFileLine(line));
    }

    @Test
    void yIsStringInsteadOfInt() {
        String line = "0 abc 1 NORTH";

        assertThrows(WrongFileFormat.class, () -> new RoadFileLine(line));
    }

    @Test
    void coordinatesCountIsStringInsteadOfInt() {
        String line = "0 2 abc NORTH";

        assertThrows(WrongFileFormat.class, () -> new RoadFileLine(line));
    }

    @Test
    void directionIsNumber() {
        String line = "0 2 1 1";

        assertThrows(WrongFileArgumentValue.class, () -> new RoadFileLine(line));
    }

    @Test
    void emptyLine() {
        String line = "";

        assertThrows(WrongFileFormat.class, () -> new RoadFileLine(line));
    }
}