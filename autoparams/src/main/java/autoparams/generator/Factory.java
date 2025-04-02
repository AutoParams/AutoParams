package autoparams.generator;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import autoparams.DefaultObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.type.TypeReference;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public final class Factory<T> implements Supplier<T> {

    private final ResolutionContext context;
    private final Type type;

    Factory(ResolutionContext context, Type type) {
        this.context = context;
        this.type = type;
    }

    public static <T> Factory<T> create(Class<T> type) {
        return new Factory<>(new ResolutionContext(), type);
    }

    public static <T> Factory<T> create(
        ResolutionContext context,
        Class<T> type
    ) {
        return new Factory<>(context, type);
    }

    public static <T> Factory<T> create(TypeReference<T> typeReference) {
        return new Factory<>(new ResolutionContext(), typeReference.getType());
    }

    public static <T> Factory<T> create(
        ResolutionContext context,
        TypeReference<T> typeReference
    ) {
        return new Factory<>(context, typeReference.getType());
    }

    @Override
    public T get() {
        return get(context);
    }

    public T get(Customizer... customizers) {
        ResolutionContext context = customizers.length == 0
            ? this.context
            : this.context.branch(customizers);
        return get(context);
    }

    @SuppressWarnings("unchecked")
    private T get(ResolutionContext context) {
        return (T) context.resolve(new DefaultObjectQuery(type));
    }

    public Stream<T> stream(Customizer... customizers) {
        Factory<T> factory = customizers.length == 0
            ? this
            : new Factory<>(context.branch(customizers), type);
        return Stream.generate(factory);
    }

    public List<T> getRange(int size, Customizer... customizers) {
        List<T> results = stream(customizers).limit(size).collect(toList());
        return unmodifiableList(results);
    }

    public void applyCustomizer(Customizer customizer) {
        context.applyCustomizer(customizer);
    }

    public void customize(Customizer... customizers) {
        context.customize(customizers);
    }
}
