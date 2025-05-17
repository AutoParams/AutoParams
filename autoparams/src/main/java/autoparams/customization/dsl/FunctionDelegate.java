package autoparams.customization.dsl;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface FunctionDelegate<T, R> extends Function<T, R>, Serializable {
}
