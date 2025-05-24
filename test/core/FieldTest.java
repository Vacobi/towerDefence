package core;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    private final Path path = Paths.get("test", "core", "resources", "several_segments.txt")
            .toAbsolutePath()
            .normalize();

    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private static final int CELLS_ON_FIELD = WIDTH * HEIGHT;

    @Test
    void cellsInitializing() {
        Field field = new Field(path.toString());

        int actualCells = field.getCells().size();
        int actualRoadCells = field.getRoad().getRoadCells().size();
        int actualCellsOnField = actualCells + actualRoadCells;

        assertEquals(CELLS_ON_FIELD, actualCellsOnField);
    }

    @Test
    void waveEnds() {
        Field field = new Field(path.toString());
        UpdateFieldControllerImpl controller = new UpdateFieldControllerImpl();
        Wave wave = new Wave(new PriorityQueue<>(), 100L, 1);

        field.setWave(wave);

        assertDoesNotThrow(() -> field.startUpdates(controller));
    }

    public class UpdateFieldControllerImpl implements UpdateFieldController {

        @Override
        public boolean shouldContinue() {
            return true;
        }
    }
}