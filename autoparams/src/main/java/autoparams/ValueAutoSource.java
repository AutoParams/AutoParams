package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Annotation that provides a set of literal values for parameterized tests
 * while automatically generating values for any additional parameters.
 * <p>
 * This annotation works similarly to JUnit Jupiter's {@code ValueSource}
 * but extends its functionality by allowing AutoParams to supply values
 * for parameters not covered by the explicitly provided values.
 * </p>
 *
 * <p><b>Example:</b></p>
 * <p>
 * This example demonstrates how to use {@link ValueAutoSource} to provide
 * specific values for one parameter while automatically generating values
 * for other parameters.
 * </p>
 * <pre>
 * &#64;ParameterizedTest
 * &#64;ValueAutoSource(strings = { "Camera", "Candle" })
 * void testMethod(String name, UUID id, BigDecimal price) {
 *     // name parameter takes values from the strings array ("Camera", "Candle")
 *     // id and price parameters are automatically generated by AutoParams
 *     Product product = new Product(id, name, price);
 *     assertTrue(product.getName().startsWith("Ca"));
 * }
 * </pre>
 *
 * @see AutoSource
 * @see ValueAutoArgumentsProvider
 * @see ValueSource
 */
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ValueAutoArgumentsProvider.class)
public @interface ValueAutoSource {

    /**
     * The {@code short} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code short} values
     * @see ValueSource#shorts
     */
    short[] shorts() default { };

    /**
     * The {@code byte} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code byte} values
     * @see ValueSource#bytes
     */
    byte[] bytes() default { };

    /**
     * The {@code int} values to use as sources of arguments; must not be empty.
     *
     * @return an array of {@code int} values
     * @see ValueSource#ints
     */
    int[] ints() default { };

    /**
     * The {@code long} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code long} values
     * @see ValueSource#longs
     */
    long[] longs() default { };

    /**
     * The {@code float} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code float} values
     * @see ValueSource#floats
     */
    float[] floats() default { };

    /**
     * The {@code double} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code double} values
     * @see ValueSource#doubles
     */
    double[] doubles() default { };

    /**
     * The {@code char} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code char} values
     * @see ValueSource#chars
     */
    char[] chars() default { };

    /**
     * The {@code boolean} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code boolean} values
     * @see ValueSource#booleans
     */
    boolean[] booleans() default { };

    /**
     * The {@link String} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code String} values
     * @see ValueSource#strings
     */
    String[] strings() default { };

    /**
     * The {@link Class} values to use as sources of arguments; must not be
     * empty.
     *
     * @return an array of {@code Class} values
     * @see ValueSource#classes
     */
    Class<?>[] classes() default { };

    /**
     * Factory for creating dynamic proxy instances of {@link ValueAutoSource}.
     * <p>
     * This utility class provides a way to programmatically create
     * {@link ValueAutoSource} annotation instances at runtime, which is useful
     * when extending the functionality of {@link ValueAutoArgumentsProvider}.
     * </p>
     *
     * @see ValueAutoArgumentsProvider#accept(ValueAutoSource)
     */
    class ProxyFactory {

        /**
         * Do not use this constructor.
         *
         * @deprecated {@link ProxyFactory} provides a static factory method
         *             only.
         */
        @Deprecated
        public ProxyFactory() {
        }

        /**
         * Creates a dynamic proxy instance of {@link ValueAutoSource} with the
         * specified attributes.
         * <p>
         * This utility method enables programmatic creation of
         * {@link ValueAutoSource} annotation instances at runtime, which is
         * useful for extending functionality of
         * {@link ValueAutoArgumentsProvider}.
         * </p>
         *
         * @param shorts array of {@code short} values
         * @param bytes array of {@code byte} values
         * @param ints array of {@code int} values
         * @param longs array of {@code long} values
         * @param floats array of {@code float} values
         * @param doubles array of {@code double} values
         * @param chars array of {@code char} values
         * @param booleans array of {@code boolean} values
         * @param strings array of {@link String} values
         * @param classes array of {@link Class} values
         * @return a proxy instance of the {@link ValueAutoSource} annotation
         * @see ValueAutoArgumentsProvider#accept(ValueAutoSource)
         */
        public static ValueAutoSource create(
            short[] shorts,
            byte[] bytes,
            int[] ints,
            long[] longs,
            float[] floats,
            double[] doubles,
            char[] chars,
            boolean[] booleans,
            String[] strings,
            Class<?>[] classes
        ) {
            return (ValueAutoSource) Proxy.newProxyInstance(
                ValueAutoSource.class.getClassLoader(),
                new Class[] { ValueAutoSource.class },
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "annotationType": return ValueAutoSource.class;
                        case "shorts": return shorts;
                        case "bytes": return bytes;
                        case "ints": return ints;
                        case "longs": return longs;
                        case "floats": return floats;
                        case "doubles": return doubles;
                        case "chars": return chars;
                        case "booleans": return booleans;
                        case "strings": return strings;
                        case "classes": return classes;
                        default: return method.getDefaultValue();
                    }
                }
            );
        }
    }
}
