package autoparams.processor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.internal.reflect.RuntimeTypeResolver;

import static java.beans.Introspector.getBeanInfo;

/**
 * Processes an object by setting its writable properties using resolved values.
 * <p>
 * This processor inspects the target object type and uses JavaBeans property
 * descriptors to find writable properties. For each property with a setter,
 * it resolves a value using the provided {@link ResolutionContext} and assigns
 * it to the property via reflection.
 * </p>
 *
 * @see ObjectProcessor
 * @see ResolutionContext
 */
public final class InstancePropertyWriter implements ObjectProcessor {

    /**
     * Creates an instance of {@link InstancePropertyWriter}.
     */
    public InstancePropertyWriter() {
    }

    /**
     * Sets writable properties of the given object using values resolved from
     * the context.
     * <p>
     * This method inspects the type of the target object and finds all writable
     * properties. For each property with a setter, it resolves a value using
     * the provided {@link ResolutionContext} and assigns it to the property via
     * reflection.
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
        if (query.getType() instanceof Class<?>) {
            setProperties((Class<?>) query.getType(), value, context);
        } else {
            setProperties((ParameterizedType) query.getType(), value, context);
        }
    }

    private void setProperties(
        Class<?> type,
        Object value,
        ResolutionContext context
    ) {
        for (PropertyDescriptor property : getProperties(type)) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                Parameter parameter = setter.getParameters()[0];
                ParameterQuery query = new ParameterQuery(
                    parameter,
                    0,
                    parameter.getParameterizedType()
                );
                setProperty(value, setter, context.resolve(query));
            }
        }

        for (Method method : type.getMethods()) {
            if (isChainingSetter(method, type)) {
                Parameter parameter = method.getParameters()[0];
                ParameterQuery query = new ParameterQuery(
                    parameter,
                    0,
                    parameter.getParameterizedType()
                );
                setProperty(value, method, context.resolve(query));
            }
        }
    }

    private void setProperties(
        ParameterizedType type,
        Object value,
        ResolutionContext context
    ) {
        RuntimeTypeResolver typeResolver = RuntimeTypeResolver.create(type);
        for (PropertyDescriptor property : getProperties(type)) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                ObjectQuery query = resolvePropertyQuery(setter, typeResolver);
                setProperty(value, setter, context.resolve(query));
            }
        }

        Class<?> rawType = (Class<?>) type.getRawType();
        for (Method method : rawType.getMethods()) {
            if (isChainingSetter(method, rawType)) {
                ObjectQuery query = resolvePropertyQuery(method, typeResolver);
                setProperty(value, method, context.resolve(query));
            }
        }
    }

    private static PropertyDescriptor[] getProperties(ParameterizedType type) {
        return getProperties((Class<?>) type.getRawType());
    }

    private static PropertyDescriptor[] getProperties(Class<?> type) {
        try {
            return getBeanInfo(type).getPropertyDescriptors();
        } catch (IntrospectionException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static ObjectQuery resolvePropertyQuery(
        Method setter,
        RuntimeTypeResolver runtimeTypeResolver
    ) {
        Parameter parameter = setter.getParameters()[0];
        Type propertyType = parameter.getParameterizedType();
        Type runtimePropertyType = runtimeTypeResolver.resolve(propertyType);
        return new ParameterQuery(parameter, 0, runtimePropertyType);
    }

    private static boolean isChainingSetter(Method method, Class<?> type) {
        return method.getName().startsWith("set") &&
               method.getParameterCount() == 1 &&
               method.getReturnType().equals(type) &&
               !isAlreadyDetectedBySetter(method, type);
    }

    private static boolean isAlreadyDetectedBySetter(Method method, Class<?> type) {
        for (PropertyDescriptor property : getProperties(type)) {
            if (method.equals(property.getWriteMethod())) {
                return true;
            }
        }
        return false;
    }

    private static void setProperty(
        Object instance,
        Method setter,
        Object propertyValue
    ) {
        try {
            setter.invoke(instance, propertyValue);
        } catch (IllegalAccessException |
                 InvocationTargetException exception) {
            throw new RuntimeException(exception);
        }
    }
}
