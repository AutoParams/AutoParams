package autoparams.customization;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;

class ParameterTypeEquals implements Predicate<ParameterQuery> {

    private final TypeLens parameterTypeLens;

    public ParameterTypeEquals(Type parameterType) {
        this.parameterTypeLens = new TypeLens(parameterType);
    }

    @Override
    public boolean test(ParameterQuery query) {
        return parameterTypeLens.equals(query.getType());
    }
}
