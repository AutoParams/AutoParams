package test.autoparams;

import java.net.URI;

import autoparams.AutoSource;
import autoparams.generator.UseURIGenerationOptions;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForUseURIGenerationOptions {

    @ParameterizedTest
    @AutoSource
    @UseURIGenerationOptions(schemes = "ftp")
    void sut_correctly_applies_schemes(URI uri) {
        assertThat(uri.getScheme()).isEqualTo("ftp");
    }

    @ParameterizedTest
    @AutoSource
    @UseURIGenerationOptions
    void sut_has_appropriate_default_schemes(URI uri) {
        assertThat(uri.getScheme()).isEqualTo("https");
    }

    @ParameterizedTest
    @AutoSource
    @UseURIGenerationOptions(hosts = "my.host.com")
    void sut_correctly_applies_hosts(URI uri) {
        assertThat(uri.getHost()).isEqualTo("my.host.com");
    }

    @ParameterizedTest
    @AutoSource
    @UseURIGenerationOptions
    void sut_has_appropriate_default_hosts(URI uri) {
        assertThat(uri.getHost()).isEqualTo("test.com");
    }

    @ParameterizedTest
    @AutoSource
    @UseURIGenerationOptions(ports = 8080)
    void sut_correctly_applies_ports(URI uri) {
        assertThat(uri.getPort()).isEqualTo(8080);
    }

    @ParameterizedTest
    @AutoSource
    @UseURIGenerationOptions
    void sut_has_appropriate_default_ports(URI uri) {
        assertThat(uri.getPort()).isNegative();
    }
}
