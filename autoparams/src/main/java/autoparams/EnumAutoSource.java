package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EnumSource;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(EnumAutoArgumentsProvider.class)
public @interface EnumAutoSource {

    Class<? extends Enum<?>> value();

    String[] names() default { };

    EnumSource.Mode mode() default EnumSource.Mode.INCLUDE;

    class ProxyFactory {

        public static EnumAutoSource create(
            Class<? extends Enum<?>> value,
            String[] names,
            EnumSource.Mode mode
        ) {
            return (EnumAutoSource) Proxy.newProxyInstance(
                EnumAutoSource.class.getClassLoader(),
                new Class[] { EnumAutoSource.class },
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "annotationType": return EnumAutoSource.class;
                        case "value": return value;
                        case "names": return names;
                        case "mode": return mode;
                        default: return method.getDefaultValue();
                    }
                }
            );
        }
    }
}
