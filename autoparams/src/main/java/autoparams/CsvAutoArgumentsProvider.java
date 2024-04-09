package autoparams;

import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsProviderCreator.createArgumentsProvider;

public final class CsvAutoArgumentsProvider
    extends AutoArgumentsProvider
    implements AnnotationConsumer<CsvAutoSource> {

    private final AnnotationConsumer<CsvSource> annotationConsumer;

    @SuppressWarnings("unused")
    public CsvAutoArgumentsProvider() {
        this(createArgumentsProvider(CsvSource.class));
    }

    @SuppressWarnings("unchecked")
    private CsvAutoArgumentsProvider(ArgumentsProvider seedProvider) {
        super(seedProvider);
        annotationConsumer = (AnnotationConsumer<CsvSource>) seedProvider;
    }

    @Override
    public void accept(CsvAutoSource annotation) {
        annotationConsumer.accept((CsvSource) Proxy.newProxyInstance(
            CsvSource.class.getClassLoader(),
            new Class[] { CsvSource.class },
            (proxy, method, args) -> {
                switch (method.getName()) {
                    case "annotationType": return CsvSource.class;
                    case "value": return annotation.value();
                    case "textBlock": return annotation.textBlock();
                    case "useHeadersInDisplayName":
                        return annotation.useHeadersInDisplayName();
                    case "quoteCharacter": return annotation.quoteCharacter();
                    case "delimiter": return annotation.delimiter();
                    case "delimiterString": return annotation.delimiterString();
                    case "emptyValue": return annotation.emptyValue();
                    case "nullValues": return annotation.nullValues();
                    case "maxCharsPerColumn":
                        return annotation.maxCharsPerColumn();
                    case "ignoreLeadingAndTrailingWhitespace":
                        return annotation.ignoreLeadingAndTrailingWhitespace();
                    default: return method.getDefaultValue();
                }
            }
        ));
    }
}
