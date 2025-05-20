package autoparams;

import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsProviderCreator.createArgumentsProvider;

/**
 * An implementation of {@link ArgumentsProvider} that processes
 * {@link EnumAutoSource} annotations and combines enum constants with
 * automatic parameter generation.
 * <p>
 * This provider acts as a bridge between JUnit 5's standard {@link EnumSource}
 * functionality and AutoParams' automatic test data generation. It enables the
 * use of enum constants for the first parameter while automatically generating
 * values for any remaining parameters.
 * </p>
 *
 * <p>
 * The provider works by:
 * </p>
 * <ol>
 *   <li>Processing the {@link EnumAutoSource} annotation</li>
 *   <li>
 *       Converting it to an {@link EnumSource} for JUnit to handle enum
 *       constant selection
 *   </li>
 *   <li>
 *       Delegating to {@link AutoArgumentsProvider} to fill in missing
 *       parameters
 *   </li>
 * </ol>
 *
 * @see EnumAutoSource
 * @see AutoArgumentsProvider
 * @see EnumSource
 */
public final class EnumAutoArgumentsProvider
    extends AutoArgumentsProvider
    implements AnnotationConsumer<EnumAutoSource> {

    private final AnnotationConsumer<EnumSource> annotationConsumer;

    /**
     * Constructs a new {@link EnumAutoArgumentsProvider} instance.
     */
    @SuppressWarnings("unused")
    public EnumAutoArgumentsProvider() {
        this(createArgumentsProvider(EnumSource.class));
    }

    @SuppressWarnings("unchecked")
    private EnumAutoArgumentsProvider(ArgumentsProvider assetProvider) {
        super(assetProvider);
        annotationConsumer = (AnnotationConsumer<EnumSource>) assetProvider;
    }

    /**
     * Accepts and processes an {@link EnumAutoSource} annotation.
     * <p>
     * This method converts the {@link EnumAutoSource} annotation to an
     * equivalent {@link EnumSource} annotation and passes it to the internal
     * annotation consumer. This allows the enum constants to be properly
     * selected while maintaining compatibility with AutoParams' automatic
     * parameter generation.
     * </p>
     *
     * @param annotation the {@link EnumAutoSource} annotation to process
     */
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
