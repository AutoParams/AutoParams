package org.javaunit.autoparams;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Stream;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectQuery;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

final class ArgumentsGenerator {

    private final ObjectGenerationContext context;
    private final int repeat;

    public ArgumentsGenerator(ObjectGenerationContext context, int repeat) {
        this.context = context;
        this.repeat = repeat;
    }

    public Stream<? extends Arguments> generateArguments(ExtensionContext context) {
        customizeGenerator(context);
        return generate(context.getRequiredTestMethod());
    }

    private void customizeGenerator(ExtensionContext context) {
        customizeGenerator(context.getRequiredTestMethod());
        Customizers.getCustomizers(context).forEach(this.context::customizeGenerator);
    }

    private void customizeGenerator(AnnotatedElement annotated) {
        Customization customization = annotated.getAnnotation(Customization.class);
        if (customization != null) {
            customizeGenerator(customization);
        }
    }

    private void customizeGenerator(Customization customization) {
        for (Class<? extends Customizer> customizerType : customization.value()) {
            customizeGenerator(customizerType);
        }
    }

    private void customizeGenerator(Class<? extends Customizer> customizerType) {
        try {
            context.customizeGenerator(customizerType.getDeclaredConstructor().newInstance());
        } catch (Exception exception) {
            return;
        }
    }

    private Stream<Arguments> generate(Method method) {
        Arguments[] streamSource = new Arguments[repeat];
        for (int i = 0; i < streamSource.length; i++) {
            streamSource[i] = createArguments(method);
        }

        return Arrays.stream(streamSource);
    }

    private Arguments createArguments(Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] arguments = Arrays.stream(parameters).map(this::createArgument).toArray();
        return Arguments.of(arguments);
    }

    private Object createArgument(Parameter parameter) {
        customizeGenerator(parameter);

        Object argument = context.generate(ObjectQuery.fromParameter(parameter));

        if (parameter.isAnnotationPresent(Fixed.class)) {
            context.customizeGenerator(new FixCustomization(parameter.getType(), argument));
        }

        return argument;
    }

}
