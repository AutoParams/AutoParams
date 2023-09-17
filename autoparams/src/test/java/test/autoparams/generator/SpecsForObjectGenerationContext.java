package test.autoparams.generator;

import autoparams.AutoSource;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecsForObjectGenerationContext {

    @SuppressWarnings("ConstantConditions")
    @ParameterizedTest
    @AutoSource
    void generate_has_guard_clause(ObjectGenerationContext sut) {
        ObjectQuery query = null;
        assertThatThrownBy(() -> sut.generate(query))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @AutoSource
    void generate_correctly_returns_generated_value(ExtensionContext extensionContext, int value) {
        ObjectGenerationContext sut = new ObjectGenerationContext(
            extensionContext,
            (query, context) -> new ObjectContainer(value));

        Object actual = sut.generate(int.class);

        assertThat(actual).isEqualTo(value);
    }

    @SuppressWarnings("ConstantConditions")
    @ParameterizedTest
    @AutoSource
    void customizeGenerator_has_guard_clause(ObjectGenerationContext sut) {
        assertThatThrownBy(() -> sut.customizeGenerator(null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @AutoSource
    void customizeGenerator_correctly_replaces_generator(
        ExtensionContext extensionContext,
        int value1,
        int value2
    ) {
        ObjectGenerationContext sut = new ObjectGenerationContext(
            extensionContext,
            (query, context) -> new ObjectContainer(value1));

        sut.customizeGenerator(generator -> (query, context) -> new ObjectContainer(value2));

        Object actual = sut.generate(int.class);
        assertThat(actual).isEqualTo(value2);
    }

}
