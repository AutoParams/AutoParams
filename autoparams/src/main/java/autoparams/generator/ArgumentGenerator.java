package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ParameterQuery;
import autoparams.ResolutionContext;

@FunctionalInterface
public interface ArgumentGenerator extends ObjectGenerator {

    ObjectContainer generate(
        ParameterQuery query,
        ResolutionContext context
    );

    default ObjectContainer generate(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return query instanceof ParameterQuery
            ? generate((ParameterQuery) query, context)
            : ObjectContainer.EMPTY;
    }
}
