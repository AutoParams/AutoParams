package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.ParameterizedTest.DISPLAY_NAME_PLACEHOLDER;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.validation.constraints.Min;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectQuery;
import org.junit.jupiter.params.ParameterizedTest;

public class SpecsForMin {

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_int(@Min(100) int value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_double(@Min(100) double value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_long(@Min(100) long value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_int_when_over_upper_bound(@Min(Long.MAX_VALUE) int value) {
        assertThat(value).isEqualTo(Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_int_when_over_lower_bound(@Min(Long.MIN_VALUE) int value) {
        assertThat(value).isGreaterThanOrEqualTo(Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_with_max_value_for_long(@Min(Long.MAX_VALUE) long value) {
        assertEquals(Long.MAX_VALUE, value);
    }

    @ParameterizedTest
    @AutoSource(repeat = 100)
    void sut_accepts_min_constraint_for_short(@Min(100) short value) {
        assertThat(value).isGreaterThanOrEqualTo((short) 100);
    }

    @ParameterizedTest(name = DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_min_constraint_is_greater_than_upper_bound(ObjectGenerationContext context)
        throws NoSuchMethodException {

        Method method = getClass().getDeclaredMethod("valueGreaterThanUpperBound", short.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        assertThatThrownBy(() -> context.generate(query))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("greater than the upper bound");
    }

    void valueGreaterThanUpperBound(@Min(Short.MAX_VALUE + 1) short arg) {
    }

    @ParameterizedTest(name = DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_min_constraint_is_less_than_lower_bound(
        ObjectGenerationContext context) throws NoSuchMethodException {

        Method method = getClass().getDeclaredMethod("valueLessThanLowerBound", short.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        assertThatThrownBy(() -> context.generate(query))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("less than the lower bound");
    }

    void valueLessThanLowerBound(@Min(Short.MIN_VALUE - 1) short arg) {
    }

}
