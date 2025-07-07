package autoparams.internal.reflect;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import autoparams.customization.dsl.FunctionDelegate;

import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;

/**
 * Represents a property with its declaring class, type, and name.
 * <p>
 * This is code for internal implementation purposes and is not safe for
 * external use because its interface and behavior can change at any time.
 * </p>
 *
 * @param <T> the declaring class type
 * @param <R> the property type
 * @see FunctionDelegate
 */
public final class Property<T, R> {

    private final Class<T> declaringClass;
    private final Class<R> type;
    private final String name;

    private Property(Class<T> declaringClass, Class<R> type, String name) {
        this.declaringClass = declaringClass;
        this.type = type;
        this.name = name;
    }

    /**
     * Creates a Property instance from a function delegate representing a getter method.
     * <p>
     * This method extracts property information from function delegates that
     * reference getter methods, supporting both Java and Kotlin implementations.
     * </p>
     * <p>
     * This is code for internal implementation purposes and is not safe for
     * external use because its interface and behavior can change at any time.
     * </p>
     *
     * @param <T> the type containing the getter method
     * @param <R> the return type of the getter method
     * @param getterDelegate a function delegate representing a getter method
     * @return a Property instance for the property
     * @throws RuntimeException if property parsing fails
     * @see FunctionDelegate
     */
    @SuppressWarnings("unchecked")
    public static <T, R> Property<T, R> parse(
        FunctionDelegate<T, R> getterDelegate
    ) {
        Method getter = getGetter(getterDelegate);
        String propertyName = inferPropertyNameFromGetter(getter);
        Class<T> declaringClass = (Class<T>) getter.getDeclaringClass();
        Class<R> returnType = (Class<R>) getter.getReturnType();
        return new Property<>(declaringClass, returnType, propertyName);
    }

    /**
     * Returns the class that declares this property.
     * <p>
     * This is code for internal implementation purposes and is not safe for
     * external use because its interface and behavior can change at any time.
     * </p>
     *
     * @return the declaring class of the property
     */
    public Class<T> getDeclaringClass() {
        return declaringClass;
    }

    /**
     * Returns the type of this property.
     * <p>
     * This is code for internal implementation purposes and is not safe for
     * external use because its interface and behavior can change at any time.
     * </p>
     *
     * @return the type of the property
     */
    public Class<R> getType() {
        return type;
    }

    /**
     * Returns the name of this property.
     * <p>
     * This is code for internal implementation purposes and is not safe for
     * external use because its interface and behavior can change at any time.
     * </p>
     *
     * @return the name of the property
     */
    public String getName() {
        return name;
    }

    private static <T, R> Method getGetter(FunctionDelegate<T, R> delegate) {
        SerializedLambda lambda = getLambda(delegate);
        return isInKotlin(lambda)
            ? getKotlinGetter(lambda)
            : getJavaGetter(lambda);
    }

    private static <T, R> SerializedLambda getLambda(
        FunctionDelegate<T, R> delegate
    ) {
        try {
            Method writeReplace = delegate
                .getClass()
                .getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(delegate);
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static boolean isInKotlin(SerializedLambda lambda) {
        return lambda.getCapturedArgCount() == 1;
    }

    private static Method getKotlinGetter(SerializedLambda lambda) {
        Object arg = lambda.getCapturedArg(0);
        Object owner = invokeGetter(arg, "getOwner");
        Class<?> componentType = invokeGetter(owner, "getJClass");
        String signature = invokeGetter(arg, "getSignature");
        String getterName = signature.substring(0, signature.indexOf('('));
        return getMethod(componentType, getterName);
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeGetter(Object instance, String getterName) {
        try {
            Method getter = instance.getClass().getMethod(getterName);
            return (T) getter.invoke(instance);
        } catch (NoSuchMethodException |
                 IllegalAccessException |
                 InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Method getJavaGetter(SerializedLambda lambda) {
        try {
            String componentTypeName = lambda.getImplClass().replace('/', '.');
            Class<?> componentType = Class.forName(componentTypeName);
            return getMethod(componentType, lambda.getImplMethodName());
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Method getMethod(Class<?> type, String methodName) {
        try {
            return type.getMethod(methodName);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static String inferPropertyNameFromGetter(Method getter) {
        return decapitalizeHead(removeGetterPrefix(getter.getName()));
    }

    private static String removeGetterPrefix(String getterName) {
        if (hasIsPrefix(getterName)) {
            return getterName.substring(2);
        } else if (hasGetPrefix(getterName)) {
            return getterName.substring(3);
        } else {
            return getterName;
        }
    }

    private static boolean hasIsPrefix(String methodName) {
        return methodName.startsWith("is")
            && methodName.length() > 2
            && isUpperCase(methodName.charAt(2));
    }

    private static boolean hasGetPrefix(String methodName) {
        return methodName.startsWith("get")
            && methodName.length() > 3
            && isUpperCase(methodName.charAt(3));
    }

    private static String decapitalizeHead(String s) {
        char head = s.charAt(0);
        return isUpperCase(head) ? toLowerCase(head) + s.substring(1) : s;
    }
}
