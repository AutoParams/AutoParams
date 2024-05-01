package test.autoparams;

import autoparams.AutoSource;
import autoparams.generator.UseEmailAddressGenerationOptions;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForUseEmailAddressGenerationOptions {

    public record Properties(String email) { }

    @ParameterizedTest
    @AutoSource
    @UseEmailAddressGenerationOptions(domains = "my.domain.com")
    void sut_correctly_applies_domains(Properties properties) {
        String regex = "^[A-Za-z0-9+_.-]+@my\\.domain\\.com$";
        assertThat(properties.email().matches(regex)).isTrue();
    }

    @ParameterizedTest
    @AutoSource
    @UseEmailAddressGenerationOptions
    void sut_has_appropriate_default_domains(Properties properties) {
        String regex = "^[A-Za-z0-9+_.-]+@test\\.com$";
        assertThat(properties.email().matches(regex)).isTrue();
    }
}
