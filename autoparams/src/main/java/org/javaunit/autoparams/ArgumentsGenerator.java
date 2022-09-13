package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectQuery;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

final class ArgumentsGenerator {

    private static final List<AnnotatedElement> PLATFORM_ELEMENTS = Arrays.asList(
        Documented.class,
        Retention.class,
        Target.class
    );

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
        getCustomizations(annotated).forEach(this::customizeGenerator);
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
            throw new RuntimeException(exception);
        }
    }

    private static Stream<Customization> getCustomizations(AnnotatedElement annotated) {
        return PLATFORM_ELEMENTS.contains(annotated)
            ? Stream.empty()
            : stream(annotated.getAnnotations()).flatMap(ArgumentsGenerator::getCustomizations);
    }

    private static Stream<Customization> getCustomizations(Annotation annotation) {
        return annotation instanceof Customization
            ? Stream.of((Customization) annotation)
            : getCustomizations(annotation.annotationType());
    }

    private Stream<Arguments> generate(Method method) {
        Arguments[] streamSource = new Arguments[repeat];
        for (int i = 0; i < streamSource.length; i++) {
            streamSource[i] = createArguments(method);
        }

        return stream(streamSource);
    }

    private Arguments createArguments(Method method) {
        Parameter[] parameters = method.getParameters();
        Stream<Object> arguments = stream(parameters).map(this::createThenProcessArgument);
        return Arguments.of(arguments.toArray());
    }

    private Object createThenProcessArgument(Parameter parameter) {
        customizeGenerator(parameter);
        Object argument = context.generate(ObjectQuery.fromParameter(parameter));
        Customizers.processArgument(parameter, argument).forEach(context::customizeGenerator);
        return argument;
    }

}
