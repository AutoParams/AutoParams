package autoparams.generator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import autoparams.ResolutionContext;
import autoparams.processor.ObjectProcessor;

public abstract class ObjectGeneratorBase<T> implements ObjectGenerator {

    private final Type type = inferType(getClass());

    private static Type inferType(Class<?> generatorType) {
        try {
            Method method = generatorType.getDeclaredMethod(
                "generateObject",
                ObjectQuery.class,
                ResolutionContext.class
            );
            return method.getGenericReturnType();
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public final ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        if (matches(query.getType())) {
            return new ObjectContainer(generateObject(query, context));
        } else {
            return ObjectContainer.EMPTY;
        }
    }

    @Override
    public final ObjectContainer generate(
        Type type,
        ResolutionContext context
    ) {
        return ObjectGenerator.super.generate(type, context);
    }

    @Override
    public final ObjectGenerator customize(ObjectGenerator generator) {
        return ObjectGenerator.super.customize(generator);
    }

    @Override
    public final ObjectProcessor customize(ObjectProcessor processor) {
        return ObjectGenerator.super.customize(processor);
    }

    private boolean matches(Type type) {
        return type instanceof Class && matches((Class<?>) type);
    }

    private boolean matches(Class<?> type) {
        return Modifier.isAbstract(type.getModifiers())
            && this.type instanceof Class
            ? type.isAssignableFrom((Class<?>) this.type)
            : type.equals(this.type);
    }

    protected abstract T generateObject(
        ObjectQuery query,
        ResolutionContext context
    );
}
