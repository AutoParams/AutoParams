package autoparams.generator;

import autoparams.ResolutionContext;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.stream.Stream;

final class GenericStreamGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), (ObjectGenerationContext) context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ObjectGenerationContext context) {
        return type.getRawType().equals(Stream.class)
            ? new ObjectContainer(factory((Class<?>) type.getActualTypeArguments()[0], context))
            : ObjectContainer.EMPTY;
    }

    private <T> Stream<T> factory(
        Class<? extends T> elementType,
        ObjectGenerationContext context
    ) {
        ArrayList<T> list = SequenceGenerator.factory(elementType, context);
        return list.stream();
    }
}
