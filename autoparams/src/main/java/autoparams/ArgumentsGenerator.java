package autoparams;

import autoparams.customization.AnnotationVisitor;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import autoparams.generator.ObjectQuery;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
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

    public Stream<? extends Arguments> generateArguments(ExtensionContext context) {
        customizeGenerator(context);
        return generate(context.getRequiredTestMethod());
    }

    private void customizeGenerator(ExtensionContext context) {
        Consumer<Customizer> visitor = this.context::customizeGenerator;
        visitCustomizers(context.getRequiredTestMethod(), visitor);
        Customizers.visitCustomizers(context, visitor);
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
            if (annotation instanceof Customization) {
                Customization customization = (Customization) annotation;
                getCustomizers(customization).forEach(visitor);
            }

            if (annotationType.isAnnotationPresent(CustomizerSource.class)) {
                CustomizerSource source = annotationType.getAnnotation(CustomizerSource.class);
                Class<? extends CustomizerFactory> factoryType = source.value();
                visitor.accept(createCustomizer(factoryType, annotation));
            }

            visitCustomizers(annotationType, visitor, onProcessing);
        }
    }

    private static Stream<Customizer> getCustomizers(Customization customization) {
        return stream(customization.value()).map(ArgumentsGenerator::createInstance);
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
        Consumer<Customizer> visitor = context::customizeGenerator;
        visitCustomizers(parameter, visitor);
        Object argument = context.generate(ObjectQuery.fromParameter(parameter));
        Customizers.processArgument(parameter, argument).forEach(visitor);
        return argument;
    }
}
