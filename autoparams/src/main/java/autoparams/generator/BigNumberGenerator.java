package autoparams.generator;

final class BigNumberGenerator extends CompositeObjectGenerator {

    public BigNumberGenerator() {
        super(
            new BigIntegerGenerator(),
            new BigDecimalGenerator()
        );
    }
}
