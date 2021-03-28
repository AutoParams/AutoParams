package org.javaunit.autoparams.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;
import org.javaunit.autoparams.CsvAutoSource;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForCsvAutoSource {

    @ParameterizedTest
    @CsvAutoSource({"1, foo"})
    void sut_correctly_fills_arguments(int value1, String value2) {
        assertEquals(1, value1);
        assertEquals("foo", value2);
    }

    @ParameterizedTest
    @CsvAutoSource({"1, foo"})
    void sut_correctly_fills_forepart_arguments(int value1, String value2, UUID value3) {
        assertEquals(1, value1);
        assertEquals("foo", value2);
    }

    @ParameterizedTest
    @CsvAutoSource({"1, foo"})
    void sut_arbitrarily_generates_remaining_arguments(
        int value1,
        String value2,
        UUID value3,
        UUID value4
    ) {
        assertNotNull(value3);
        assertNotNull(value4);
        assertNotEquals(value3, value4);
    }

}
