package autoparams;

import static java.util.Arrays.stream;

import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.stream.Stream;
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
        return getCustomizations(new ArrayList<>(), annotated);
    }

    private static Stream<Customization> getCustomizations(
        ArrayList<AnnotatedElement> visited,
        AnnotatedElement annotated
    ) {
        if (visited.contains(annotated)) {
            return Stream.empty();
        }

        Annotation[] annotations = annotated.getAnnotations();
        visited.add(annotated);
        return stream(annotations).flatMap(annotation -> getCustomizations(visited, annotation));
    }

    private static Stream<Customization> getCustomizations(
        ArrayList<AnnotatedElement> visited,
        Annotation annotation
    ) {
        return annotation instanceof Customization
            ? Stream.of((Customization) annotation)
            : getCustomizations(visited, annotation.annotationType());
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
