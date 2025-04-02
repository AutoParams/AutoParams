package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

final class FactoryGenerator implements ObjectGenerator {

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
        if (type.getRawType().equals(Factory.class)) {
            Type targetType = type.getActualTypeArguments()[0];
            ResolutionContext branch = context.branch();
            return new ObjectContainer(new Factory<>(branch, targetType));
        } else {
            return ObjectContainer.EMPTY;
        }
    }
}
