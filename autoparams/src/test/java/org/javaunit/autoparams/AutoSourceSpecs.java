package org.javaunit.autoparams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.AbstractList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.params.ParameterizedTest;

public class AutoSourceSpecs {

    @ParameterizedTest
    @AutoSource
    void sut_creates_builder_generate_integer_array(Builder<Integer[]> builder) {
        Integer[] value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_builder_generate_integer_list(Builder<List<Integer>> builder) {
        List<Integer> value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_builder_generate_complex_type_array(Builder<ComplexObject[]> builder) {
        ComplexObject[] value = builder.build();
        assertThat(value).hasSize(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_builder_generate_complex_type_map(
        Builder<Map<String, ComplexObject>> builder
    ) {
        Map<String, ComplexObject> value = builder.build();
        assertThat(value).isNotEmpty();
    }

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_interface_is_requested(
        Builder<Cloneable> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(ObjectGenerationException.class)
            .hasMessageContaining("interface");
    }

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_abstract_class_is_requested(
        Builder<AbstractList<Object>> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(ObjectGenerationException.class)
            .hasMessageContaining("abstract");
    }

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_abstract_class_with_public_constructor_is_requested(
        Builder<AbstractWithPublicConstructor> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(ObjectGenerationException.class)
            .hasMessageContaining("abstract");
    }

}
