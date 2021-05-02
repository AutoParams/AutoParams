package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.stream.Stream;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.customization.Customizer;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class AutoArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<AutoSource> {

    private static class GeneratorAdapter implements ObjectGenerator {
        public org.javaunit.autoparams.generator.ObjectGenerator generator;

        public GeneratorAdapter() {
            generator = org.javaunit.autoparams.generator.ObjectGenerator.DEFAULT;
        }

        @Override
        public GenerationResult generate(ObjectQuery query, ObjectGenerationContext context) {
            org.javaunit.autoparams.generator.ObjectGenerationContext generationContext =
                new org.javaunit.autoparams.generator.ObjectGenerationContext(generator);

            return GenerationResult.fromContainer(
                generator
                    .generate(
                        () -> query instanceof GenericObjectQuery
                            ? ((GenericObjectQuery) query).getParameterizedType()
                            : query.getType(),
                        generationContext));
        }
    }

    private final GeneratorAdapter adapter = new GeneratorAdapter();
    private final CompositeObjectGenerator generator =
        new CompositeObjectGenerator(
            adapter,
            new BuilderGenerator());
    private ObjectGenerationContext context = new ObjectGenerationContext(generator, this::fix);
    private int repeat = 1;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        Method method = context.getRequiredTestMethod();
        customizeGenerator(method);
        applyFixedValues(context);
        return generate(method);
    }

    private void customizeGenerator(Method method) {
        Customization customization = method.getAnnotation(Customization.class);
        if (customization != null) {
            Class<? extends Customizer>[] types = customization.value();
            for (Class<? extends Customizer> type : types) {
                try {
                    Customizer customizer = type.getDeclaredConstructor().newInstance();
                    adapter.generator = customizer.customize(adapter.generator);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    return;
                }
            }
        }
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
        Object argument = null;
        try {
            org.javaunit.autoparams.generator.ObjectGenerationContext generationContext =
                new org.javaunit.autoparams.generator.ObjectGenerationContext(adapter.generator);

            argument = adapter.generator
                .generate(
                    org.javaunit.autoparams.generator.ObjectQuery.fromParameter(parameter),
                    generationContext)
                .unwrapOrElseThrow();
        } catch (Exception exception) {
            ObjectQuery query = ObjectQuery.create(parameter);
            argument = context.generate(query);
        }

        if (parameter.isAnnotationPresent(Fixed.class)) {
            context.fix(parameter.getType(), argument);
            fix(parameter.getType(), argument);
        }

        return argument;
    }

    private void fix(Class<?> type, Object value) {
        org.javaunit.autoparams.generator.ObjectGenerator generator = adapter.generator;
        adapter.generator = (query, context) -> {
            if (query.getType() == type) {
                return new org.javaunit.autoparams.generator.ObjectContainer(value);
            }

            return generator.generate(query, context);
        };
    }

    @Override
    public void accept(AutoSource annotation) {
        repeat = annotation.repeat();
    }

}
