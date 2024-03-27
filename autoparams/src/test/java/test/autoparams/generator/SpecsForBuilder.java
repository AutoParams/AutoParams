package test.autoparams.generator;

import java.util.List;
import java.util.Map;

import autoparams.AutoSource;
import autoparams.Repeat;
import autoparams.generator.Builder;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.ComplexObject;

import static org.assertj.core.api.Assertions.assertThat;

class SpecsForBuilder {

    @ParameterizedTest
    @AutoSource
    void sut_correctly_generates_integer_array(Builder<Integer[]> builder) {
        Integer[] value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_generates_integer_list(Builder<List<Integer>> builder) {
        List<Integer> value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_generates_array_of_complex_type(Builder<ComplexObject[]> builder) {
        ComplexObject[] value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_generates_map(
        Builder<Map<String, ComplexObject>> builder
    ) {
        Map<String, ComplexObject> value = builder.build();
        assertThat(value).isNotEmpty();
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_correctly_fixes_int_value(
        int fixedInt, Builder<ComplexObject> builder
    ) {

        ComplexObject actual = builder.fix(int.class, fixedInt).build();
        assertThat(actual.getValue1()).isEqualTo(fixedInt);
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_correctly_fixes_elements_of_array(
        int fixedInt, Builder<int[]> builder
    ) {

        int[] actual = builder.fix(int.class, fixedInt).build();
        for (int fixedElement : actual) {
            assertThat(fixedElement).isEqualTo(fixedInt);
        }
    }

    @ParameterizedTest
    @AutoSource
    @Repeat(10)
    void sut_separately_fixes_primitive_value_and_boxed_value(
        int fixedInt, Builder<Integer> builder
    ) {

        int actual = builder.fix(int.class, fixedInt).build();
        assertThat(actual).isNotEqualTo(fixedInt);
    }
}
