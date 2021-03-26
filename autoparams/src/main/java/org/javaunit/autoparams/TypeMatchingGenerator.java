package org.javaunit.autoparams;

import static org.javaunit.autoparams.GenerationResult.absence;
import static org.javaunit.autoparams.GenerationResult.presence;

import java.util.Optional;
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
        return type -> {
            for (int i = 0; i < types.length; i++) {
                if (type.equals(types[i])) {
                    return true;
                }
            }

            return false;
        };
    }

    @Override
    public final GenerationResult generateObject(
        ObjectQuery query,
        ObjectGenerationContext context
    ) {
        return predicate.apply(query.getType()) ? presence(factory.get()) : absence();
    }

    @Override
    public Optional<Object> generate(ObjectQuery query, ObjectGenerationContext context) {
        String message = "This method is not supported. Use generateObject method instead.";
        throw new UnsupportedOperationException(message);
    }

}
