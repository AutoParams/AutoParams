package autoparams.customization.dsl;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.type.TypeReference;

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

    public static FreezeArgument freezeArgumentOf(Type parameterType) {
        return new FreezeArgument(new ParameterTypeMatches(parameterType));
    }

    public static <T> FreezeArgument freezeArgumentOf(
        TypeReference<T> parameterTypeReference
    ) {
        return freezeArgumentOf(parameterTypeReference.getType());
    }
}
