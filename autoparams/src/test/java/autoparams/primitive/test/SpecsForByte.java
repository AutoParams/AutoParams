package autoparams.primitive.test;

import autoparams.AutoParameterizedTest;
import autoparams.Repeat;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
    void sut_creates_positive_byte_value(byte arg) {
        assertThat(arg).isPositive();
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
    void sut_throws_if_max_constraint_is_excessively_large(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelyLargeMaxConstraint", byte.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelyLargeMaxConstraint(@Max(Byte.MAX_VALUE + 1) byte arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_excessively_small(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelySmallMaxConstraint", byte.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelySmallMaxConstraint(@Max(Byte.MIN_VALUE - 1) byte arg) {
    }

    @AutoParameterizedTest
    @Repeat(10)
    void sut_accepts_min_constraint_for_byte(@Min(100) byte value) {
        assertThat(value).isGreaterThanOrEqualTo((byte) 100);
    }

    @AutoParameterizedTest
    void sut_throws_if_min_constraint_is_excessively_large(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelyLargeMinConstraint", byte.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelyLargeMinConstraint(@Min(Byte.MAX_VALUE + 1) byte arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_min_constraint_is_excessively_small(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("excessivelySmallMinConstraint", byte.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void excessivelySmallMinConstraint(@Min(Byte.MIN_VALUE - 1) byte arg) {
    }

    @AutoParameterizedTest
    void sut_throws_if_max_constraint_is_smaller_than_min_constraint(
        ObjectGenerationContext context
    ) throws NoSuchMethodException {
        Parameter parameter = getClass()
            .getDeclaredMethod("maxConstraintIsSmallerThanMinConstraint", byte.class)
            .getParameters()[0];
        ObjectQuery query = ObjectQuery.fromParameter(parameter);
        assertThrows(IllegalArgumentException.class, () -> context.generate(query));
    }

    void maxConstraintIsSmallerThanMinConstraint(@Min(1) @Max(0) byte arg) {
    }
}
