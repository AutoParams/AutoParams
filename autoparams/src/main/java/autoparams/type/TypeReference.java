package autoparams.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Provides functionality to retrieve a {@link Type} that represents a generic
 * type {@code T}.
 * <p>
 * This abstract class is used to capture full generic type information. It is
 * often used in scenarios where type erasure would otherwise make it difficult
 * to access the complete generic type at runtime. Users should create an
 * anonymous subclass of {@link TypeReference} to capture the generic type.
 * </p>
 * <p>
 * For example, to obtain the type {@code List<String>}, you can write:
 * </p>
 * <pre>
 * Type listOfString = new TypeReference&lt;List&lt;String&gt;&gt;() { }.getType();
 * </pre>
 *
 * @param <T> the referenced type
 */
@SuppressWarnings("unused")
public abstract class TypeReference<T> {

    private final Type type;

    /**
     * Constructs a {@link TypeReference}.
     * <p>
     * This constructor is invoked by an anonymous subclass. It captures the
     * generic type argument {@code T} by inspecting the direct superclass of
     * the anonymous subclass, which will be this {@link TypeReference} with
     * {@code T} bound to a specific type.
     * </p>
     */
    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        ParameterizedType reference = (ParameterizedType) superClass;
        Type[] typeArguments = reference.getActualTypeArguments();
        type = typeArguments[0];
    }

    /**
     * Gets the {@link Type} represented by this {@link TypeReference}.
     * <p>
     * This method returns the actual generic type {@code T} that was captured
     * when the anonymous subclass of this {@link TypeReference} was created.
     * </p>
     *
     * @return the captured generic {@link Type}.
     * @see Type
     */
    public Type getType() {
        return type;
    }
}
