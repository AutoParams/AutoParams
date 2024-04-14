package autoparams.generator;

import java.lang.reflect.Type;

import autoparams.ResolutionContext;

abstract class PlainObjectGenerator<T> implements ObjectGenerator {

    private final Class<T> type;

    PlainObjectGenerator(Class<T> type) {
        this.type = type;
    }

    @Override
    public final ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return matches(query.getType())
            ? new ObjectContainer(generateValue(query, context))
            : ObjectContainer.EMPTY;
    }

    private boolean matches(Type type) {
        return type instanceof Class && matches((Class<?>) type);
    }

    private boolean matches(Class<?> type) {
        return type.isAssignableFrom(this.type);
    }

    protected abstract T generateValue(
        ObjectQuery query,
        ResolutionContext context
    );
}
