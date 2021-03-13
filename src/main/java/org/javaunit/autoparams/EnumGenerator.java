package org.javaunit.autoparams;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class EnumGenerator implements ObjectGenerator {
    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return query.getType().getSuperclass().equals(Enum.class) ? factory(query.getType()) : Optional.empty();
    }

    private Optional<Object> factory(Class<?> type) {
        Object[] values;
        try {
            values = (Object[]) type.getDeclaredMethod("values").invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        int index = RANDOM.nextInt(values.length);
        return Optional.of(values[index]);
    }
}
