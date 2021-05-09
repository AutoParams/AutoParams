package org.javaunit.autoparams.customization;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectGenerator;
import org.javaunit.autoparams.generator.ObjectQuery;

public final class SettablePropertyWriter implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            ObjectContainer container = generator.generate(query, context);
            writeProperties(container.unwrapOrElseThrow(), context);
            return container;
        };
    }

    private static void writeProperties(Object obj, ObjectGenerationContext context) {
        try {
            Class<?> type = obj.getClass();
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
}
