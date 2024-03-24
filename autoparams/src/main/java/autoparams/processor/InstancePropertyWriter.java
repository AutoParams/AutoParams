package autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.generator.ObjectQuery;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import static java.beans.Introspector.getBeanInfo;

public final class InstancePropertyWriter implements ObjectProcessor {

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
            Method method = property.getWriteMethod();
            if (method != null) {
                Parameter parameter = method.getParameters()[0];
                ObjectQuery query = ObjectQuery.fromParameter(parameter);
                Object propertyValue = context.generate(query);
                try {
                    method.invoke(value, propertyValue);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void setProperties(
        ParameterizedType parameterizedType,
        Object value,
        ResolutionContext context
    ) {
        Map<TypeVariable<?>, Type> typeMap = getTypeMap(parameterizedType);
        Class<?> type = (Class<?>) parameterizedType.getRawType();
        for (PropertyDescriptor property : getProperties(type)) {
            Method method = property.getWriteMethod();
            if (method != null) {
                ObjectQuery query = resolveArgumentQuery(method, typeMap);
                Object propertyValue = context.generate(query);
                try {
                    method.invoke(value, propertyValue);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static Map<TypeVariable<?>, Type> getTypeMap(
        ParameterizedType parameterizedType
    ) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        TypeVariable<?>[] typeVariables = rawType.getTypeParameters();
        Type[] typeValues = parameterizedType.getActualTypeArguments();

        HashMap<TypeVariable<?>, Type> map = new HashMap<>();
        for (int i = 0; i < typeVariables.length; i++) {
            map.put(typeVariables[i], typeValues[i]);
        }

        return map;
    }

    private static ObjectQuery resolveArgumentQuery(
        Method method,
        Map<TypeVariable<?>, Type> typeMap
    ) {
        Type parameterType = method.getGenericParameterTypes()[0];
        return ObjectQuery.fromType(parameterType instanceof TypeVariable
            && typeMap.containsKey((TypeVariable<?>) parameterType)
            ? typeMap.get((TypeVariable<?>) parameterType)
            : method.getParameterTypes()[0]);
    }

    private static PropertyDescriptor[] getProperties(Class<?> type) {
        try {
            return getBeanInfo(type).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
}
