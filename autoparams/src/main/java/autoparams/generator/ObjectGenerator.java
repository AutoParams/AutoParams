package autoparams.generator;

import java.lang.reflect.Type;

public interface ObjectGenerator {

    ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context);

    static ObjectGenerator DEFAULT = new DefaultObjectGenerator();

    default ObjectContainer generate(Type type, ObjectGenerationContext context) {
        return generate(ObjectQuery.fromType(type), context);
    }
}
