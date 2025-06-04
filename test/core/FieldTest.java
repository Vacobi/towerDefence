package core;

import factory.MonsterFactory;
import monster.Monster;
import monster.MovingMonsterStrategy;
import monster.PlainRoadMoving;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;

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
    void waveStarts() throws InterruptedException {
        Field field = new Field(path.toString());
        UpdateFieldControllerImpl controller = new UpdateFieldControllerImpl();

        MonsterFactory factory = new MonsterFactory();
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 1000);
        Queue<Monster> monstersToSpawn = factory.createMonsters(1, strategy);

        long spawnDelay = 1L;
        Wave wave = new Wave(monstersToSpawn, spawnDelay, 1);

        field.setWave(wave);

        assertDoesNotThrow(() -> field.startUpdates(controller));

        Thread.sleep(spawnDelay);
        assertEquals(1, wave.getAliveMonsters().size());
    }

    public class UpdateFieldControllerImpl implements UpdateFieldController {

        @Override
        public boolean shouldContinue() {
            return true;
        }
    }
}