package autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;

import autoparams.AutoSource;
import autoparams.Builder;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.params.ParameterizedTest;

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
