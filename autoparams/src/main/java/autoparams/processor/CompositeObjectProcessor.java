package autoparams.processor;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

/**
 * An {@link ObjectProcessor} that composes multiple {@link ObjectProcessor}
 * instances.
 * <p>
 * This class allows for the aggregation of several object processors into a
 * single processor. When an object is processed, it iterates through the
 * composed processors in the order they were provided, applying each
 * processor's logic to the object.
 * </p>
 *
 * @see ObjectProcessor
 * @see ResolutionContext
 */
public class CompositeObjectProcessor implements ObjectProcessor {

    private final ObjectProcessor[] processors;

    /**
     * Creates a new instance of {@link CompositeObjectProcessor} with the
     * specified {@link ObjectProcessor} instances.
     * <p>
     * The order of the processors is preserved. When an object is processed,
     * the processors will be applied in the same order they are provided
     * here.
     * </p>
     *
     * @param processors an array of {@link ObjectProcessor} instances to
     *                   compose. This argument can be a varargs array.
     */
    public CompositeObjectProcessor(ObjectProcessor... processors) {
        this.processors = processors;
    }

    /**
     * Processes the given value by applying each composed
     * {@link ObjectProcessor}.
     * <p>
     * This method iterates through the array of {@link ObjectProcessor}
     * instances provided to the constructor of this
     * {@link CompositeObjectProcessor}, in their original order. For each
     * processor, it invokes its
     * {@link ObjectProcessor#process(ObjectQuery, Object, ResolutionContext)}
     * method with the specified {@code query}, {@code value}, and
     * {@code context}.
     * </p>
     *
     * @param query   the {@link ObjectQuery} associated with the value.
     * @param value   the object value to be processed.
     * @param context the {@link ResolutionContext} providing services and
     *                information for processing.
     * @see ObjectQuery
     * @see ResolutionContext
     * @see ObjectProcessor#process(ObjectQuery, Object, ResolutionContext)
     */
    @Override
    public void process(
        ObjectQuery query,
        Object value,
        ResolutionContext context
    ) {
        for (ObjectProcessor processor : processors) {
            processor.process(query, value, context);
        }
    }
}
