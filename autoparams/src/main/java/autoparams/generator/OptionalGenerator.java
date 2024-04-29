package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.TypeQuery;

final class OptionalGenerator implements ObjectGenerator {

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
        return type.getRawType().equals(Optional.class)
            ? new ObjectContainer(generateOptional(type, context))
            : ObjectContainer.EMPTY;
    }

    private Optional<Object> generateOptional(
        ParameterizedType optionalType,
        ResolutionContext context
    ) {
        Type elementType = optionalType.getActualTypeArguments()[0];
        return Optional.of(context.resolve(new TypeQuery(elementType)));
    }
}
