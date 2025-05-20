package autoparams;

import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.ArgumentsProviderCreator.createArgumentsProvider;

/**
 * An implementation of {@link ArgumentsProvider} that processes
 * {@link ValueAutoSource} annotations and combines literal values with
 * automatic parameter generation.
 * <p>
 * This provider acts as a bridge between JUnit 5's standard {@link ValueSource}
 * functionality and AutoParams' automatic test data generation. It enables the
 * use of explicit literal values for some parameters while automatically
 * generating values for any remaining parameters.
 * </p>
 *
 * <p>
 * The provider works by:
 * </p>
 * <ol>
 *   <li>Processing the {@link ValueAutoSource} annotation</li>
 *   <li>
 *       Converting it to a {@link ValueSource} for JUnit to handle literal
 *       value selection
 *   </li>
 *   <li>
 *       Delegating to {@link AutoArgumentsProvider} to fill in missing
 *       parameters
 *   </li>
 * </ol>
 *
 * @see ValueAutoSource
 * @see AutoArgumentsProvider
 * @see ValueSource
 */
public final class ValueAutoArgumentsProvider
    extends AutoArgumentsProvider
    implements AnnotationConsumer<ValueAutoSource> {

    private final AnnotationConsumer<ValueSource> annotationConsumer;

    /**
     * Constructs a new {@code ValueAutoArgumentsProvider} instance.
     */
    @SuppressWarnings("unused")
    public ValueAutoArgumentsProvider() {
        this(createArgumentsProvider(ValueSource.class));
    }

    /**
     * Constructs a {@code ValueAutoArgumentsProvider} with the specified
     * {@link ArgumentsProvider}.
     *
     * @param assetProvider the {@link ArgumentsProvider} to delegate to
     * @deprecated This constructor has been deprecated. Use the default
     *             constructor instead.
     */
    @Deprecated
    @SuppressWarnings({ "unchecked", "DeprecatedIsStillUsed" })
    public ValueAutoArgumentsProvider(ArgumentsProvider assetProvider) {
        super(assetProvider);
        annotationConsumer = (AnnotationConsumer<ValueSource>) assetProvider;
    }

    /**
     * Accepts and processes a {@link ValueAutoSource} annotation.
     * <p>
     * This method converts the {@link ValueAutoSource} annotation to an
     * equivalent {@link ValueSource} annotation and passes it to the internal
     * annotation consumer. This allows the literal values to be properly
     * processed while maintaining compatibility with AutoParams' automatic
     * parameter generation.
     * </p>
     *
     * @param annotation the {@link ValueAutoSource} annotation to process
     */
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
