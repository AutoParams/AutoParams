package org.javaunit.autoparams;

import java.util.Optional;

final class EnumGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        Class<?> superType = query.getType().getSuperclass();
        return superType != null && superType.equals(Enum.class) ? factory(query.getType())
            : Optional.empty();
    }

    private Optional<Object> factory(Class<?> type) {
        Object[] values = EnumValuesResolver.resolveValues(type);
        int index = RANDOM.nextInt(values.length);
        return Optional.of(values[index]);
    }
}
