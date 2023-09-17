package test.autoparams;

import autoparams.CsvAutoSource;
import autoparams.customization.Fix;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @ParameterizedTest
    @CsvAutoSource({"16, foo"})
    void sut_reuses_values_fixed_by_specified_argument(
        @Fix int value1,
        @Fix String value2,
        ComplexObject value3
    ) {
        assertEquals(16, value3.getValue1());
        assertEquals("foo", value3.getValue2());
    }

}
