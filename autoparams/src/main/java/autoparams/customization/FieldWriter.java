package autoparams.customization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectQuery;

import static java.util.Arrays.stream;

@Deprecated
public final class FieldWriter implements Customizer {

    private final Class<?> targetType;
    private final Predicate<Field> predicate;

    public FieldWriter(Class<?> targetType) {
        this(targetType, x -> true);
    }

    private FieldWriter(Class<?> targetType, Predicate<Field> predicate) {
        this.targetType = targetType;
        this.predicate = predicate;
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            ObjectContainer container = generator.generate(query, context);
            Type underlyingType = query.getType();
            return getRawType(underlyingType) == targetType
                ? container.process(target -> process(target, underlyingType, context))
                : container;
        };
    }

    private Class<?> getRawType(Type type) {
        return type instanceof ParameterizedType
            ? (Class<?>) ((ParameterizedType) type).getRawType()
            : (Class<?>) type;
    }

    private Object process(Object target, Type underlyingType, ResolutionContext context) {
        writeAllFields(target, underlyingType, context);
        return target;
    }

    private void writeAllFields(
        Object target,
        Type underlyingType,
        ResolutionContext context
    ) {
        writeFields(target, underlyingType, context, RuntimeTypeResolver.create(underlyingType));
        Type superType = getRawType(underlyingType).getGenericSuperclass();
        if (superType != null) {
            writeAllFields(target, superType, context);
        }
    }

    private void writeFields(
        Object target,
        Type genericType,
        ResolutionContext context,
        RuntimeTypeResolver typeResolver
    ) {
        stream(getRawType(genericType).getDeclaredFields())
            .filter(FieldWriter::isNonStatic)
            .filter(predicate)
            .forEach(field -> writeField(target, field, context, typeResolver));
    }

    private static boolean isNonStatic(Field field) {
        return Modifier.isStatic(field.getModifiers()) == false;
    }

    private void writeField(
        Object target,
        Field field,
        ResolutionContext context,
        RuntimeTypeResolver typeResolver
    ) {
        field.setAccessible(true);
        Type type = typeResolver.resolve(field.getGenericType());
        Object argument = context.generate(ObjectQuery.fromType(type));
        try {
            field.set(target, argument);
        } catch (IllegalArgumentException |
                 IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public FieldWriter including(String... fieldNames) {
        return new FieldWriter(
            targetType,
            predicate.and(field -> stream(fieldNames)
                .anyMatch(x -> field.getName().equals(x)))
        );
    }

    public FieldWriter excluding(String... fieldNames) {
        return new FieldWriter(
            targetType,
            predicate.and(field -> stream(fieldNames)
                .allMatch(x -> field.getName().equals(x) == false))
        );
    }
}
