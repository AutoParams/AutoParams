package org.javaunit.autoparams.test;

import static org.assertj.core.api.BDDAssertions.then;

import java.net.URL;
import java.util.stream.IntStream;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.Builder;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForUrl {

    @ParameterizedTest
    @AutoSource
    void sut_supports_various_protocols(Builder<URL> builder) {
        long actual = IntStream.range(0, 100)
            .mapToObj(it -> builder.build())
            .map(URL::getProtocol)
            .distinct()
            .count();

        then(actual).isGreaterThanOrEqualTo(3);
    }

}
