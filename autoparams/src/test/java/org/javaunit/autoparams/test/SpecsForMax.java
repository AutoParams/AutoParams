package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.ParameterizedTest.DISPLAY_NAME_PLACEHOLDER;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.Repeat;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectQuery;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForMax {

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_for_int(@Max(100) int value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_with_big_value_for_int(@Min(0) @Max(0x80000000L) int value) {
        assertThat(value).isGreaterThanOrEqualTo(0);
    }

    @ParameterizedTest
    @AutoSource
    void sut_includes_maximum_value(@Min(1) @Max(1) int value) {
        assertEquals(1, value);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_for_double(@Max(100) double value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_for_long(@Max(100) long value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_for_byte(@Max(100) byte value) {
        assertThat(value).isLessThanOrEqualTo((byte) 100);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_for_int_when_over_lower_bound(@Max(Long.MIN_VALUE) int value) {
        assertThat(value).isEqualTo(Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_for_int_when_over_upper_bound(@Max(Long.MAX_VALUE) int value) {
        assertThat(value).isLessThanOrEqualTo(Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @AutoSource
    void sut_includes_max_value(ObjectGenerationContext context) throws NoSuchMethodException {
        // Arrange
        Method method = getClass().getDeclaredMethod("consumeInt", int.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        // Act
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add((Integer) context.generate(query));
        }

        // Assert
        assertThat(values).contains(Integer.MAX_VALUE);
    }

    void consumeInt(@Min(0x7ffffff0) @Max(Integer.MAX_VALUE) int arg) {
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_max_constraint_for_short(@Max(100) short value) {
        assertThat(value).isLessThanOrEqualTo((short) 100);
    }

    @ParameterizedTest(name = DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_max_constraint_is_greater_than_upper_bound(ObjectGenerationContext context)
        throws NoSuchMethodException {

        Method method = getClass().getDeclaredMethod("valueGreaterThanUpperBound", short.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        assertThatThrownBy(() -> context.generate(query))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("greater than the upper bound");
    }

    void valueGreaterThanUpperBound(@Max(Short.MAX_VALUE + 1) short arg) {
    }

    @ParameterizedTest(name = DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_max_constraint_is_less_than_lower_bound(
        ObjectGenerationContext context) throws NoSuchMethodException {

        Method method = getClass().getDeclaredMethod("valueLessThanLowerBound", short.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        assertThatThrownBy(() -> context.generate(query))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("less than the lower bound");
    }

    void valueLessThanLowerBound(@Max(Short.MIN_VALUE - 1) short arg) {
    }

    @ParameterizedTest
    @AutoSource
    void sut_includes_max_value_for_short(
        ObjectGenerationContext context) throws NoSuchMethodException {
        // Arrange
        Method method = getClass().getDeclaredMethod("consumeShort", short.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        // Act
        List<Short> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add((short) context.generate(query));
        }

        // Assert
        assertThat(values).contains(Short.MAX_VALUE);
    }

    void consumeShort(@Min(Short.MAX_VALUE - 1) @Max(Short.MAX_VALUE) short arg) {
    }

    @ParameterizedTest
    @AutoSource
    void sut_includes_max_value_long(ObjectGenerationContext context) throws NoSuchMethodException {
        // Arrange
        Method method = getClass().getDeclaredMethod("consumeLong", long.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        // Act
        List<Long> values = new ArrayList<>();
        for (long i = 0; i < 100; i++) {
            values.add((Long) context.generate(query));
        }

        // Assert
        assertThat(values).contains(Long.MAX_VALUE);
    }

    void consumeLong(@Min(0x7ffffffffffffff0L) @Max(Long.MAX_VALUE) long arg) {

    }

    @ParameterizedTest
    @AutoSource()
    void sut_throws_when_over_upper_bound_of_byte(ObjectGenerationContext context)
        throws NoSuchMethodException {
        // Arrange
        Method method = getClass().getDeclaredMethod("consumeOverUpperBoundByte", byte.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void consumeOverUpperBoundByte(@Max(Byte.MAX_VALUE + 1) byte arg) {
    }

    @ParameterizedTest
    @AutoSource()
    void sut_throws_when_over_lower_bound_of_byte(ObjectGenerationContext context)
        throws NoSuchMethodException {
        // Arrange
        Method method = getClass().getDeclaredMethod("consumeOverLowerBoundByte", byte.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void consumeOverLowerBoundByte(@Max(Byte.MIN_VALUE - 1) byte arg) {
    }

    @ParameterizedTest
    @AutoSource()
    void sut_throws_when_max_lower_than_min_byte(ObjectGenerationContext context)
        throws NoSuchMethodException {
        // Arrange
        Method method = getClass().getDeclaredMethod("consumeMaxLowerThanMinByte", byte.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void consumeMaxLowerThanMinByte(@Min(1) @Max(-1) byte arg) {
    }

    @ParameterizedTest
    @AutoSource
    void sut_includes_max_value_for_byte(ObjectGenerationContext context)
        throws NoSuchMethodException {
        // Arrange
        Method method = getClass().getDeclaredMethod("consumeByte", byte.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        // Act
        List<Byte> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            values.add((Byte) context.generate(query));
        }

        // Assert
        assertThat(values).contains(Byte.MAX_VALUE);
    }

    void consumeByte(@Min(126) @Max(Byte.MAX_VALUE) byte arg) {
    }
}
