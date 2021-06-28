package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectQuery;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForMax {

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_int(@Max(100) int value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_with_big_value_for_int(@Min(0) @Max(0x80000000L) int value) {
        assertThat(value).isGreaterThanOrEqualTo(0);
    }

    @ParameterizedTest
    @AutoSource
    void sut_includes_maximum_value(@Min(1) @Max(1) int value) {
        assertEquals(1, value);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_double(@Max(100) double value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_long(@Max(100) long value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_int_when_over_lower_bound(@Max(Long.MIN_VALUE) int value) {
        assertThat(value).isEqualTo(Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
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
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_short(@Max(100) short value) {
        assertThat(value).isLessThanOrEqualTo((short) 100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_short_when_over_lower_bound(
        @Max(Long.MIN_VALUE) short value) {
        assertThat(value).isEqualTo(Short.MIN_VALUE);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_max_constraint_for_short_when_over_upper_bound(
        @Max(Long.MAX_VALUE) short value) {
        assertThat(value).isLessThanOrEqualTo(Short.MAX_VALUE);
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

}
