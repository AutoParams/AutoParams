package org.javaunit.autoparams.generator;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import org.javaunit.autoparams.Builder;

final class ComplexObjectGenerator implements ObjectGenerator {

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        try {
            if (query.getType() instanceof Class<?>
                && query.getType() != ArrayList.class
                && query.getType() != HashSet.class
                && query.getType() != HashMap.class
                && query.getType() != Builder.class) {
                Class<?> type = (Class<?>) query.getType();
                Constructor<?> constructor = ConstructorResolver
                    .compose(
                        t -> Arrays.stream(t.getConstructors())
                            .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
                            .sorted(Comparator.comparing(c -> c.getParameterCount()))
                            .findFirst(),
                        t -> Arrays.stream(t.getConstructors())
                            .sorted(Comparator.comparing(c -> c.getParameterCount()))
                            .findFirst()
                    )
                    .resolve(type)
                    .get();
                Parameter[] parameters = constructor.getParameters();
                Object[] arguments = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    arguments[i] = context.getGenerator().generate(
                        ObjectQuery.fromParameter(parameter),
                        context).unwrapOrElseThrow();
                }
                return new ObjectContainer(constructor.newInstance(arguments));
            }

            return ObjectContainer.EMPTY;
        } catch (Exception exception) {
            return ObjectContainer.EMPTY;
        }
    }

}
