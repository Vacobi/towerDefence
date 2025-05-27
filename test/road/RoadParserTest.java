package road;

import core.AbstractCell;
import core.RoadCell;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import utils.CoordinatesConverter;
import utils.Direction;
import utils.Position;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoadParserTest {

    @Nested
    class ExtractRoadCell {
        @Test
        void extractOneRoadCell() {
            String line = "1 3 1 NORTH";

            List<RoadCell> expectedRoadCells = List.of(
                    new RoadCell(new Position(1, 3))
            );

            RoadFileLine rfl = new RoadFileLine(line);
            List<RoadCell> actualRoadCells = RoadParser.extractRoadCell(rfl);

            assertListsRoadCellsEquals(expectedRoadCells, actualRoadCells);
        }

        @Test
        void extractRoadCellWithZeroCoordinates() {
            String line = "0 0 1 NORTH";

            List<RoadCell> expectedRoadCells = List.of(
                    new RoadCell(new Position(0, 0))
            );

            RoadFileLine rfl = new RoadFileLine(line);
            List<RoadCell> actualRoadCells = RoadParser.extractRoadCell(rfl);

            assertListsRoadCellsEquals(expectedRoadCells, actualRoadCells);
        }

        @Test
        void extractRoadCellWithNegativeCoordinates() {
            String line = "-1 -3 1 NORTH";

            List<RoadCell> expectedRoadCells = List.of(
                    new RoadCell(new Position(-1, -3))
            );

            RoadFileLine rfl = new RoadFileLine(line);
            List<RoadCell> actualRoadCells = RoadParser.extractRoadCell(rfl);

            assertListsRoadCellsEquals(expectedRoadCells, actualRoadCells);
        }

        @Test
        void severalRoadCellsInNorth() {
            String line = "0 0 3 NORTH";

            List<RoadCell> expectedRoadCells = List.of(
                    new RoadCell(new Position(0, 0)),
                    new RoadCell(new Position(0, 1)),
                    new RoadCell(new Position(0, 2))
            );

            RoadFileLine rfl = new RoadFileLine(line);
            List<RoadCell> actualRoadCells = RoadParser.extractRoadCell(rfl);

            assertListsRoadCellsEquals(expectedRoadCells, actualRoadCells);
        }

        @Test
        void severalRoadCellsInWest() {
            String line = "3 0 3 WEST";

            List<RoadCell> expectedRoadCells = List.of(
                    new RoadCell(new Position(3, 0)),
                    new RoadCell(new Position(2, 0)),
                    new RoadCell(new Position(1, 0))
            );

            RoadFileLine rfl = new RoadFileLine(line);
            List<RoadCell> actualRoadCells = RoadParser.extractRoadCell(rfl);

            assertListsRoadCellsEquals(expectedRoadCells, actualRoadCells);
        }

        @Test
        void severalRoadCellsInSouth() {
            String line = "0 3 3 SOUTH";

            List<RoadCell> expectedRoadCells = List.of(
                    new RoadCell(new Position(0, 3)),
                    new RoadCell(new Position(0, 2)),
                    new RoadCell(new Position(0, 1))
            );

            RoadFileLine rfl = new RoadFileLine(line);
            List<RoadCell> actualRoadCells = RoadParser.extractRoadCell(rfl);

            assertListsRoadCellsEquals(expectedRoadCells, actualRoadCells);
        }

        @Test
        void severalRoadCellsInEast() {
            String line = "0 0 3 EAST";

            List<RoadCell> expectedRoadCells = List.of(
                    new RoadCell(new Position(0, 0)),
                    new RoadCell(new Position(1, 0)),
                    new RoadCell(new Position(2, 0))
            );

            RoadFileLine rfl = new RoadFileLine(line);
            List<RoadCell> actualRoadCells = RoadParser.extractRoadCell(rfl);

            assertListsRoadCellsEquals(expectedRoadCells, actualRoadCells);
        }
    }

    @Nested
    class ExtractRoadSegment {
        @Test
        void extractRoadSegment() {
            String line = "1 3 2 NORTH";
            RoadFileLine rfl = new RoadFileLine(line);

            Position expectedStartPosition = AbstractCell.toGlobalPosition(new Position(1, 3));
            Direction expectedDirection = Direction.NORTH;
            int expectedLength = CoordinatesConverter.lengthOfSegment(2);
            RoadSegment expectedRoadSegment = new RoadSegment(expectedStartPosition, expectedDirection, expectedLength);

            RoadSegment actualRoadSegment = RoadParser.extractRoadSegment(rfl);

            assertRoadSegmentsEquals(expectedRoadSegment, actualRoadSegment);
        }

        @Test
        void roadSegmentDirectionWest() {
            String line = "1 3 2 WEST";
            RoadFileLine rfl = new RoadFileLine(line);

            Position expectedStartPosition = AbstractCell.toGlobalPosition(new Position(1, 3));
            Direction expectedDirection = Direction.WEST;
            int expectedLength = CoordinatesConverter.lengthOfSegment(2);
            RoadSegment expectedRoadSegment = new RoadSegment(expectedStartPosition, expectedDirection, expectedLength);

            RoadSegment actualRoadSegment = RoadParser.extractRoadSegment(rfl);

            assertRoadSegmentsEquals(expectedRoadSegment, actualRoadSegment);
        }

        @Test
        void roadSegmentDirectionSouth() {
            String line = "1 3 2 SOUTH";
            RoadFileLine rfl = new RoadFileLine(line);

            Position expectedStartPosition = AbstractCell.toGlobalPosition(new Position(1, 3));
            Direction expectedDirection = Direction.SOUTH;
            int expectedLength = CoordinatesConverter.lengthOfSegment(2);
            RoadSegment expectedRoadSegment = new RoadSegment(expectedStartPosition, expectedDirection, expectedLength);

            RoadSegment actualRoadSegment = RoadParser.extractRoadSegment(rfl);

            assertRoadSegmentsEquals(expectedRoadSegment, actualRoadSegment);
        }

        @Test
        void roadSegmentDirectionEast() {
            String line = "1 3 2 EAST";
            RoadFileLine rfl = new RoadFileLine(line);

            Position expectedStartPosition = new Position(1, 3);
            Direction expectedDirection = Direction.EAST;
            int expectedLength = CoordinatesConverter.lengthOfSegment(2);
            RoadSegment expectedRoadSegment = new RoadSegment(expectedStartPosition, expectedDirection, expectedLength);

            //RoadSegment actualRoadSegment = RoadParser.extractRoadSegment(rfl);

            //assertRoadSegmentsEquals(expectedRoadSegment, actualRoadSegment);
        }
    }

    @Nested
    class ParseRoad {
        @Test
        void roadContainsOneSegment() {
            Path path = Paths.get("test", "road", "resources", "one_road_segment_one_road_cell.txt")
                    .toAbsolutePath()
                    .normalize();

            List<RoadSegment> expectedRoadSegment = new ArrayList<>();
            Position expectedCellPosition = new Position(0,2);
            Position expectedStart = AbstractCell.toGlobalPosition(expectedCellPosition);
            expectedRoadSegment.add(new RoadSegment(expectedStart, Direction.EAST, CoordinatesConverter.lengthOfSegment(1)));
            List<RoadCell> expectedRoadCells = new ArrayList<>();
            expectedRoadCells.add(new RoadCell(new Position(0, 2)));
            Road expectedRoad = new Road(expectedRoadSegment, expectedRoadCells);

            RoadParser parser = new RoadParser(path.toString());

            assertRoadsEquals(expectedRoad, parser.getRoad());
        }

        @Test
        void roadContainsOneSegmentsSeveralCells() {
            Path path = Paths.get("test", "road", "resources", "one_road_segment_several_road_cells.txt")
                    .toAbsolutePath()
                    .normalize();

            Position expectedStartCellPosition = new Position(0,2);
            Position expectedStart = AbstractCell.toGlobalPosition(expectedStartCellPosition);

            List<RoadSegment> expectedRoadSegment = new ArrayList<>();
            expectedRoadSegment.add(new RoadSegment(expectedStart, Direction.EAST, CoordinatesConverter.lengthOfSegment(3)));

            List<RoadCell> expectedRoadCells = new ArrayList<>();
            expectedRoadCells.add(new RoadCell(expectedStartCellPosition));
            expectedRoadCells.add(new RoadCell(new Position(1, 2)));
            expectedRoadCells.add(new RoadCell(new Position(2, 2)));


            Road expectedRoad = new Road(expectedRoadSegment, expectedRoadCells);


            RoadParser parser = new RoadParser(path.toString());


            assertRoadsEquals(expectedRoad, parser.getRoad());
        }

        @Test
        void roadContainsSeveralSegments() {
            Path path = Paths.get("test", "road", "resources", "several_segments.txt")
                    .toAbsolutePath()
                    .normalize();

            List<RoadSegment> expectedRoadSegment = new ArrayList<>();
            expectedRoadSegment.add(new RoadSegment(AbstractCell.toGlobalPosition(new Position(0, 2)), Direction.EAST, CoordinatesConverter.lengthOfSegment(1)));
            expectedRoadSegment.add(new RoadSegment(AbstractCell.toGlobalPosition(new Position(1, 2)), Direction.NORTH, CoordinatesConverter.lengthOfSegment(4)));
            expectedRoadSegment.add(new RoadSegment(AbstractCell.toGlobalPosition(new Position(1, 6)), Direction.EAST, CoordinatesConverter.lengthOfSegment(2)));
            expectedRoadSegment.add(new RoadSegment(AbstractCell.toGlobalPosition(new Position(3, 6)), Direction.SOUTH, CoordinatesConverter.lengthOfSegment(3)));

            List<RoadCell> expectedRoadCells = new ArrayList<>();
            expectedRoadCells.add(new RoadCell(new Position(0, 2)));
            expectedRoadCells.add(new RoadCell(new Position(1, 2)));
            expectedRoadCells.add(new RoadCell(new Position(1, 3)));
            expectedRoadCells.add(new RoadCell(new Position(1, 4)));
            expectedRoadCells.add(new RoadCell(new Position(1, 5)));
            expectedRoadCells.add(new RoadCell(new Position(1, 6)));
            expectedRoadCells.add(new RoadCell(new Position(2, 6)));
            expectedRoadCells.add(new RoadCell(new Position(3, 6)));
            expectedRoadCells.add(new RoadCell(new Position(3, 5)));
            expectedRoadCells.add(new RoadCell(new Position(3, 4)));


            Road expectedRoad = new Road(expectedRoadSegment, expectedRoadCells);


            RoadParser parser = new RoadParser(path.toString());


            assertRoadsEquals(expectedRoad, parser.getRoad());
        }
    }

    void assertRoadSegmentsEquals(RoadSegment expected, RoadSegment actual) {
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getDirection(), actual.getDirection());
        assertEquals(expected.getLength(), actual.getLength());
    }

    void assertListsRoadSegmentsEquals(List<RoadSegment> expected, List<RoadSegment> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            RoadSegment expectedRoadSegment = expected.get(i);
            RoadSegment actualRoadSegment = actual.get(i);

            assertRoadSegmentsEquals(expectedRoadSegment, actualRoadSegment);
        }
    }

    void assertRoadCellsEquals(RoadCell expected, RoadCell actual) {
        assertEquals(expected.position(), actual.position());
    }

    void assertListsRoadCellsEquals(List<RoadCell> expected, List<RoadCell> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            RoadCell expectedRoadCell = expected.get(i);
            RoadCell actualRoadCell = actual.get(i);

            assertRoadCellsEquals(expectedRoadCell, actualRoadCell);
        }
    }

    void assertRoadsEquals(Road expected, Road actual) {
       assertListsRoadCellsEquals(expected.getRoadCells(), actual.getRoadCells());
       assertListsRoadSegmentsEquals(expected.getRoadSegments(), actual.getRoadSegments());
    }
}