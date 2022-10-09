package autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;

import autoparams.AutoSource;
import java.util.Optional;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForOptional {

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_creates_Optional_argument(Optional<String> arg) {
    }

    @ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER)
    @AutoSource
    void sut_creates_Optional_argument_containing_value(Optional<ComplexObject> arg) {
        assertThat(arg).isNotEmpty();
    }
}
