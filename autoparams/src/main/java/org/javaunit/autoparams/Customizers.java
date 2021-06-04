package org.javaunit.autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.javaunit.autoparams.customization.ArgumentProcessing;
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
            .map(Annotation::annotationType)
            .flatMap(type -> Arrays.stream(type.getAnnotationsByType(ArgumentProcessing.class)))
            .map(ArgumentProcessing::value)
            .map(Customizers::createInstance)
            .map(processor -> processor.process(parameter, argument));
    }

    private static <T> T createInstance(Class<? extends T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
            | IllegalAccessException
            | IllegalArgumentException
            | InvocationTargetException
            | NoSuchMethodException
            | SecurityException exception) {
            throw new RuntimeException(exception);
        }
    }

}
