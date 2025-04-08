package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Optional;

import autoparams.internal.Folder;

import static java.util.Arrays.stream;

public class CompositeConstructorResolver implements ConstructorResolver {

    private final ConstructorResolver[] resolvers;

    public CompositeConstructorResolver(ConstructorResolver... resolvers) {
        this.resolvers = resolvers;
    }

    @Override
    public final Optional<Constructor<?>> resolve(Class<?> type) {
        return Folder.foldl(
            (resolved, resolver) -> resolved.isPresent()
                ? resolved
                : resolver.resolve(type),
            Optional.empty(),
            stream(resolvers)
        );
    }
}
