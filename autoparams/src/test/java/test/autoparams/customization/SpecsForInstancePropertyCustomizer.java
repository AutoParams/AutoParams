package test.autoparams.customization;

import java.util.UUID;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import autoparams.customization.InstancePropertyCustomizer;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.HasGenericSetters;
import test.autoparams.HasSetter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpecsForInstancePropertyCustomizer {

    @ParameterizedTest
    @AutoSource
    @Customization(InstancePropertyCustomizer.class)
    void sut_sets_property(HasSetter bag) {
        assertThat(bag.getValue()).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    @Customization(InstancePropertyCustomizer.class)
    void sut_sets_generic_properties(HasGenericSetters<UUID, HasSetter> bag) {
        assertThat(bag.getValue2()).isInstanceOf(UUID.class);
        assertThat(bag.getValue3()).isInstanceOf(HasSetter.class);
    }

    @ParameterizedTest
    @AutoSource
    @Customization(InstancePropertyCustomizer.class)
    public void sut_sets_nested_generics_property(
        HasGenericSetters<String, HasGenericSetters<UUID, HasSetter>> bag
    ) {
        assertNotNull(bag);
        assertThat(bag.getValue2()).isInstanceOf(String.class);
        assertThat(bag.getValue3()).isInstanceOf(HasGenericSetters.class);
        assertThat(bag.getValue3().getValue3()).isInstanceOf(HasSetter.class);
    }
}
