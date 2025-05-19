package autoparams;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import autoparams.AnnotationScanner.Edge;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import static autoparams.AnnotationScanner.scanAnnotations;
import static java.util.stream.Collectors.toList;

/**
 * An implementation of {@link ArgumentsProvider} that automatically generates
 * test arguments.
 * <p>
 * This class provides the core functionality for the {@link AutoSource}
 * annotation, enabling parameterized tests with automatically generated values.
 * It can work independently or in combination with other argument providers.
 * </p>
 *
 * <p>
 * When used with {@link AutoSource}, it automatically generates values for all
 * parameters in a test method. When combined with other argument providers,
 * such as {@link ValueAutoArgumentsProvider}, {@link CsvAutoArgumentsProvider},
 * it fills in any parameters not explicitly provided.
 * </p>
 *
 * <p>
 * It also handles test repetition through the {@link Repeat} annotation,
 * allowing tests to be executed multiple times with different generated values.
 * </p>
 *
 * @see AutoSource
 * @see Repeat
 * @see ValueAutoArgumentsProvider
 * @see CsvAutoArgumentsProvider
 * @see MethodAutoArgumentsProvider
 * @see MethodAutoArgumentsProvider
 */
public class AutoArgumentsProvider implements ArgumentsProvider {

    private final ArgumentsProvider assetProvider;

    /**
     * Constructs a new {@link AutoArgumentsProvider} with a custom asset
     * provider.
     * <p>
     * This constructor allows combining {@link AutoArgumentsProvider} with
     * another {@link ArgumentsProvider} that supplies initial values (assets)
     * which can then be supplemented with automatically generated values.
     * </p>
     *
     * @param assetProvider the argument provider that supplies the initial
     *                      arguments
     */
    public AutoArgumentsProvider(ArgumentsProvider assetProvider) {
        this.assetProvider = assetProvider;
    }

    /**
     * Constructs a new {@link AutoArgumentsProvider} with a default empty asset
     * provider.
     * <p>
     * This constructor is used when {@link AutoArgumentsProvider} is used
     * standalone, such as with the {@link AutoSource} annotation, where all
     * arguments are to be generated automatically.
     * </p>
     */
    @SuppressWarnings("unused")
    public AutoArgumentsProvider() {
        this(context -> Stream.of(Arguments.of()));
    }

    /**
     * Provides arguments for a parameterized test.
     * <p>
     * This method processes any {@link Repeat} annotations to determine how
     * many times the test should be executed, collects initial arguments from
     * the asset provider, and then generates any missing arguments to complete
     * each test case.
     * </p>
     *
     * @param context the current extension context
     * @return a stream of arguments for parameterized test execution
     * @throws Exception if an error occurs while providing arguments
     */
    @Override
    public Stream<? extends Arguments> provideArguments(
        ExtensionContext context
    ) throws Exception {
        int repetition = getRepetition(context);
        Stream.Builder<Arguments> stream = Stream.builder();
        for (Arguments asset : getAssets(context)) {
            for (int i = 0; i < repetition; i++) {
                stream.accept(getTestCase(context, asset));
            }
        }

        return stream.build();
    }

    private static int getRepetition(ExtensionContext context) {
        int repetition = 0;
        Method method = context.getRequiredTestMethod();
        for (Edge<Repeat> edge : scanAnnotations(method, Repeat.class)) {
            repetition += edge.getCurrent().value();
        }

        return Math.max(1, repetition);
    }

    private List<? extends Arguments> getAssets(ExtensionContext context)
        throws Exception {
        return assetProvider.provideArguments(context).collect(toList());
    }

    private static Arguments getTestCase(
        ExtensionContext context,
        Arguments asset
    ) {
        return TestResolutionContext.create(context).getTestCase(asset);
    }
}
