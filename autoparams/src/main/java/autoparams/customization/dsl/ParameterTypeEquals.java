package autoparams.customization.dsl;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.internal.type.TypeLens;

class ParameterTypeEquals implements Predicate<ParameterQuery> {

    private final TypeLens parameterTypeLens;

    public ParameterTypeEquals(Type parameterType) {
        this.parameterTypeLens = new TypeLens(parameterType);
    }

    @Override
    public boolean test(ParameterQuery query) {
        return parameterTypeLens.matches(query.getType());
    }
}
