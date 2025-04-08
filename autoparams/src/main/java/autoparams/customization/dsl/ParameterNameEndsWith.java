package autoparams.customization.dsl;

import java.util.function.Predicate;

import autoparams.ParameterQuery;

class ParameterNameEndsWith implements Predicate<ParameterQuery> {

    private final String parameterNameSuffix;

    public ParameterNameEndsWith(String parameterNameSuffix) {
        this.parameterNameSuffix = parameterNameSuffix;
    }

    @Override
    public boolean test(ParameterQuery query) {
        String name = query.getRequiredParameterName();
        return name.endsWith(parameterNameSuffix);
    }
}
