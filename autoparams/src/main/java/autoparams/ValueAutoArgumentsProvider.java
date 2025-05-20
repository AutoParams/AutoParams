package autoparams;

import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsProviderCreator.createArgumentsProvider;

public final class ValueAutoArgumentsProvider
    extends AutoArgumentsProvider
    implements AnnotationConsumer<ValueAutoSource> {

    private final AnnotationConsumer<ValueSource> annotationConsumer;

    @SuppressWarnings("unused")
    public ValueAutoArgumentsProvider() {
        this(createArgumentsProvider(ValueSource.class));
    }

    @Deprecated
    @SuppressWarnings({ "unchecked", "DeprecatedIsStillUsed" })
    public ValueAutoArgumentsProvider(ArgumentsProvider assetProvider) {
        super(assetProvider);
        annotationConsumer = (AnnotationConsumer<ValueSource>) assetProvider;
    }

    @Override
    public void accept(ValueAutoSource annotation) {
        annotationConsumer.accept((ValueSource) Proxy.newProxyInstance(
            ValueSource.class.getClassLoader(),
            new Class[] { ValueSource.class },
            (proxy, method, args) -> {
                switch (method.getName()) {
                    case "annotationType": return ValueSource.class;
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
        ));
    }
}
