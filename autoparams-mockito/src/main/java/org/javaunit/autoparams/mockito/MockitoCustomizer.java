package org.javaunit.autoparams.mockito;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        return isCollection(type) == false && isAbstract(type)
            ? new ObjectContainer(mock(type))
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterizedType parameterizedType) {
        Type type = parameterizedType.getRawType();
        return type instanceof Class<?> ? generate((Class<?>) type) : ObjectContainer.EMPTY;
    }

    private boolean isCollection(Class<?> type) {
        return type.equals(Iterable.class)
            || type.equals(Collection.class)
            || type.equals(AbstractCollection.class)
            || type.equals(List.class)
            || type.equals(AbstractList.class)
            || type.equals(Set.class)
            || type.equals(AbstractSet.class)
            || type.equals(Map.class)
            || type.equals(AbstractMap.class);
    }

    private boolean isAbstract(Class<?> type) {
        return type.isInterface() || isAbstractClass(type);
    }

    private boolean isAbstractClass(Class<?> type) {
        return type.isPrimitive() == false
            && type.isArray() == false
            && Modifier.isAbstract(type.getModifiers());
    }

}
