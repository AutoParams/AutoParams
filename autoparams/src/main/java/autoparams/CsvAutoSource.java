package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(CsvAutoArgumentsProvider.class)
public @interface CsvAutoSource {

    String[] value() default { };

    String textBlock() default "";

    boolean useHeadersInDisplayName() default false;

    char quoteCharacter() default '\'';

    char delimiter() default '\0';

    String delimiterString() default "";

    String emptyValue() default "";

    String[] nullValues() default { };

    int maxCharsPerColumn() default 4096;

    boolean ignoreLeadingAndTrailingWhitespace() default true;
}
