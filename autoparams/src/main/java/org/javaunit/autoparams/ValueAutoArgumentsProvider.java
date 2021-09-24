package org.javaunit.autoparams;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class ValueAutoArgumentsProvider implements
    ArgumentsProvider,
    AnnotationConsumer<ValueAutoSource> {

    private final ArgumentsProvider valueProvider;
    private final AutoArgumentsProvider autoProvider;

    public ValueAutoArgumentsProvider() {
        valueProvider = ArgumentsProviderCreator.createProvider(ValueSource.class);
        autoProvider = new AutoArgumentsProvider();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return ArgumentsAssembler.assembleArguments(context, valueProvider, autoProvider);
    }

    private static ValueSource createDelegate(ValueAutoSource source) {
        return new ValueSource() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return ValueSource.class;
            }

            @Override
            public short[] shorts() {
                return source.shorts();
            }

            @Override
            public byte[] bytes() {
                return source.bytes();
            }

            @Override
            public int[] ints() {
                return source.ints();
            }

            @Override
            public long[] longs() {
                return source.longs();
            }

            @Override
            public float[] floats() {
                return source.floats();
            }

            @Override
            public double[] doubles() {
                return source.doubles();
            }

            @Override
            public char[] chars() {
                return source.chars();
            }

            @Override
            public boolean[] booleans() {
                return source.booleans();
            }

            @Override
            public String[] strings() {
                return source.strings();
            }

            @Override
            public Class<?>[] classes() {
                return source.classes();
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(ValueAutoSource annotation) {
        ((AnnotationConsumer<ValueSource>) valueProvider).accept(createDelegate(annotation));
    }

}
