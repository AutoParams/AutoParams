package test.autoparams;

import autoparams.CsvAutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpecsForCsvAutoSource {

    @ParameterizedTest
    @CsvAutoSource({ "1, foo" })
    void sut_correctly_fills_arguments(int value1, String value2) {
        assertEquals(1, value1);
        assertEquals("foo", value2);
    }
}
