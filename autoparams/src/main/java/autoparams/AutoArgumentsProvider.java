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

class AutoArgumentsProvider implements ArgumentsProvider {

    private final ArgumentsProvider assetProvider;

    public AutoArgumentsProvider(ArgumentsProvider assetProvider) {
        this.assetProvider = assetProvider;
    }

    @SuppressWarnings("unused")
    public AutoArgumentsProvider() {
        this(context -> Stream.of(Arguments.of()));
    }

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
