package org.javaunit.autoparams.mockito;

import static org.mockito.Mockito.mock;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerator;

public final class MockitoCustomizer implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) ->
            generator.generate(query, context).yieldIfEmpty(() -> generate(query.getType()));
    }

    private ObjectContainer generate(Type type) {
        return type instanceof Class<?> ? generate((Class<?>) type)
            : type instanceof ParameterizedType ? generate((ParameterizedType) type)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(Class<?> type) {
        return new ObjectContainer(mock(type));
    }

    private ObjectContainer generate(ParameterizedType parameterizedType) {
        Type type = parameterizedType.getRawType();
        return type instanceof Class<?> ? generate((Class<?>) type) : ObjectContainer.EMPTY;
    }

}
