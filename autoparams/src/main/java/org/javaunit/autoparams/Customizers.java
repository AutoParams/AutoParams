package org.javaunit.autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.javaunit.autoparams.customization.AnnotationVisitor;
import org.javaunit.autoparams.customization.ArgumentProcessing;
import org.javaunit.autoparams.customization.ArgumentProcessor;
import org.javaunit.autoparams.customization.Customizer;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

final class Customizers {

    private static final Namespace NAMESPACE = Namespace.create(new Object());

    static void addCustomizer(ExtensionContext context, Customizer customizer) {
        getStore(context).add(customizer);
    }

    static Stream<Customizer> getCustomizers(ExtensionContext context) {
        return getStore(context).stream();
    }

    @SuppressWarnings("unchecked")
    private static List<Customizer> getStore(ExtensionContext context) {
        return (List<Customizer>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
            Customizers.class,
            k -> new ArrayList<Customizer>(),
            List.class);
    }

    static Stream<Customizer> processArgument(Parameter parameter, Object argument) {
        return Arrays
            .stream(parameter.getAnnotations())
            .flatMap(annotation -> Arrays
                .stream(annotation.annotationType().getAnnotationsByType(ArgumentProcessing.class))
                .map(ArgumentProcessing::value)
                .map(Customizers::createProcessor)
                .map(processor -> visitAnnotation(processor, annotation)))
            .map(processor -> processor.process(parameter, argument));
    }

    private static <T extends ArgumentProcessor> T createProcessor(Class<? extends T> type) {
        try {
            Constructor<? extends T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException
            | IllegalAccessException
            | IllegalArgumentException
            | InvocationTargetException
            | NoSuchMethodException
            | SecurityException exception) {
            throw new RuntimeException(exception);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation> ArgumentProcessor visitAnnotation(
        ArgumentProcessor processor,
        T annotation
    ) {
        if (processor instanceof AnnotationVisitor<?>) {
            ((AnnotationVisitor<T>) processor).visit(annotation);
        }

        return processor;
    }

}
