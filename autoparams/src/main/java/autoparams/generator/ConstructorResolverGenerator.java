package autoparams.generator;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;

final class ConstructorResolverGenerator implements ObjectGenerator {

    private final ObjectContainer value = new ObjectContainer(
        ConstructorResolver
            .compose(
                t -> Arrays
                    .stream(t.getConstructors())
                    .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
                    .min(Comparator.comparing(Constructor::getParameterCount)),
                t -> Arrays
                    .stream(t.getConstructors())
                    .min(Comparator.comparing(Constructor::getParameterCount))
            )
    );

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() == ConstructorResolver.class
            ? value
            : ObjectContainer.EMPTY;
    }

}
