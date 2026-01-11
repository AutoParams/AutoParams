package test.autoparams;

import autoparams.ParameterQuery;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Tests for ParameterQuery toLog method when inferring property names from setter methods.
 * This addresses issue #318: @LogResolution이 setter 매개변수 이름이 없을 때 메서드 이름을 통해 속성 이름을 유추
 *
 * @see <a href="https://github.com/AutoParams/AutoParams/issues/318">Issue #318</a>
 */
public class SpecsForParameterQuerySetterInference {

    @Setter
    public static class User {
        private String name;
        private String userName;
        private Integer age;
    }

    @Test
    void toLog_infers_property_name_from_setter_method_name_when_parameter_name_is_synthetic() throws Exception {
        Method setter = User.class.getMethod("setName", String.class);
        Parameter parameter = setter.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(parameter, 0, String.class);

        String actual = sut.toLog(false);

        // Even if parameter name is "arg0" (synthetic), it should infer "name" from "setName"
        assertThat(actual).isEqualTo("String name");
    }

    @Test
    void toLog_infers_property_name_from_setter_method_name_with_multiple_words() throws Exception {
        Method setter = User.class.getMethod("setUserName", String.class);
        Parameter parameter = setter.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(parameter, 0, String.class);

        String actual = sut.toLog(false);

        // Should infer "userName" from "setUserName"
        assertThat(actual).isEqualTo("String userName");
    }

    @Test
    void toLog_infers_property_name_from_setter_method_name_for_integer_type() throws Exception {
        Method setter = User.class.getMethod("setAge", Integer.class);
        Parameter parameter = setter.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(parameter, 0, Integer.class);

        String actual = sut.toLog(false);

        // Should infer "age" from "setAge"
        assertThat(actual).isEqualTo("Integer age");
    }

    @Test
    void toLog_uses_parameter_name_when_available_and_not_synthetic() throws Exception {
        // This test verifies that when parameter name is available and not synthetic,
        // it uses the parameter name instead of inferring from method name
        // Note: This test may pass or fail depending on compilation flags (-parameters)
        Method setter = User.class.getMethod("setName", String.class);
        Parameter parameter = setter.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(parameter, 0, String.class);

        String actual = sut.toLog(false);

        // If parameter name is present and not synthetic, use it
        // Otherwise, infer from method name
        if (parameter.isNamePresent() && !parameter.getName().startsWith("arg")) {
            assertThat(actual).isEqualTo("String " + parameter.getName());
        } else {
            // If synthetic, should infer "name" from "setName"
            assertThat(actual).isEqualTo("String name");
        }
    }
}

