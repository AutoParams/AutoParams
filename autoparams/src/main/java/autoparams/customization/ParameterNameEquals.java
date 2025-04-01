package autoparams.customization;

import java.util.function.Predicate;

import autoparams.ParameterQuery;

class ParameterNameEquals implements Predicate<ParameterQuery> {

    private final String parameterName;

    public ParameterNameEquals(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public boolean test(ParameterQuery query) {
        return query
            .getParameterName()
            .filter(parameterName::equals)
            .isPresent();
    }
}
