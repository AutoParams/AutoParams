package test.autoparams;

import java.util.UUID;

import autoparams.AutoParams;
import autoparams.AutoSource;
import autoparams.customization.Freeze;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class SpecsForGenerics {

    @ParameterizedTest
    @AutoSource
    void sut_correctly_creates_generic_object(GenericObject<UUID, ComplexObject> value) {
        assertThat(value).isNotNull();
        assertThat(value.getValue2()).isInstanceOf(UUID.class);
        assertThat(value.getValue3()).isInstanceOf(ComplexObject.class);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_creates_nested_generic_object(
        GenericObject<String, GenericObject<UUID, ComplexObject>> value
    ) {
        assertThat(value).isNotNull();
        assertThat(value.getValue2()).isInstanceOf(String.class);
        assertThat(value.getValue3()).isInstanceOf(GenericObject.class);
        assertThat(value.getValue3().getValue3()).isInstanceOf(ComplexObject.class);
    }

    @AllArgsConstructor
    @Getter
    public static class Container<T> {

        private final T value;
    }

    @AllArgsConstructor
    public static class Accessor<T> {

        private final Container<T> container;

        public T getValue() {
            return container.getValue();
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_works_with_generic_argument(
        @Freeze String value,
        Accessor<String> accessor
    ) {
        assertThat(accessor.getValue()).isSameAs(value);
    }

    @Test
    @AutoParams
    void sut_correctly_creates_generic_object_with_wildcard(
        Container<? extends String> container
    ) {
        assertThat(container).isNotNull();
        assertThat(container.getValue()).isInstanceOf(String.class);
    }
}
