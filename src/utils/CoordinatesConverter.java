package utils;

public class CoordinatesConverter {
    private static final int CellPositionToGlobalPositionCoeffX = 30;
    private static final int CellPositionToGlobalPositionCoeffY = 30;

    public static int xToGlobalCoordinate(int x) {
        return x * CellPositionToGlobalPositionCoeffX;
    }

    public static int yToGlobalCoordinate(int y) {
        return y * CellPositionToGlobalPositionCoeffY;
    }

    public static int coeffConvertToGlobalX() {
        return CellPositionToGlobalPositionCoeffX;
    }

    public static int coeffConvertToGlobalY() {
        return CellPositionToGlobalPositionCoeffY;
    }

    public static int lengthOfSegment(int coordinatesCount, Direction direction) {
        if (coordinatesCount < 0) {
            throw new IllegalArgumentException("Coordinates count must be greater than zero");
        }

        switch (direction) {
            case NORTH, SOUTH -> { return coordinatesCount * CellPositionToGlobalPositionCoeffY; }
            case WEST, EAST -> {return coordinatesCount * CellPositionToGlobalPositionCoeffX;}
        }

        return coordinatesCount;
    }
}
