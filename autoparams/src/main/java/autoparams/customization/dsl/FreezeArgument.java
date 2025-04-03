package autoparams.customization.dsl;

import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.generator.ObjectGenerator;

public final class FreezeArgument {

    private final Predicate<ParameterQuery> predicate;

    FreezeArgument(Predicate<ParameterQuery> predicate) {
        this.predicate = predicate;
    }

    public FreezeArgument where(Predicate<ParameterQuery> predicate) {
        return new FreezeArgument(this.predicate.and(predicate));
    }

    public FreezeArgument in(Class<?> declaringClass) {
        return where(new DeclaringClassEquals(declaringClass));
    }

    public ObjectGenerator to(Object value) {
        return new FrozenArgumentGenerator(predicate, value);
    }
}
