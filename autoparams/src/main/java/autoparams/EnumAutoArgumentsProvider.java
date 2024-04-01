package autoparams;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsAssembler.assembleArguments;
import static autoparams.ArgumentsProviderCreator.createProvider;

public final class EnumAutoArgumentsProvider implements
    ArgumentsProvider,
    AnnotationConsumer<EnumAutoSource> {

    private final ArgumentsProvider enumProvider;
    private final AutoArgumentsProvider autoProvider;

    public EnumAutoArgumentsProvider() {
        enumProvider = createProvider(EnumSource.class);
        autoProvider = new AutoArgumentsProvider();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(
        ExtensionContext context
    ) {
        return assembleArguments(context, enumProvider, autoProvider);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void accept(EnumAutoSource annotation) {
        EnumSource delegate = (EnumSource) Proxy.newProxyInstance(
            EnumSource.class.getClassLoader(),
            new Class[] { EnumSource.class },
            (proxy, method, args) -> {
                switch (method.getName()) {
                    case "value": return annotation.value();
                    case "names": return annotation.names();
                    case "mode": return annotation.mode();
                    default: return method.getDefaultValue();
                }
            }
        );
        ((AnnotationConsumer<EnumSource>) enumProvider).accept(delegate);
    }
}
