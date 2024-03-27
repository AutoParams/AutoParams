package autoparams.generator;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Comparator;

import autoparams.ResolutionContext;

class ConstructorResolverGenerator extends TypeMatchingGenerator {

    private static final Comparator<Constructor<?>> byParameters =
        Comparator.comparingInt(Constructor::getParameterCount);

    public ConstructorResolverGenerator() {
        super(
            ConstructorResolverGenerator::factory,
            ConstructorResolver.class
        );
    }

    private static ConstructorResolver factory(ResolutionContext context) {
        ConstructorExtractor extractor = context.generate(ConstructorExtractor.class);
        return createDefensiveResolver(extractor);
    }

    private static ConstructorResolver createDefensiveResolver(ConstructorExtractor extractor) {
        return new CompositeConstructorResolver(
            t -> extractor.extract(t).stream()
                .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
                .min(byParameters),
            t -> extractor.extract(t).stream().min(byParameters)
        );
    }
}
