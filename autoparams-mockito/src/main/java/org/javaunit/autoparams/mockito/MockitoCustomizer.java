package org.javaunit.autoparams.mockito;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerator;

public final class MockitoCustomizer implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) ->
            generate(query.getType()).yieldIfEmpty(() -> generator.generate(query, context));
    }

    private ObjectContainer generate(Type type) {
        return type instanceof Class<?> ? generate((Class<?>) type)
            : type instanceof ParameterizedType ? generate((ParameterizedType) type)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(Class<?> type) {
        return isAbstract(type) ? new ObjectContainer(mock(type)) : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType parameterizedType) {
        Type type = parameterizedType.getRawType();
        return type instanceof Class<?> ? generate((Class<?>) type) : ObjectContainer.EMPTY;
    }

    private boolean isAbstract(Class<?> type) {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

}
