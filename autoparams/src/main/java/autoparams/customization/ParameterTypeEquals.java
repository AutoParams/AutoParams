package autoparams.customization;

import java.lang.reflect.Type;
import java.util.function.Predicate;

import autoparams.ParameterQuery;

class ParameterTypeEquals implements Predicate<ParameterQuery> {

    private final Type parameterType;

    public ParameterTypeEquals(Type parameterType) {
        this.parameterType = parameterType;
    }

    @Override
    public boolean test(ParameterQuery query) {
        return new TypeSpokesman(query.getType()).equals(parameterType);
    }
}
