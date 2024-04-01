package autoparams;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsAssembler.assembleArguments;
import static autoparams.ArgumentsProviderCreator.createProvider;

public final class MethodAutoArgumentsProvider implements
    ArgumentsProvider,
    AnnotationConsumer<MethodAutoSource> {

    private final ArgumentsProvider methodProvider;
    private final AutoArgumentsProvider autoProvider;

    public MethodAutoArgumentsProvider() {
        methodProvider = createProvider(MethodSource.class);
        autoProvider = new AutoArgumentsProvider();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(
        ExtensionContext context
    ) {
        return assembleArguments(context, methodProvider, autoProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(MethodAutoSource annotation) {
        MethodSource delegate = (MethodSource) Proxy.newProxyInstance(
            MethodSource.class.getClassLoader(),
            new Class[] { MethodSource.class },
            (proxy, method, args) -> method.getName().equals("value")
                ? annotation.value()
                : method.getDefaultValue()
        );
        ((AnnotationConsumer<MethodSource>) methodProvider).accept(delegate);
    }
}
