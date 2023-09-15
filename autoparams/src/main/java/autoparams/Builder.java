package autoparams;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import java.lang.reflect.Type;

public final class Builder<T> {

    private final Type type;
    private final ObjectGenerationContext context;

    private Builder(Type type, ObjectGenerationContext context) {
        this.type = type;
        this.context = context;
    }

    static <T> Builder<T> create(Type type, ObjectGenerationContext context) {
        return new Builder<>(type, context);
    }

    public <U> Builder<T> fix(Class<U> type, U value) {
        context.customizeGenerator(generator -> (query, context) -> query.getType() == type
            ? new ObjectContainer(value)
            : generator.generate(query, context));
        return this;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        return (T) context.generate(ObjectQuery.fromType(type));
    }
}
