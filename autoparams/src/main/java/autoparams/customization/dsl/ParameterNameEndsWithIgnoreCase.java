package autoparams.customization.dsl;

import java.util.function.Predicate;

import autoparams.ParameterQuery;

class ParameterNameEndsWithIgnoreCase implements Predicate<ParameterQuery> {

    private final String parameterNameSuffix;
    private final String parameterNameSuffixInLowerCase;

    public ParameterNameEndsWithIgnoreCase(String parameterNameSuffix) {
        this.parameterNameSuffix = parameterNameSuffix;
        this.parameterNameSuffixInLowerCase = parameterNameSuffix.toLowerCase();
    }

    @Override
    public boolean test(ParameterQuery query) {
        String name = query.getRequiredParameterName();
        return match(name);
    }

    private boolean match(String name) {
        return name.endsWith(parameterNameSuffix)
            || name.toLowerCase().endsWith(parameterNameSuffixInLowerCase);
    }
}
