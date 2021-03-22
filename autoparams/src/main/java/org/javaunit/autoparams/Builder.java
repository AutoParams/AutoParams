package org.javaunit.autoparams;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class Builder<T> {

    private final GenericObjectQuery builderQuery;
    private final ObjectGenerationContext context;

    Builder(GenericObjectQuery builderQuery, ObjectGenerationContext context) {
        this.builderQuery = builderQuery;
        this.context = context;
    }

    public <U> Builder<T> fix(Class<U> type, U value) {
        this.context.fix(type, value);
        return this;
    }

    public T build() {
        return generate(this.builderQuery);
    }

    @SuppressWarnings("unchecked")
    private T generate(GenericObjectQuery builderQuery) {
        ObjectQuery query = this.createGenerateObjectQuery(builderQuery);
        return (T) this.context.generate(query);
    }

    private ObjectQuery createGenerateObjectQuery(GenericObjectQuery builderQuery) {
        Type generateType = builderQuery.getParameterizedType().getActualTypeArguments()[0];
        if (generateType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) generateType;
            return new GenericObjectQuery(
                (Class<?>) parameterizedType.getRawType(), parameterizedType);
        } else {
            return new ObjectQuery((Class<?>) generateType);
        }
    }

}
