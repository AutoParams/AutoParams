package autoparams.test;

import autoparams.ValueAutoSource;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecsForValueAutoSource {

    @ParameterizedTest
    @ValueAutoSource(shorts = {16})
    void sut_correctly_fills_short_argument(short value) {
        assertEquals(16, value);
    }

    @ParameterizedTest
    @ValueAutoSource(bytes = {16})
    void sut_correctly_fills_byte_argument(byte value) {
        assertEquals(16, value);
    }

    @ParameterizedTest
    @ValueAutoSource(ints = {16})
    void sut_correctly_fills_int_argument(int value) {
        assertEquals(16, value);
    }

    @ParameterizedTest
    @ValueAutoSource(longs = {16})
    void sut_correctly_fills_long_argument(long value) {
        assertEquals(16, value);
    }

    @ParameterizedTest
    @ValueAutoSource(floats = {16})
    void sut_correctly_fills_float_argument(float value) {
        assertEquals(16, value);
    }

    @ParameterizedTest
    @ValueAutoSource(doubles = {16})
    void sut_correctly_fills_double_argument(double value) {
        assertEquals(16, value);
    }

    @ParameterizedTest
    @ValueAutoSource(chars = {'f'})
    void sut_correctly_fills_char_argument(char value) {
        assertEquals('f', value);
    }

    @ParameterizedTest
    @ValueAutoSource(booleans = {true})
    void sut_correctly_fills_boolean_argument(boolean value) {
        assertTrue(value);
    }

    @ParameterizedTest
    @ValueAutoSource(strings = {"foo"})
    void sut_correctly_fills_string_argument(String value) {
        assertEquals("foo", value);
    }

    @ParameterizedTest
    @ValueAutoSource(classes = {String.class})
    void sut_correctly_fills_class_argument(Class<?> value) {
        assertEquals(String.class, value);
    }

    @ParameterizedTest
    @ValueAutoSource(ints = {16})
    void sut_arbitrarily_generates_remaining_arguments(int value1, UUID value2, UUID value3) {
        assertNotNull(value2);
        assertNotNull(value3);
        assertNotEquals(value2, value3);
    }
}
