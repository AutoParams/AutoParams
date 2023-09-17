package autoparams.generator;

class ConstructorResolverGenerator extends TypeMatchingGenerator {

    public ConstructorResolverGenerator() {
        super(
            ConstructorResolverGenerator::factory,
            ConstructorResolver.class
        );
    }

    private static ConstructorResolver factory(ObjectGenerationContext context) {
        return new DefensiveConstructorResolver(context.generate(ConstructorExtractor.class));
    }
}
