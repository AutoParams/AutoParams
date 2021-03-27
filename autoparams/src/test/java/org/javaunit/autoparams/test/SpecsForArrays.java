package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForArrays {

    @ParameterizedTest
    @AutoSource
    void sut_correctly_creates_array_of_int(int[] array) {
        assertThat(array).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_fills_array_of_int_with_arbitrary_values(int[] array) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }

        assertThat(set).hasSize(array.length);
    }

    @ParameterizedTest
    @AutoSource
    void sut_fills_array_of_Integer_with_arbitrary_values(Integer[] array) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }

        assertThat(set).hasSize(array.length);
    }

    @ParameterizedTest
    @AutoSource
    void sut_fills_array_of_complex_type_with_arbitrary_objects(ComplexObject[] array) {
        HashSet<ComplexObject> set = new HashSet<ComplexObject>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }

        assertThat(set).hasSize(array.length);
    }

    @ParameterizedTest
    @AutoSource(repeat = 10)
    void sut_fills_array_of_enum_type_with_arbitrary_objects(EnumType[] array) {
        HashSet<EnumType> set = new HashSet<>();
        for (int i = 0; i < array.length; i++) {
            set.add(array[i]);
        }

        assertThat(set.size()).isGreaterThanOrEqualTo(1);
        assertThat(set.size()).isLessThanOrEqualTo(EnumType.values().length);
    }

}
