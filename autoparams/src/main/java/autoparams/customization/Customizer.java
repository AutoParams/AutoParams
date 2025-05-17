package autoparams.customization;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;

/**
 * Interface for customizing object generation and processing in AutoParams.
 * <p>
 * Implementations of this interface can modify or extend the behavior of
 * {@link ObjectGenerator} and {@link ObjectProcessor} by providing custom
 * logic through the {@code customize} methods.
 * </p>
 *
 * @see ObjectGenerator
 * @see ObjectProcessor
 */
public interface Customizer {

    /**
     * Customizes the given {@link ObjectGenerator}.
     * <p>
     * Implementations can override this method to provide custom logic for
     * modifying or extending the behavior of the specified {@code generator}
     * By default, this method returns the given generator without modification.
     * </p>
     *
     * @param generator the object generator to customize
     * @return the customized object generator
     * @throws IllegalArgumentException if the argument {@code generator} is
     *                                  null
     */
    default ObjectGenerator customize(ObjectGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("The argument 'generator' is null.");
        }

        return generator;
    }

    /**
     * Customizes the given {@link ObjectProcessor}.
     * <p>
     * Implementations can override this method to provide custom logic for
     * modifying or extending the behavior of the specified {@code processor}.
     * By default, this method returns the given processor without modification.
     * </p>
     *
     * @param processor the object processor to customize
     * @return the customized object processor
     * @throws IllegalArgumentException if the argument {@code processor} is
     *                                  null
     */
    default ObjectProcessor customize(ObjectProcessor processor) {
        if (processor == null) {
            throw new IllegalArgumentException("The argument 'processor' is null.");
        }

        return processor;
    }
}
