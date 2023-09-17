package autoparams.generator;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Comparator;

public class AggressiveConstructorResolver extends CompositeConstructorResolver {

    public AggressiveConstructorResolver(ConstructorExtractor extractor) {
        super(
            type -> extractor
                .extract(type)
                .stream()
                .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
                .max(Comparator.comparingInt(Constructor::getParameterCount)),
            type -> extractor
                .extract(type)
                .stream()
                .max(Comparator.comparingInt(Constructor::getParameterCount))
        );
    }
}
