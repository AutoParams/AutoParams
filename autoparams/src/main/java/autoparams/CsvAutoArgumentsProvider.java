package autoparams;

import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsProviderCreator.createArgumentsProvider;

/**
 * An implementation of {@link ArgumentsProvider} that processes
 * {@link CsvAutoSource} annotations and combines CSV-formatted test data with
 * automatic parameter generation.
 * <p>
 * This provider acts as a bridge between JUnit 5's standard {@link CsvSource}
 * functionality and AutoParams' automatic test data generation. It enables the
 * use of explicit values for some parameters while automatically generating
 * values for any remaining parameters.
 * </p>
 *
 * <p>
 * The provider works by:
 * </p>
 * <ol>
 *   <li>Processing the {@link CsvAutoSource} annotation</li>
 *   <li>
 *       Converting it to a {@link CsvSource} for JUnit to handle CSV parsing
 *   </li>
 *   <li>
 *       Delegating to {@link AutoArgumentsProvider} to fill in missing
 *       parameters
 *   </li>
 * </ol>
 *
 * @see CsvAutoSource
 * @see AutoArgumentsProvider
 * @see CsvSource
 */
public final class CsvAutoArgumentsProvider
    extends AutoArgumentsProvider
    implements AnnotationConsumer<CsvAutoSource> {

    private final AnnotationConsumer<CsvSource> annotationConsumer;

    @SuppressWarnings("unused")
    public CsvAutoArgumentsProvider() {
        this(createArgumentsProvider(CsvSource.class));
    }

    @SuppressWarnings("unchecked")
    private CsvAutoArgumentsProvider(ArgumentsProvider assetProvider) {
        super(assetProvider);
        annotationConsumer = (AnnotationConsumer<CsvSource>) assetProvider;
    }

    /**
     * Accepts and processes a {@link CsvAutoSource} annotation.
     * <p>
     * This method converts the {@link CsvAutoSource} annotation to an
     * equivalent {@link CsvSource} annotation and passes it to the internal
     * annotation consumer. This allows the CSV data to be properly parsed while
     * maintaining compatibility with AutoParams' automatic parameter
     * generation.
     * </p>
     *
     * @param annotation the {@link CsvAutoSource} annotation to process
     */
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
