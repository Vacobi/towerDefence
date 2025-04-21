package road;

import core.AbstractCell;
import core.RoadCell;
import exception.FileNotDetectedException;
import exception.WrongFileFormat;
import utils.CoordinatesConverter;
import utils.Direction;
import utils.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoadParser {
    private final List<RoadSegment> roadSegments;
    private final List<RoadCell> roadCells;

    public RoadParser(String path) {
        List<RoadSegment> roadSegments = new ArrayList<>();
        List<RoadCell> roadCells = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.lines().forEach(line -> {
                RoadFileLine rfl = new RoadFileLine(line);
                roadSegments.add(extractRoadSegment(rfl));
                roadCells.add(extractRoadCell(rfl));
            });
        } catch (FileNotDetectedException e) {
            throw new FileNotDetectedException("Ошибка: файл не найден - " + path);
        } catch (IOException e) {
            throw new RuntimeException("Input exception: " + e.getMessage());
        }

        this.roadSegments = roadSegments;
        this.roadCells = roadCells;
    }

    protected static RoadSegment extractRoadSegment(RoadFileLine line) {
        Position start = new Position(line.x, line.y);
        int lengthOfSegment = CoordinatesConverter.lengthOfSegment(line.cellsCount, line.direction);
        return new RoadSegment(start, line.direction, lengthOfSegment);
    }

    protected static RoadCell extractRoadCell(RoadFileLine line) {
        return new RoadCell(new Position(line.x, line.y));
    }

    public List<RoadSegment> getRoadSegments() {
        return roadSegments;
    }

    public List<RoadCell> getRoadCells() {
        return roadCells;
    }

    public Road getRoad() {
        return new Road(roadSegments, roadCells);
    }
}
