package org.javaunit.autoparams;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

final class EnumGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        String message = "This method is not supported. Use generateObject method instead.";
        throw new UnsupportedOperationException(message);
    }

    @Override
    public final GenerationResult generateObject(
        ObjectQuery query,
        ObjectGenerationContext context
    ) {
        Class<?> superType = query.getType().getSuperclass();
        return superType != null && superType.equals(Enum.class)
            ? GenerationResult.presence(factory(query.getType()))
            : GenerationResult.absence();
    }

    private Object factory(Class<?> type) {
        Object[] values = EnumValuesResolver.resolveValues(type);
        int index = ThreadLocalRandom.current().nextInt(values.length);
        return values[index];
    }
}
