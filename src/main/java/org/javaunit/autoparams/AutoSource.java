package org.javaunit.autoparams;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(AutoArgumentsProvider.class)
public @interface AutoSource {

    int repeat() default 1;
    Class<? extends ObjectGenerator>[] generators() default {};
}
