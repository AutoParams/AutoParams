package org.javaunit.autoparams;

import static org.javaunit.autoparams.GenerationResult.absence;
import static org.javaunit.autoparams.GenerationResult.presence;

import java.util.function.Function;
import java.util.function.Supplier;

class TypeMatchingGenerator implements ObjectGenerator {

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
    public final GenerationResult generate(ObjectQuery query, ObjectGenerationContext context) {
        return predicate.apply(query.getType()) ? presence(factory.get()) : absence();
    }

}
