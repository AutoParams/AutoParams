package test.autoparams;

import java.net.URL;
import java.util.stream.IntStream;

import autoparams.AutoSource;
import autoparams.generator.Factory;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.offset;

class SpecsForUrl {

    @ParameterizedTest
    @AutoSource
    void sut_supports_various_protocols(Factory<URL> factory) {
        long actual = IntStream.range(0, 100)
            .mapToObj(it -> factory.get())
            .map(URL::getProtocol)
            .distinct()
            .count();

        assertThat(actual).isGreaterThanOrEqualTo(3);
    }

    @ParameterizedTest
    @AutoSource
    void sut_supports_various_ports(Factory<URL> factory) {
        long actual = IntStream.range(0, 100)
            .mapToObj(it -> factory.get())
            .map(URL::getPort)
            .distinct()
            .count();

        assertThat(actual).isGreaterThanOrEqualTo(10);
    }

    @ParameterizedTest
    @AutoSource
    void sut_generates_urls_with_port_numbers_about_50_percent(
        Factory<URL> factory
    ) {
        long actual = IntStream.range(0, 100)
            .mapToObj(it -> factory.get())
            .filter(it -> it.getPort() >= 0)
            .count();

        assertThat(actual / (double) 100).isEqualTo(0.5, offset(0.25));
    }
}
