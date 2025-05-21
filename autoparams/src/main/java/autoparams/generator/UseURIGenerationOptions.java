package autoparams.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URI;

import autoparams.customization.CustomizerSource;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Specifies options for generating {@link URI} objects when used with
 * AutoParams.
 * <p>
 * This annotation allows users to define a custom set of schemes, hosts, and
 * ports that will be used by {@link URIGenerationOptions} when AutoParams
 * generates {@link URI} instances.
 * </p>
 * <p>
 * When a test method, parameter, or another annotation is marked with
 * {@link UseURIGenerationOptions @UseURIGenerationOptions}, the specified
 * values for schemes, hosts, and ports will override the default URI generation
 * behavior for the scope where the annotation is applied.
 * </p>
 *
 * @see URIGenerationOptions
 */
@Target({ ANNOTATION_TYPE, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(URIGenerationOptionsProviderFactory.class)
public @interface UseURIGenerationOptions {

    /**
     * Specifies the {@link URI} schemes to be used for generation.
     * <p>
     * Defaults to {@code { "https" }}.
     * </p>
     *
     * @return an array of {@link URI} scheme strings.
     */
    String[] schemes() default { "https" };

    /**
     * Specifies the host names to be used for {@link URI} generation.
     * <p>
     * Defaults to {@code { "test.com" }}.
     * </p>
     *
     * @return an array of host name strings.
     */
    String[] hosts() default { "test.com" };

    /**
     * Specifies the port numbers to be used for {@link URI} generation.
     * <p>
     * Defaults to an empty array, which means the default port for the selected
     * scheme will be used.
     * </p>
     *
     * @return an array of port numbers.
     */
    int[] ports() default { };
}
