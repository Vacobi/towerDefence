package monster;

import core.AbstractCell;
import core.Field;
import factory.MonsterFactory;
import org.junit.jupiter.api.Test;
import road.RoadSegment;
import utils.Direction;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static asserts.TestAsserts.assertRoadSegmentsEquals;
import static org.junit.jupiter.api.Assertions.*;

class PlainRoadMovingTest {

    private final int SPEED_COEFF = PlainRoadMoving.getSpeedCoeff();

    private final MonsterFactory monsterFactory = new MonsterFactory();

    @Test
    void moveInOneSegmentContainsOneCell() {
        Path path = Paths.get("test", "monster", "resources", "one_segment_road.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 20);
        strategy.setMonster(monsterFactory.createMonster(strategy.clone()));
        // To initialize lastMovingTime
        strategy.moveMonster(System.currentTimeMillis());

        long delta = TimeUnit.SECONDS.toMillis(2);
        long tickTime = strategy.lastMovingTime() + delta;
        Position positionOfFirstSegment = field.getRoad().getRoadSegments().get(0).getStart();

        Position expectedPosition = positionOfFirstSegment.move(Direction.EAST, AbstractCell.getSize());

        strategy.moveMonster(tickTime);

        assertEquals(expectedPosition, strategy.currentPosition());
        assertTrue(strategy.monsterReachedEnd());
        assertEquals(tickTime, strategy.lastMovingTime());
    }

    @Test
    void moveInOneSegmentContainsOneCellAndNotReachedEnd() {
        Path path = Paths.get("test", "monster", "resources", "one_segment_road.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 20);
        strategy.setMonster(monsterFactory.createMonster(strategy.clone()));
        strategy.moveMonster(System.currentTimeMillis());

        long delta = TimeUnit.MILLISECONDS.toMillis(200);
        long tickTime = strategy.lastMovingTime() + delta;
        Position positionOfFirstSegment = field.getRoad().getRoadSegments().get(0).getStart();

        Position expectedPosition = positionOfFirstSegment.move(Direction.EAST, (int) (delta * strategy.speed()) / SPEED_COEFF);

        strategy.moveMonster(tickTime);

        assertEquals(expectedPosition, strategy.currentPosition());
        assertFalse(strategy.monsterReachedEnd());
        assertEquals(tickTime, strategy.lastMovingTime());
    }

    @Test
    void moveWithSwitchOfSegment() {
        Path path = Paths.get("test", "monster", "resources", "several_segments.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 10);
        strategy.setMonster(monsterFactory.createMonster(strategy.clone()));
        strategy.moveMonster(System.currentTimeMillis());

        long delta = TimeUnit.SECONDS.toMillis(2);
        long tickTime = strategy.lastMovingTime() + delta;
        RoadSegment firstRoadSegment = field.getRoad().getRoadSegments().get(0);
        RoadSegment secondRoadSegment = field.getRoad().getRoadSegments().get(1);
        Position positionOfSecondSegment = secondRoadSegment.getStart();

        int traveledDistance = (int) (delta * strategy.speed()) / SPEED_COEFF - firstRoadSegment.getLength();
        Position expectedPosition = positionOfSecondSegment.move(secondRoadSegment.getDirection(), traveledDistance);

        strategy.moveMonster(tickTime);

        assertEquals(expectedPosition, strategy.currentPosition());
        assertRoadSegmentsEquals(secondRoadSegment, strategy.currentRoadSegment());
        assertFalse(strategy.monsterReachedEnd());
        assertEquals(tickTime, strategy.lastMovingTime());
    }

    @Test
    void moveOnZeroDistance() {
        Path path = Paths.get("test", "monster", "resources", "one_segment_road.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 0);
        strategy.setMonster(monsterFactory.createMonster(strategy.clone()));
        // To initialize lastMovingTime
        long initialMovingTime = System.currentTimeMillis();
        strategy.moveMonster(initialMovingTime);

        long delta = TimeUnit.SECONDS.toMillis(2);
        long tickTime = strategy.lastMovingTime() + delta;
        Position positionOfFirstSegment = field.getRoad().getRoadSegments().get(0).getStart();

        Position expectedPosition = positionOfFirstSegment;

        strategy.moveMonster(tickTime);

        assertEquals(expectedPosition, strategy.currentPosition());
        assertFalse(strategy.monsterReachedEnd());
        assertEquals(initialMovingTime, strategy.lastMovingTime());
    }

    @Test
    void moveAfterReachedEnd() {
        Path path = Paths.get("test", "monster", "resources", "one_segment_road.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 20);
        strategy.setMonster(monsterFactory.createMonster(strategy.clone()));
        // To initialize lastMovingTime
        long lastMoveTime = System.currentTimeMillis();
        strategy.moveMonster(lastMoveTime);

        //--------------------------------------------------------------------------------------------------------------

        long delta = TimeUnit.SECONDS.toMillis(2);
        lastMoveTime += delta;
        Position positionOfFirstSegment = field.getRoad().getRoadSegments().get(0).getStart();

        Position expectedPosition = positionOfFirstSegment.move(Direction.EAST, AbstractCell.getSize());

        strategy.moveMonster(lastMoveTime);

        delta = TimeUnit.SECONDS.toMillis(2);
        long tickTime = lastMoveTime + delta;
        strategy.moveMonster(tickTime);

        //--------------------------------------------------------------------------------------------------------------

        assertEquals(expectedPosition, strategy.currentPosition());
        assertTrue(strategy.monsterReachedEnd());
        assertEquals(lastMoveTime, strategy.lastMovingTime());
    }

    @Test
    void cloneWithSameCharacteristics() {
        Path path = Paths.get("test", "monster", "resources", "one_segment_road.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        PlainRoadMoving strategy = new PlainRoadMoving(field, 20);
        strategy.setMonster(monsterFactory.createMonster(strategy.clone()));
        // To initialize lastMovingTime
        strategy.moveMonster(System.currentTimeMillis());

        PlainRoadMoving actualCloned = strategy.clone();

        assertNotEquals(strategy, actualCloned);
        assertEquals(strategy.currentPosition(), actualCloned.currentPosition());
        assertEquals(strategy.speed(), actualCloned.speed());
        assertEquals(strategy.field(), actualCloned.field());

        assertNull(actualCloned.lastMovingTime());
        assertFalse(actualCloned.monsterReachedEnd());
        assertEquals(strategy.currentRoadSegment(), actualCloned.currentRoadSegment());
        assertEquals(0, actualCloned.traveledInCurrentSegment());
    }
}