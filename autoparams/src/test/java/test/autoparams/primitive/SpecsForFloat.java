package test.autoparams.primitive;

import autoparams.Repeat;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectQuery;
import java.lang.reflect.Parameter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpecsForFloat {

    @AutoParameterizedTest
    void sut_creates_arbitrary_float_values(
        float value1,
        float value2,
        float value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Float_values(
        Float value1,
        Float value2,
        Float value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_value_between_zero_and_one(
        float value
    ) {
        assertThat(value).isBetween(0.0f, 1.0f);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_max_constraint_for_float(@Max(100) float value) {
        assertThat(value).isLessThanOrEqualTo(100.0f);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_generates_non_positive_value_if_max_constraint_is_non_positive(
        @Max(0) float arg
    ) {
        assertThat(arg).isNotPositive();
    }

    @AutoParameterizedTest
    void sut_accepts_min_constraint_for_float(@Min(100) float value) {
        assertThat(value).isGreaterThanOrEqualTo(100.0f);
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_less_than_min_constraint(
        ResolutionContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintLessThanMinConstraint", float.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void maxConstraintLessThanMinConstraint(@Min(0) @Max(-1) float arg) {
    }
}
