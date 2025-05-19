package autoparams.processor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.FieldQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.internal.reflect.RuntimeTypeResolver;

import static java.util.Arrays.stream;

/**
 * Injects values into instance fields of a specified type using reflection.
 * <p>
 * This class implements {@link ObjectProcessor} and assigns values to all
 * non-static instance fields of the target type. You can customize which fields
 * are processed by including or excluding specific field names.
 * </p>
 *
 * <b>Example:</b>
 * <pre>
 * // Creates a writer for MyClass that only injects values into field1 and field2,
 * // excluding field3.
 * InstanceFieldWriter writer = new InstanceFieldWriter(MyClass.class)
 *     .including("field1", "field2")
 *     .excluding("field3");
 * </pre>
 *
 * @see ObjectProcessor
 * @see FieldQuery
 */
public class InstanceFieldWriter implements ObjectProcessor {

    private final Class<?> targetType;
    private final Predicate<Field> predicate;

    /**
     * Creates a new writer for injecting values into instance fields of the
     * given type.
     * <p>
     * All non-static fields of the specified type will be targeted for value
     * injection. You can further filter the fields by calling including or
     * excluding methods.
     * </p>
     *
     * <b>Example:</b>
     * <pre>
     * // Creates a writer for MyClass that injects values into all instance fields.
     * InstanceFieldWriter writer = new InstanceFieldWriter(MyClass.class);
     * </pre>
     *
     * @param targetType the class whose instance fields will be injected
     * @see #including(String...)
     * @see #excluding(String...)
     */
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

    /**
     * Processes the given object by injecting values into its instance fields.
     * <p>
     * This method checks if the query type matches the target type and, if so,
     * injects values into all non-static instance fields using the provided
     * context.
     * </p>
     *
     * @param query   the object query describing the requested object
     * @param value   the generated object to process
     * @param context the resolution context for further object resolution
     * @see ObjectQuery
     * @see ResolutionContext
     */
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
        Object argument = context.resolve(new FieldQuery(field, type));
        try {
            field.set(target, argument);
        } catch (IllegalArgumentException |
                 IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Returns a new writer that injects values only into the specified fields.
     * <p>
     * The returned writer will target only the fields whose names are included
     * in the given list. All other fields will be ignored during value
     * injection.
     * </p>
     *
     * <b>Example:</b>
     * <pre>
     * // Creates a writer that injects values only into "field1" and "field2".
     * InstanceFieldWriter writer = new InstanceFieldWriter(MyClass.class)
     *     .including("field1", "field2");
     * </pre>
     *
     * @param fieldNames the names of the fields to include
     * @return a new {@link InstanceFieldWriter} with the specified field filter
     * @see #excluding(String...)
     */
    public InstanceFieldWriter including(String... fieldNames) {
        return new InstanceFieldWriter(
            targetType,
            predicate.and(field ->
                stream(fieldNames)
                    .anyMatch(x -> field.getName().equals(x)))
        );
    }

    /**
     * Returns a new writer that injects values into all currently included
     * fields except those whose names are listed in the given arguments.
     * <p>
     * This allows you to create a writer that skips injection for specific
     * fields.
     * </p>
     *
     * <b>Example:</b>
     * <pre>
     * // Creates a writer that does not inject values into "field3".
     * InstanceFieldWriter writer = new InstanceFieldWriter(MyClass.class)
     *     .excluding("field3");
     * </pre>
     *
     * @param fieldNames the names of the fields to exclude
     * @return a new {@link InstanceFieldWriter} with the specified exclusion
     *         filter
     * @see #including(String...)
     */
    public InstanceFieldWriter excluding(String... fieldNames) {
        return new InstanceFieldWriter(
            targetType,
            predicate.and(field ->
                stream(fieldNames)
                    .allMatch(x -> field.getName().equals(x) == false))
        );
    }
}
