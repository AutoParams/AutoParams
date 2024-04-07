package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsSource;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(MethodAutoArgumentsProvider.class)
public @interface MethodAutoSource {

    String[] value() default { "" };

    class ProxyFactory {

        public static MethodAutoSource create(String[] value) {
            return (MethodAutoSource) Proxy.newProxyInstance(
                MethodAutoSource.class.getClassLoader(),
                new Class[] { MethodAutoSource.class },
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "annotationType": return MethodAutoSource.class;
                        case "value": return value;
                        default: return method.getDefaultValue();
                    }
                }
            );
        }
    }
}
