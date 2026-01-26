package test.autoparams;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.Repeat;
import autoparams.ResolutionContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpecsForBigDecimal {

    @AutoParameterizedTest
    void sut_creates_arbitrary_BigDecimal_values(
        BigDecimal value1,
        BigDecimal value2,
        BigDecimal value3
    ) {
        assertNotEquals(value1, value2);
        assertNotEquals(value2, value3);
        assertNotEquals(value3, value1);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_BigDecimal_value(BigDecimal arg) {
        assertThat(arg).isPositive();
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_max_constraint_for_BigDecimal(@Max(100) BigDecimal value) {
        assertThat(value).isLessThanOrEqualTo(BigDecimal.valueOf(100));
    }

    @AutoParameterizedTest
    void sut_includes_max_value_for_BigDecimal(
        @Min(Long.MAX_VALUE) @Max(Long.MAX_VALUE) BigDecimal arg
    ) {
        assertThat(arg).isEqualTo(BigDecimal.valueOf(Long.MAX_VALUE));
    }

    @AutoParameterizedTest
    void sut_generates_non_positive_value_if_max_constraint_is_non_positive(
        @Max(0) BigDecimal arg
    ) {
        assertThat(arg).isLessThanOrEqualTo(BigDecimal.ZERO);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_min_constraint_for_BigDecimal(@Min(100) BigDecimal value) {
        assertThat(value).isGreaterThanOrEqualTo(BigDecimal.valueOf(100));
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_less_than_min_constraint(
        ResolutionContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintLessThanMinConstraint", BigDecimal.class)
            .getParameters()[0];
        ObjectQuery query = new ParameterQuery(
            parameter,
            0,
            parameter.getAnnotatedType().getType()
        );
        assertThrows(RuntimeException.class, () -> context.resolve(query));
    }

    void maxConstraintLessThanMinConstraint(@Min(100) @Max(99) BigDecimal arg) {
    }
}
