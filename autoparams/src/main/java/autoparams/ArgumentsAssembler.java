package autoparams;

import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

final class ArgumentsAssembler {

    public static Stream<? extends Arguments> assembleArguments(
        ExtensionContext context,
        ArgumentsProvider... providers
    ) {
        return foldl(
            (sets, provider) -> sets.flatMap(set -> supplementArguments(set, provider, context)),
            Stream.of(Arguments.of()),
            Arrays.stream(providers)
        );
    }

    private static <T, U> U foldl(BiFunction<U, T, U> f, U z, Stream<T> xs) {
        Iterator<T> i = xs.iterator();
        U a = z;
        while (i.hasNext()) {
            a = f.apply(a, i.next());
        }
        return a;
    }

    private static Stream<? extends Arguments> supplementArguments(
        Arguments source,
        ArgumentsProvider provider,
        ExtensionContext context
    ) {
        try {
            return provider
                .provideArguments(context)
                .map(supplement -> {
                    processArguments(context, supplement.get());
                    return coalesceArguments(source, supplement);
                });
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void processArguments(
        ExtensionContext context,
        Object[] arguments
    ) {
        Optional<Method> method = context.getTestMethod();
        method.map(Executable::getParameters).ifPresent(parameters -> {
            for (int i = 0; i < arguments.length; i++) {
                Parameter parameter = parameters[i];
                Object argument = convertArgument(
                    i,
                    parameter,
                    refineArgument(arguments[i])
                );
                processArgument(context, parameter, argument);
            }
        });
    }

    private static Object refineArgument(Object argument) {
        return argument instanceof Named
            ? ((Named<?>) argument).getPayload()
            : argument;
    }

    private static Object convertArgument(
        int index,
        Parameter parameter,
        Object argument
    ) {
        DefaultArgumentConverter converter = DefaultArgumentConverter.INSTANCE;
        try {
            return converter.convert(argument, parameter.getType());
        } catch (NoSuchMethodError e) {
            return callConvertWithParameterContextReflectively(
                converter, index,
                parameter,
                argument
            );
        }
    }

    private static Object callConvertWithParameterContextReflectively(
        DefaultArgumentConverter converter,
        int index,
        Parameter parameter,
        Object argument
    ) {
        try {
            Method method = DefaultArgumentConverter.class.getMethod(
                "convert",
                Object.class,
                ParameterContext.class
            );
            return method.invoke(
                converter,
                argument,
                new IncompleteParameterContext(parameter, index)
            );
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processArgument(
        ExtensionContext context,
        Parameter parameter,
        Object argument
    ) {
        Customizers
            .processArgument(parameter, argument)
            .forEach(customizer -> Customizers.addCustomizer(context, customizer));
    }

    private static Arguments coalesceArguments(Arguments source, Arguments supplement) {
        ArrayList<Object> arguments = new ArrayList<>();
        Collections.addAll(arguments, source.get());
        Arrays.stream(supplement.get()).skip(arguments.size()).forEach(arguments::add);
        return Arguments.of(arguments.toArray());
    }
}
