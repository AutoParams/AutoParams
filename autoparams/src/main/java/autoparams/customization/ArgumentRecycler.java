package autoparams.customization;

import java.lang.reflect.Parameter;
import java.util.function.BiFunction;

/**
 * Interface for recycling arguments into customizers during test parameter
 * resolution.
 * <p>
 * This functional interface defines a contract for creating {@link Customizer}
 * instances based on previously resolved arguments and their parameter
 * metadata. It enables the implementation of value reuse strategies that can
 * influence how the following parameters are generated in AutoParams.
 * </p>
 * <p>
 * Implementations of this interface are typically used with the
 * {@link RecycleArgument} annotation to specify how a parameter's value should
 * be recycled into the resolution process for other parameters.
 * </p>
 *
 * @see Customizer
 * @see RecycleArgument
 */
@FunctionalInterface
public interface ArgumentRecycler
    extends BiFunction<Object, Parameter, Customizer> {

    /**
     * Creates a customizer from the given argument and parameter.
     * <p>
     * This method transforms a previously resolved argument and its parameter
     * metadata into a {@link Customizer} that can influence how the following
     * parameters are generated or processed in AutoParams.
     * </p>
     * <p>
     * The generated customizer typically controls value reuse patterns
     * by specifying when and how the argument should be recycled in the
     * resolution process.
     * </p>
     *
     * @param argument the resolved value from a previous parameter
     * @param parameter the metadata of the parameter that produced the argument
     * @return a customizer that incorporates the argument into the resolution
     *         process for the following parameters
     */
    Customizer recycle(Object argument, Parameter parameter);

    /**
     * Implementation of the {@link BiFunction#apply} method from the
     * {@link BiFunction} interface.
     * <p>
     * This method delegates to the {@link #recycle} method, providing
     * compatibility with the functional interface contract. It allows
     * {@link ArgumentRecycler} implementations to be used in contexts
     * that expect a {@link BiFunction}.
     * </p>
     *
     * @param argument the resolved value from a previous parameter
     * @param parameter the metadata of the parameter that produced the argument
     * @return a customizer that incorporates the argument into the resolution
     *         process for the following parameters
     * @see #recycle
     */
    @Override
    default Customizer apply(Object argument, Parameter parameter) {
        return recycle(argument, parameter);
    }
}
