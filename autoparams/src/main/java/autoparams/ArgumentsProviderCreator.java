package autoparams;

import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

final class ArgumentsProviderCreator {

    public static ArgumentsProvider createArgumentsProvider(Class<?> entry) {
        ArgumentsSource source = entry.getAnnotation(ArgumentsSource.class);
        Class<? extends ArgumentsProvider> provider = source.value();
        return Instantiator.instantiate(provider);
    }
}
