package test.autoparams.primitive;

import java.lang.reflect.Parameter;
import java.util.HashSet;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.Repeat;
import autoparams.ResolutionContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpecsForByte {

    @AutoParameterizedTest
    void sut_creates_arbitrary_byte_values(
        byte value1,
        byte value2,
        byte value3,
        byte value4,
        byte value5,
        byte value6,
        byte value7,
        byte value8,
        byte value9,
        byte value10
    ) {
        HashSet<Byte> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        set.add(value6);
        set.add(value7);
        set.add(value8);
        set.add(value9);
        set.add(value10);
        assertThat(set.size()).isGreaterThan(3);
    }

    @AutoParameterizedTest
    void sut_creates_arbitrary_Byte_values(
        Byte value1,
        Byte value2,
        Byte value3,
        Byte value4,
        Byte value5,
        Byte value6,
        Byte value7,
        Byte value8,
        Byte value9,
        Byte value10
    ) {
        HashSet<Byte> set = new HashSet<>();
        set.add(value1);
        set.add(value2);
        set.add(value3);
        set.add(value4);
        set.add(value5);
        set.add(value6);
        set.add(value7);
        set.add(value8);
        set.add(value9);
        set.add(value10);
        assertThat(set.size()).isGreaterThan(3);
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_creates_positive_byte_value(ResolutionContext context) {
        byte value = context.resolve(byte.class);
        assertThat(value).isPositive();
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_max_constraint_for_byte(@Max(100) byte value) {
        assertThat(value).isLessThanOrEqualTo((byte) 100);
    }

    @AutoParameterizedTest
    void sut_includes_max_value_for_byte(
        @Min(Byte.MAX_VALUE) @Max(Byte.MAX_VALUE) byte arg
    ) {
        assertThat(arg).isEqualTo(Byte.MAX_VALUE);
    }

    @AutoParameterizedTest
    void sut_generates_non_positive_value_if_max_constraint_is_non_positive(
        @Max(0) byte arg
    ) {
        assertThat(arg).isNotPositive();
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_excessively_large(
        ResolutionContext context
    ) throws NoSuchMethodException {

        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelyLargeMaxConstraint", byte.class)
            .getParameters()[0];

        ObjectQuery query = getFirstParameterQuery(parameter);

        assertThrows(
            IllegalArgumentException.class,
            () -> context.resolve(query)
        );
    }

    void excessivelyLargeMaxConstraint(
        @SuppressWarnings("unused") @Max(Byte.MAX_VALUE + 1) byte arg
    ) {
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_excessively_small(
        ResolutionContext context
    ) throws NoSuchMethodException {

        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelySmallMaxConstraint", byte.class)
            .getParameters()[0];

        ObjectQuery query = getFirstParameterQuery(parameter);

        assertThrows(
            IllegalArgumentException.class,
            () -> context.resolve(query)
        );
    }

    void excessivelySmallMaxConstraint(
        @SuppressWarnings("unused") @Max(Byte.MIN_VALUE - 1) byte arg
    ) {
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_min_constraint_for_byte(@Min(100) byte value) {
        assertThat(value).isGreaterThanOrEqualTo((byte) 100);
    }

    @AutoParameterizedTest
    void sut_throws_if_min_constraint_is_excessively_large(
        ResolutionContext context
    ) throws NoSuchMethodException {

        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelyLargeMinConstraint", byte.class)
            .getParameters()[0];

        ObjectQuery query = getFirstParameterQuery(parameter);

        assertThrows(
            IllegalArgumentException.class,
            () -> context.resolve(query)
        );
    }

    void excessivelyLargeMinConstraint(
        @SuppressWarnings("unused") @Min(Byte.MAX_VALUE + 1) byte arg
    ) {
    }

    @AutoParameterizedTest
    void sut_throws_if_min_constraint_is_excessively_small(
        ResolutionContext context
    ) throws NoSuchMethodException {

        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelySmallMinConstraint", byte.class)
            .getParameters()[0];

        ObjectQuery query = getFirstParameterQuery(parameter);

        assertThrows(
            IllegalArgumentException.class,
            () -> context.resolve(query)
        );
    }

    void excessivelySmallMinConstraint(
        @SuppressWarnings("unused") @Min(Byte.MIN_VALUE - 1) byte arg
    ) {
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_less_than_min_constraint(
        ResolutionContext context
    ) throws NoSuchMethodException {

        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintLessThanMinConstraint", byte.class)
            .getParameters()[0];

        ObjectQuery query = getFirstParameterQuery(parameter);

        assertThrows(
            IllegalArgumentException.class,
            () -> context.resolve(query)
        );
    }

    void maxConstraintLessThanMinConstraint(
        @SuppressWarnings("unused") @Min(1) @Max(0) byte arg
    ) {
    }

    private static ObjectQuery getFirstParameterQuery(Parameter parameter) {
        return new ParameterQuery(
            parameter,
            0,
            parameter.getAnnotatedType().getType()
        );
    }
}
