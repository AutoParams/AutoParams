package autoparams.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import autoparams.customization.CustomizerSource;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Specifies options for generating email address strings when used with
 * AutoParams.
 * <p>
 * This annotation allows users to define a custom set of domains that will be
 * used by {@link EmailAddressGenerationOptions} when AutoParams generates email
 * address strings.
 * </p>
 * <p>
 * When a test method, parameter, or another annotation is marked with
 * {@link UseEmailAddressGenerationOptions @UseEmailAddressGenerationOptions},
 * the specified values for domains will override the default email address
 * generation behavior for the scope where the annotation is applied.
 * </p>
 *
 * @see EmailAddressGenerationOptions
 * @see CustomizerSource
 */
@Target({ ANNOTATION_TYPE, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(EmailAddressGenerationOptionsProviderFactory.class)
public @interface UseEmailAddressGenerationOptions {

    /**
     * Specifies the domains to be used for generating email address strings.
     * <p>
     * Defaults to {@code { "test.com" }}.
     * </p>
     *
     * @return an array of domain strings.
     */
    String[] domains() default { "test.com" };
}
