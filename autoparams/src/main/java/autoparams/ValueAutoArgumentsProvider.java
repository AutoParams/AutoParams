package autoparams;

import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsAssembler.assembleArguments;
import static autoparams.ArgumentsProviderCreator.createProvider;

public final class ValueAutoArgumentsProvider implements
    ArgumentsProvider,
    AnnotationConsumer<ValueAutoSource> {

    private final ArgumentsProvider valueProvider;
    private final AutoArgumentsProvider autoProvider;

    public ValueAutoArgumentsProvider() {
        valueProvider = createProvider(ValueSource.class);
        autoProvider = new AutoArgumentsProvider();
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return assembleArguments(context, valueProvider, autoProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void accept(ValueAutoSource annotation) {
        ValueSource delegate = (ValueSource) Proxy.newProxyInstance(
            ValueSource.class.getClassLoader(),
            new Class[] { ValueSource.class },
            (proxy, method, args) -> {
                switch (method.getName()) {
                    case "shorts": return annotation.shorts();
                    case "bytes": return annotation.bytes();
                    case "ints": return annotation.ints();
                    case "longs": return annotation.longs();
                    case "floats": return annotation.floats();
                    case "doubles": return annotation.doubles();
                    case "chars": return annotation.chars();
                    case "booleans": return annotation.booleans();
                    case "strings": return annotation.strings();
                    case "classes": return annotation.classes();
                    default: return method.getDefaultValue();
                }
            }
        );
        ((AnnotationConsumer<ValueSource>) valueProvider).accept(delegate);
    }
}
