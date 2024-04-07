package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import autoparams.customization.AnnotationVisitor;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import autoparams.generator.ObjectQuery;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

import static java.util.Arrays.stream;

final class ArgumentsGenerator {

    private final ResolutionContext context;
    private final int repeat;

    public ArgumentsGenerator(ResolutionContext context, int repeat) {
        this.context = context;
        this.repeat = repeat;
    }

    public Stream<? extends Arguments> generateArguments(
        ExtensionContext context
    ) {
        Consumer<Customizer> visitor = this.context::applyCustomizer;
        visitCustomizers(context.getRequiredTestMethod(), visitor);
        Customizers.visitCustomizers(context, visitor);
        return generate(context.getRequiredTestMethod());
    }

    private static void visitCustomizers(
        AnnotatedElement annotated,
        Consumer<Customizer> visitor
    ) {
        visitCustomizers(annotated, visitor, new HashSet<>());
    }

    private static void visitCustomizers(
        AnnotatedElement annotated,
        Consumer<Customizer> visitor,
        Set<Class<? extends Annotation>> onProcessing
    ) {
        for (Annotation annotation : annotated.getAnnotations()) {
            visitCustomizers(annotation, visitor, onProcessing);
        }
    }

    private static void visitCustomizers(
        Annotation annotation,
        Consumer<Customizer> visitor,
        Set<Class<? extends Annotation>> onProcessing
    ) {
        Class<? extends Annotation> annotationType = annotation.annotationType();

        if (onProcessing.add(annotationType)) {
            try {
                if (annotation instanceof Customization) {
                    Customization customization = (Customization) annotation;
                    stream(customization.value())
                        .<Customizer>map(ArgumentsGenerator::createInstance)
                        .forEach(visitor);
                }

                if (annotationType.isAnnotationPresent(CustomizerSource.class)) {
                    Class<? extends CustomizerFactory> factoryType = annotationType
                        .getAnnotation(CustomizerSource.class)
                        .value();
                    visitor.accept(createCustomizer(factoryType, annotation));
                }

                visitCustomizers(annotationType, visitor, onProcessing);
            } finally {
                onProcessing.remove(annotationType);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Customizer createCustomizer(
        Class<? extends CustomizerFactory> factoryType,
        Annotation annotation
    ) {
        CustomizerFactory factory = createInstance(factoryType);
        if (factory instanceof AnnotationVisitor<?>) {
            ((AnnotationVisitor<Annotation>) factory).visit(annotation);
        }
        return factory.createCustomizer();
    }

    private static <T> T createInstance(Class<? extends T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private Stream<Arguments> generate(Method method) {
        Parameter[] parameters = getTargetParameters(method);
        Arguments[] sets = new Arguments[repeat];
        for (int i = 0; i < sets.length; i++) {
            Object[] set = stream(parameters)
                .map(this::createThenProcessArgument)
                .toArray();
            sets[i] = Arguments.of(set);
        }

        return stream(sets);
    }

    private static Parameter[] getTargetParameters(Method method) {
        Parameter[] parameters = method.getParameters();
        return Arrays.copyOf(parameters, countTargetParameter(parameters));
    }

    private static int countTargetParameter(Parameter[] parameters) {
        int count = 0;
        for (Parameter parameter : parameters) {
            if (isPlatformType(parameter.getType())) {
                break;
            }
            count++;
        }
        return count;
    }

    private static boolean isPlatformType(Class<?> parameterType) {
        return parameterType.equals(TestInfo.class)
            || parameterType.equals(TestReporter.class);
    }

    private Object createThenProcessArgument(Parameter parameter) {
        Consumer<Customizer> visitor = context::applyCustomizer;
        visitCustomizers(parameter, visitor);
        ObjectQuery argumentQuery = ObjectQuery.fromParameter(parameter);
        Object argument = context.resolve(argumentQuery);
        Customizers.processArgument(parameter, argument).forEach(visitor);
        return argument;
    }
}
