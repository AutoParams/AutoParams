package autoparams;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

final class MethodAutoArgumentsProvider implements
    ArgumentsProvider,
    AnnotationConsumer<MethodAutoSource> {

    private final ArgumentsProvider methodProvider;
    private final AutoArgumentsProvider autoProvider;

    public MethodAutoArgumentsProvider() {
        methodProvider = ArgumentsProviderCreator.createProvider(MethodSource.class);
        autoProvider = new AutoArgumentsProvider();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return ArgumentsAssembler.assembleArguments(context, methodProvider, autoProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(MethodAutoSource annotation) {
        ((AnnotationConsumer<MethodSource>) methodProvider).accept(createDelegate(annotation));
    }

    private static MethodSource createDelegate(MethodAutoSource source) {
        return new MethodSource() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return MethodSource.class;
            }

            @Override
            public String[] value() {
                return source.value();
            }
        };
    }

}
