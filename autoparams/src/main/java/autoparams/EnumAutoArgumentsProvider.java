package autoparams;

import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsProviderCreator.createArgumentsProvider;

public final class EnumAutoArgumentsProvider
    extends AutoArgumentsProvider
    implements AnnotationConsumer<EnumAutoSource> {

    private final AnnotationConsumer<EnumSource> annotationConsumer;

    @SuppressWarnings("unused")
    public EnumAutoArgumentsProvider() {
        this(createArgumentsProvider(EnumSource.class));
    }

    @SuppressWarnings("unchecked")
    private EnumAutoArgumentsProvider(ArgumentsProvider seedProvider) {
        super(seedProvider);
        annotationConsumer = (AnnotationConsumer<EnumSource>) seedProvider;
    }

    @Override
    public void accept(EnumAutoSource annotation) {
        annotationConsumer.accept((EnumSource) Proxy.newProxyInstance(
            EnumSource.class.getClassLoader(),
            new Class[] { EnumSource.class },
            (proxy, method, args) -> {
                switch (method.getName()) {
                    case "annotationType": return EnumSource.class;
                    case "value": return annotation.value();
                    case "names": return annotation.names();
                    case "mode": return annotation.mode();
                    default: return method.getDefaultValue();
                }
            }
        ));
    }
}
