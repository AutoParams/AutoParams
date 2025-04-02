package autoparams.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.internal.reflect.RuntimeTypeResolver;

import static java.util.Arrays.stream;

public class InstanceFieldWriter implements ObjectProcessor {

    private final Class<?> targetType;
    private final Predicate<Field> predicate;

    public InstanceFieldWriter(Class<?> targetType) {
        this(targetType, x -> true);
    }

    private InstanceFieldWriter(
        Class<?> targetType,
        Predicate<Field> predicate
    ) {
        this.targetType = targetType;
        this.predicate = predicate;
    }

    @Override
    public void process(
        ObjectQuery query,
        Object value,
        ResolutionContext context
    ) {
        Type type = query.getType();
        if (getRawType(type).equals(targetType)) {
            writeFields(type, value, context);
        }
    }

    private void writeFields(
        Type type,
        Object value,
        ResolutionContext context
    ) {
        RuntimeTypeResolver typeResolver = RuntimeTypeResolver.create(type);
        stream(getRawType(type).getDeclaredFields())
            .filter(field -> Modifier.isStatic(field.getModifiers()) == false)
            .filter(predicate)
            .forEach(field -> writeField(value, field, context, typeResolver));

        Type superType = getRawType(type).getGenericSuperclass();
        if (superType != null) {
            writeFields(superType, value, context);
        }
    }

    private Class<?> getRawType(Type type) {
        return type instanceof ParameterizedType
            ? (Class<?>) ((ParameterizedType) type).getRawType()
            : (Class<?>) type;
    }

    private void writeField(
        Object target,
        Field field,
        ResolutionContext context,
        RuntimeTypeResolver typeResolver
    ) {
        field.setAccessible(true);
        Type type = typeResolver.resolve(field.getGenericType());
        Object argument = context.resolve(new DefaultObjectQuery(type));
        try {
            field.set(target, argument);
        } catch (IllegalArgumentException |
                 IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public InstanceFieldWriter including(String... fieldNames) {
        return new InstanceFieldWriter(
            targetType,
            predicate.and(field ->
                stream(fieldNames)
                    .anyMatch(x -> field.getName().equals(x)))
        );
    }

    public InstanceFieldWriter excluding(String... fieldNames) {
        return new InstanceFieldWriter(
            targetType,
            predicate.and(field ->
                stream(fieldNames)
                    .allMatch(x -> field.getName().equals(x) == false))
        );
    }
}
