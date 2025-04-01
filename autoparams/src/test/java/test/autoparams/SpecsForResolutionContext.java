package test.autoparams;

import java.util.UUID;

import autoparams.AutoParams;
import autoparams.AutoSource;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.customization.Freeze;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;
import autoparams.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecsForResolutionContext {

    @ParameterizedTest
    @AutoSource
    void resolve_has_guard_clause(ResolutionContext sut) {
        assertThatThrownBy(() -> sut.resolve((ObjectQuery) null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @AutoSource
    void resolve_correctly_returns_generated_value(int value) {
        ResolutionContext sut = new ResolutionContext(
            (query, context) -> new ObjectContainer(value),
            ObjectProcessor.DEFAULT
        );

        Object actual = sut.resolve(int.class);

        assertThat(actual).isEqualTo(value);
    }

    @Test
    @AutoParams
    void resolve_correctly_processes_TypeReference(
        @Freeze UUID value,
        ResolutionContext sut
    ) {
        GenericBag<UUID> actual = sut.resolve(
            new TypeReference<GenericBag<UUID>>() { }
        );

        assertThat(actual).isNotNull();
        assertThat(actual.getValue()).isEqualTo(value);
    }

    @SuppressWarnings("ConstantConditions")
    @ParameterizedTest
    @AutoSource
    void applyCustomizer_has_guard_clause_for_customizer(
        ResolutionContext sut
    ) {
        assertThatThrownBy(() -> sut.applyCustomizer((Customizer) null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("ConstantConditions")
    @ParameterizedTest
    @AutoSource
    void applyCustomizer_has_guard_clause_for_generator(
        ResolutionContext sut
    ) {
        assertThatThrownBy(() -> sut.applyCustomizer((ObjectGenerator) null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("ConstantConditions")
    @ParameterizedTest
    @AutoSource
    void applyCustomizer_has_guard_clause_for_processor(
        ResolutionContext sut
    ) {
        assertThatThrownBy(() -> sut.applyCustomizer((ObjectProcessor) null))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @AutoSource
    void applyCustomizer_correctly_replaces_generator(int value1, int value2) {
        ResolutionContext sut = new ResolutionContext(
            (query, context) -> new ObjectContainer(value1),
            ObjectProcessor.DEFAULT
        );

        sut.applyCustomizer((query, context) -> new ObjectContainer(value2));

        Object actual = sut.resolve(int.class);
        assertThat(actual).isEqualTo(value2);
    }
}
