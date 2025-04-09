package autoparams.generator;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

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
