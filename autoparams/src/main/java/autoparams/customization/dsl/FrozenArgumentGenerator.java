package autoparams.customization.dsl;

import java.util.function.Predicate;

import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ArgumentGenerator;
import autoparams.generator.ObjectContainer;

class FrozenArgumentGenerator implements ArgumentGenerator {

    private final Predicate<ParameterQuery> predicate;
    private final Object argument;

    public FrozenArgumentGenerator(
        Predicate<ParameterQuery> predicate,
        Object argument
    ) {
        this.predicate = predicate;
        this.argument = argument;
    }

    @Override
    public ObjectContainer generate(
        ParameterQuery query,
        ResolutionContext context
    ) {
        return predicate.test(query)
            ? new ObjectContainer(argument)
            : ObjectContainer.EMPTY;
    }
}
