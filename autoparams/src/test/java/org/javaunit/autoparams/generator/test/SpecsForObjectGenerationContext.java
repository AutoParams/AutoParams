package org.javaunit.autoparams.generator.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForObjectGenerationContext {

    @ParameterizedTest
    @AutoSource
    void generate_correctly_returns_generated_value(int value) {
        ObjectGenerationContext sut = new ObjectGenerationContext(
            (query, context) -> new ObjectContainer(value));

        Object actual = sut.generate(() -> int.class);

        assertThat(actual).isEqualTo(value);
    }

    @ParameterizedTest
    @AutoSource
    void customizeGenerator_correctly_replaces_generator(int value1, int value2) {
        ObjectGenerationContext sut = new ObjectGenerationContext(
            (query, context) -> new ObjectContainer(value1));

        sut.customizeGenerator(generator -> (query, context) -> new ObjectContainer(value2));

        Object actual = sut.generate(() -> int.class);
        assertThat(actual).isEqualTo(value2);
    }

}
