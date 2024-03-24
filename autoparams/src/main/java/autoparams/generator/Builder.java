package autoparams.generator;

import autoparams.ResolutionContext;
import java.lang.reflect.Type;

public final class Builder<T> {

    private final Type type;
    private final ResolutionContext context;

    private Builder(Type type, ResolutionContext context) {
        this.type = type;
        this.context = context;
    }

    static <T> Builder<T> create(Type type, ResolutionContext context) {
        return new Builder<>(type, context);
    }

    public <U> Builder<T> fix(Class<U> type, U value) {
        context.applyCustomizer(generator -> (query, context) -> query.getType() == type
            ? new ObjectContainer(value)
            : generator.generate(query, context));
        return this;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        return (T) context.generate(ObjectQuery.fromType(type));
    }
}
