package test.autoparams.lombok;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import autoparams.lombok.BuilderCustomizer;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForBuilderCustomizer {

    @ParameterizedTest
    @AutoSource
    @Customization(BuilderCustomizer.class)
    void sut_creates_instance_using_builder(HasBuilder arg) {
        assertThat(arg.getId()).isNotNull();
        assertThat(arg.getName()).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    @Customization(ConfiguredBuilderCustomizer.class)
    void sut_creates_instance_using_configured_builder(HasConfiguredBuilder arg) {
        assertThat(arg.getId()).isNotNull();
        assertThat(arg.getName()).isNotNull();
    }
}
