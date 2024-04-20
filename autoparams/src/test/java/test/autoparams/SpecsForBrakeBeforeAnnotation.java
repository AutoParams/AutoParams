package test.autoparams;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Parameter;
import java.util.UUID;

import autoparams.AutoSource;
import autoparams.BrakeBeforeAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForBrakeBeforeAnnotation {

    public static class StringExtension implements ParameterResolver {

        @Override
        public boolean supportsParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext
        ) throws ParameterResolutionException {
            Parameter parameter = parameterContext.getParameter();
            return parameter.getType().equals(String.class);
        }

        @Override
        public Object resolveParameter(
            ParameterContext parameterContext,
            ExtensionContext extensionContext
        ) throws ParameterResolutionException {
            return "hello world";
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ResolveWithExtension {
    }

    @ParameterizedTest
    @AutoSource
    @BrakeBeforeAnnotation(ResolveWithExtension.class)
    @ExtendWith(StringExtension.class)
    void sut_correctly_stops_generating_parameters(
        UUID arg1,
        int arg2,
        @ResolveWithExtension String arg3
    ) {
        assertThat(arg3).isEqualTo("hello world");
    }
}
