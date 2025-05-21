package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Optional;

import autoparams.ResolutionContext;

/**
 * Defines a strategy for resolving a constructor for a given class type.
 * <p>
 * This functional interface is used to select an appropriate
 * {@link Constructor Constructor&lt;?&gt;} that AutoParams will use to create
 * an instance of a class. Implementations can define various policies for
 * constructor selection, such as choosing the one with the most parameters,
 * the fewest parameters, or one marked with a specific annotation.
 * </p>
 * <p>
 * AutoParams retrieves {@link ConstructorResolver} instances via the
 * {@link ResolutionContext}. This allows for advanced
 * customization scenarios where a custom {@link ObjectGenerator} can be
 * implemented to supply a specific {@link ConstructorResolver}
 * implementation, thereby controlling the constructor selection policy for
 * certain types or conditions.
 * </p>
 */
@FunctionalInterface
public interface ConstructorResolver {

    /**
     * Resolves a constructor for the specified class type.
     * <p>
     * Implementations of this method should analyze the given {@code type} and
     * return an {@link Optional Optional&lt;T&gt;}
     * containing a {@link Constructor Constructor&lt;?&gt;} if a suitable one
     * is found according to the resolver's strategy. If no constructor can be
     * resolved, it should return {@link Optional#empty()}.
     * </p>
     *
     * @param type the {@link Class Class&lt;?&gt;} for which to resolve a
     *             constructor.
     * @return an {@link Optional Optional&lt;T&gt;} containing the resolved
     *         {@link Constructor Constructor&lt;?&gt;},
     *         or {@link Optional#empty()} if no constructor is found.
     */
    Optional<Constructor<?>> resolve(Class<?> type);

    /**
     * Resolves a constructor for the specified class type, or throws an
     * exception if no constructor can be found.
     * <p>
     * This default method provides a convenient way to get a constructor
     * directly. It calls the {@link #resolve(Class) resolve(Class&lt;?&gt;)}
     * method and, if the result is empty, throws a {@link RuntimeException}
     * indicating that no constructor could be resolved for the given type.
     * Otherwise, it returns the resolved constructor.
     * </p>
     *
     * @param type the {@link Class Class&lt;?&gt;} for which to resolve a
     *             constructor.
     * @return the resolved {@link Constructor Constructor&lt;?&gt;}.
     * @throws RuntimeException if no constructor is found for the specified
     *                          type.
     */
    default Constructor<?> resolveOrElseThrow(Class<?> type) {
        return resolve(type).orElseThrow(()
            -> new RuntimeException("No constructor found for " + type));
    }
}
