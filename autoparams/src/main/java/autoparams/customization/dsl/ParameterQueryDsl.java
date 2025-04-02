package autoparams.customization.dsl;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;

public class ParameterQueryDsl {

    public static Predicate<ParameterQuery> parameterNameEquals(
        String parameterName
    ) {
        return new ParameterNameEquals(parameterName);
    }

    public static Predicate<ParameterQuery> parameterNameEqualsIgnoreCase(
        String parameterName
    ) {
        return new ParameterNameEqualsIgnoreCase(parameterName);
    }

    public static Predicate<ParameterQuery> parameterNameEndsWith(
        String parameterNameSuffix
    ) {
        return new ParameterNameEndsWith(parameterNameSuffix);
    }

    public static Predicate<ParameterQuery> parameterNameEndsWithIgnoreCase(
        String parameterNameSuffix
    ) {
        return new ParameterNameEndsWithIgnoreCase(parameterNameSuffix);
    }

    public static Predicate<ParameterQuery> parameterTypeMatches(
        Type parameterType
    ) {
        return new ParameterTypeMatches(parameterType);
    }
}
