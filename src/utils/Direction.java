package utils;

public enum Direction {
    NORTH,
    WEST,
    SOUTH,
    EAST;

    public static double toRadians(Direction direction) {
        return switch (direction) {
            case NORTH -> -Math.PI / 2;
            case WEST -> Math.PI;
            case SOUTH -> Math.PI / 2;
            case EAST -> 0;
        };
    }
}
