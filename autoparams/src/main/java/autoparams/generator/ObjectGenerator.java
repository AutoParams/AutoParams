package autoparams.generator;

import java.lang.reflect.Type;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customizer;

/**
 * Functional interface for generating objects based on a given query and
 * resolution context.
 * <p>
 * Implementations of this interface are responsible for producing an
 * {@link ObjectContainer} that holds the generated object, using the provided
 * {@link ObjectQuery} and {@link ResolutionContext}. If an object cannot be
 * generated, {@link ObjectContainer#EMPTY} is returned.
 * </p>
 * <p>
 * This interface also extends {@link Customizer},
 * allowing for customization and composition of object generators.
 * </p>
 *
 * @see ObjectQuery
 * @see ResolutionContext
 * @see ObjectContainer
 * @see ObjectGeneratorBase
 */
@FunctionalInterface
public interface ObjectGenerator extends Customizer {

    /**
     * Generates an object for the given {@link ObjectQuery} with the specified
     * {@link ResolutionContext}.
     * <p>
     * Implementations should return an {@link ObjectContainer} containing the
     * generated object, or {@link ObjectContainer#EMPTY} if the object cannot
     * be generated.
     * </p>
     *
     * @param query   the object query describing the requested object
     * @param context the resolution context for object generation
     * @return an {@link ObjectContainer} with the generated object, or
     *         {@link ObjectContainer#EMPTY} if not possible
     */
    ObjectContainer generate(ObjectQuery query, ResolutionContext context);

    /**
     * The default {@link ObjectGenerator} implementation.
     * <p>
     * This instance delegates to an internal implementation and is intended for
     * use wherever a standard object generator is required.
     * </p>
     */
    ObjectGenerator DEFAULT = new DefaultObjectGenerator();

    /**
     * Generates an object of the given {@link Type} with the specified
     * {@link ResolutionContext}.
     * <p>
     * This is a convenience method that creates a {@link DefaultObjectQuery}
     * from the specified type and delegates to
     * {@link #generate(ObjectQuery, ResolutionContext)}.
     * </p>
     *
     * @param type    the type of object to generate
     * @param context the resolution context for object generation
     * @return an {@link ObjectContainer} with the generated object, or
     * {@link ObjectContainer#EMPTY} if not possible
     */
    default ObjectContainer generate(Type type, ResolutionContext context) {
        return generate(new DefaultObjectQuery(type), context);
    }

    /**
     * Composes this generator with another {@link ObjectGenerator}.
     * <p>
     * Returns a new {@link ObjectGenerator} that first attempts to generate an
     * object using this generator. If unsuccessful, it delegates to the
     * provided generator.
     * </p>
     *
     * @param generator the secondary object generator to use if this generator
     *                  cannot generate an object
     * @return a composite {@link ObjectGenerator}
     */
    @Override
    default ObjectGenerator customize(ObjectGenerator generator) {
        return new CompositeObjectGenerator(this, generator);
    }
}
