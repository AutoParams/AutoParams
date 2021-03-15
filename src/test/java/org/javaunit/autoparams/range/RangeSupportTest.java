package org.javaunit.autoparams.range;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RangeSupportTest {
    @Test
    void should_int_value_in_range() {
        RangeSupport<Integer> support = RangeSupport.integerSupport(100, 102);

        IntStream.range(0, 100).forEach(i -> {
            int actual = support.valueInRange(ThreadLocalRandom.current());
            assertThat(actual).isGreaterThanOrEqualTo(100);
            assertThat(actual).isLessThanOrEqualTo(102);
        });
    }

    @Test
    void should_double_value_in_range() {
        RangeSupport<Double> support = RangeSupport.doubleSupport(10.0D, 12.5D);

        IntStream.range(0, 1000).forEach(i -> {
            double actual = support.valueInRange(ThreadLocalRandom.current());
            assertThat(actual).isGreaterThanOrEqualTo(10.0D);
            assertThat(actual).isLessThanOrEqualTo(12.5D);
        });
    }

    @Test
    void should_long_value_in_range() {
        RangeSupport<Long> support = RangeSupport.longSupport(10L, 11L);

        IntStream.range(0, 1000).forEach(i -> {
            double actual = support.valueInRange(ThreadLocalRandom.current());
            assertThat(actual).isGreaterThanOrEqualTo(10L);
            assertThat(actual).isLessThanOrEqualTo(11L);
        });
    }

    @Test
    void should_float_value_in_range() {
        RangeSupport<Float> support = RangeSupport.floatSupport(5.0F, 6.5F);

        IntStream.range(0, 1000).forEach(i -> {
            float actual = support.valueInRange(ThreadLocalRandom.current());
            assertThat(actual).isGreaterThanOrEqualTo(5.0F);
            assertThat(actual).isLessThanOrEqualTo(6.5F);
        });
    }
}
