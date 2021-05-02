package org.javaunit.autoparams;

import java.lang.reflect.Type;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectGenerator;

public final class Builder<T> {

    private Type type;
    private ObjectGenerationContext context;

    private Builder(Type type, ObjectGenerationContext context) {
        this.type = type;
        this.context = context;
    }

    static <T> Builder<T> create(Type type, ObjectGenerator generator) {
        return new Builder<T>(type, new ObjectGenerationContext(generator));
    }

    public <U> Builder<T> fix(Class<U> type, U value) {
        context.customizeGenerator(generator -> (query, context) -> query.getType() == type
            ? new ObjectContainer(value)
            : generator.generate(query, context));

        return this;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        return (T) context.generate(() -> type);
    }

}
