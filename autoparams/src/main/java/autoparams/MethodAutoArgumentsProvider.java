package autoparams;

import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsProviderCreator.createArgumentsProvider;

/**
 * An implementation of {@link ArgumentsProvider} that processes
 * {@link MethodAutoSource} annotations and combines method-sourced test data
 * with automatic parameter generation.
 * <p>
 * This provider acts as a bridge between JUnit 5's standard
 * {@link MethodSource} functionality and AutoParams' automatic test data
 * generation. It enables the use of explicit values from factory methods while
 * automatically generating values for any remaining parameters.
 * </p>
 *
 * <p>
 * The provider works by:
 * </p>
 * <ol>
 *   <li>Processing the {@link MethodAutoSource} annotation</li>
 *   <li>
 *       Converting it to a {@link MethodSource} for JUnit to handle method
 *       data retrieval
 *   </li>
 *   <li>
 *       Delegating to {@link AutoArgumentsProvider} to fill in missing
 *       parameters
 *   </li>
 * </ol>
 *
 * @see MethodAutoSource
 * @see AutoArgumentsProvider
 * @see MethodSource
 */
public final class MethodAutoArgumentsProvider
    extends AutoArgumentsProvider
    implements AnnotationConsumer<MethodAutoSource> {

    private final AnnotationConsumer<MethodSource> annotationConsumer;

    /**
     * Constructs a new {@link MethodAutoArgumentsProvider} instance.
     */
    @SuppressWarnings("unused")
    public MethodAutoArgumentsProvider() {
        this(createArgumentsProvider(MethodSource.class));
    }

    /**
     * Constructs a {@link MethodAutoArgumentsProvider} with the specified
     * {@link ArgumentsProvider}.
     *
     * @param assetProvider the {@link ArgumentsProvider} to delegate to
     * @deprecated This constructor has been deprecated. Use the default
     *             constructor instead.
     */
    @Deprecated
    @SuppressWarnings({ "unchecked", "DeprecatedIsStillUsed" })
    public MethodAutoArgumentsProvider(ArgumentsProvider assetProvider) {
        super(assetProvider);
        annotationConsumer = (AnnotationConsumer<MethodSource>) assetProvider;
    }

    /**
     * Accepts and processes a {@link MethodAutoSource} annotation.
     * <p>
     * This method converts the {@link MethodAutoSource} annotation to an
     * equivalent {@link MethodSource} annotation and passes it to the internal
     * annotation consumer. This allows the method source to be properly
     * processed while maintaining compatibility with AutoParams' automatic
     * parameter generation.
     * </p>
     *
     * @param annotation the {@link MethodAutoSource} annotation to process
     */
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
