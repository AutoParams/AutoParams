package test.autoparams;

import autoparams.AutoSource;
import autoparams.generator.Builder;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecsForFailures {

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_object_of_interface_is_requested(
        Builder<Cloneable> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("interface");
    }

    public abstract static class AbstractClass {
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_object_of_abstract_class_is_requested(
        Builder<AbstractClass> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("abstract");
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_when_object_of_abstract_class_with_public_constructor_is_requested(
        Builder<AbstractWithPublicConstructor> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("abstract");
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_throws_with_message_containing_type_information(
        Builder<Cloneable> builder
    ) {
        assertThatThrownBy(builder::build)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("java.lang.Cloneable");
    }
}
