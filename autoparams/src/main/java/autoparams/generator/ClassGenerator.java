package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class ClassGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return matches(query.getType())
            ? new ObjectContainer(String.class)
            : ObjectContainer.EMPTY;
    }

    private static boolean matches(Type type) {
        return type instanceof ParameterizedType
            && ((ParameterizedType) type).getRawType().equals(Class.class);
    }
}
