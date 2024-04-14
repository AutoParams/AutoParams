package autoparams.generator;

final class TimeGenerator extends CompositeObjectGenerator {

    public TimeGenerator() {
        super(
            new ClockGenerator(),
            new DurationGenerator(),
            new LocalDateGenerator(),
            new LocalDateTimeGenerator(),
            new LocalTimeGenerator(),
            new OffsetDateTimeGenerator(),
            new PeriodGenerator(),
            new ZonedDateTimeGenerator(),
            new ZoneIdGenerator()
        );
    }
}
