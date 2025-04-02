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
