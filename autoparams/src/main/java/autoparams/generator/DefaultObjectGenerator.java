package autoparams.generator;

import autoparams.customization.RecursionGuard;

final class DefaultObjectGenerator extends CompositeObjectGenerator {

    public DefaultObjectGenerator() {
        super(
            new RecursionGuard().customize(
                new CompositeObjectGenerator(
                    new ConstructorResolverGenerator(),
                    new ObjectGenerationContextGenerator(),
                    new PrimitiveValueGenerator(),
                    new SimpleValueObjectGenerator(),
                    new CollectionGenerator(),
                    new StreamGenerator(),
                    new OptionalGenerator(),
                    new ComplexObjectGenerator()))
        );
    }

}
