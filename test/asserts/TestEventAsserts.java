package asserts;

import events.CellEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEventAsserts {

    public static void assertCellEventEquals(CellEvent expected, CellEvent actual) {
        assertEquals(expected.getTower(), actual.getTower());
        assertEquals(expected.getSource(), actual.getSource());
    }
}
