package autoparams.generator;

final class ServiceGenerator extends CompositeObjectGenerator {

    public ServiceGenerator() {
        super(
            new ConstructorExtractorGenerator(),
            new ConstructorResolverGenerator(),
            new AssetConverterGenerator(),
            new SupportedParameterPredicateGenerator()
        );
    }
}
