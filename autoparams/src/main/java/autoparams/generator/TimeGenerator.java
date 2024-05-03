package autoparams.generator;

final class TimeGenerator extends CompositeObjectGenerator {

    public TimeGenerator() {
        super(
            new ClockGenerator(),
            new DurationGenerator(),
            new YearGenerator(),
            new YearMonthGenerator(),
            new MonthDayGenerator(),
            new LocalDateGenerator(),
            new LocalDateTimeGenerator(),
            new LocalTimeGenerator(),
            new OffsetDateTimeGenerator(),
            new InstantGenerator(),
            new PeriodGenerator(),
            new ZonedDateTimeGenerator(),
            new ZoneIdGenerator(),
            new ZoneOffsetGenerator()
        );
    }
}
