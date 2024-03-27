package test.autoparams.customization;

public class Entity<T> extends Versioned {

    private T id;

    public T getId() {
        return id;
    }
}
