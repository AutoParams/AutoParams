package test.autoparams.customization;

import java.util.function.Function;

import autoparams.AutoParams;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customization;
import autoparams.customization.Decorator;
import autoparams.generator.ObjectGeneratorBase;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForDecorator {

    public interface IntegerFunction extends Function<Integer, Integer> {
    }

    public static class Twice implements IntegerFunction {

        @Override
        public Integer apply(Integer integer) {
            return integer * 2;
        }
    }

    public static class TwiceGenerator
        extends ObjectGeneratorBase<IntegerFunction> {

        @Override
        protected IntegerFunction generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return new Twice();
        }
    }

    @Test
    @AutoParams
    @Customization(TwiceGenerator.class)
    void sut_correctly_decorates_service(ResolutionContext context, int value) {
        context.customize(new Decorator<IntegerFunction>() {
            @Override
            protected IntegerFunction decorate(IntegerFunction component) {
                return value -> component.apply(value) * 5;
            }
        });

        IntegerFunction function = context.resolve();

        int result = function.apply(value);
        assertThat(result).isEqualTo(value * 10);
    }

    @Test
    @AutoParams
    @Customization(TwiceGenerator.class)
    void sut_yields_against_unmatched_type(ResolutionContext context) {
        context.customize(new Decorator<IntegerFunction>() {
            @Override
            protected IntegerFunction decorate(IntegerFunction component) {
                return value -> component.apply(value) * 5;
            }
        });

        String result = context.resolve();
        assertThat(result).isNotNull();
    }
}
