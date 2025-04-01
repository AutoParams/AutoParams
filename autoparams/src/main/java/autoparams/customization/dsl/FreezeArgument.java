package autoparams.customization.dsl;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.customization.Customizer;

public final class FreezeArgument {

    private final Predicate<ParameterQuery> predicate;

    FreezeArgument(Predicate<ParameterQuery> predicate) {
        this.predicate = predicate;
    }

    static FreezeArgument withParameterType(Type parameterType) {
        return new FreezeArgument(new ParameterTypeEquals(parameterType));
    }

    static FreezeArgument withParameterName(String parameterName) {
        return new FreezeArgument(new ParameterNameEquals(parameterName));
    }

    static FreezeArgument withParameterTypeAndParameterName(
        Type parameterType,
        String parameterName
    ) {
        return new FreezeArgument(
            new ParameterTypeEquals(parameterType)
                .and(new ParameterNameEquals(parameterName))
        );
    }

    public FreezeArgument in(Class<?> declaringClass) {
        return narrowScope(new DeclaringClassEquals(declaringClass));
    }

    private FreezeArgument narrowScope(Predicate<ParameterQuery> predicate) {
        return new FreezeArgument(this.predicate.and(predicate));
    }

    public Customizer to(Object value) {
        return new ArgumentGenerator(predicate, value);
    }
}
