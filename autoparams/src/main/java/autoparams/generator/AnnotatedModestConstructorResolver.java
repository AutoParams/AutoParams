package autoparams.generator;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Optional;

final class AnnotatedModestConstructorResolver implements ConstructorResolver {

    private final ConstructorExtractor extractor;

    public AnnotatedModestConstructorResolver(ConstructorExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public Optional<Constructor<?>> resolve(Class<?> type) {
        return extractor
            .extract(type)
            .stream()
            .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
            .min(Comparator.comparingInt(Constructor::getParameterCount));
    }
}
