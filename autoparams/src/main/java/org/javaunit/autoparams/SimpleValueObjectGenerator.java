package org.javaunit.autoparams;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

final class SimpleValueObjectGenerator extends CompositeObjectGenerator {

    public SimpleValueObjectGenerator() {
        super(
            new TypeMatchingGenerator(() -> UUID.randomUUID().toString(), String.class),
            new TypeMatchingGenerator(UUID::randomUUID, UUID.class),
            new TypeMatchingGenerator(Factories::createBigInteger, BigInteger.class),
            new TypeMatchingGenerator(Factories::createBigDecimal, BigDecimal.class),
            new TypeMatchingGenerator(Factories::createDuration, Duration.class),
            new TypeMatchingGenerator(Factories::createLocalDate, LocalDate.class),
            new TypeMatchingGenerator(Factories::createLocalTime, LocalTime.class),
            new TypeMatchingGenerator(Factories::createLocalDateTime, LocalDateTime.class),
            new TypeMatchingGenerator(Factories::createClass, Class.class),
            new UrlGenerator());
    }

}
