package autoparams.customization;

import java.util.function.Predicate;

final class Negative<T> implements Predicate<T> {

    @Override
    public boolean test(T t) {
        return false;
    }
}
