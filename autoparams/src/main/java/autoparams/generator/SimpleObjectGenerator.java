package autoparams.generator;

final class SimpleObjectGenerator extends CompositeObjectGenerator {

    public SimpleObjectGenerator() {
        super(
            new RootGenerator(),
            new ClassGenerator(),
            new StringGenerator(),
            new UUIDGenerator(),
            new BigIntegerGenerator(),
            new BigDecimalGenerator(),
            new ClockGenerator(),
            new DurationGenerator(),
            new PeriodGenerator(),
            new LocalDateGenerator(),
            new LocalTimeGenerator(),
            new LocalDateTimeGenerator(),
            new OffsetDateTimeGenerator(),
            new ZoneIdGenerator(),
            new ZonedDateTimeGenerator(),
            new EnumGenerator(),
            new URLGenerator()
        );
    }
}
