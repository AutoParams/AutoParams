package org.javaunit.autoparams.autofixture;

import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class AutoParamTest {
    @ParameterizedTest
    @AutoData
    void generate(@AutoParam(generator = HelloWorldDataGenerator.class) String a) {
        assertThat(a).isEqualTo("hello world");
    }
}
