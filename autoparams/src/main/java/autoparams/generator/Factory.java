package autoparams.generator;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import autoparams.DefaultObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customizer;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public final class Factory<T> implements Supplier<T> {

    private final Type type;
    private final ResolutionContext context;

    private Factory(Type type, ResolutionContext context) {
        this.type = type;
        this.context = context;
    }

    static <T> Factory<T> create(Type type, ResolutionContext context) {
        return new Factory<>(type, context);
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
            : new Factory<>(type, context.branch(customizers));
        return Stream.generate(factory);
    }

    public List<T> getRange(int size, Customizer... customizers) {
        List<T> results = stream(customizers).limit(size).collect(toList());
        return unmodifiableList(results);
    }

    public void applyCustomizer(Customizer customizer) {
        context.applyCustomizer(customizer);
    }
}
