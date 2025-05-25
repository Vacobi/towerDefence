package utils;

import core.AbstractCell;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesConverterTest {

    @Test
    void toGlobalCoordinate() {
        int coordinate = 2;

        int expected = AbstractCell.getSize() * coordinate;

        int actual = CoordinatesConverter.toGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void yToGlobalCoordinate() {
        int coordinate = 2;

        int expected = AbstractCell.getSize() * coordinate;

        int actual = CoordinatesConverter.toGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void xCoordinateIsZero() {
        int coordinate = 0;

        int expected = AbstractCell.getSize() * coordinate;

        int actual = CoordinatesConverter.toGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void yCoordinateIsZero() {
        int coordinate = 0;

        int expected = AbstractCell.getSize() * coordinate;

        int actual = CoordinatesConverter.toGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void xCoordinateIsNegative() {
        int coordinate = -3;

        int expected = AbstractCell.getSize() * coordinate;

        int actual = CoordinatesConverter.toGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void yCoordinateIsNegative() {
        int coordinate = -3;

        int expected = AbstractCell.getSize() * coordinate;

        int actual = CoordinatesConverter.toGlobalCoordinate(coordinate);

        assertEquals(expected, actual);
    }

    @Test
    void lengthOfSegmentInNorthDirection() {
        int coordinatesCount = 3;

        int expectedLength = coordinatesCount * AbstractCell.getSize();

        int actualLength = CoordinatesConverter.lengthOfSegment(coordinatesCount);

        assertEquals(expectedLength, actualLength);
    }

    @Test
    void zeroCoordinates() {
        int coordinatesCount = 0;

        int expectedLength = coordinatesCount * AbstractCell.getSize();

        int actualLength = CoordinatesConverter.lengthOfSegment(coordinatesCount);

        assertEquals(expectedLength, actualLength);
    }

    @Test
    void negativeCoordinatesCount() {
        int coordinatesCount = -1;

        assertThrows(IllegalArgumentException.class, () -> CoordinatesConverter.lengthOfSegment(coordinatesCount));
    }
}