package autoparams.generator;

import autoparams.customization.RecursionGuard;

final class DefaultObjectGenerator extends CompositeObjectGenerator {

    public DefaultObjectGenerator() {
        super(
            new RecursionGuard().customize(
                new CompositeObjectGenerator(
                    new PrimitiveValueGenerator(),
                    new SimpleObjectGenerator(),
                    new CollectionGenerator(),
                    new StreamGenerator(),
                    new OptionalGenerator(),
                    new FactoryGenerator(),
                    new ComplexObjectGenerator(),
                    new ResolutionContextGenerator(),
                    new ServiceGenerator()
                )
            )
        );
    }
}
