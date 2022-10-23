package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;

public interface ConstructorResolver {

    Optional<Constructor<?>> resolve(Class<?> type);

    static ConstructorResolver compose(ConstructorResolver... resolvers) {
        return type -> Folder.foldl(
            (resolved, resolver) -> resolved.isPresent() ? resolved : resolver.resolve(type),
            Optional.empty(),
            Arrays.stream(resolvers));
    }

}
