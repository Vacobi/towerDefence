package road;

import exception.WrongFileFormat;
import exception.WrongFileArgumentValue;
import utils.Direction;

class RoadFileLine {
    private final int x;
    private final int y;
    private final int cellsCount;
    private final Direction direction;

    protected RoadFileLine(String line) {
        final int PARTS_IN_STRING = 4;

        String[] parts = line.split("\\s+");
        if (parts.length != PARTS_IN_STRING) {
            throw new WrongFileFormat("4 elements (Num, Num, Num, Direction)", line);
        }

        try {
            x = Integer.parseInt(parts[0]);
            y = Integer.parseInt(parts[1]);
            cellsCount = Integer.parseInt(parts[2]);
            direction = Direction.valueOf(parts[3]);
        } catch (NumberFormatException e) {
            throw new WrongFileFormat("Num, Num, Num, Direction", line);
        } catch (IllegalArgumentException e) {
            throw new WrongFileArgumentValue("Direction", parts[2]);
        }

        if (cellsCount < 0) {
            throw new WrongFileArgumentValue("Number of cells must be greater than zero", line);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCellsCount() {
        return cellsCount;
    }

    public Direction getDirection() {
        return direction;
    }
}