package org.javaunit.autoparams;

import static org.assertj.core.api.BDDAssertions.then;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;

public class UrlGenerationSpecs {

    @ParameterizedTest
    @AutoSource
    void sut_supports_various_protocols(
        List<URL> x,
        List<URL> y,
        List<URL> z,
        List<URL> l,
        List<URL> m,
        List<URL> n
    ) {
        long actual = Stream.of(x, y, z, l, m, n)
            .flatMap(Collection::stream)
            .map(URL::getProtocol)
            .distinct()
            .count();

        then(actual).isGreaterThanOrEqualTo(3);
    }

}
