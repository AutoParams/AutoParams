package autoparams;

import java.lang.reflect.Proxy;
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
    public Stream<? extends Arguments> provideArguments(
        ExtensionContext context
    ) {
        return ArgumentsAssembler.assembleArguments(
            context,
            csvProvider,
            autoProvider
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(CsvAutoSource annotation) {
        CsvSource delegate = (CsvSource) Proxy.newProxyInstance(
            CsvSource.class.getClassLoader(),
            new Class[] { CsvSource.class },
            (proxy, method, args) -> method.getName().equals("value")
                ? annotation.value()
                : method.getDefaultValue()
        );
        ((AnnotationConsumer<CsvSource>) csvProvider).accept(delegate);
    }
}
