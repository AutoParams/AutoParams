package autoparams.customization;

public final class ArgumentCustomizationDsl {

    private ArgumentCustomizationDsl() {
    }

    public static FreezeArgument freezeArgument(String name) {
        return FreezeArgument.withName(name);
    }

    public static FreezeArgument freezeArgument(
        Class<?> parameterType,
        String name
    ) {
        return FreezeArgument.withParameterTypeAndName(parameterType, name);
    }

    public static FreezeArgument freezeArgumentOf(Class<?> parameterType) {
        return FreezeArgument.withParameterType(parameterType);
    }
}
