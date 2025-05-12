package monster;

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
    private final Position monsterPosition = new Position(100, 100);

    private final int MILLIS_TO_SECONDS_COEFF = 1000;

    @Test
    void moveInOneSegment() {
        Path path = Paths.get("test", "monster", "resources", "one_segment_road.txt")
                .toAbsolutePath()
                .normalize();
        Field field = new Field(path.toString());
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 2);
        long tickTime = strategy.lastMovingTime() + TimeUnit.SECONDS.toMillis(2);
        Position positionOfFirstSegment = field.getRoad().getRoadSegments().get(0).getStart();

        Position expectedPosition = positionOfFirstSegment.move(Direction.EAST, (int) (TimeUnit.SECONDS.toMillis(2) / MILLIS_TO_SECONDS_COEFF) * strategy.speed());

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
        MovingMonsterStrategy strategy = new PlainRoadMoving(field, 30);
        long tickTime = strategy.lastMovingTime() + TimeUnit.SECONDS.toMillis(2);
        Position positionOfFirstSegment = field.getRoad().getRoadSegments().get(0).getStart();

        long delta = TimeUnit.SECONDS.toMillis(1);
        RoadSegment expectedRoadSegment = field.getRoad().getRoadSegments().get(1);
        Position expectedPosition = positionOfFirstSegment.move(Direction.EAST, (int) (delta / MILLIS_TO_SECONDS_COEFF) * strategy.speed());
        expectedPosition = expectedPosition.move(Direction.NORTH, (int) (delta / MILLIS_TO_SECONDS_COEFF) * strategy.speed());

        strategy.moveMonster(tickTime);

        assertEquals(expectedPosition, strategy.currentPosition());
        assertRoadSegmentsEquals(expectedRoadSegment, strategy.currentRoadSegment());
        assertFalse(strategy.monsterReachedEnd());
        assertEquals(tickTime, strategy.lastMovingTime());
    }
}