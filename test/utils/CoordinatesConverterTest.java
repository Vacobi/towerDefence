package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesConverterTest {

    @Test
    void xToGlobalCoordinate() {
        int xCoeff = CoordinatesConverter.coeffConvertToGlobalX();
        int coordinate = 2;

        int expected = xCoeff * coordinate;

        int actual = CoordinatesConverter.xToGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void yToGlobalCoordinate() {
        int yCoeff = CoordinatesConverter.coeffConvertToGlobalY();
        int coordinate = 2;

        int expected = yCoeff * coordinate;

        int actual = CoordinatesConverter.yToGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void xCoordinateIsZero() {
        int xCoeff = CoordinatesConverter.coeffConvertToGlobalX();
        int coordinate = 0;

        int expected = xCoeff * coordinate;

        int actual = CoordinatesConverter.xToGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void yCoordinateIsZero() {
        int yCoeff = CoordinatesConverter.coeffConvertToGlobalY();
        int coordinate = 0;

        int expected = yCoeff * coordinate;

        int actual = CoordinatesConverter.yToGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void xCoordinateIsNegative() {
        int xCoeff = CoordinatesConverter.coeffConvertToGlobalX();
        int coordinate = -3;

        int expected = xCoeff * coordinate;

        int actual = CoordinatesConverter.xToGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void yCoordinateIsNegative() {
        int yCoeff = CoordinatesConverter.coeffConvertToGlobalY();
        int coordinate = -3;

        int expected = yCoeff * coordinate;

        int actual = CoordinatesConverter.yToGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void lengthOfSegmentInNorthDirection() {
        int coordinatesCount = 3;
        Direction direction = Direction.NORTH;

        int expectedLength = coordinatesCount * CoordinatesConverter.coeffConvertToGlobalX();

        int actualLength = CoordinatesConverter.lengthOfSegment(coordinatesCount, direction);

        assertEquals(expectedLength, actualLength);
    }

    @Test
    void lengthOfSegmentInWestDirection() {
        int coordinatesCount = 3;
        Direction direction = Direction.WEST;

        int expectedLength = coordinatesCount * CoordinatesConverter.coeffConvertToGlobalY();

        int actualLength = CoordinatesConverter.lengthOfSegment(coordinatesCount, direction);

        assertEquals(expectedLength, actualLength);
    }

    @Test
    void lengthOfSegmentInSouthDirection() {
        int coordinatesCount = 3;
        Direction direction = Direction.SOUTH;

        int expectedLength = coordinatesCount * CoordinatesConverter.coeffConvertToGlobalX();

        int actualLength = CoordinatesConverter.lengthOfSegment(coordinatesCount, direction);

        assertEquals(expectedLength, actualLength);
    }

    @Test
    void lengthOfSegmentInEastDirection() {
        int coordinatesCount = 3;
        Direction direction = Direction.EAST;

        int expectedLength = coordinatesCount * CoordinatesConverter.coeffConvertToGlobalY();

        int actualLength = CoordinatesConverter.lengthOfSegment(coordinatesCount, direction);

        assertEquals(expectedLength, actualLength);
    }

    @Test
    void zeroCoordinates() {
        int coordinatesCount = 0;
        Direction direction = Direction.EAST;

        int expectedLength = coordinatesCount * CoordinatesConverter.coeffConvertToGlobalY();

        int actualLength = CoordinatesConverter.lengthOfSegment(coordinatesCount, direction);

        assertEquals(expectedLength, actualLength);
    }

    @Test
    void negativeCoordinatesCount() {
        int coordinatesCount = -1;
        Direction direction = Direction.EAST;

        assertThrows(IllegalArgumentException.class, () -> CoordinatesConverter.lengthOfSegment(coordinatesCount, direction));
    }
}