package autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Proxy;

import org.junit.jupiter.params.provider.ArgumentsSource;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(ValueAutoArgumentsProvider.class)
public @interface ValueAutoSource {

    short[] shorts() default { };

    byte[] bytes() default { };

    int[] ints() default { };

    long[] longs() default { };

    float[] floats() default { };

    double[] doubles() default { };

    char[] chars() default { };

    boolean[] booleans() default { };

    String[] strings() default { };

    Class<?>[] classes() default { };

    class ProxyFactory {

        public static ValueAutoSource create(
            short[] shorts,
            byte[] bytes,
            int[] ints,
            long[] longs,
            float[] floats,
            double[] doubles,
            char[] chars,
            boolean[] booleans,
            String[] strings,
            Class<?>[] classes
        ) {
            return (ValueAutoSource) Proxy.newProxyInstance(
                ValueAutoSource.class.getClassLoader(),
                new Class[] { ValueAutoSource.class },
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "annotationType": return ValueAutoSource.class;
                        case "shorts": return shorts;
                        case "bytes": return bytes;
                        case "ints": return ints;
                        case "longs": return longs;
                        case "floats": return floats;
                        case "doubles": return doubles;
                        case "chars": return chars;
                        case "booleans": return booleans;
                        case "strings": return strings;
                        case "classes": return classes;
                        default: return method.getDefaultValue();
                    }
                }
            );
        }
    }
}
