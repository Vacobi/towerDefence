package utils;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Position move(Direction direction, int shift) {
        switch (direction) {
            case NORTH -> {
                return new Position(x, y + shift);
            }
            case WEST -> {
                return new Position(x - shift, y);
            }
            case SOUTH -> {
                return new Position(x, y - shift);
            }
            case EAST -> {
                return new Position(x + shift, y);
            }
        }

        return new Position(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "x: " + x + "\ny: " + y + "\n";
    }
}
