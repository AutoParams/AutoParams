package test.autoparams.primitive;

import java.lang.reflect.Parameter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.Repeat;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectQuery;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpecsForDouble {

    @AutoParameterizedTest
    void sut_creates_arbitrary_double_values(
        double value1,
        double value2,
        double value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Double_values(
        Double value1,
        Double value2,
        Double value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_value_between_zero_and_one(
        double value
    ) {
        assertThat(value).isBetween(0.0, 1.0);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_max_constraint_for_double(@Max(100) double value) {
        assertThat(value).isLessThanOrEqualTo(100.0);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_generates_non_positive_value_if_max_constraint_is_non_positive(
        @Max(0) double arg
    ) {
        assertThat(arg).isNotPositive();
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_min_constraint_for_double(@Min(100) double value) {
        assertThat(value).isGreaterThanOrEqualTo(100.0);
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_less_than_min_constraint(
        ResolutionContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintLessThanMinConstraint", double.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.resolve(query));
    }

    @SuppressWarnings("unused")
    void maxConstraintLessThanMinConstraint(@Min(100) @Max(0) double arg) {
    }
}
