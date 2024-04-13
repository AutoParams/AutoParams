package autoparams.generator;

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
        return query.getType().equals(type)
            ? new ObjectContainer(generateValue(query, context))
            : ObjectContainer.EMPTY;
    }

    protected abstract T generateValue(
        ObjectQuery query,
        ResolutionContext context
    );
}
