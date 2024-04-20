package autoparams;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import autoparams.customization.ArgumentProcessing;
import autoparams.customization.ArgumentProcessor;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import autoparams.generator.ObjectQuery;
import autoparams.generator.ParameterQuery;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import static autoparams.AnnotationTraverser.traverseAnnotations;
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

    private static Stream<? extends Arguments> provideTuples(
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
        Object[] supplement = getSupplement(convertedSeed, context);
        return concat(convertedSeed, supplement);
    }

    private static Object[] convertSeed(
        Object[] seed,
        ResolutionContext context
    ) {
        ArgumentConverter converter = context.resolve(ArgumentConverter.class);
        Object[] result = new Object[seed.length];
        Parameter[] parameters = getTargetParameters(context);
        for (int index = 0; index < seed.length; index++) {
            result[index] = convertArgument(
                converter,
                seed[index],
                parameters[index],
                index
            );
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
        return getSupplement(unfilledParameters, context);
    }

    private static Object[] getSupplement(
        Parameter[] parameters,
        ResolutionContext context
    ) {
        Object[] supplement = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            supplement[i] = createThenProcessArgument(
                parameters[i],
                i,
                context
            );
        }
        return supplement;
    }

    private static Object createThenProcessArgument(
        Parameter parameter,
        int index,
        ResolutionContext context
    ) {
        applyCustomizers(parameter, context);
        Object convertedArgument = createArgument(parameter, index, context);
        processArgument(parameter, convertedArgument, context);
        return convertedArgument;
    }

    private static Object createArgument(
        Parameter parameter,
        int index,
        ResolutionContext context
    ) {
        ArgumentConverter converter = context.resolve(ArgumentConverter.class);
        ObjectQuery query = new ParameterQuery(
            parameter,
            index,
            parameter.getAnnotatedType().getType()
        );
        Object argument = context.resolve(query);
        return convertArgument(converter, argument, parameter, index);
    }

    private static void applyCustomizers(
        AnnotatedElement element,
        ResolutionContext context
    ) {
        traverseAnnotations(element, (annotation, parent) -> {
            if (annotation.annotationType().equals(Customization.class)) {
                applyCustomization((Customization) annotation, context);
            } else {
                applyCustomizerSource(annotation, parent, context);
            }
        });
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
        Annotation parent,
        ResolutionContext context
    ) {
        if (annotation instanceof CustomizerSource) {
            CustomizerSource source = (CustomizerSource) annotation;
            CustomizerFactory factory = instantiate(source.value());
            consumeAnnotationIfMatch(factory, parent);
            context.applyCustomizer(factory.createCustomizer());
        }
    }

    private static void processArgument(
        Parameter parameter,
        Object argument,
        ResolutionContext context
    ) {
        if (argument instanceof Named<?>) {
            Object unnamedArgument = ((Named<?>) argument).getPayload();
            processArgument(parameter, unnamedArgument, context);
            return;
        }

        traverseAnnotations(
            parameter,
            ArgumentProcessing.class,
            (annotation, parent) -> {
                ArgumentProcessor processor = instantiate(annotation.value());
                consumeAnnotationIfMatch(processor, parent);
                Customizer customizer = processor.process(parameter, argument);
                context.applyCustomizer(customizer);
            }
        );
    }

    private static Parameter[] getTargetParameters(ResolutionContext context) {
        return getTargetParameters(getTestMethod(context));
    }

    private static Parameter[] getTargetParameters(Method method) {
        ParameterScanBrake brake = getParameterScanBrake(method);
        Parameter[] allParameters = method.getParameters();
        int targetCount = 0;
        for (int index = 0; index < allParameters.length; index++) {
            Parameter parameter = allParameters[index];
            ParameterContext parameterContext = new SimpleParameterContext(
                parameter,
                index
            );
            if (brake.shouldBrakeBefore(parameterContext)) {
                break;
            }
            targetCount++;
        }
        return Arrays.copyOf(allParameters, targetCount);
    }

    private static ParameterScanBrake getParameterScanBrake(Method method) {
        List<ParameterScanBrake> brakes = new ArrayList<>();

        brakes.add(DefaultParameterScanBrake.INSTANCE);

        traverseAnnotations(
            method,
            BrakeParameterScanWith.class,
            (annotation, parent) -> {
                ParameterScanBrake brake = instantiate(annotation.value());
                consumeAnnotationIfMatch(brake, parent);
                brakes.add(brake);
            }
        );

        return parameterContext -> brakes
            .stream()
            .anyMatch(brake -> brake.shouldBrakeBefore(parameterContext));
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
            (annotation, parent) -> annotations.add(annotation)
        );
        return Math.max(1, annotations.stream().mapToInt(Repeat::value).sum());
    }

    private static Object[] concat(Object[] x, Object[] y) {
        Object[] result = new Object[x.length + y.length];
        arraycopy(x, 0, result, 0, x.length);
        arraycopy(y, 0, result, x.length, y.length);
        return result;
    }

    public static Object convertArgument(
        ArgumentConverter converter,
        Object argument,
        Parameter parameter,
        int index
    ) {
        if (argument instanceof Named<?>) {
            return convertArgument(
                converter,
                (Named<?>) argument,
                parameter,
                index
            );
        }

        ParameterContext parameterContext = new SimpleParameterContext(
            parameter,
            index
        );

        return converter.convert(argument, parameterContext);
    }

    private static Object convertArgument(
        ArgumentConverter converter,
        Named<?> argument,
        Parameter parameter,
        int index
    ) {
        Object payload = convertArgument(
            converter,
            argument.getPayload(),
            parameter,
            index
        );

        return Named.of(argument.getName(), payload);
    }

    @SuppressWarnings("unchecked")
    private static void consumeAnnotationIfMatch(
        Object service,
        Annotation annotation
    ) {
        if (isAnnotationConsumer(service, getAnnotationType(annotation))) {
            ((AnnotationConsumer<Annotation>) service).accept(annotation);
        }
    }

    private static boolean isAnnotationConsumer(
        Object service,
        Class<?> annotationType
    ) {
        for (Type type : service.getClass().getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                ParameterizedType implemented = (ParameterizedType) type;
                if (isAnnotationConsumer(implemented, annotationType)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isAnnotationConsumer(
        ParameterizedType implemented,
        Class<?> annotationType
    ) {
        return implemented.getRawType().equals(AnnotationConsumer.class)
            && implemented.getActualTypeArguments()[0].equals(annotationType);
    }

    private static Class<?> getAnnotationType(Annotation annotation) {
        for (Class<?> type : annotation.getClass().getInterfaces()) {
            if (type.isAnnotation()) {
                return type;
            }
        }

        throw new RuntimeException("Unreachable code");
    }
}
