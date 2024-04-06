package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;

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

    class ProxyFactory {

        public static CsvAutoSource create(
            String[] value,
            String textBlock,
            boolean useHeadersInDisplayName,
            char quoteCharacter,
            char delimiter,
            String delimiterString,
            String emptyValue,
            String[] nullValues,
            int maxCharsPerColumn,
            boolean ignoreLeadingAndTrailingWhitespace
        ) {
            return (CsvAutoSource) Proxy.newProxyInstance(
                CsvAutoSource.class.getClassLoader(),
                new Class[] { CsvAutoSource.class },
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "annotationType": return CsvAutoSource.class;
                        case "value": return value;
                        case "textBlock": return textBlock;
                        case "useHeadersInDisplayName": return useHeadersInDisplayName;
                        case "quoteCharacter": return quoteCharacter;
                        case "delimiter": return delimiter;
                        case "delimiterString": return delimiterString;
                        case "emptyValue": return emptyValue;
                        case "nullValues": return nullValues;
                        case "maxCharsPerColumn": return maxCharsPerColumn;
                        case "ignoreLeadingAndTrailingWhitespace":
                            return ignoreLeadingAndTrailingWhitespace;
                        default: return method.getDefaultValue();
                    }
                }
            );
        }
    }
}
