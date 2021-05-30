package org.javaunit.autoparams;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
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
        Method method = context.getRequiredTestMethod();
        customizeGenerator(method);
        applyFixedValues(context);
        return generate(method);
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

    private void applyFixedValues(ExtensionContext context) {
        applyFixedValues(FixedValueAccessor.entries(context));
    }

    private void applyFixedValues(Iterable<Entry<Class<?>, Object>> fixedValues) {
        for (Map.Entry<Class<?>, Object> entry : fixedValues) {
            fix(entry.getKey(), entry.getValue());
        }
    }

    private void fix(Class<?> type, Object value) {
        context.customizeGenerator(new FixCustomization(type, value));
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
            fix(parameter.getType(), argument);
        }

        return argument;
    }

}
