package org.javaunit.autoparams.autofixture;

import static java.util.Arrays.stream;

import java.lang.reflect.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.javaunit.autoparams.autofixture.generators.DataGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

public final class AutoDataProvider implements ArgumentsProvider,
    AnnotationConsumer<AutoData> {

    private static final Stream<Arguments> EMPTY = stream(new Arguments[0]);

    private static final List<DataGenerator> dataGenerators = BasicSupportGenerators.dataGenerators;

    private int repeat = 1;

    public AutoDataProvider() { }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return context.getTestMethod().map(this::generate).orElse(EMPTY);
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
        Object[] arguments = stream(parameters)
            .map(this::createArgument).toArray();
        return Arguments.of(arguments);
    }

    private Object createArgument(Parameter parameter) {
        AutoParam autoParam = parameter.getAnnotation(AutoParam.class);

        if(autoParam != null) {
            Class<? extends DataGenerator> generator = autoParam.generator();
            try {
                DataGenerator dataGenerator = generator.getConstructor().newInstance();
                return dataGenerator.generate(parameter.getType(), null).orElse(null);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

        } else {
            Class<?> type = parameter.getType();

            // TODO refactor this
            ParameterizedType tempParameterizedType = null;
            try {
                tempParameterizedType = (ParameterizedType) parameter.getParameterizedType();
            } catch (ClassCastException e) {
                // ignore
            }
            final ParameterizedType parameterizedType = tempParameterizedType;
            Optional<Object> optional = dataGenerators.stream()
                .filter(x -> x.isSupport(type))
                .map(x -> x.generate(type, parameterizedType))
                .findFirst().orElse(null);
            return optional.orElse(null);
        }


    }

    @Override
    public void accept(AutoData autoData) {
        this.repeat = autoData.repeat();
    }
}
