package autoparams.processor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectGenerator;

public abstract class ObjectProcessorBase<T> implements ObjectProcessor {

    private final Type type = inferType(getClass());

    private Type inferType(Class<?> processorType) {
        return getBaseType(processorType).getActualTypeArguments()[0];
    }

    private static ParameterizedType getBaseType(Class<?> processorType) {
        return (ParameterizedType) processorType.getGenericSuperclass();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void process(
        ObjectQuery query,
        Object value,
        ResolutionContext context
    ) {
        if (matches(query.getType())) {
            processObject(query, (T) value, context);
        }
    }

    private boolean matches(Type type) {
        return type instanceof Class && matches((Class<?>) type);
    }

    private boolean matches(Class<?> type) {
        return this.type instanceof Class<?>
            && ((Class<?>) this.type).isAssignableFrom(type);
    }

    protected abstract void processObject(
        ObjectQuery query,
        T value,
        ResolutionContext context
    );

    @Override
    public final ObjectProcessor customize(ObjectProcessor processor) {
        return ObjectProcessor.super.customize(processor);
    }

    @Override
    public final ObjectGenerator customize(ObjectGenerator generator) {
        return ObjectProcessor.super.customize(generator);
    }
}
