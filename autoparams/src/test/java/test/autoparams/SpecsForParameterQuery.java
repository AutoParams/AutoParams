package test.autoparams;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import autoparams.AutoSource;
import autoparams.ParameterQuery;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForParameterQuery {

    @ParameterizedTest
    @AutoSource
    void constructor_correctly_sets_index(
        int index
    ) throws NoSuchMethodException {

        Method method = getClass().getMethod("method", String.class);
        Parameter parameter = method.getParameters()[0];

        ParameterQuery sut = new ParameterQuery(
            parameter,
            index,
            parameter.getAnnotatedType().getType()
        );

        assertThat(sut.getIndex()).isEqualTo(index);
    }

    @SuppressWarnings("unused")
    public void method(String a) {
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void getParameterName_returns_empty_if_no_name_is_present(
        ExtensionContext context,
        String arg
    ) {
        Method method = context.getRequiredTestMethod();
        int index = 1;
        Parameter parameter = method.getParameters()[index];
        ParameterQuery sut = new ParameterQuery(
            parameter,
            index,
            parameter.getAnnotatedType().getType()
        );

        Optional<String> actual = sut.getParameterName();

        assertThat(actual).isEmpty();
    }

    @Getter
    public static class User {

        private final String name;
        private final String email;

        @ConstructorProperties({ "name", "email" })
        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }

    @Test
    void getParameterName_returns_name_if_present_via_ConstructorProperties() {
        Constructor<?> constructor = User.class.getConstructors()[0];
        int index = 1;
        Parameter parameter = constructor.getParameters()[index];
        ParameterQuery sut = new ParameterQuery(
            parameter,
            index,
            parameter.getAnnotatedType().getType()
        );

        Optional<String> actual = sut.getParameterName();

        assertThat(actual).contains("email");
    }

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void getRequiredParameterName_throws_exception_if_no_name_is_present(
        ExtensionContext context,
        String arg
    ) {
        Method method = context.getRequiredTestMethod();
        int index = 1;
        Parameter parameter = method.getParameters()[index];
        ParameterQuery sut = new ParameterQuery(
            parameter,
            index,
            parameter.getAnnotatedType().getType()
        );

        assertThatThrownBy(sut::getRequiredParameterName)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void getRequiredParameterName_returns_name_if_present() {
        Constructor<?> constructor = User.class.getConstructors()[0];
        int index = 1;
        Parameter parameter = constructor.getParameters()[index];
        ParameterQuery sut = new ParameterQuery(
            parameter,
            index,
            parameter.getAnnotatedType().getType()
        );

        String actual = sut.getRequiredParameterName();

        assertThat(actual).isEqualTo("email");
    }
}
