package autoparams;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

class EnumAutoArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<EnumAutoSource> {

    private final ArgumentsProvider enumProvider;
    private final AutoArgumentsProvider autoProvider;

    public EnumAutoArgumentsProvider() {
        enumProvider = ArgumentsProviderCreator.createProvider(EnumSource.class);
        autoProvider = new AutoArgumentsProvider();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return ArgumentsAssembler.assembleArguments(context, enumProvider, autoProvider);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void accept(EnumAutoSource annotation) {
        ((AnnotationConsumer<EnumSource>) enumProvider).accept(createDelegate(annotation));
    }

    private EnumSource createDelegate(EnumAutoSource source) {
        return new EnumSource() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return EnumSource.class;
            }

            @Override
            public Class<? extends Enum<?>> value() {
                return source.value();
            }

            @Override
            public String[] names() {
                return source.names();
            }

            @Override
            public Mode mode() {
                return source.mode();
            }
        };
    }
}
