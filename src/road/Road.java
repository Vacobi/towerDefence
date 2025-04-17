package road;

import java.util.List;
import java.util.Optional;

public class Road {

    private final List<RoadSegment> road;

    public Road(List<RoadSegment> road) {
        this.road = road;
    }

    public Road() {
        this(RoadParser.extractRoadSegments("../../roads.txt"));
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

}
