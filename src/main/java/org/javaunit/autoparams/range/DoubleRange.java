package org.javaunit.autoparams.range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface DoubleRange {
    double min() default 0.0D;
    double max() default Double.MAX_VALUE;
}
