package org.javaunit.autoparams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;

import org.junit.jupiter.params.ParameterizedTest;

public class AutoSourceSpecs {

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

}
