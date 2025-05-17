package autoparams.customization.dsl;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

@FunctionalInterface
public interface FunctionDelegate<T, R> extends Function<T, R>, Serializable {

    default SerializedLambda getLambda() {
        try {
            Method method = getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(this);
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
