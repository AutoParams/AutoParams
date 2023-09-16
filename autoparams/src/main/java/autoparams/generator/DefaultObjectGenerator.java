package autoparams.generator;

import autoparams.customization.RecursionGuard;

final class DefaultObjectGenerator extends CompositeObjectGenerator {

    public DefaultObjectGenerator() {
        super(
            new RecursionGuard().customize(
                new CompositeObjectGenerator(
                    new ObjectGenerationContextGenerator(),
                    new PrimitiveValueGenerator(),
                    new SimpleObjectGenerator(),
                    new CollectionGenerator(),
                    new StreamGenerator(),
                    new OptionalGenerator(),
                    new ConstructorResolverGenerator(),
                    new ComplexObjectGenerator()
                )
            )
        );
    }
}
