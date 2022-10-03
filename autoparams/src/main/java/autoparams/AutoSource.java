package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.params.provider.ArgumentsSource;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(AutoArgumentsProvider.class)
public @interface AutoSource {

    /**
     * This property will be removed. Use @Repeat annotation instead.
     *
     * @return Number of times to run the test repeatedly
     */
    @Deprecated
    int repeat() default 1;

}
