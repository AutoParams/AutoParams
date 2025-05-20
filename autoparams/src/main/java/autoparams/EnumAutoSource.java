package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * An annotation that provides a source of test enum constants combined with
 * automatic parameter generation.
 * <p>
 * This annotation is similar to JUnit Jupiter's {@link EnumSource} but extends
 * its functionality by automatically generating values for any additional
 * parameters in the test method that are not covered by the enum constants.
 * </p>
 *
 * <p>
 * The {@link EnumAutoSource} annotation works in two main ways:
 * </p>
 * <ol>
 *   <li>
 *       It supplies enum constants to the first parameter of the test method,
 *       similar to {@link EnumSource}
 *   </li>
 *   <li>
 *       It automatically generates values for any remaining parameters using
 *       AutoParams
 *   </li>
 * </ol>
 *
 * <p><b>Example:</b></p>
 * <p>
 * In this example, the test will run once for each value of the
 * {@link TimeUnit} enum, while the second parameter is automatically generated
 * by AutoParams for each run.
 * </p>
 *
 * <pre>
 * &#64;ParameterizedTest
 * &#64;EnumAutoSource(TimeUnit.class)
 * void testMethod(TimeUnit unit, String value) {
 *     assertNotNull(unit);
 *     assertNotNull(value);
 * }
 * </pre>
 *
 * @see AutoSource
 * @see EnumAutoArgumentsProvider
 * @see EnumSource
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(EnumAutoArgumentsProvider.class)
public @interface EnumAutoSource {

    /**
     * The enum class to provide constants from.
     *
     * @return the enum class
     * @see EnumSource#value
     */
    Class<? extends Enum<?>> value();

    /**
     * The enum constant names to filter on.
     * <p>
     * If no names are specified, all enum constants will be used.
     * </p>
     *
     * @return the enum constant names
     * @see EnumSource#names
     */
    String[] names() default { };

    /**
     * The enum constant selection mode.
     * <p>
     * Defaults to {@link EnumSource.Mode#INCLUDE INCLUDE}.
     * </p>
     *
     * @return the selection mode
     * @see EnumSource.Mode
     */
    EnumSource.Mode mode() default EnumSource.Mode.INCLUDE;

    /**
     * Factory for creating dynamic proxy instances of {@link EnumAutoSource}.
     * <p>
     * This utility class provides a way to programmatically create
     * {@link EnumAutoSource} annotation instances at runtime, which is useful
     * when extending the functionality of {@link EnumAutoArgumentsProvider}.
     * </p>
     *
     * @see EnumAutoArgumentsProvider#accept(EnumAutoSource)
     */
    class ProxyFactory {

        /**
         * Creates a dynamic proxy instance of {@link EnumAutoSource} with the
         * specified attributes.
         * <p>
         * This utility method enables programmatic creation of
         * {@link EnumAutoSource} annotation instances at runtime, which is
         * useful for extending functionality of
         * {@link EnumAutoArgumentsProvider}.
         * </p>
         *
         * @param value the enum class to provide constants from
         * @param names the enum constant names to filter on
         * @param mode the enum constant selection mode
         * @return a proxy instance of the {@link EnumAutoSource} annotation
         * @see EnumAutoArgumentsProvider#accept(EnumAutoSource)
         */
        public static EnumAutoSource create(
            Class<? extends Enum<?>> value,
            String[] names,
            EnumSource.Mode mode
        ) {
            return (EnumAutoSource) Proxy.newProxyInstance(
                EnumAutoSource.class.getClassLoader(),
                new Class[] { EnumAutoSource.class },
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "annotationType": return EnumAutoSource.class;
                        case "value": return value;
                        case "names": return names;
                        case "mode": return mode;
                        default: return method.getDefaultValue();
                    }
                }
            );
        }
    }
}
