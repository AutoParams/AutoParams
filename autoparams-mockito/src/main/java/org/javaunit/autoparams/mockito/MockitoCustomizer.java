package org.javaunit.autoparams.mockito;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerator;

public final class MockitoCustomizer implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> isAbstract(query.getType())
            ? new ObjectContainer(mock((Class<?>) query.getType()))
            : generator.generate(query, context);
    }

    private boolean isAbstract(Type type) {
        return type instanceof Class<?> ? isAbstract((Class<?>) type) : false;
    }

    private boolean isAbstract(Class<?> type) {
        return type.isInterface() || Modifier.isAbstract(type.getModifiers());
    }

}
