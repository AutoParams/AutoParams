package autoparams.processor;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customizer;

/**
 * Functional interface for processing objects after generation.
 * <p>
 * Implementations of this interface can perform additional processing
 * or customization on objects created by the object generator, using
 * the provided {@link ObjectQuery}, generated object, and
 * {@link ResolutionContext}.
 * </p>
 * <p>
 * This interface also extends {@link Customizer},
 * allowing for composition and customization of object processors.
 * </p>
 *
 * @see ObjectQuery
 * @see ResolutionContext
 * @see Customizer
 * @see InstancePropertyWriter
 * @see InstanceFieldWriter
 */
@FunctionalInterface
public interface ObjectProcessor extends Customizer {

    /**
     * Processes the given object after it has been generated.
     * <p>
     * Implementations can use the provided {@link ObjectQuery} to get
     * information about the requested object, modify or initialize the
     * generated {@code value}, and use the {@link ResolutionContext} to resolve
     * additional dependencies if needed.
     * </p>
     *
     * @param query   the object query describing the requested object
     * @param value   the generated object to process
     * @param context the resolution context for further object resolution
     */
    void process(ObjectQuery query, Object value, ResolutionContext context);

    /**
     * The default {@link ObjectProcessor} implementation that performs no
     * processing.
     * <p>
     * This constant can be used when no additional processing of generated
     * objects is required.
     * </p>
     */
    ObjectProcessor DEFAULT = (query, value, context) -> { };

    /**
     * Customizes this {@link ObjectProcessor} by composing it with another
     * processor.
     * <p>
     * The returned processor will first apply the given {@code processor}, then
     * this processor. This allows for chaining multiple processors performing
     * additional processing steps.
     * </p>
     *
     * @param processor the processor to compose with this processor
     * @return a new {@link ObjectProcessor} that applies both processors in
     *         sequence
     */
    @Override
    default ObjectProcessor customize(ObjectProcessor processor) {
        return new CompositeObjectProcessor(processor, this);
    }
}
