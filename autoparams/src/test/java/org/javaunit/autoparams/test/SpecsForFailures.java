package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.AbstractList;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.Builder;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForFailures {

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_interface_is_requested(
        Builder<Cloneable> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("interface");
    }

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_abstract_class_is_requested(
        Builder<AbstractList<Object>> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("abstract");
    }

    @ParameterizedTest
    @AutoSource
    void sut_throws_when_object_of_abstract_class_with_public_constructor_is_requested(
        Builder<AbstractWithPublicConstructor> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("abstract");
    }

}
