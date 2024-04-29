package autoparams.generator;

import autoparams.customization.RecursionGuard;

final class DefaultObjectGenerator extends CompositeObjectGenerator {

    public DefaultObjectGenerator() {
        super(
            new RecursionGuard().customize(
                new CompositeObjectGenerator(
                    new ResolutionContextGenerator(),
                    new ExtensionContextGenerator(),
                    new PrimitiveValueGenerator(),
                    new RootGenerator(),
                    new ClassGenerator(),
                    new EmailGenerator(),
                    new StringGenerator(),
                    new UUIDGenerator(),
                    new NumberGenerator(),
                    new TimeGenerator(),
                    new EnumGenerator(),
                    new URLGenerator(),
                    new CollectionGenerator(),
                    new StreamGenerator(),
                    new OptionalGenerator(),
                    new FactoryGenerator(),
                    new ServiceGenerator(),
                    new ComplexObjectGenerator()
                )
            )
        );
    }
}
