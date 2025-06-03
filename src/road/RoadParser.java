package road;

import core.AbstractCell;
import core.RoadCell;
import exception.FileNotDetectedException;
import utils.CoordinatesConverter;
import utils.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class RoadParser {
    private final List<RoadSegment> roadSegments;
    private final List<RoadCell> roadCells;

    public RoadParser(String path) {
        roadSegments = new LinkedList<>();
        roadCells = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.lines().forEach(line -> {
                RoadFileLine rfl = new RoadFileLine(line);
                roadSegments.add(extractRoadSegment(rfl));
                roadCells.addAll(extractRoadCells(rfl));
            });
        } catch (FileNotDetectedException e) {
            throw new FileNotDetectedException("Ошибка: файл не найден - " + path);
        } catch (IOException e) {
            throw new RuntimeException("Input exception: " + e.getMessage());
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    protected static RoadSegment extractRoadSegment(RoadFileLine line) {
        Position start = AbstractCell.toGlobalPosition(new Position(line.getX(), line.getY()));

        int lengthOfSegment = CoordinatesConverter.lengthOfSegment(line.getCellsCount());

        return new RoadSegment(start, line.getDirection(), lengthOfSegment);
    }

    protected static List<RoadCell> extractRoadCells(RoadFileLine line) {
        List<RoadCell> cells = new LinkedList<>();
        Position currentCellPosition = new Position(line.getX(), line.getY());
        for (int i = 0; i < line.getCellsCount(); i++) {
            cells.add(new RoadCell(currentCellPosition.move(line.getDirection(), i)));
        }
        return cells;
    }

    //------------------------------------------------------------------------------------------------------------------

    public List<RoadSegment> getRoadSegments() {
        return new LinkedList<>(roadSegments);
    }

    public List<RoadCell> getRoadCells() {
        return new LinkedList<> (roadCells);
    }

    public Road getRoad() {
        return new Road(roadSegments, roadCells);
    }
}
