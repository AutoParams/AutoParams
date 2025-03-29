package autoparams;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Optional.empty;

class DefaultStringConverter implements StringConverter {

    private interface Converter extends
        BiFunction<String, Type, Optional<Object>> {

        static Converter with(Converter function) {
            return function;
        }

        static Converter with(Class<?> key, Function<String, Object> function) {
            return (source, type) -> type.equals(key)
                ? Optional.of(function.apply(source))
                : empty();
        }

        default Converter and(Converter next) {
            return (source, type) -> {
                Optional<Object> result = apply(source, type);
                return result.isPresent() ? result : next.apply(source, type);
            };
        }

        default Converter and(Class<?> key, Function<String, Object> next) {
            return and((source, targetType) -> targetType.equals(key)
                ? Optional.of(next.apply(source))
                : empty());
        }
    }

    private static final Converter PRIMITIVE_CONVERTER = Converter
        .with(int.class, Integer::decode)
        .and(Integer.class, Integer::decode)
        .and(long.class, Long::decode)
        .and(Long.class, Long::decode)
        .and(short.class, Short::decode)
        .and(Short.class, Short::decode)
        .and(byte.class, Byte::decode)
        .and(Byte.class, Byte::decode)
        .and(float.class, Float::parseFloat)
        .and(Float.class, Float::parseFloat)
        .and(double.class, Double::parseDouble)
        .and(Double.class, Double::parseDouble)
        .and(boolean.class, Boolean::parseBoolean)
        .and(Boolean.class, Boolean::parseBoolean)
        .and(char.class, DefaultStringConverter::convertToCharacter)
        .and(Character.class, DefaultStringConverter::convertToCharacter);

    private static final Converter TIME_OBJECT_CONVERTER = Converter
        .with(Duration.class, Duration::parse)
        .and(Instant.class, Instant::parse)
        .and(LocalDateTime.class, LocalDateTime::parse)
        .and(LocalDate.class, LocalDate::parse)
        .and(LocalTime.class, LocalTime::parse)
        .and(MonthDay.class, MonthDay::parse)
        .and(OffsetDateTime.class, OffsetDateTime::parse)
        .and(OffsetTime.class, OffsetTime::parse)
        .and(Period.class, Period::parse)
        .and(Year.class, Year::parse)
        .and(YearMonth.class, YearMonth::parse)
        .and(ZonedDateTime.class, ZonedDateTime::parse)
        .and(ZoneId.class, ZoneId::of)
        .and(ZoneOffset.class, ZoneOffset::of);

    private static final Converter OTHER_BCL_OBJECT_CONVERTER = Converter
        .with(DefaultStringConverter::convertToString)
        .and(DefaultStringConverter::convertToEnum)
        .and(UUID.class, UUID::fromString)
        .and(Locale.class, Locale::new)
        .and(Currency.class, Currency::getInstance)
        .and(URI.class, URI::create)
        .and(DefaultStringConverter::convertToURL)
        .and(Path.class, Paths::get)
        .and(Charset.class, Charset::forName)
        .and(BigDecimal.class, BigDecimal::new)
        .and(BigInteger.class, BigInteger::new)
        .and(File.class, File::new)
        .and(DefaultStringConverter::convertToClass);

    private static final Converter CONVERTER = Converter
        .with(PRIMITIVE_CONVERTER)
        .and(TIME_OBJECT_CONVERTER)
        .and(OTHER_BCL_OBJECT_CONVERTER);

    @Override
    public Optional<Object> convert(String source, ObjectQuery query) {
        return CONVERTER.apply(source, query.getType());
    }

    private static Optional<Object> convertToString(String source, Type type) {
        return type.equals(String.class) ? Optional.of(source) : empty();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Optional<Object> convertToEnum(String source, Type type) {
        if (type instanceof Class<?> && ((Class<?>) type).isEnum()) {
            Enum<?> value = Enum.valueOf((Class<? extends Enum>) type, source);
            return Optional.of(value);
        } else {
            return empty();
        }
    }

    private static Object convertToCharacter(String source) {
        if (source.length() != 1) {
            String message = "Cannot convert \"" + source + "\" to char.";
            throw new IllegalArgumentException(message);
        }

        return source.charAt(0);
    }

    private static Optional<Object> convertToURL(String source, Type type) {
        try {
            return type.equals(URL.class)
                ? Optional.of(new URL(source))
                : empty();
        } catch (MalformedURLException exception) {
            String message = "Cannot convert \"" + source + "\" to URL.";
            throw new IllegalArgumentException(message, exception);
        }
    }

    private static Optional<Object> convertToClass(String source, Type type) {
        if (type instanceof ParameterizedType) {
            return convertToClass(source, (ParameterizedType) type);
        } else {
            return empty();
        }
    }

    private static Optional<Object> convertToClass(
        String source,
        ParameterizedType type
    ) {
        if (type.getRawType().equals(Class.class) &&
            isUnboundedWildcard(type.getActualTypeArguments()[0])) {
            try {
                return Optional.of(Class.forName(source));
            } catch (ClassNotFoundException exception) {
                String message = "Cannot convert \"" + source + "\" to Class.";
                throw new IllegalArgumentException(message, exception);
            }
        } else {
            return empty();
        }
    }

    private static boolean isUnboundedWildcard(Type typeArgument) {
        if (typeArgument instanceof WildcardType) {
            WildcardType wildcard = (WildcardType) typeArgument;
            return wildcard.getUpperBounds().length == 1
                && wildcard.getUpperBounds()[0].equals(Object.class)
                && wildcard.getLowerBounds().length == 0;
        } else {
            return false;
        }
    }
}
