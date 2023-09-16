package autoparams.generator;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Optional;

import static java.util.Arrays.stream;

@FunctionalInterface
public interface ConstructorResolver {

    Optional<Constructor<?>> resolve(Class<?> type);

    ConstructorResolver DEFENSIVE_STRATEGY = compose(
        type -> stream(type.getConstructors())
            .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
            .min(Comparator.comparing(Constructor::getParameterCount)),
        type -> stream(type.getConstructors())
            .min(Comparator.comparing(Constructor::getParameterCount))
    );

    ConstructorResolver AGGRESSIVE_STRATEGY = compose(
        type -> stream(type.getConstructors())
            .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
            .max(Comparator.comparing(Constructor::getParameterCount)),
        type -> stream(type.getConstructors())
            .max(Comparator.comparing(Constructor::getParameterCount))
    );

    default Constructor<?> resolveOrElseThrow(Class<?> type) {
        return resolve(type).orElseThrow(()
            -> new RuntimeException("No constructor found for " + type));
    }

    static ConstructorResolver compose(ConstructorResolver... resolvers) {
        return type -> Folder.foldl(
            (resolved, resolver) -> resolved.isPresent() ? resolved : resolver.resolve(type),
            Optional.empty(),
            stream(resolvers));
    }
}
