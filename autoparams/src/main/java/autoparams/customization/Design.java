package autoparams.customization;

import autoparams.ResolutionContext;

public class Design<T> {

    private final Class<T> type;

    private Design(Class<T> type) {
        this.type = type;
    }

    public static <T> Design<T> of(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("The argument 'type' must not be null");
        }
        return new Design<>(type);
    }

    public T instantiate() {
        ResolutionContext context = new ResolutionContext();
        return context.resolve(type);
    }
}
