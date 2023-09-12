package autoparams.test;

import autoparams.AutoSource;
import java.util.UUID;
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
        GenericObject<String, GenericObject<UUID, ComplexObject>> value) {
        assertThat(value).isNotNull();
        assertThat(value.getValue2()).isInstanceOf(String.class);
        assertThat(value.getValue3()).isInstanceOf(GenericObject.class);
        assertThat(value.getValue3().getValue3()).isInstanceOf(ComplexObject.class);
    }

}
