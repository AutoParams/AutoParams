package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

/**
 * A specialized {@link ObjectGenerator} that focuses on generating arguments
 * for method parameters.
 * <p>
 * This functional interface extends {@link ObjectGenerator} and is primarily
 * designed to produce values based on {@link ParameterQuery}. It provides a
 * more specific contract for components that are responsible for resolving
 * method parameter arguments within the AutoParams framework.
 * </p>
 * <p>
 * While it inherits the general
 * {@link ObjectGenerator#generate(ObjectQuery, ResolutionContext)} method, its
 * main functional method is
 * {@link #generate(ParameterQuery, ResolutionContext)}, tailored for
 * parameter-specific resolution.
 * </p>
 *
 * @see ObjectGenerator
 * @see ParameterQuery
 * @see ResolutionContext
 * @see ObjectContainer
 */
@FunctionalInterface
public interface ArgumentGenerator extends ObjectGenerator {

    /**
     * Generates an argument value for a method parameter based on the specified
     * query and resolution context.
     * <p>
     * This method is the primary way to produce arguments for parameters
     * represented by a {@link ParameterQuery}. Implementations should use the
     * information within the {@code query} and the capabilities of the
     * {@code context} to create an appropriate argument.
     * </p>
     *
     * @param query   the {@link ParameterQuery} detailing the parameter for
     *                which an argument is to be generated.
     * @param context the {@link ResolutionContext} for object generation
     * @return an {@link ObjectContainer} holding the generated argument. If no
     *         argument can be generated, this method should return
     *         {@link ObjectContainer#EMPTY}.
     */
    ObjectContainer generate(
        ParameterQuery query,
        ResolutionContext context
    );

    /**
     * Generates an object based on the specified query and resolution context.
     * <p>
     * This default implementation checks if the provided {@link ObjectQuery} is
     * an instance of {@link ParameterQuery}. If it is, this method delegates
     * to the {@link #generate(ParameterQuery, ResolutionContext)} method to
     * handle parameter-specific generation. Otherwise, it returns
     * {@link ObjectContainer#EMPTY}, indicating that this generator does not
     * handle the given query type directly.
     * </p>
     *
     * @param query   the {@link ObjectQuery} for which an object is to be
     *                generated.
     * @param context the {@link ResolutionContext} for object generation
     * @return an {@link ObjectContainer} holding the generated object. Returns
     *         {@link ObjectContainer#EMPTY} if the query is not a
     *         {@link ParameterQuery}.
     */
    default ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query instanceof ParameterQuery
            ? generate((ParameterQuery) query, context)
            : ObjectContainer.EMPTY;
    }
}
