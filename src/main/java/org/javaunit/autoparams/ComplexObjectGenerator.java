package org.javaunit.autoparams;

import static java.util.Arrays.stream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

final class ComplexObjectGenerator implements ObjectGenerator {

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        return resolveConstructor(query.getType()).map(c -> createInstance(c, context)).map(Optional::of)
                .orElse(Optional.empty());
    }

    private Optional<Constructor<?>> resolveConstructor(Class<?> type) {
        return isSimpleType(type) ? Optional.empty() : stream(type.getConstructors()).findFirst();
    }

    private boolean isSimpleType(Class<?> type) {
        return type.equals(Boolean.class) || type.equals(Integer.class) || type.equals(Float.class)
                || type.equals(Double.class) || type.equals(String.class) || type.equals(BigDecimal.class)
                || type.equals(UUID.class);
    }

    private Object createInstance(Constructor<?> constructor, ObjectGenerationContext context) {
        try {
            Stream<ObjectQuery> queries = stream(constructor.getParameters()).map(ObjectQuery::create);
            return constructor.newInstance(resolveArguments(queries, context));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] resolveArguments(Stream<ObjectQuery> queries, ObjectGenerationContext context) {
        ObjectGenerator generator = context.getGenerator();
        return queries.map(p -> generator.generate(p, context)).map(a -> a.orElse(null)).toArray();
    }

}
