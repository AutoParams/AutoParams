package autoparams;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import autoparams.AnnotationScanner.Edge;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import static autoparams.AnnotationScanner.scanAnnotations;
import static autoparams.Brake.collectBrakes;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

    private List<? extends Arguments> getAssets(
        ExtensionContext context
    ) throws Exception {
        return assetProvider.provideArguments(context).collect(toList());
    }

    private static Arguments getTestCase(
        ExtensionContext extensionContext,
        Arguments asset
    ) {
        TestCaseContext testCaseContext = TestCaseContext.create(
            extensionContext,
            TestResolutionContext.create(extensionContext),
            asset
        );
        return testCaseContext.getTestCase();
    }

    private static class TestCaseContext {

        private final TestParameterContext[] parameterContexts;
        private final Arguments asset;
        private final Brake brake;

        private TestCaseContext(
            TestParameterContext[] parameterContexts,
            Arguments asset,
            Brake brake
        ) {
            this.parameterContexts = parameterContexts;
            this.asset = asset;
            this.brake = brake;
        }

        public static TestCaseContext create(
            ExtensionContext extensionContext,
            TestResolutionContext resolutionContext,
            Arguments asset
        ) {
            return new TestCaseContext(
                resolutionContext.getParameterContexts(extensionContext),
                asset,
                getBrake(extensionContext)
            );
        }

        private static Brake getBrake(ExtensionContext context) {
            List<Brake> brakes = new ArrayList<>();
            collectBrakes(context.getRequiredTestMethod(), brakes::add);
            brakes.add(TestGearBrake.INSTANCE);
            return Brake.compose(brakes.toArray(new Brake[0]));
        }

        public Arguments getTestCase() {
            List<Object> arguments = new ArrayList<>();
            for (TestParameterContext parameterContext : parameterContexts) {
                if (brake.shouldBrakeBefore(parameterContext)) {
                    break;
                }

                arguments.add(parameterContext.resolveArgument(asset));
            }

            return arguments(arguments.toArray());
        }
    }
}
