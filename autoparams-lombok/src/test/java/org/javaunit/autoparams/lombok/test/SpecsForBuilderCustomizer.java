package org.javaunit.autoparams.lombok.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.lombok.BuilderCustomizer;
import org.junit.jupiter.params.ParameterizedTest;

public class SpecsForBuilderCustomizer {

    @ParameterizedTest
    @AutoSource
    @Customization(BuilderCustomizer.class)
    void sut_creates_instance_using_builder(HasBuilder arg) {
        assertThat(arg.getId()).isNotNull();
        assertThat(arg.getName()).isNotNull();
    }
}
