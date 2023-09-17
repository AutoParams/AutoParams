package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;

@FunctionalInterface
public interface ConstructorResolver {

    Optional<Constructor<?>> resolve(Class<?> type);

    default Constructor<?> resolveOrElseThrow(Class<?> type) {
        return resolve(type).orElseThrow(()
            -> new RuntimeException("No constructor found for " + type));
    }

    @Deprecated
    ConstructorResolver DEFENSIVE_STRATEGY =
        new DefensiveConstructorResolver(t -> asList(t.getConstructors()));

    @Deprecated
    ConstructorResolver AGGRESSIVE_STRATEGY =
        new AggressiveConstructorResolver(t -> asList(t.getConstructors()));

    @Deprecated
    static ConstructorResolver compose(ConstructorResolver... resolvers) {
        return type -> Folder.foldl(
            (resolved, resolver) -> resolved.isPresent() ? resolved : resolver.resolve(type),
            Optional.empty(),
            stream(resolvers));
    }
}
