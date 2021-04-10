package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.stream.Stream;
import org.javaunit.autoparams.generator.ObjectGenerator;
import org.javaunit.autoparams.generator.UnwrapFailedException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class AutoArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<AutoSource> {

    private static final Stream<Arguments> EMPTY = stream(new Arguments[0]);

    public static final CompositeObjectGenerator DEFAULT_OBJECT_GENERATOR =
        new CompositeObjectGenerator(
            new PrimitiveValueGenerator(),
            new SimpleValueObjectGenerator(),
            new CollectionGenerator(),
            new StreamGenerator(),
            new BuilderGenerator(),
            new ComplexObjectGenerator());

    private final ObjectGenerationContext context;
    private int repeat;

    public AutoArgumentsProvider() {
        this(new ObjectGenerationContext(DEFAULT_OBJECT_GENERATOR));
    }

    private AutoArgumentsProvider(ObjectGenerationContext context) {
        this.context = context;
        this.repeat = 1;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        applyFixedValues(context);
        return context.getTestMethod().map(this::generate).orElse(EMPTY);
    }

    private void applyFixedValues(ExtensionContext context) {
        for (Map.Entry<Class<?>, Object> entry : FixedValueAccessor.entries(context)) {
            this.context.fix(entry.getKey(), entry.getValue());
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
        Object[] arguments = stream(parameters).map(this::createArgument).toArray();
        return Arguments.of(arguments);
    }

    private Object createArgument(Parameter parameter) {
        try {
            org.javaunit.autoparams.generator.ObjectGenerationContext context =
                new org.javaunit.autoparams.generator.ObjectGenerationContext(
                    ObjectGenerator.DEFAULT
                );

            org.javaunit.autoparams.generator.ObjectQuery query =
                org.javaunit.autoparams.generator.ObjectQuery.fromParameter(parameter);

            return context.generate(query);
        } catch (UnwrapFailedException exception) {
            ObjectGenerationContext legacyContext = this.context;
            ObjectQuery query = ObjectQuery.create(parameter);
            Object argument = legacyContext.generate(query);

            if (parameter.isAnnotationPresent(Fixed.class)) {
                legacyContext.fix(parameter.getType(), argument);
            }

            return argument;
        }
    }

    @Override
    public void accept(AutoSource annotation) {
        repeat = annotation.repeat();
    }

}
