package autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.ParameterizedTest.DISPLAY_NAME_PLACEHOLDER;

import autoparams.AutoSource;
import autoparams.Repeat;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.junit.jupiter.params.ParameterizedTest;

public class SpecsForMin {

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_min_constraint_for_int(@Min(100) int value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_min_constraint_for_double(@Min(100) @Max(Short.MAX_VALUE) double value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_accept_min_constraint_without_max_constraint_for_double(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod(
            "hasDoubleParameterWithOnlyMinAnnotation",
            double.class
        );
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        assertThatThrownBy(() -> context.generate(query))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(
                "The parameter annotated with @Min is missing the required"
                + " @Max annotation. Please annotate the parameter with"
                + " both @Min and @Max annotations to specify"
                + " the minimum and maximum allowed values.");
    }

    void hasDoubleParameterWithOnlyMinAnnotation(@Min(100) double arg) {
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_min_constraint_for_long(@Min(100) long value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_min_constraint_for_int_when_over_upper_bound(@Min(Long.MAX_VALUE) int value) {
        assertThat(value).isEqualTo(Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_min_constraint_for_int_when_over_lower_bound(@Min(Long.MIN_VALUE) int value) {
        assertThat(value).isGreaterThanOrEqualTo(Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_min_constraint_with_max_value_for_long(@Min(Long.MAX_VALUE) long value) {
        assertEquals(Long.MAX_VALUE, value);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
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

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_accepts_min_constraint_for_byte(@Min(100) byte value) {
        assertThat(value).isGreaterThanOrEqualTo((byte) 100);
    }

    @ParameterizedTest
    @AutoSource()
    void sut_throws_when_min_constraint_of_byte_argument_is_greater_than_upper_bound(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod(
            "byteMinConstraintGreaterThanUpperBound",
            byte.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void byteMinConstraintGreaterThanUpperBound(@Min(Byte.MAX_VALUE + 1) byte arg) {
    }

    @ParameterizedTest
    @AutoSource()
    void sut_throws_when_min_constraint_of_byte_argument_is_less_than_lower_bound(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod(
            "byteMinConstraintLessThanLowerBound",
            byte.class);
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void byteMinConstraintLessThanLowerBound(@Min(Byte.MIN_VALUE - 1) byte arg) {
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(100)
    void sut_accepts_min_constraint_for_float(@Min(100) @Max(Short.MAX_VALUE) float value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_accept_min_constraint_without_max_constraint_for_float(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Method method = getClass().getDeclaredMethod(
            "hasFloatParameterWithOnlyMinAnnotation",
            float.class
        );
        Parameter parameter = method.getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);

        assertThatThrownBy(() -> context.generate(query))
            .isInstanceOf(RuntimeException.class)
            .hasMessage(
                "The parameter annotated with @Min is missing the required"
                + " @Max annotation. Please annotate the parameter with"
                + " both @Min and @Max annotations to specify"
                + " the minimum and maximum allowed values.");
    }

    void hasFloatParameterWithOnlyMinAnnotation(@Min(100) float arg) {
    }
}
