package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

import static autoparams.generator.CollectionGenerator.getSize;

final class GenericStreamGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (query.getType() instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) query.getType();
            if (type.getRawType().equals(Stream.class)) {
                int size = getSize(query);
                return new ObjectContainer(generateStream(type, size, context));
            }
        }

        return ObjectContainer.EMPTY;
    }

    private static Stream<Object> generateStream(
        ParameterizedType streamType,
        int size,
        ResolutionContext context
    ) {
        Type elementType = streamType.getActualTypeArguments()[0];
        ObjectQuery query = new DefaultObjectQuery(elementType);
        return Stream.generate(() -> context.resolve(query)).limit(size);
    }
}
