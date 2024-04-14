package autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Optional;

final class ModestConstructorResolver implements ConstructorResolver {

    private final ConstructorExtractor extractor;

    public ModestConstructorResolver(ConstructorExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public Optional<Constructor<?>> resolve(Class<?> type) {
        return extractor
            .extract(type)
            .stream()
            .min(Comparator.comparingInt(Constructor::getParameterCount));
    }
}
