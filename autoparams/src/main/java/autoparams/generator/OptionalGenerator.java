package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;

final class OptionalGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType() instanceof ParameterizedType
            ? generate((ParameterizedType) query.getType(), context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType type, ObjectGenerationContext context) {
        return type.getRawType().equals(Optional.class)
            ? new ObjectContainer(factory((Class<?>) type.getActualTypeArguments()[0], context))
            : ObjectContainer.EMPTY;
    }

    private <T> Optional<T> factory(
        Class<? extends T> elementType,
        ObjectGenerationContext context
    ) {
        return Optional.of(context.generate(elementType));
    }

}
