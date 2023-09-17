package autoparams.generator;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Comparator;

public class DefensiveConstructorResolver extends CompositeConstructorResolver {

    public DefensiveConstructorResolver(ConstructorExtractor extractor) {
        super(
            type -> extractor
                .extract(type)
                .stream()
                .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
                .min(Comparator.comparingInt(Constructor::getParameterCount)),
            type -> extractor
                .extract(type)
                .stream()
                .min(Comparator.comparingInt(Constructor::getParameterCount))
        );
    }
}
