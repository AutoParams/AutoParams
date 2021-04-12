package org.javaunit.autoparams.generator;

import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.function.Supplier;

final class TypeMatchingGenerator implements ObjectGenerator {

    private final Function<Class<?>, Boolean> predicate;
    private final Supplier<Object> factory;

    public TypeMatchingGenerator(Function<Class<?>, Boolean> predicate, Supplier<Object> factory) {
        this.predicate = predicate;
        this.factory = factory;
    }

    public TypeMatchingGenerator(Supplier<Object> factory, Class<?>... types) {
        this(buildPredicateWithTypes(types), factory);
    }

    private static Function<Class<?>, Boolean> buildPredicateWithTypes(Class<?>... types) {
        return queryType -> {
            for (Class<?> type : types) {
                if (queryType.equals(type)) {
                    return true;
                }
            }

            return false;
        };
    }

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();

        if (!(type instanceof Class<?>)) {
            return ObjectContainer.EMPTY;
        }

        return predicate.apply((Class<?>) type)
            ? new ObjectContainer(factory.get())
            : ObjectContainer.EMPTY;
    }
}
