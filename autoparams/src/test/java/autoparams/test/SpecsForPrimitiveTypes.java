package autoparams.test;

import autoparams.AutoSource;
import java.util.HashSet;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class SpecsForPrimitiveTypes {

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_boolean_values(
        boolean value1,
        boolean value2,
        boolean value3,
        boolean value4,
        boolean value5,
        boolean value6,
        boolean value7,
        boolean value8,
        boolean value9,
        boolean value10,
        boolean value11,
        boolean value12,
        boolean value13,
        boolean value14,
        boolean value15,
        boolean value16,
        boolean value17,
        boolean value18,
        boolean value19,
        boolean value20
    ) {
        HashSet<Boolean> set = new HashSet<Boolean>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        set.add(value6);
        set.add(value7);
        set.add(value8);
        set.add(value9);
        set.add(value10);
        set.add(value11);
        set.add(value12);
        set.add(value13);
        set.add(value14);
        set.add(value15);
        set.add(value16);
        set.add(value17);
        set.add(value18);
        set.add(value19);
        set.add(value20);
        assertThat(set).hasSize(2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_Boolean_values(
        Boolean value1,
        Boolean value2,
        Boolean value3,
        Boolean value4,
        Boolean value5,
        Boolean value6,
        Boolean value7,
        Boolean value8,
        Boolean value9,
        Boolean value10,
        Boolean value11,
        Boolean value12,
        Boolean value13,
        Boolean value14,
        Boolean value15,
        Boolean value16,
        Boolean value17,
        Boolean value18,
        Boolean value19,
        Boolean value20
    ) {
        HashSet<Boolean> set = new HashSet<Boolean>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        set.add(value6);
        set.add(value7);
        set.add(value8);
        set.add(value9);
        set.add(value10);
        set.add(value11);
        set.add(value12);
        set.add(value13);
        set.add(value14);
        set.add(value15);
        set.add(value16);
        set.add(value17);
        set.add(value18);
        set.add(value19);
        set.add(value20);
        assertThat(set).hasSize(2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_byte_values(
        byte value1,
        byte value2,
        byte value3,
        byte value4,
        byte value5,
        byte value6,
        byte value7,
        byte value8,
        byte value9,
        byte value10
    ) {
        HashSet<Byte> set = new HashSet<Byte>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        set.add(value6);
        set.add(value7);
        set.add(value8);
        set.add(value9);
        set.add(value10);
        assertThat(set.size()).isGreaterThan(3);
    }

    @ParameterizedTest
    @AutoSource()
    void sut_creates_arbitrary_Byte_values(
        Byte value1,
        Byte value2,
        Byte value3,
        Byte value4,
        Byte value5,
        Byte value6,
        Byte value7,
        Byte value8,
        Byte value9,
        Byte value10
    ) {
        HashSet<Byte> set = new HashSet<Byte>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        set.add(value6);
        set.add(value7);
        set.add(value8);
        set.add(value9);
        set.add(value10);
        assertThat(set.size()).isGreaterThan(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_char_values(
        char value1,
        char value2,
        char value3,
        char value4,
        char value5
    ) {
        HashSet<Character> set = new HashSet<Character>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_Character_values(
        Character value1,
        Character value2,
        Character value3,
        Character value4,
        Character value5
    ) {
        HashSet<Character> set = new HashSet<Character>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_short_values(
        short value1,
        short value2,
        short value3,
        short value4,
        short value5
    ) {
        HashSet<Short> set = new HashSet<Short>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_Short_values(
        Short value1,
        Short value2,
        Short value3,
        Short value4,
        Short value5
    ) {
        HashSet<Short> set = new HashSet<Short>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_int_values(int value1, int value2, int value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_Integer_values(Integer value1, Integer value2, Integer value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_long_values(long value1, long value2, long value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_Long_values(Long value1, Long value2, Long value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_float_values(float value1, float value2, float value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_Float_values(Float value1, Float value2, Float value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_double_values(double value1, double value2, double value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_Double_values(Double value1, Double value2, Double value3) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

}
