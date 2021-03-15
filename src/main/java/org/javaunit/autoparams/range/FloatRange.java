package org.javaunit.autoparams.range;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface FloatRange {
    float min() default 0.0F;
    float max() default Float.MAX_VALUE;
}
