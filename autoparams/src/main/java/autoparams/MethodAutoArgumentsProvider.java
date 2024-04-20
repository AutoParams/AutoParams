package autoparams;

import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsProviderCreator.createArgumentsProvider;

public final class MethodAutoArgumentsProvider
    extends AutoArgumentsProvider
    implements AnnotationConsumer<MethodAutoSource> {

    private final AnnotationConsumer<MethodSource> annotationConsumer;

    @SuppressWarnings("unused")
    public MethodAutoArgumentsProvider() {
        this(createArgumentsProvider(MethodSource.class));
    }

    @SuppressWarnings("unchecked")
    public MethodAutoArgumentsProvider(ArgumentsProvider assetProvider) {
        super(assetProvider);
        annotationConsumer = (AnnotationConsumer<MethodSource>) assetProvider;
    }

    @Override
    public void accept(MethodAutoSource annotation) {
        annotationConsumer.accept((MethodSource) Proxy.newProxyInstance(
            MethodSource.class.getClassLoader(),
            new Class[] { MethodSource.class },
            (proxy, method, args) -> {
                switch (method.getName()) {
                    case "annotationType": return MethodSource.class;
                    case "value": return annotation.value();
                    default: return method.getDefaultValue();
                }
            }
        ));
    }
}
