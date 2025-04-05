package test.autoparams.customization;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import autoparams.AutoParams;
import autoparams.AutoSource;
import autoparams.customization.UseImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForUseImplementation {

    public static class IntFactory implements IntSupplier {

        @Override
        public int getAsInt() {
            return 1024;
        }
    }

    @Test
    @AutoParams
    @UseImplementation(IntFactory.class)
    void sut_provides_implementation_for_interface_correctly(
        IntSupplier supplier
    ) {
        Integer actual = supplier.getAsInt();
        assertThat(actual).isEqualTo(1024);
    }

    public static class IntegerFactory implements Supplier<Integer> {

        @Override
        public Integer get() {
            return 2048;
        }
    }

    @Test
    @AutoParams
    @UseImplementation(IntegerFactory.class)
    void sut_provides_implementation_for_generic_interface_correctly(
        Supplier<Integer> supplier
    ) {
        Integer actual = supplier.get();
        assertThat(actual).isEqualTo(2048);
    }

    public static class ZeroFactory implements Supplier<Integer> {

        @Override
        public Integer get() {
            return 0;
        }
    }

    @Test
    @AutoParams
    @UseImplementation(IntegerFactory.class)
    void sut_can_be_used_on_parameter(
        Supplier<Integer> supplier1,
        @UseImplementation(ZeroFactory.class) Supplier<Integer> supplier2
    ) {
        assertThat(supplier1.get()).isEqualTo(2048);
        assertThat(supplier2.get()).isEqualTo(0);
    }

    @ParameterizedTest
    @AutoSource
    @UseImplementation(IntegerFactory.class)
    void sut_works_with_AutoSource_correctly(
        Supplier<Integer> supplier
    ) {
        Integer actual = supplier.get();
        assertThat(actual).isEqualTo(2048);
    }

    @Test
    @AutoParams
    @UseImplementation({ IntFactory.class, IntegerFactory.class })
    void sut_provides_implementation_for_multiple_interfaces_correctly(
        IntSupplier intSupplier,
        Supplier<Integer> integerSupplier
    ) {
        assertThat(intSupplier.getAsInt()).isEqualTo(1024);
        assertThat(integerSupplier.get()).isEqualTo(2048);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @AutoParams
    @UseImplementation(IntegerFactory.class)
    public @interface AutoParamsWithUseImplementation {
    }

    @Test
    @AutoParamsWithUseImplementation
    void sut_can_be_used_on_annotation(Supplier<Integer> supplier) {
        assertThat(supplier.get()).isEqualTo(2048);
    }
}
