package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AutoArgumentsProvider implements ArgumentsProvider {

    private static final CompositeArgumentGenerator primetiveValueGenerator = new CompositeArgumentGenerator(
            new BooleanArgumentGenerator(), new IntegerArgumentGenerator(), new FloatArgumentGenerator(),
            new DoubleArgumentGenerator());

    private static final CompositeArgumentGenerator simpleValueObjectGenerator = new CompositeArgumentGenerator(
            new BigDecimalArgumentGenerator(), new StringArgumentGenerator(), new UUIDArgumentGenerator());

    private static final Stream<Arguments> empty = stream(new Arguments[0]);

    private final ArgumentGenerator generator = new CompositeArgumentGenerator(new ComplexObjectArgumentGenerator(),
            primetiveValueGenerator, simpleValueObjectGenerator);

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return context.getTestMethod().map(method -> createArguments(method)).orElse(empty);
    }

    private Stream<Arguments> createArguments(Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] arguments = stream(parameters).map(this::createArgument).toArray();
        return stream(new Arguments[] { Arguments.of(arguments) });
    }

    private Object createArgument(Parameter parameter) {
        ArgumentGenerationContext context = new ArgumentGenerationContext(generator);
        return generator.generate(parameter, context).orElse(null);
    }
}
