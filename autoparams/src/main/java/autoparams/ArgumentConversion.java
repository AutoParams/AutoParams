package autoparams;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;

final class ArgumentConversion {

    @SuppressWarnings("unchecked")
    public static Object convertArgument(
        Parameter parameter,
        Object argument
    ) {
        return composeConverters(
            getNameUnwrapper(),
            getDefaultConverter()
        ).apply(parameter, argument);
    }

    @SuppressWarnings("unchecked")
    private static BiFunction<Parameter, Object, Object> composeConverters(
        BiFunction<Parameter, Object, Object>... converters
    ) {
        return (parameter, argument) -> {
            Object result = argument;
            for (BiFunction<Parameter, Object, Object> converter : converters) {
                result = converter.apply(parameter, result);
            }
            return result;
        };
    }

    private static BiFunction<Parameter, Object, Object> getNameUnwrapper() {
        return (parameter, argument) -> {
            if (argument instanceof Named<?> &&
                parameter.getType().equals(Named.class) == false
            ) {
                return ((Named<?>) argument).getPayload();
            } else {
                return argument;
            }
        };
    }

    private static BiFunction<Parameter, Object, Object> getDefaultConverter() {
        DefaultArgumentConverter converter = DefaultArgumentConverter.INSTANCE;
        return (parameter, argument) -> {
            try {
                return converter.convert(argument, parameter.getType());
            } catch (NoSuchMethodError e) {
                return callConvertWithParameterContextReflectively(
                    converter,
                    parameter,
                    argument
                );
            }
        };
    }

    private static Object callConvertWithParameterContextReflectively(
        DefaultArgumentConverter converter,
        Parameter parameter,
        Object argument
    ) {
        try {
            Method method = converter.getClass().getMethod(
                "convert",
                Object.class,
                ParameterContext.class
            );
            return method.invoke(
                converter,
                argument,
                new IncompleteParameterContext(parameter)
            );
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
