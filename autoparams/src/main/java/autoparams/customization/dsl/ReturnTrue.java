package autoparams.customization.dsl;

import java.util.function.Predicate;

final class ReturnTrue<T> implements Predicate<T> {

    @Override
    public boolean test(T t) {
        return true;
    }
}
