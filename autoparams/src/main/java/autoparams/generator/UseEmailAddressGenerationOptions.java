package autoparams.generator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import autoparams.customization.CustomizerSource;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({ ANNOTATION_TYPE, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@CustomizerSource(EmailAddressGenerationOptionsProviderFactory.class)
public @interface UseEmailAddressGenerationOptions {

    String[] domains() default { "test.com" };
}
