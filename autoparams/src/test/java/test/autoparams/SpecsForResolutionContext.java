package test.autoparams;

import autoparams.AutoSource;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectQuery;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecsForResolutionContext {

    @SuppressWarnings("DataFlowIssue")
    @ParameterizedTest
    @AutoSource
    void resolve_has_guard_clause(ResolutionContext sut) {
        assertThatThrownBy(() -> sut.resolve((ObjectQuery) null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @AutoSource
    void resolve_correctly_returns_generated_value(
        ExtensionContext extensionContext,
        int value
    ) {
        ResolutionContext sut = new ResolutionContext(
            extensionContext,
            (query, context) -> new ObjectContainer(value),
            ObjectProcessor.DEFAULT
        );

        Object actual = sut.resolve(int.class);

        assertThat(actual).isEqualTo(value);
    }

    @Deprecated
    @SuppressWarnings("ConstantConditions")
    @ParameterizedTest
    @AutoSource
    void generate_has_guard_clause(ResolutionContext sut) {
        ObjectQuery query = null;
        assertThatThrownBy(() -> sut.generate(query))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Deprecated
    @ParameterizedTest
    @AutoSource
    void generate_correctly_returns_generated_value(
        ExtensionContext extensionContext,
        int value
    ) {
        ResolutionContext sut = new ResolutionContext(
            extensionContext,
            (query, context) -> new ObjectContainer(value),
            ObjectProcessor.DEFAULT
        );

        Object actual = sut.generate(int.class);

        assertThat(actual).isEqualTo(value);
    }

    @SuppressWarnings("ConstantConditions")
    @ParameterizedTest
    @AutoSource
    void applyCustomizer_has_guard_clause(ResolutionContext sut) {
        assertThatThrownBy(() -> sut.applyCustomizer(null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @AutoSource
    void applyCustomizer_correctly_replaces_generator(
        ExtensionContext extensionContext,
        int value1,
        int value2
    ) {
        ResolutionContext sut = new ResolutionContext(
            extensionContext,
            (query, context) -> new ObjectContainer(value1),
            ObjectProcessor.DEFAULT
        );

        sut.applyCustomizer(
            generator -> (query, context) -> new ObjectContainer(value2));

        Object actual = sut.resolve(int.class);
        assertThat(actual).isEqualTo(value2);
    }
}
