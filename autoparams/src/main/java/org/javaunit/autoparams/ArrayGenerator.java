package org.javaunit.autoparams;

import java.lang.reflect.Array;

final class ArrayGenerator implements ObjectGenerator {

    @Override
    public GenerationResult generateObject(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.isArray()
            ? GenerationResult.presence(factory(type, context))
            : GenerationResult.absence();
    }

    private Object factory(Class<?> type, ObjectGenerationContext context) {
        Class<?> componentType = type.getComponentType();
        int length = 3;
        Object array = Array.newInstance(componentType, length);
        ObjectQuery query = new ObjectQuery(componentType);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, context.generate(query));
        }

        return array;
    }

}
