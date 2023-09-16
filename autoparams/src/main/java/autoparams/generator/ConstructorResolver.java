package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;

@FunctionalInterface
public interface ConstructorResolver {

    Optional<Constructor<?>> resolve(Class<?> type);

    default Constructor<?> resolveOrElseThrow(Class<?> type) {
        return resolve(type).orElseThrow(()
            -> new RuntimeException("No constructor found for " + type));
    }

    static ConstructorResolver compose(ConstructorResolver... resolvers) {
        return type -> Folder.foldl(
            (resolved, resolver) -> resolved.isPresent() ? resolved : resolver.resolve(type),
            Optional.empty(),
            Arrays.stream(resolvers));
    }
}
