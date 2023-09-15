package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(EnumAutoArgumentsProvider.class)
public @interface EnumAutoSource {

    Class<? extends Enum<?>> value();

    String[] names() default {};

    EnumSource.Mode mode() default EnumSource.Mode.INCLUDE;
}
