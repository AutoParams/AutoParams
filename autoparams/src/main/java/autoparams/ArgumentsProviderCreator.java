package autoparams;

import java.lang.reflect.AccessibleObject;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

final class ArgumentsProviderCreator {

    public static ArgumentsProvider createProvider(Class<?> sourceType) {
        ArgumentsSource source = sourceType.getAnnotation(ArgumentsSource.class);
        Class<? extends ArgumentsProvider> providerType = source.value();
        try {
            return makeAccessible(providerType.getDeclaredConstructor()).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends AccessibleObject> T makeAccessible(T object) {
        object.setAccessible(true);
        return object;
    }
}
