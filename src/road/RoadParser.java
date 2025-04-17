package road;

import exception.FileNotDetectedException;
import exception.WrongFileFormat;
import utils.Direction;
import utils.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoadParser {
    public static List<RoadSegment> extractRoadSegments(String path) {
        List<RoadSegment> road = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.lines().forEach(line -> {
                road.add(extractRoadSegment(line));
            });
        } catch (FileNotDetectedException e) {
            throw new FileNotDetectedException("Ошибка: файл не найден - " + path);
        } catch (IOException e) {
            throw new RuntimeException("Input exception: " + e.getMessage());
        }

        return road;
    }

    private static RoadSegment extractRoadSegment(String line) {
        final int PARTS_IN_STRING = 4;

        String[] parts = line.split("\\s+");
        if (parts.length != PARTS_IN_STRING) {
            throw new WrongFileFormat("4 elements (Num, Num, Num, Direction)", line);
        }

        try {
            Position position = new Position(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            Direction direction = Direction.valueOf(parts[2]);
            int length = Integer.parseInt(parts[3]);
            return new RoadSegment(position, direction, length);
        } catch (NumberFormatException e) {
            throw new WrongFileFormat("Num, Num, Num, Direction", line);
        } catch (IllegalArgumentException e) {
            throw new WrongFileFormat("Direction", parts[2]);
        }
    }
}
