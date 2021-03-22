package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.Builder;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForBuilderFix {

    @ParameterizedTest
    @AutoSource(repeat = 10)
    void sut_correctly_fixes_int_value(
        int fixedInt, Builder<ComplexObject> builder) {

        ComplexObject actual = builder.fix(int.class, fixedInt).build();
        assertThat(actual.getValue1()).isEqualTo(fixedInt);
    }

    @ParameterizedTest
    @AutoSource(repeat = 10)
    void sut_correctly_fixes_elements_of_array(
        int fixedInt, Builder<int[]> builder) {

        int[] actual = builder.fix(int.class, fixedInt).build();
        for (int fixedElement : actual) {
            assertThat(fixedElement).isEqualTo(fixedInt);
        }
    }

    @ParameterizedTest
    @AutoSource(repeat = 10)
    void sut_separately_fixes_primitive_value_and_boxed_value(
        int fixedInt, Builder<Integer> builder) {

        int actual = builder.fix(int.class, fixedInt).build();
        assertThat(actual).isNotEqualTo(fixedInt);
    }
}
