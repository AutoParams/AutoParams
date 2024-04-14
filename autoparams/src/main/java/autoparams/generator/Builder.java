package autoparams.generator;

import java.util.List;
import java.util.stream.Stream;

@Deprecated
public final class Builder<T> {

    private final Factory<T> factory;

    public Builder(Factory<T> factory) {
        this.factory = factory;
    }

    public <U> Builder<T> fix(Class<U> type, U value) {
        ObjectGenerator generator = (query, context) ->
            query.getType() == type
                ? new ObjectContainer(value)
                : ObjectContainer.EMPTY;
        factory.applyCustomizer(generator);
        return this;
    }

    public T build() {
        return factory.get();
    }

    public List<T> buildRange(int size) {
        return factory.getRange(size);
    }

    public Stream<T> stream() {
        return factory.stream();
    }
}
