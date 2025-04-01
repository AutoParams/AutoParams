package autoparams.customization.dsl;

import java.util.function.Predicate;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

class ArgumentGenerator implements ObjectGenerator {

    private final Predicate<ParameterQuery> predicate;
    private final Object argument;

    public ArgumentGenerator(
        Predicate<ParameterQuery> predicate,
        Object argument
    ) {
        this.predicate = predicate;
        this.argument = argument;
    }

    @Override
    public ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query instanceof ParameterQuery
            ? generate((ParameterQuery) query)
            : ObjectContainer.EMPTY;
    }

    private ObjectContainer generate(ParameterQuery query) {
        return predicate.test(query)
            ? new ObjectContainer(argument)
            : ObjectContainer.EMPTY;
    }
}
