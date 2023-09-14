package autoparams.generator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public final class TypeMatchingGenerator implements ObjectGenerator {

    private final Function<Type, Boolean> predicate;
    private final Function<ObjectGenerationContext, Object> factory;

    public TypeMatchingGenerator(
        Function<Type, Boolean> predicate,
        Function<ObjectGenerationContext, Object> factory
    ) {
        this.predicate = predicate;
        this.factory = factory;
    }

    public TypeMatchingGenerator(
        Supplier<Object> factory,
        Class<?>... candidates
    ) {
        this(buildPredicateWithTypes(candidates), context -> factory.get());
    }

    public TypeMatchingGenerator(
        Function<ObjectGenerationContext, Object> factory,
        Class<?>... candidates
    ) {
        this(buildPredicateWithTypes(candidates), factory);
    }

    private static Function<Type, Boolean> buildPredicateWithTypes(Class<?>... candidates) {
        return type -> Arrays.stream(candidates).anyMatch(candidate -> match(type, candidate));
    }

    private static boolean match(Type type, Class<?> candidate) {
        return type.equals(candidate)
            || (type instanceof ParameterizedType && match((ParameterizedType) type, candidate));
    }

    private static boolean match(ParameterizedType type, Class<?> candidate) {
        return type.getRawType().equals(candidate);
    }

    public static <T> TypeMatchingGenerator create(
        Class<?> type,
        Supplier<T> factory
    ) {
        return new TypeMatchingGenerator(factory::get, type);
    }

    public static <T> TypeMatchingGenerator create(
        Class<?> type,
        Function<ObjectGenerationContext, T> factory
    ) {
        return new TypeMatchingGenerator(factory::apply, type);
    }

    @Override
    public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
        Type type = query.getType();

        return predicate.apply(type)
            ? new ObjectContainer(factory.apply(context))
            : ObjectContainer.EMPTY;
    }
}
