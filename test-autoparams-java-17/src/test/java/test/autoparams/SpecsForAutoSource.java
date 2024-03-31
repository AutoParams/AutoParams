package test.autoparams;

import autoparams.AutoSource;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForAutoSource {

    @ParameterizedTest
    @AutoSource
    void sut_generates_record(Point point) {
        assertThat(point).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_generates_record_containing_iterable(IterableBag<Point> bag) {
        assertThat(bag.items()).isNotNull();
    }
}
