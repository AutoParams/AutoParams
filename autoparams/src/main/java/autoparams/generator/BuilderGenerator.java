package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ResolutionContext;

final class BuilderGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(
        ParameterizedType type,
        ResolutionContext context
    ) {
        if (type.getRawType().equals(Builder.class)) {
            Type targetType = type.getActualTypeArguments()[0];
            return new ObjectContainer(Builder.create(targetType, context));
        } else {
            return ObjectContainer.EMPTY;
        }
    }
}
