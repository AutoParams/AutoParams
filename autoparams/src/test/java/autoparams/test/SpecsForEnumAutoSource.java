package autoparams.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import autoparams.EnumAutoSource;
import java.lang.annotation.ElementType;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class SpecsForEnumAutoSource {

    @ParameterizedTest
    @EnumAutoSource(ElementType.class)
    void sut_correctly_fills_first_argument(ElementType value) {
    }

    @ParameterizedTest
    @EnumAutoSource(ElementType.class)
    void sut_arbitrarily_generates_remaining_arguments(
        ElementType value1,
        UUID value2,
        UUID value3
    ) {
        assertNotNull(value2);
        assertNotNull(value3);
        assertNotEquals(value2, value3);
    }

    @ParameterizedTest
    @EnumAutoSource(value = ElementType.class, names = { "TYPE" })
    void sut_accepts_names(ElementType value) {
        assertEquals(ElementType.TYPE, value);
    }

    @ParameterizedTest
    @EnumAutoSource(value = ElementType.class, names = { "TYPE" }, mode = EnumSource.Mode.EXCLUDE)
    void sut_accepts_mode(ElementType value) {
        assertNotEquals(ElementType.TYPE, value);
    }

}
