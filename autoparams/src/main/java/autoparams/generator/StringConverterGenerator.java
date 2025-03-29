package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.StringConverter;

class StringConverterGenerator
    extends ObjectGeneratorBase<StringConverter> {

    @Override
    protected StringConverter generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return StringConverter.DEFAULT;
    }
}
