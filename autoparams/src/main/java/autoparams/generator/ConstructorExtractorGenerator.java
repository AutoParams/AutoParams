package autoparams.generator;

import autoparams.ResolutionContext;

final class ConstructorExtractorGenerator
    extends PlainObjectGenerator<ConstructorExtractor> {

    ConstructorExtractorGenerator() {
        super(ConstructorExtractor.class);
    }

    @Override
    protected ConstructorExtractor generateValue(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return new DefaultConstructorExtractor();
    }
}
