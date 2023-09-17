package test.autoparams;

import autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

class SpecsForConstructorResolving {

    @ParameterizedTest
    @AutoSource
    void sut_selects_constructor_with_the_fewest_parameters(HasMultipleConstructors container) {
        assertThat(container.getValue()).isNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_selects_constructor_decorated_with_ConstructorProperties(
        DecoratedWithConstructorProperties container) {

        assertThat(container.getValue()).isNotNull();
    }

}
