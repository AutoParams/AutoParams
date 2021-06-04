package org.javaunit.autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.javaunit.autoparams.customization.ArgumentProcessing;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentProcessing(FixedArgumentProcessor.class)
public @interface Fixed {
}
