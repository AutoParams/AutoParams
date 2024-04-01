package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import autoparams.ResolutionContext;

final class GenericStreamGenerator implements ObjectGenerator {

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
        return type.getRawType().equals(Stream.class)
            ? new ObjectContainer(factory(type, context))
            : ObjectContainer.EMPTY;
    }

    private Stream<?> factory(
        ParameterizedType streamType,
        ResolutionContext context
    ) {
        Type elementType = streamType.getActualTypeArguments()[0];
        ObjectQuery query = ObjectQuery.fromType(elementType);
        return Stream.generate(() -> context.resolve(query)).limit(3);
    }
}
