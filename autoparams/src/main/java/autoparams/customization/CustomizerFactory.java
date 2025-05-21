package autoparams.customization;

/**
 * Factory interface for creating {@link Customizer} instances.
 * <p>
 * This functional interface defines a factory method for creating customizers
 * that can modify or extend the behavior of object generation and processing
 * in AutoParams.
 * </p>
 * <p>
 * Implementations of this interface can be referenced by the
 * {@link CustomizerSource} annotation to specify how customizers should be
 * created for a particular context.
 * </p>
 *
 * @see Customizer
 * @see CustomizerSource
 */
@FunctionalInterface
public interface CustomizerFactory {

    /**
     * Creates a new {@link Customizer} instance.
     * <p>
     * This method is responsible for instantiating and configuring a customizer
     * that can be used to modify the behavior of object generation and
     * processing in AutoParams.
     * </p>
     *
     * @return a new customizer instance
     */
    Customizer createCustomizer();
}
