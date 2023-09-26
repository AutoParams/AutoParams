package autoparams.generator;

import java.lang.reflect.Type;

@FunctionalInterface
public interface ObjectGenerator {

    ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context);

    ObjectGenerator DEFAULT = new DefaultObjectGenerator();

    default ObjectContainer generate(Type type, ObjectGenerationContext context) {
        return generate(ObjectQuery.fromType(type), context);
    }
}
