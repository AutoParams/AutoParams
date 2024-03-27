package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.stream.Stream;

import autoparams.ResolutionContext;

final class GenericStreamGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ResolutionContext context) {
        return type.getRawType().equals(Stream.class)
            ? new ObjectContainer(factory((Class<?>) type.getActualTypeArguments()[0], context))
            : ObjectContainer.EMPTY;
    }

    private <T> Stream<T> factory(
        Class<? extends T> elementType,
        ResolutionContext context
    ) {
        ArrayList<T> list = SequenceGenerator.factory(elementType, context);
        return list.stream();
    }
}
