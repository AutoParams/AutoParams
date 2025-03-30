package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.SupportedParameterPredicate;

class SupportedParameterPredicateGenerator
    extends ObjectGeneratorBase<SupportedParameterPredicate> {

    @Override
    protected SupportedParameterPredicate generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return SupportedParameterPredicate.DEFAULT;
    }
}
