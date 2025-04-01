package autoparams.customization;

import java.lang.reflect.Type;

import autoparams.type.TypeReference;

public final class ArgumentCustomizationDsl {

    private ArgumentCustomizationDsl() {
    }

    public static FreezeArgument freezeArgumentsOf(Type parameterType) {
        return FreezeArgument.withParameterType(parameterType);
    }

    public static <T> FreezeArgument freezeArgumentsOf(
        TypeReference<T> parameterTypeReference
    ) {
        return freezeArgumentsOf(parameterTypeReference.getType());
    }

    public static FreezeArgument freezeArgument(String parameterName) {
        return FreezeArgument.withParameterName(parameterName);
    }

    public static FreezeArgument freezeArgument(
        Type parameterType,
        String parameterName
    ) {
        return FreezeArgument.withParameterTypeAndParameterName(
            parameterType,
            parameterName
        );
    }

    public static <T> FreezeArgument freezeArgument(
        TypeReference<T> parameterTypeReference,
        String parameterName
    ) {
        return freezeArgument(parameterTypeReference.getType(), parameterName);
    }
}
