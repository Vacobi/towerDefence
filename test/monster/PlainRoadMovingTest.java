package monster;

import core.AbstractCell;
import core.Field;
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

    @Test
    void moveInOneSegmentContainsOneCell() {
        Path path = Paths.get("test", "monster", "resources", "one_segment_road.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 2);
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
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 2);
        // To initialize lastMovingTimestrategy.moveMonster(System.currentTimeMillis());
        strategy.moveMonster(System.currentTimeMillis());

        long delta = TimeUnit.MILLISECONDS.toMillis(200);
        long tickTime = strategy.lastMovingTime() + delta;
        Position positionOfFirstSegment = field.getRoad().getRoadSegments().get(0).getStart();

        Position expectedPosition = positionOfFirstSegment.move(Direction.EAST, (int) (delta / SPEED_COEFF) * strategy.speed());

        strategy.moveMonster(tickTime);

        assertEquals(expectedPosition, strategy.currentPosition());
        assertFalse(strategy.monsterReachedEnd());
        assertEquals(tickTime, strategy.lastMovingTime());
    }

    @Test
    void moveInTwoSegments() {
        Path path = Paths.get("test", "monster", "resources", "several_segments.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 1);
        strategy.moveMonster(System.currentTimeMillis());

        long delta = TimeUnit.SECONDS.toMillis(2);
        long tickTime = strategy.lastMovingTime() + delta;
        RoadSegment firstRoadSegment = field.getRoad().getRoadSegments().get(0);
        RoadSegment secondRoadSegment = field.getRoad().getRoadSegments().get(1);
        Position positionOfSecondSegment = secondRoadSegment.getStart();

        int traveledDistance = (int) (delta / SPEED_COEFF) * strategy.speed() - firstRoadSegment.getLength();
        Position expectedPosition = positionOfSecondSegment.move(secondRoadSegment.getDirection(), traveledDistance);

        strategy.moveMonster(tickTime);

        assertEquals(expectedPosition, strategy.currentPosition());
        assertRoadSegmentsEquals(secondRoadSegment, strategy.currentRoadSegment());
        assertFalse(strategy.monsterReachedEnd());
        assertEquals(tickTime, strategy.lastMovingTime());
    }
}