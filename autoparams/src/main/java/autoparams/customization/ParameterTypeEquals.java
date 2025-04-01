package autoparams.customization;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;

class ParameterTypeEquals implements Predicate<ParameterQuery> {

    private final TypeSpokesman parameterTypeSpokesman;

    public ParameterTypeEquals(Type parameterType) {
        this.parameterTypeSpokesman = new TypeSpokesman(parameterType);
    }

    @Override
    public boolean test(ParameterQuery query) {
        return parameterTypeSpokesman.equals(query.getType());
    }
}
