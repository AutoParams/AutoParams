package autoparams.customization.dsl;

import java.io.Serializable;
import java.util.function.Function;

/**
 * A serializable function interface that extends
 * {@link Function Function&lt;T, R&gt;} and {@link Serializable}.
 * <p>
 * This functional interface is designed to represent method references.
 * It leverages serialization capability to capture type information and method
 * signatures in a way that preserves type safety at compile time.
 * </p>
 * <p>
 * The interface is particularly useful when working with property references in
 * the AutoParams DSL. By using method references instead of string-based
 * property names, it enables type-safe property access and manipulation in the
 * DSL, which improves compile-time safety, enables IDE refactoring support, and
 * provides better discoverability of available properties.
 * </p>
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface FunctionDelegate<T, R> extends Function<T, R>, Serializable {
}
