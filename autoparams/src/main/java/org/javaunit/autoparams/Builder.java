package org.javaunit.autoparams;

import java.lang.reflect.Type;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectQuery;

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
        context.customizeGenerator(new FixCustomization(type, value));
        return this;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        return (T) context.generate(ObjectQuery.fromType(type));
    }

}
