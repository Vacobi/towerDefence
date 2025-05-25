package utils;

import core.AbstractCell;

public class CoordinatesConverter {

    public static Position centerToLeftTop (Position center, int width, int height) {
        return new Position(center.getX() - width / 2, center.getY() + height / 2);
    }

    public static Position toGlobalCoordinates(Position position) {
        return new Position(
                toGlobalCoordinate(position.getX()),
                toGlobalCoordinate(position.getY())
        );
    }

    public static int toGlobalCoordinate(int coordinate) {
        return coordinate * AbstractCell.getSize();
    }

    public static int lengthOfSegment(int coordinatesCount) {
        if (coordinatesCount < 0) {
            throw new IllegalArgumentException("Coordinates count must be greater than zero");
        }

        return coordinatesCount * AbstractCell.getSize();
    }
}
