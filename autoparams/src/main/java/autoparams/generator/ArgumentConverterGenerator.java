package autoparams.generator;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;

final class ArgumentConverterGenerator
    extends ObjectGeneratorBase<ArgumentConverter> {

    @Override
    protected ArgumentConverter generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return DefaultArgumentConverter.INSTANCE;
    }
}
