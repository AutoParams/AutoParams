package autoparams.customization.dsl;

import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.customization.Customizer;

public final class FreezeArgument {

    private final Predicate<ParameterQuery> predicate;

    FreezeArgument(Predicate<ParameterQuery> predicate) {
        this.predicate = predicate;
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
