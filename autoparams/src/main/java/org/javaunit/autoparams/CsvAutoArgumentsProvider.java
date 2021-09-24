package org.javaunit.autoparams;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class CsvAutoArgumentsProvider implements
    ArgumentsProvider,
    AnnotationConsumer<CsvAutoSource> {

    private final ArgumentsProvider csvProvider;
    private final AutoArgumentsProvider autoProvider;

    public CsvAutoArgumentsProvider() {
        csvProvider = ArgumentsProviderCreator.createProvider(CsvSource.class);
        autoProvider = new AutoArgumentsProvider();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return ArgumentsAssembler.assembleArguments(context, csvProvider, autoProvider);
    }

    private static CsvSource createDelegate(CsvAutoSource source) {
        return new CsvSource() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return CsvSource.class;
            }

            @Override
            public String[] value() {
                return source.value();
            }

            @Override
            public char delimiter() {
                return '\0';
            }

            @Override
            public String delimiterString() {
                return "";
            }

            @Override
            public String emptyValue() {
                return "";
            }

            @Override
            public String[] nullValues() {
                return new String[] {};
            }

            @Override
            public int maxCharsPerColumn() {
                return 4096;
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(CsvAutoSource annotation) {
        ((AnnotationConsumer<CsvSource>) csvProvider).accept(createDelegate(annotation));
    }

}
