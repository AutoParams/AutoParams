package test.autoparams;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.UUID;

import autoparams.ParameterQuery;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForParameterQueryUsingName {

    public record User(UUID id, String email) {
    }

    @Test
    void toLog_returns_parameter_type_with_package_and_parameter_name_when_verbose_is_true() {
        Constructor<?> constructor = User.class.getConstructors()[0];
        Parameter parameter = constructor.getParameters()[1];
        ParameterQuery sut = new ParameterQuery(parameter, 1, String.class);

        String actual = sut.toLog(true);

        assertThat(actual).isEqualTo("java.lang.String email");
    }

    @Test
    void toLog_returns_simple_type_name_and_parameter_name_when_verbose_is_false() {
        Constructor<?> constructor = User.class.getConstructors()[0];
        Parameter parameter = constructor.getParameters()[1];
        ParameterQuery sut = new ParameterQuery(parameter, 1, String.class);

        String actual = sut.toLog(false);

        assertThat(actual).isEqualTo("String email");
    }
}
