package autoparams.customization;

import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Optional;

import autoparams.generator.ConstructorExtractor;
import autoparams.generator.ConstructorResolver;

final class RichConstructorResolver implements ConstructorResolver {

    private final ConstructorExtractor extractor;

    public RichConstructorResolver(ConstructorExtractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public Optional<Constructor<?>> resolve(Class<?> type) {
        return extractor
            .extract(type)
            .stream()
            .max(Comparator.comparingInt(Constructor::getParameterCount));
    }
}
