package autoparams.generator;

class ConstructorResolverGenerator extends TypeMatchingGenerator {

    public ConstructorResolverGenerator() {
        super(
            () -> ConstructorResolver.DEFENSIVE_STRATEGY,
            ConstructorResolver.class
        );
    }
}
