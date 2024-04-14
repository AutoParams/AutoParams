package test.autoparams.customization;

import autoparams.AutoSource;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForCustomizer {

    public static class SystemUnderTest implements Customizer {
    }

    @ParameterizedTest
    @AutoSource
    void customize_with_generator_has_null_guard(SystemUnderTest sut) {
        assertThatThrownBy(() -> sut.customize((ObjectGenerator) null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @AutoSource
    void customize_with_generator_is_identity(
        SystemUnderTest sut,
        ObjectGenerator generator
    ) {
        assertThat(sut.customize(generator)).isSameAs(generator);
    }

    @ParameterizedTest
    @AutoSource
    void customize_with_processor_has_null_guard(SystemUnderTest sut) {
        assertThatThrownBy(() -> sut.customize((ObjectProcessor) null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @AutoSource
    void customize_with_processor_is_identity(
        SystemUnderTest sut,
        ObjectProcessor processor
    ) {
        assertThat(sut.customize(processor)).isSameAs(processor);
    }
}
