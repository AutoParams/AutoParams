package org.javaunit.autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.javaunit.autoparams.customization.ArgumentProcessing;
import org.javaunit.autoparams.customization.Fix;

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
