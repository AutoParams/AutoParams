package autoparams.generator;

import autoparams.ResolutionContext;

final class ConstructorResolverGenerator
    extends PlainObjectGenerator<ConstructorResolver> {

    ConstructorResolverGenerator() {
        super(ConstructorResolver.class);
    }

    @Override
    protected ConstructorResolver generateValue(
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
