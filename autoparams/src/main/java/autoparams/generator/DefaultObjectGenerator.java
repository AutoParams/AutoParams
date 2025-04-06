package autoparams.generator;

import autoparams.customization.RecursionGuard;

final class DefaultObjectGenerator extends CompositeObjectGenerator {

    public DefaultObjectGenerator() {
        super(
            new RecursionGuard().customize(
                new CompositeObjectGenerator(
                    new ResolutionContextGenerator(),
                    new PrimitiveValueGenerator(),
                    new RootGenerator(),
                    new ClassGenerator(),
                    new EmailAddressGenerationOptionsProvider(),
                    new EmailAddressStringGenerator(),
                    new UUIDGenerator(),
                    new BigNumberGenerator(),
                    new TimeGenerator(),
                    new EnumGenerator(),
                    new PasswordStringGenerator(),
                    new URIGenerationOptionsProvider(),
                    new URIGenerator(),
                    new URLGenerator(),
                    new URIStringGenerator(),
                    new StringGenerator(),
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
