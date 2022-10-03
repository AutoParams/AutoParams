package autoparams.customization;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectQuery;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public final class SettablePropertyWriter implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            ObjectContainer container = generator.generate(query, context);
            if (query.getType() instanceof Class<?>) {
                writePropertiesNonGeneric(
                    (Class<?>) query.getType(), container.unwrapOrElseThrow(), context);
            } else if (query.getType() instanceof ParameterizedType) {
                writePropertiesGeneric(
                    (ParameterizedType) query.getType(), container.unwrapOrElseThrow(), context);
            }
            return container;
        };
    }

    private static void writePropertiesNonGeneric(
        Class<?> type,
        Object obj,
        ObjectGenerationContext context
    ) {
        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(type)
                .getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                Method method = descriptor.getWriteMethod();
                if (method != null) {
                    Parameter parameter = method.getParameters()[0];
                    ObjectQuery writerValueQuery = ObjectQuery.fromParameter(parameter);
                    Object writerValue = context.generate(writerValueQuery);
                    method.invoke(obj, writerValue);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void writePropertiesGeneric(
        ParameterizedType parameterizedType,
        Object obj,
        ObjectGenerationContext context
    ) {
        try {
            Class<?> type = (Class<?>) parameterizedType.getRawType();
            Map<TypeVariable<?>, Type> genericMap = getGenericMap(type, parameterizedType);

            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(type)
                .getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                Method method = descriptor.getWriteMethod();
                if (method != null) {
                    ObjectQuery query = resolveArgumentQuery(method, genericMap);
                    Object writerValue = context.generate(query);
                    method.invoke(obj, writerValue);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Map<TypeVariable<?>, Type> getGenericMap(
        Class<?> type,
        ParameterizedType parameterizedType
    ) {
        HashMap<TypeVariable<?>, Type> map = new HashMap<>();

        TypeVariable<?>[] typeVariables = type.getTypeParameters();
        Type[] typeValues = parameterizedType.getActualTypeArguments();
        for (int i = 0; i < typeVariables.length; i++) {
            map.put(typeVariables[i], typeValues[i]);
        }

        return map;
    }

    private static ObjectQuery resolveArgumentQuery(
        Method method,
        Map<TypeVariable<?>, Type> genericMap
    ) {
        Type parameterType = method.getGenericParameterTypes()[0];
        return parameterType instanceof TypeVariable
            && genericMap.containsKey((TypeVariable<?>) parameterType)
            ? ObjectQuery.fromType(genericMap.get((TypeVariable<?>) parameterType))
            : ObjectQuery.fromType(method.getParameterTypes()[0]);
    }
}
