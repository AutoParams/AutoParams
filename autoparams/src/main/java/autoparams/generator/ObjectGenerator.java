package autoparams.generator;

import java.lang.reflect.Type;

import autoparams.ResolutionContext;

@FunctionalInterface
public interface ObjectGenerator {

    ObjectContainer generate(ObjectQuery query, ResolutionContext context);

    ObjectGenerator DEFAULT = new DefaultObjectGenerator();

    default ObjectContainer generate(Type type, ResolutionContext context) {
        return generate(ObjectQuery.fromType(type), context);
    }
}
