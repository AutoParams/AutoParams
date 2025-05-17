package autoparams.customization.dsl;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.type.TypeReference;

import static autoparams.customization.dsl.ParameterNameInferencer.inferParameterName;

public final class ArgumentCustomizationDsl {

    private ArgumentCustomizationDsl() {
    }

    public static FreezeArgument freezeArgument() {
        return new FreezeArgument(new ReturnTrue<>());
    }

    public static FreezeArgument freezeArgument(
        Predicate<ParameterQuery> predicate
    ) {
        return new FreezeArgument(predicate);
    }

    public static FreezeArgument freezeArgument(String parameterName) {
        return new FreezeArgument(new ParameterNameEquals(parameterName));
    }

    public static FreezeArgument freezeArgument(
        Type parameterType,
        String parameterName
    ) {
        return new FreezeArgument(
            new ParameterTypeMatches(parameterType)
                .and(new ParameterNameEquals(parameterName))
        );
    }

    public static <T> FreezeArgument freezeArgument(
        TypeReference<T> parameterTypeReference,
        String parameterName
    ) {
        return freezeArgument(parameterTypeReference.getType(), parameterName);
    }

    public static <T, P> FreezeArgument set(
        FunctionDelegate<T, P> getterDelegate
    ) {
        SerializedLambda lambda = getterDelegate.getLambda();
        Class<?> componentType = getComponentClass(lambda);
        Method getter = getMethod(componentType, lambda.getImplMethodName());
        String parameterName = inferParameterName(getter.getName());
        return new FreezeArgument(
            new ParameterTypeMatches(getter.getReturnType())
                .and(new ParameterNameEquals(parameterName))
                .and(new DeclaringClassEquals(componentType))
        );
    }

    private static Class<?> getComponentClass(SerializedLambda lambda) {
        try {
            return Class.forName(lambda.getImplClass().replace('/', '.'));
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static Method getMethod(Class<?> type, String methodName) {
        try {
            return type.getMethod(methodName);
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static FreezeArgument freezeArgumentOf(Type parameterType) {
        return new FreezeArgument(new ParameterTypeMatches(parameterType));
    }

    public static <T> FreezeArgument freezeArgumentOf(
        TypeReference<T> parameterTypeReference
    ) {
        return freezeArgumentOf(parameterTypeReference.getType());
    }
}
