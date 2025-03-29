package test.autoparams;

import autoparams.StringConverter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForStringConverter {

    @Test
    void default_converter_fails_to_convert_long_string_to_char() {
        StringConverter sut = StringConverter.DEFAULT;
        String value = "1234567890";

        assertThatThrownBy(() -> sut.convert(value, () -> char.class))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(value);
    }
}
