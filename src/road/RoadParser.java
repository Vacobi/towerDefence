package road;

import core.RoadCell;
import exception.FileNotDetectedException;
import utils.CoordinatesConverter;
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
                roadCells.addAll(extractRoadCell(rfl));
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

    protected static List<RoadCell> extractRoadCell(RoadFileLine line) {
        List<RoadCell> cells = new ArrayList<>();
        Position currentCellPosition = new Position(line.x, line.y);
        for (int i = 0; i < line.cellsCount; i++) {
            cells.add(new RoadCell(currentCellPosition.move(line.direction, i)));
        }
        return cells;
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
