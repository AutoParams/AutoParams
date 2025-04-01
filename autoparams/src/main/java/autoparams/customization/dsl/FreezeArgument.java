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
        return new FreezeArgument(
            predicate.and(new DeclaringClassEquals(declaringClass))
        );
    }

    public Customizer to(Object value) {
        return new ArgumentGenerator(predicate, value);
    }
}
