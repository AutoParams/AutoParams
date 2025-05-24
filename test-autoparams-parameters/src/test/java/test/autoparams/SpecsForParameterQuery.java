package test.autoparams;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import autoparams.AutoParams;
import autoparams.ParameterQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForParameterQuery {

    @Test
    @AutoParams
    void toString_returns_value_that_starts_with_correct_prefix(
        ExtensionContext context,
        String myString
    ) {
        Method method = context.getRequiredTestMethod();
        Parameter parameter = method.getParameters()[1];
        ParameterQuery sut = new ParameterQuery(
            parameter,
            0,
            parameter.getAnnotatedType().getType()
        );

        String actual = sut.toString();

        assertThat(actual).startsWith("Parameter ");
    }

    @Test
    @AutoParams
    void toString_returns_value_that_contains_name_of_parameter(
        ExtensionContext context,
        String myString
    ) {
        Method method = context.getRequiredTestMethod();
        Parameter parameter = method.getParameters()[1];
        ParameterQuery sut = new ParameterQuery(
            parameter,
            0,
            parameter.getAnnotatedType().getType()
        );

        String actual = sut.toString();

        assertThat(actual).contains("myString");
    }

    @Test
    @AutoParams
    void toString_returns_value_that_contains_type_of_parameter(
        ExtensionContext context,
        String myString
    ) {
        Method method = context.getRequiredTestMethod();
        Parameter parameter = method.getParameters()[1];
        ParameterQuery sut = new ParameterQuery(
            parameter,
            0,
            parameter.getAnnotatedType().getType()
        );

        String actual = sut.toString();

        assertThat(actual).contains("java.lang.String");
    }
}
