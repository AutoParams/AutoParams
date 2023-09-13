package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;
import autoparams.Repeat;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import java.lang.reflect.Parameter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpecsForLong {

    @AutoParameterizedTest
    void sut_creates_arbitrary_long_values(
        long value1,
        long value2,
        long value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Long_values(
        Long value1,
        Long value2,
        Long value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_long_value(long arg) {
        assertThat(arg).isPositive();
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_max_constraint_for_long(@Max(100) long value) {
        assertThat(value).isLessThanOrEqualTo(100);
    }

    @AutoParameterizedTest
    void sut_includes_max_value_for_long(
        @Min(Long.MAX_VALUE) @Max(Long.MAX_VALUE) long arg
    ) {
        assertThat(arg).isEqualTo(Long.MAX_VALUE);
    }

    @AutoParameterizedTest
    void sut_generates_non_positive_value_if_max_constraint_is_non_positive(
        @Max(0) long arg
    ) {
        assertThat(arg).isLessThanOrEqualTo(0);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_min_constraint_for_long(@Min(100) long value) {
        assertThat(value).isGreaterThanOrEqualTo(100);
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_less_than_min_constraint(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintLessThanMinConstraint", long.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void maxConstraintLessThanMinConstraint(@Min(100) @Max(99) long arg) {
    }
}
