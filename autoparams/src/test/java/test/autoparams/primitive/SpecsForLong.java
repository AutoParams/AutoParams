package test.autoparams.primitive;

import java.lang.reflect.Parameter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.AutoSource;
import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.Repeat;
import autoparams.ResolutionContext;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.AutoParameterizedTest;

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

    @ParameterizedTest
    @AutoSource
    @Repeat(100)
    void sut_creates_long_value_in_default_range(
        long value
    ) {
        assertThat(value).isBetween(0L, (long) Short.MAX_VALUE + 1);
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
    void sut_creates_positive_long_value(ResolutionContext context) {
        long value = context.resolve(long.class);
        assertThat(value).isPositive();
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
        ResolutionContext context
    ) throws NoSuchMethodException {

        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintLessThanMinConstraint", long.class)
            .getParameters()[0];

        ObjectQuery query = new ParameterQuery(
            parameter,
            0,
            parameter.getAnnotatedType().getType()
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> context.resolve(query)
        );
    }

    void maxConstraintLessThanMinConstraint(@Min(100) @Max(99) long arg) {
    }
}
