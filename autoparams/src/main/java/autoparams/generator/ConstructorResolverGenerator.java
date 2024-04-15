package autoparams.generator;

import autoparams.ResolutionContext;

final class ConstructorResolverGenerator
    extends ObjectGeneratorBase<ConstructorResolver> {

    @Override
    protected ConstructorResolver generateObject(
        ObjectQuery query,
        ResolutionContext context
    ) {
        return generateResolver(context.resolve(ConstructorExtractor.class));
    }

    private static CompositeConstructorResolver generateResolver(
        ConstructorExtractor extractor
    ) {
        return new CompositeConstructorResolver(
            new AnnotatedModestConstructorResolver(extractor),
            new ModestConstructorResolver(extractor)
        );
    }
}
