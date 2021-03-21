package org.javaunit.autoparams;

import java.lang.reflect.Array;
import java.util.Optional;

final class ArrayGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> type = query.getType();
        return type.isArray() ? factory(type, context) : Optional.empty();
    }

    private Optional<Object> factory(Class<?> type, ObjectGenerationContext context) {
        Class<?> componentType = type.getComponentType();
        int length = 3;
        Object array = Array.newInstance(componentType, length);
        ObjectQuery query = new ObjectQuery(componentType);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, context.generate(query));
        }

        return Optional.of(array);
    }

}
