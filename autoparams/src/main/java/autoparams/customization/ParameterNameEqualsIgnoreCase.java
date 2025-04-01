package autoparams.customization;

import java.util.function.Predicate;

import autoparams.ParameterQuery;

class ParameterNameEqualsIgnoreCase implements Predicate<ParameterQuery> {

    private final String parameterName;

    public ParameterNameEqualsIgnoreCase(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public boolean test(ParameterQuery query) {
        return query
            .getParameterName()
            .filter(name -> name.equalsIgnoreCase(parameterName))
            .isPresent();
    }
}
