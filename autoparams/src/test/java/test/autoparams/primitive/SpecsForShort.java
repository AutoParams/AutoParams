package test.autoparams.primitive;

import java.lang.reflect.Parameter;
import java.util.HashSet;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import autoparams.Repeat;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectQuery;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpecsForShort {

    @AutoParameterizedTest
    void sut_creates_arbitrary_short_values(
        short value1,
        short value2,
        short value3,
        short value4,
        short value5
    ) {
        HashSet<Short> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Short_values(
        Short value1,
        Short value2,
        Short value3,
        Short value4,
        Short value5
    ) {
        HashSet<Short> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        assertThat(set.size()).isGreaterThan(3);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_short_value(short arg) {
        assertThat(arg).isPositive();
    }

    @AutoParameterizedTest
    void sut_accepts_max_constraint_for_short(@Max(100) short value) {
        assertThat(value).isLessThanOrEqualTo((short) 100);
    }

    @AutoParameterizedTest
    void sut_includes_max_value_for_short(
        @Min(Short.MAX_VALUE) @Max(Short.MAX_VALUE) short arg
    ) {
        assertThat(arg).isEqualTo(Short.MAX_VALUE);
    }

    @AutoParameterizedTest
    void sut_generates_non_positive_value_if_max_constraint_is_non_positive(
        @Max(0) short arg
    ) {
        assertThat(arg).isNotPositive();
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_excessively_large(
        ResolutionContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelyLargeMaxConstraint", short.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelyLargeMaxConstraint(@Max(Short.MAX_VALUE + 1) short arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_excessively_small(
        ResolutionContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelySmallMaxConstraint", short.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelySmallMaxConstraint(@Max(Short.MIN_VALUE - 1) short arg) {
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_min_constraint_for_short(@Min(100) short value) {
        assertThat(value).isGreaterThanOrEqualTo((short) 100);
    }

    @AutoParameterizedTest
    void sut_throws_if_min_constraint_is_excessively_large(
        ResolutionContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelyLargeMinConstraint", short.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelyLargeMinConstraint(@Min(Short.MAX_VALUE + 1) short arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_min_constraint_is_excessively_small(
        ResolutionContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelySmallMinConstraint", short.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelySmallMinConstraint(@Min(Short.MIN_VALUE - 1) short arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_less_than_min_constraint(
        ResolutionContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintLessThanMinConstraint", short.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void maxConstraintLessThanMinConstraint(@Min(100) @Max(99) short arg) {
    }
}
