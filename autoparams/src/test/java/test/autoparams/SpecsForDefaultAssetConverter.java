package test.autoparams;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import autoparams.DefaultAssetConverter;
import autoparams.ParameterQuery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForDefaultAssetConverter {

    @AllArgsConstructor
    @Getter
    public static class CharBag {

        private final char value;
    }

    @Test
    void sut_fails_to_convert_long_string_to_char() {
        DefaultAssetConverter sut = new DefaultAssetConverter();

        Constructor<?> constructor = CharBag.class.getConstructors()[0];
        Parameter parameter = constructor.getParameters()[0];
        ParameterQuery query = new ParameterQuery(
            parameter,
            0,
            parameter.getParameterizedType()
        );

        String value = "1234567890";
        assertThatThrownBy(() -> sut.convert(query, value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(value);
    }
}
