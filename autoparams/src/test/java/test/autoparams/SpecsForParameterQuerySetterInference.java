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
    void toLog_returns_parameter_name_as_is_when_parameter_name_is_synthetic_and_method_is_not_setter() throws Exception {
        Method nonSetter = NonSetterTarget.class.getMethod("notSetter", String.class);
        Parameter parameter = nonSetter.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(parameter, 0, String.class);

        String actual = sut.toLog(false);

        // Since this is not a setter method, it should keep the synthetic parameter name (e.g., "arg0")
        assertThat(actual).isEqualTo("String arg0");
    }

    @Test
    void toLog_infers_property_name_from_setter_method_name_when_setter_name_is_single_uppercase_character() throws Exception {
        Method setter = EdgeCaseUser.class.getMethod("setX", String.class);
        Parameter parameter = setter.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(parameter, 0, String.class);

        String actual = sut.toLog(false);

        // Should infer "x" from "setX"
        assertThat(actual).isEqualTo("String x");
    }

    @Test
    void toLog_infers_property_name_from_setter_method_name_when_setter_name_contains_uppercase_acronym() throws Exception {
        Method setter = EdgeCaseUser.class.getMethod("setURL", String.class);
        Parameter parameter = setter.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(parameter, 0, String.class);

        String actual = sut.toLog(false);

        // Should decapitalize only the first character: "URL" -> "uRL"
        assertThat(actual).isEqualTo("String uRL");
    }

    @Test
    void toLog_does_not_infer_property_name_when_method_name_starts_with_set_but_fourth_character_is_not_uppercase() throws Exception {
        Method setupMethod = InvalidSetterUser.class.getMethod("setup", String.class);
        Parameter setupParameter = setupMethod.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(setupParameter, 0, String.class);

        String actual = sut.toLog(false);

        // "setup" is not a valid setter because the fourth character is not uppercase
        assertThat(actual).isEqualTo("String arg0");
    }

    @Test
    void toLog_does_not_infer_property_name_when_method_has_more_than_one_parameter() throws Exception {
        Method multiParamMethod = InvalidSetterUser.class.getMethod("setAge", String.class, String.class);
        Parameter multiParam = multiParamMethod.getParameters()[0];
        ParameterQuery sut = new ParameterQuery(multiParam, 0, String.class);

        String actual = sut.toLog(false);

        // Method has more than one parameter, so it should not be treated as a setter
        assertThat(actual).isEqualTo("String arg0");
    }

    @Setter
    public static class User {
        private String name;
        private String userName;
        private Integer age;
    }

    public static class NonSetterTarget {
        public void notSetter(String value) {
        }
    }

    public static class EdgeCaseUser {
        public void setX(String value) {
        }

        public void setURL(String value) {
        }
    }

    public static class InvalidSetterUser {
        public void setup(String value) {
        }

        public void setAge(String a, String b) {
        }
    }
}

