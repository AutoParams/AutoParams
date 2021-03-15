package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

public final class AutoArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<AutoSource> {

    private static final Stream<Arguments> EMPTY = stream(new Arguments[0]);

    private static final ObjectGenerator PRIMITIVE_VALUE_GENERATOR = new CompositeObjectGenerator(
            new BooleanGenerator(), new IntegerGenerator(), new LongGenerator(), new FloatGenerator(),
            new DoubleGenerator());

    private static final ObjectGenerator SIMPLE_VALUE_OBJECT_GENERATOR = new CompositeObjectGenerator(
            new BigDecimalGenerator(), new StringGenerator(), new UUIDGenerator(), new EnumGenerator());

    private static final ObjectGenerator COLLECTION_GENERATOR = new CompositeObjectGenerator(new ArrayGenerator(),
            new CollectionGenerator(), new StreamGenerator(), new MapGenerator(), new SetGenerator());

    public static final CompositeObjectGenerator DEFAULT_OBJECT_GENERATOR = new CompositeObjectGenerator(
            PRIMITIVE_VALUE_GENERATOR, SIMPLE_VALUE_OBJECT_GENERATOR, COLLECTION_GENERATOR,
            new ComplexObjectGenerator());

    private final ObjectGenerator generator;
    private int repeat;

    public AutoArgumentsProvider() {
        this(DEFAULT_OBJECT_GENERATOR);
    }

    private AutoArgumentsProvider(ObjectGenerator generator) {
        this.generator = generator;
        repeat = 1;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
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
        Object[] arguments = stream(parameters).map(this::createArgument).toArray();
        return Arguments.of(arguments);
    }

    private Object createArgument(Parameter parameter) {
        ObjectQuery query = ObjectQuery.create(parameter);
        ObjectGenerationContext context = new ObjectGenerationContext(generator);
        return generator.generate(query, context).orElse(null);
    }

    @Override
    public void accept(AutoSource annotation) {
        repeat = annotation.repeat();
    }

}
