package road;

import core.RoadCell;

import java.util.List;
import java.util.Optional;

public class Road {
    private final List<RoadSegment> road;
    private final List<RoadCell> roadCells;

    public Road(List<RoadSegment> road, List<RoadCell> roadCells) {
        this.road = road;
        this.roadCells = roadCells;
    }

    public Road(String path) {
        RoadParser roadParser = new RoadParser(path);
        this.road = roadParser.getRoadSegments();
        this.roadCells = roadParser.getRoadCells();
    }

    public Road() {
        RoadParser roadParser = new RoadParser("road.txt");
        this.road = roadParser.getRoadSegments();
        this.roadCells = roadParser.getRoadCells();
    }

    public List<RoadSegment> getRoad() {
        return road;
    }

    public Optional<RoadSegment> getNextRoadSegment(RoadSegment roadSegment) {
        int indexOfCurrent = road.indexOf(roadSegment);
        if (indexOfCurrent == road.size() - 1) {
            return Optional.empty();
        }

        return Optional.of(road.get(indexOfCurrent + 1));
    }

    public List<RoadSegment> getRoadSegments() {
        return road;
    }

    public List<RoadCell> getRoadCells() {
        return roadCells;
    }
}
