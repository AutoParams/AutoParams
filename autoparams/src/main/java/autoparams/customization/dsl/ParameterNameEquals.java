package autoparams.customization.dsl;

import java.util.function.Predicate;

import autoparams.ParameterQuery;

class ParameterNameEquals implements Predicate<ParameterQuery> {

    private final String parameterName;

    public ParameterNameEquals(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public boolean test(ParameterQuery query) {
        String name = query.getRequiredParameterName();
        return name.equals(parameterName);
    }
}
