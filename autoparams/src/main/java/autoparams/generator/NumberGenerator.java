package autoparams.generator;

final class NumberGenerator extends CompositeObjectGenerator {

    public NumberGenerator() {
        super(
            new BigIntegerGenerator(),
            new BigDecimalGenerator()
        );
    }
}
