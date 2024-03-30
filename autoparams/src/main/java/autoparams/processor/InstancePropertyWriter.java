package autoparams.processor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import autoparams.ResolutionContext;
import autoparams.generator.ObjectQuery;

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
                } catch (IllegalAccessException |
                         InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void setProperties(
        ParameterizedType type,
        Object value,
        ResolutionContext context
    ) {
        RuntimeTypeResolver typeResolver = RuntimeTypeResolver.of(type);
        for (PropertyDescriptor property : getProperties(type)) {
            Method method = property.getWriteMethod();
            if (method != null) {
                ObjectQuery query = resolveArgumentQuery(method, typeResolver);
                Object propertyValue = context.generate(query);
                try {
                    method.invoke(value, propertyValue);
                } catch (IllegalAccessException |
                         InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static PropertyDescriptor[] getProperties(
        ParameterizedType parameterizedType
    ) {
        return getProperties((Class<?>) parameterizedType.getRawType());
    }

    private static PropertyDescriptor[] getProperties(Class<?> type) {
        try {
            return getBeanInfo(type).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectQuery resolveArgumentQuery(
        Method method,
        RuntimeTypeResolver typeResolver
    ) {
        Type parameterType = method.getGenericParameterTypes()[0];
        return ObjectQuery.fromType(typeResolver.resolve(parameterType));
    }
}
