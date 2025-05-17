package autoparams.customization.dsl;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.function.Function;

@FunctionalInterface
public interface FunctionDelegate<T, R> extends Function<T, R>, Serializable {

    /**
     * This method was mistakenly added during refactoring and is not intended
     * to be part of the API. It has been deprecated and should not be used.
     */
    @Deprecated
    default SerializedLambda getLambda() {
        return GetterDelegate.getLambda(this);
    }
}
