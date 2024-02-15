package autoparams.generator;

import autoparams.ResolutionContext;
import java.lang.reflect.Array;

final class ArrayGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ResolutionContext context) {
        return query.getType() instanceof Class<?>
            ? generate((Class<?>) query.getType(), (ObjectGenerationContext) context)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(Class<?> type, ObjectGenerationContext context) {
        return type.isArray()
            ? new ObjectContainer(generateArray(type.getComponentType(), context))
            : ObjectContainer.EMPTY;
    }

    private Object generateArray(Class<?> elementType, ObjectGenerationContext context) {
        Object array = Array.newInstance(elementType, 3);
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(array, i, generateElement(elementType, context));
        }
        return array;
    }

    private Object generateElement(Class<?> elementType, ObjectGenerationContext context) {
        return context.generate(elementType);
    }
}
