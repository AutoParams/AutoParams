package autoparams;

import autoparams.customization.AnnotationVisitor;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
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
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;

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
        getCustomizers(annotated).forEach(context::customizeGenerator);
    }

    private Stream<Customizer> getCustomizers(AnnotatedElement annotated) {
        return PLATFORM_ELEMENTS.contains(annotated)
            ? Stream.empty()
            : stream(annotated.getAnnotations()).flatMap(this::getCustomizers);
    }

    private Stream<Customizer> getCustomizers(Annotation annotation) {
        return annotation instanceof Customization
            ? getCustomizers((Customization) annotation)
            : concat(
                createCustomizer(annotation),
                getCustomizers(annotation.annotationType())
            );
    }

    private Stream<Customizer> getCustomizers(Customization customization) {
        return stream(customization.value()).map(this::createInstance);
    }

    private Stream<Customizer> createCustomizer(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (annotationType.isAnnotationPresent(CustomizerSource.class)) {
            CustomizerSource source = annotationType.getAnnotation(CustomizerSource.class);
            Class<? extends CustomizerFactory> factoryType = source.value();
            return Stream.of(createCustomizer(factoryType, annotation));
        } else {
            return Stream.empty();
        }
    }

    @SuppressWarnings("unchecked")
    private Customizer createCustomizer(
        Class<? extends CustomizerFactory> factoryType,
        Annotation annotation
    ) {
        CustomizerFactory factory = createInstance(factoryType);
        if (factory instanceof AnnotationVisitor<?>) {
            ((AnnotationVisitor<Annotation>) factory).visit(annotation);
        }
        return factory.createCustomizer();
    }

    private <T> T createInstance(Class<? extends T> type) {
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
        customizeGenerator(parameter);
        Object argument = context.generate(ObjectQuery.fromParameter(parameter));
        Customizers.processArgument(parameter, argument).forEach(context::customizeGenerator);
        return argument;
    }
}
