package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.internal.Folder.foldl;
import static java.util.Arrays.stream;

/**
 * An {@link ObjectGenerator} that composes multiple {@link ObjectGenerator}
 * instances.
 * <p>
 * This class allows for the creation of a single generator from a sequence of
 * other generators. When an object is requested, it iterates through the
 * composed generators in the order they were provided. The first generator
 * that successfully produces an object for the given query will have its result
 * returned. If none of the composed generators can satisfy the request, this
 * generator will also fail to produce an object, typically by returning
 * {@link ObjectContainer#EMPTY}.
 * </p>
 *
 * @see ObjectGenerator
 * @see ObjectContainer
 */
public class CompositeObjectGenerator implements ObjectGenerator {

    private final ObjectGenerator[] generators;

    /**
     * Creates a new instance of {@link CompositeObjectGenerator} with the
     * specified {@link ObjectGenerator} instances.
     * <p>
     * The order of the generators is preserved. When {@link #generate} is
     * called, the generators will be queried in the same order they are
     * provided here.
     * </p>
     *
     * @param generators an array of {@link ObjectGenerator} instances to
     *                   compose. This argument can be a varargs array.
     */
    public CompositeObjectGenerator(ObjectGenerator... generators) {
        this.generators = generators;
    }

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return foldl(
            (result, generator) -> result
                .yieldIfEmpty(() -> generator.generate(query, context)),
            ObjectContainer.EMPTY,
            stream(generators)
        );
    }
}
