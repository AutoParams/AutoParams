package autoparams;

import autoparams.customization.ArgumentProcessing;
import autoparams.customization.Fix;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation reuse the argument for the same type as the parameter type.
 *
 * @deprecated This annotation is replaced with {@link Fix} annotation.
 */
@Deprecated
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentProcessing(FixedArgumentProcessor.class)
public @interface Fixed {
}
