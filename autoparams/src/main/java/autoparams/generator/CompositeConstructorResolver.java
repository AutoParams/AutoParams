package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Optional;

import static autoparams.internal.Folder.foldl;
import static java.util.Arrays.stream;

/**
 * An implementation of {@link ConstructorResolver} that composes multiple
 * {@link ConstructorResolver}s into a single resolver.
 * <p>
 * This class allows for the creation of a single constructor resolver from a
 * sequence of other resolvers. When a constructor is requested for a type, it
 * iterates through the composed resolvers in the order they were provided. The
 * first resolver that successfully provides
 * a {@link Constructor Constructor&lt;?&gt;} for the given type will have its
 * result returned. If none of the composed resolvers can satisfy the request,
 * this resolver will also fail to provide a constructor, typically by returning
 * {@link Optional#empty()}.
 * </p>
 *
 * @see ConstructorResolver
 */
public class CompositeConstructorResolver implements ConstructorResolver {

    private final ConstructorResolver[] resolvers;

    /**
     * Creates a new instance of {@link CompositeConstructorResolver} with the
     * specified {@link ConstructorResolver} instances.
     * <p>
     * The order of the resolvers is preserved. When a constructor is requested,
     * the resolvers will be queried in the same order they are provided here.
     * </p>
     *
     * @param resolvers an array of {@link ConstructorResolver} instances to
     *                  compose. This argument can be a varargs array.
     */
    public CompositeConstructorResolver(ConstructorResolver... resolvers) {
        this.resolvers = resolvers;
    }

    /**
     * Resolves a constructor for the specified class type by querying the
     * composed {@link ConstructorResolver} instances in order.
     * <p>
     * This method iterates through the internal list of resolvers. For each
     * resolver, it calls its
     * {@link ConstructorResolver#resolve(Class) ConstructorResolver.resolve(Class&lt;?&gt;)}
     * method. If a resolver returns a non-empty
     * {@link Optional Optional&lt;T&gt;} containing a
     * {@link Constructor Constructor&lt;?&gt;}, that constructor is immediately
     * returned. If all resolvers return an empty
     * {@link Optional Optional&lt;T&gt;}, this method also returns
     * {@link Optional#empty()}.
     * </p>
     *
     * @param type the {@link Class Class&lt;?&gt;} for which to resolve a constructor.
     * @return an {@link Optional Optional&lt;T&gt;} containing the resolved
     *         {@link Constructor Constructor&lt;?&gt;}, or
     *         {@link Optional#empty()} if no constructor is found by any of the
     *         composed resolvers.
     */
    @Override
    public final Optional<Constructor<?>> resolve(Class<?> type) {
        return foldl(
            (resolved, resolver) -> resolved.isPresent()
                ? resolved
                : resolver.resolve(type),
            Optional.empty(),
            stream(resolvers)
        );
    }
}
