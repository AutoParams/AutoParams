package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

final class ArgumentsProviderCreator {

    public static ArgumentsProvider createArgumentsProvider(
        Class<? extends Annotation> source
    ) {
        Class<? extends ArgumentsProvider> provider = source
            .getAnnotation(ArgumentsSource.class)
            .value();
        return createInstance(provider);
    }

    private static <T> T createInstance(Class<? extends T> type) {
        try {
            return getConstructor(type).newInstance();
        } catch (NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException exception
        ) {
            throw new RuntimeException(exception);
        }
    }

    private static <T> Constructor<? extends T> getConstructor(
        Class<? extends T> type
    ) throws NoSuchMethodException {
        Constructor<? extends T> constructor = type.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor;
    }
}
