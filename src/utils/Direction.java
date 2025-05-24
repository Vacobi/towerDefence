package utils;

public enum Direction {
    EAST(Math.toRadians(0)),
    NORTH(Math.toRadians(90)),
    WEST(Math.toRadians(180)),
    SOUTH(Math.toRadians(270));

    private final double angleRadians;

    Direction(double angleRadians) {
        this.angleRadians = angleRadians;
    }

    public double toRadians() {
        return angleRadians;
    }
}
