package autoparams.customization;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Optional;

import autoparams.generator.ConstructorExtractor;
import autoparams.generator.ConstructorResolver;

final class AnnotatedRichConstructorResolver implements ConstructorResolver {

    private final ConstructorExtractor extractor;

    public AnnotatedRichConstructorResolver(ConstructorExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public Optional<Constructor<?>> resolve(Class<?> type) {
        return extractor
            .extract(type)
            .stream()
            .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
            .max(Comparator.comparingInt(Constructor::getParameterCount));
    }
}
