package autoparams.generator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import autoparams.ResolutionContext;

final class Factories {

    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    private static long createLong() {
        return random().nextLong();
    }

    public static BigInteger createBigInteger() {
        int maxBitLength = 256;
        return new BigInteger(maxBitLength, random());
    }

    public static BigDecimal createBigDecimal() {
        return new BigDecimal(random().nextInt());
    }

    public static Clock createClock() {
        return Clock.systemDefaultZone();
    }

    public static Duration createDuration() {
        return Duration.ofMillis(createLong());
    }

    public static LocalDate createLocalDate() {
        final LocalDate today = LocalDate.now();
        return today.plusDays(random().nextInt(-28, 29));
    }

    public static LocalTime createLocalTime() {
        long inclusiveMin = LocalTime.MIN.toNanoOfDay();
        long exclusiveMax = LocalTime.MAX.toNanoOfDay() + 1;
        long randomNanoOfDay = random().nextLong(inclusiveMin, exclusiveMax);
        return LocalTime.ofNanoOfDay(randomNanoOfDay);
    }

    public static LocalDateTime createLocalDateTime() {
        return LocalDateTime.of(createLocalDate(), createLocalTime());
    }

    public static IntStream createIntStream(ResolutionContext context) {
        return IntStream.generate(() -> context.resolve(int.class)).limit(3);
    }

    public static LongStream createLongStream(ResolutionContext context) {
        return LongStream.generate(() -> context.resolve(long.class)).limit(3);
    }

    public static DoubleStream createDoubleStream(ResolutionContext context) {
        return DoubleStream.generate(() -> context.resolve(double.class)).limit(3);
    }

    public static Period createPeriod(ResolutionContext context) {
        final Period period = Period.between(
            context.resolve(LocalDate.class),
            context.resolve(LocalDate.class)
        );
        return period.isNegative() ? period.negated() : period;
    }

    public static OffsetDateTime createOffsetDateTime(ResolutionContext context) {
        int bound = (int) TimeUnit.DAYS.toSeconds(7);
        int seconds = random().nextInt(bound);
        Clock clock = context.resolve(Clock.class);
        return OffsetDateTime.now(clock).minusSeconds(seconds);
    }

    public static ZonedDateTime createZonedDateTime(ResolutionContext context) {
        LocalDateTime localDateTime = context.resolve(LocalDateTime.class);
        ZoneId zoneId = context.resolve(ZoneId.class);
        return localDateTime.atZone(zoneId);
    }

    public static ZoneId createZoneId() {
        int randomZoneIdIndex = random()
            .nextInt(0, ZoneId.getAvailableZoneIds().size());

        return Optional.of(ZoneId.getAvailableZoneIds())
            .map(ArrayList::new)
            .map(zoneIds -> zoneIds.get(randomZoneIdIndex))
            .map(ZoneId::of)
            .orElse(ZoneId.systemDefault());
    }
}
