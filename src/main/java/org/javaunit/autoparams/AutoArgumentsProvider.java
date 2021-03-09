package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AutoArgumentsProvider implements ArgumentsProvider {

    private static final CompositeObjectGenerator primetiveValueGenerator = new CompositeObjectGenerator(
            new BooleanGenerator(), new IntegerGenerator(), new FloatGenerator(),
            new DoubleGenerator());

    private static final CompositeObjectGenerator simpleValueObjectGenerator = new CompositeObjectGenerator(
            new BigDecimalGenerator(), new StringGenerator(), new UUIDGenerator());

    private static final Stream<Arguments> empty = stream(new Arguments[0]);

    private final ObjectGenerator generator = new CompositeObjectGenerator(new ComplexObjectGenerator(),
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
        ObjectGenerationContext context = new ObjectGenerationContext(generator);
        return generator.generate(ObjectQuery.create(parameter), context).orElse(null);
    }
}
