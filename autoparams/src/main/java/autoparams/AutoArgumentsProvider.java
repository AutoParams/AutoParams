package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import autoparams.customization.ArgumentProcessing;
import autoparams.customization.ArgumentProcessor;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import autoparams.generator.ObjectQuery;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import static autoparams.Annotations.findService;
import static autoparams.Annotations.traverseAnnotations;
import static autoparams.ArgumentConversion.convertArgument;
import static autoparams.Instantiator.instantiate;
import static java.lang.System.arraycopy;

class AutoArgumentsProvider implements ArgumentsProvider {

    private final ArgumentsProvider seedProvider;

    public AutoArgumentsProvider(ArgumentsProvider seedProvider) {
        this.seedProvider = seedProvider;
    }

    @SuppressWarnings("unused")
    public AutoArgumentsProvider() {
        this(context -> Stream.of(Arguments.of()));
    }

    @Override
    public Stream<? extends Arguments> provideArguments(
        ExtensionContext context
    ) throws Exception {
        return seedProvider
            .provideArguments(context)
            .flatMap(seed -> provideTuples(seed, context));
    }

    private Stream<? extends Arguments> provideTuples(
        Arguments seed,
        ExtensionContext context
    ) {
        return Stream
            .generate(() -> provideTuple(
                seed,
                new ResolutionContext(context)
            ))
            .limit(getRepetitions(context));
    }

    private static Arguments provideTuple(
        Arguments seed,
        ResolutionContext context
    ) {
        return Arguments.of(provideTuple(seed.get(), context));
    }

    private static Object[] provideTuple(
        Object[] seed,
        ResolutionContext context
    ) {
        applyCustomizers(getTestMethod(context), context);
        Object[] convertedSeed = convertSeed(seed, context);
        processSeed(convertedSeed, context);
        Object[] supplement = getSupplement(seed, context);
        return concat(seed, supplement);
    }

    private static Object[] convertSeed(
        Object[] seed,
        ResolutionContext context
    ) {
        Object[] result = new Object[seed.length];
        Parameter[] parameters = getTargetParameters(context);
        for (int i = 0; i < seed.length; i++) {
            result[i] = convertArgument(parameters[i], seed[i]);
        }
        return result;
    }

    private static void processSeed(Object[] seed, ResolutionContext context) {
        Parameter[] parameters = getTargetParameters(context);
        for (int i = 0; i < seed.length; i++) {
            processArgument(parameters[i], seed[i], context);
        }
    }

    private static Object[] getSupplement(
        Object[] seed,
        ResolutionContext context
    ) {
        Parameter[] parameters = getTargetParameters(context);
        Parameter[] unfilledParameters = Arrays.copyOfRange(
            parameters,
            seed.length,
            parameters.length
        );
        return getSupplement(context, unfilledParameters);
    }

    private static Object[] getSupplement(
        ResolutionContext context,
        Parameter[] parameters
    ) {
        Object[] supplement = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            supplement[i] = createThenProcessArgument(parameters[i], context);
        }
        return supplement;
    }

    private static Object createThenProcessArgument(
        Parameter parameter,
        ResolutionContext context
    ) {
        applyCustomizers(parameter, context);
        Object argument = context.resolve(ObjectQuery.fromParameter(parameter));
        Object convertedArgument = convertArgument(parameter, argument);
        processArgument(parameter, convertedArgument, context);
        return argument;
    }

    private static void applyCustomizers(
        AnnotatedElement element,
        ResolutionContext context
    ) {
        traverseAnnotations(
            element,
            annotation -> applyCustomizers(annotation, context)
        );
    }

    private static void applyCustomizers(
        Annotation annotation,
        ResolutionContext context
    ) {
        if (annotation.annotationType().equals(Customization.class)) {
            applyCustomization((Customization) annotation, context);
        } else {
            applyCustomizerSource(annotation, context);
        }
    }

    private static void applyCustomization(
        Customization annotation,
        ResolutionContext context
    ) {
        for (Class<? extends Customizer> customizerType : annotation.value()) {
            context.applyCustomizer(instantiate(customizerType));
        }
    }

    private static void applyCustomizerSource(
        Annotation annotation,
        ResolutionContext context
    ) {
        findService(annotation, CustomizerSource.class, CustomizerSource::value)
            .map(CustomizerFactory::createCustomizer)
            .ifPresent(context::applyCustomizer);
    }

    private static void processArgument(
        Parameter parameter,
        Object argument,
        ResolutionContext context
    ) {
        traverseAnnotations(
            parameter,
            annotation -> {
                Optional<ArgumentProcessor> processor = findService(
                    annotation,
                    ArgumentProcessing.class,
                    ArgumentProcessing::value
                );
                processor
                    .map(p -> p.process(parameter, argument))
                    .ifPresent(context::applyCustomizer);
            }
        );
    }

    private static Parameter[] getTargetParameters(ResolutionContext context) {
        Parameter[] allParameters = getTestMethod(context).getParameters();
        int targetCount = 0;
        for (Parameter parameter : allParameters) {
            Class<?> parameterType = parameter.getType();
            if (parameterType.equals(TestInfo.class) ||
                parameterType.equals(TestReporter.class)) {
                break;
            }
            targetCount++;
        }
        return Arrays.copyOf(allParameters, targetCount);
    }

    private static Method getTestMethod(ResolutionContext context) {
        return context
            .getExtensionContext()
            .getRequiredTestMethod();
    }

    private static int getRepetitions(ExtensionContext context) {
        List<Repeat> annotations = new ArrayList<>();
        traverseAnnotations(
            context.getRequiredTestMethod(),
            Repeat.class,
            annotations::add
        );
        return Math.max(1, annotations.stream().mapToInt(Repeat::value).sum());
    }

    private static Object[] concat(Object[] x, Object[] y) {
        Object[] result = new Object[x.length + y.length];
        arraycopy(x, 0, result, 0, x.length);
        arraycopy(y, 0, result, x.length, y.length);
        return result;
    }
}
