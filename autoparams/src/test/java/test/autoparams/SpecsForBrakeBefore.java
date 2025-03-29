package test.autoparams;

import java.lang.reflect.Parameter;
import java.util.UUID;
import java.util.function.Predicate;

import autoparams.AutoSource;
import autoparams.BrakeBefore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForBrakeBefore {

    public static class StringPredicate implements Predicate<Parameter> {

        @Override
        public boolean test(Parameter parameter) {
            return parameter.getType().equals(String.class);
        }
    }

    public static class StringExtension implements ParameterResolver {

        @Override
        public boolean supportsParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext
        ) throws ParameterResolutionException {
            return new StringPredicate().test(parameterContext.getParameter());
        }

        @Override
        public Object resolveParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext
        ) throws ParameterResolutionException {
            return "hello world";
        }
    }

    @ParameterizedTest
    @AutoSource
    @BrakeBefore(StringPredicate.class)
    @ExtendWith(StringExtension.class)
    void sut_correctly_stops_generating_parameters(
        UUID arg1,
        int arg2,
        String arg3
    ) {
        assertThat(arg3).isEqualTo("hello world");
    }
}
