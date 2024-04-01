package test.autoparams;

import autoparams.AutoSource;
import autoparams.generator.Factory;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecsForFailures {

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_object_of_interface_is_requested(
        Factory<Cloneable> factory
    ) {
        assertThatThrownBy(factory::get)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("interface");
    }

    public abstract static class AbstractClass {
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_object_of_abstract_class_is_requested(
        Factory<AbstractClass> factory
    ) {
        assertThatThrownBy(factory::get)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("abstract");
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_object_of_abstract_class_with_public_constructor_is_requested(
        Factory<AbstractWithPublicConstructor> factory
    ) {
        assertThatThrownBy(factory::get)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("abstract");
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_with_message_containing_type_information(
        Factory<Cloneable> factory
    ) {
        assertThatThrownBy(factory::get)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("java.lang.Cloneable");
    }
}
