package org.javaunit.autoparams.generator;

import java.lang.reflect.Type;
import java.util.concurrent.ThreadLocalRandom;

final class EnumGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();
        return type instanceof Class && Enum.class.isAssignableFrom((Class<?>) type)
            ? new ObjectContainer(factory(query))
            : ObjectContainer.EMPTY;
    }

    private Object factory(ObjectQuery query) {
        Class<?> enumType = ((Class<?>) query.getType());
        Object[] values = EnumValuesResolver.resolveValues(enumType);
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index];
    }
}
