package autoparams.generator;

public final class Designer<T> {

    private final Class<T> type;

    Designer(Class<T> type) {
        this.type = type;
    }

    public T create() {
        return Factory.create(type).get();
    }
}
