package autoparams.generator;

import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;

abstract class PrimitiveTypeGenerator<T> implements ObjectGenerator {

    private final Class<?> primitiveType;
    private final Class<T> boxedType;

    PrimitiveTypeGenerator(Class<?> primitiveType, Class<T> boxedType) {
        this.primitiveType = primitiveType;
        this.boxedType = boxedType;
    }

    @Override
    public final ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        Type type = query.getType();
        return type.equals(primitiveType) || type.equals(boxedType)
            ? new ObjectContainer(generateValue(query, context))
            : ObjectContainer.EMPTY;
    }

    protected abstract T generateValue(
        ObjectQuery query,
        ResolutionContext context
    );
}
