package org.javaunit.autoparams.autofixture;

import org.javaunit.autoparams.autofixture.generators.DataGenerator;
import org.javaunit.autoparams.autofixture.generators.NotImplementDataGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoParam {
    Class<? extends DataGenerator> generator() default NotImplementDataGenerator.class;
}
