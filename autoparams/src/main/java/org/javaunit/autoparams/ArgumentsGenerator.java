package org.javaunit.autoparams;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apiguardian.api.API;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectQuery;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
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
        Optional<Customization> customization = findAnnotation(annotated);
        customization.ifPresent(this::customizeGenerator);
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

    private static Optional<Customization> findAnnotation(AnnotatedElement annotated) {
        return getDescendantAnnotations(annotated.getAnnotations())
            .filter(Customization.class::isInstance)
            .map(Customization.class::cast)
            .findFirst();
    }

    private static Stream<Annotation> getDescendantAnnotations(Annotation[] annotations) {
        List<Annotation> annotationsToSearch = ListOfBuiltInAnnotationsToSkipSearch
            .removeAnnotationsToSkipSearch(annotations);
        return Stream.concat(
            annotationsToSearch.stream(),
            annotationsToSearch.stream()
                .map(a -> a.annotationType().getAnnotations())
                .flatMap(ArgumentsGenerator::getDescendantAnnotations)
        );
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
        Stream<Object> arguments = Arrays.stream(parameters).map(this::createThenProcessArgument);
        return Arguments.of(arguments.toArray());
    }

    private Object createThenProcessArgument(Parameter parameter) {
        customizeGenerator(parameter);
        Object argument = context.generate(ObjectQuery.fromParameter(parameter));
        Customizers.processArgument(parameter, argument).forEach(context::customizeGenerator);
        return argument;
    }

    private static class ListOfBuiltInAnnotationsToSkipSearch {
        private static final Class<?>[] annotationList = {
            Target.class,
            API.class,
            Retention.class,
            Documented.class,
            ParameterizedTest.class,
        };

        public static List<Annotation> removeAnnotationsToSkipSearch(Annotation[] annotations) {
            return Arrays.stream(annotations)
                .filter(ListOfBuiltInAnnotationsToSkipSearch::doesNotContain)
                .collect(Collectors.toList());
        }

        private static boolean doesNotContain(Annotation annotation) {
            return Arrays.stream(annotationList)
                .noneMatch(annotation.annotationType()::equals);
        }
    }

}
