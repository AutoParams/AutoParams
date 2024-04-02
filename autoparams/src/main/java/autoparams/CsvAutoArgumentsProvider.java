package autoparams;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsAssembler.assembleArguments;
import static autoparams.ArgumentsProviderCreator.createProvider;

public final class CsvAutoArgumentsProvider implements
    ArgumentsProvider,
    AnnotationConsumer<CsvAutoSource> {

    private final ArgumentsProvider csvProvider;
    private final AutoArgumentsProvider autoProvider;

    public CsvAutoArgumentsProvider() {
        csvProvider = createProvider(CsvSource.class);
        autoProvider = new AutoArgumentsProvider();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(
        ExtensionContext context
    ) {
        return assembleArguments(context, csvProvider, autoProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(CsvAutoSource annotation) {
        CsvSource delegate = (CsvSource) Proxy.newProxyInstance(
            CsvSource.class.getClassLoader(),
            new Class[] { CsvSource.class },
            (proxy, method, args) -> {
                switch (method.getName()) {
                    case "value": return annotation.value();
                    case "textBlock": return annotation.textBlock();
                    case "useHeadersInDisplayName": return annotation.useHeadersInDisplayName();
                    case "quoteCharacter": return annotation.quoteCharacter();
                    case "delimiter": return annotation.delimiter();
                    default: return method.getDefaultValue();
                }
            }
        );
        ((AnnotationConsumer<CsvSource>) csvProvider).accept(delegate);
    }
}
