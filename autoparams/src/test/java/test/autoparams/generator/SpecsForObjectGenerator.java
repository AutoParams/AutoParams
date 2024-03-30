package test.autoparams.generator;

import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.TypeMatchingGenerator;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForObjectGenerator {

    @FunctionalInterface
    public interface StringFactory {

        String get();
    }

    @AutoParameterizedTest
    void generate_with_type_correctly_generates_value_of_the_type(
        ResolutionContext context
    ) {
        ObjectGenerator sut = ObjectGenerator.DEFAULT;

        ObjectContainer actual = sut.generate(String.class, context);

        assertThat(actual).isNotNull();
        assertThat(actual.unwrapOrElseThrow()).isInstanceOf(String.class);
    }

    @AutoParameterizedTest
    void toCustomizer_returns_customizer(String value) {
        ObjectGenerator sut = TypeMatchingGenerator.create(
            StringFactory.class,
            () -> () -> value
        );

        Customizer actual = sut.toCustomizer();

        assertThat(actual).isNotNull();
    }

    @AutoParameterizedTest
    void toCustomizer_returns_customizer_that_works_correctly(
        ResolutionContext context,
        String value
    ) {
        ObjectGenerator sut = TypeMatchingGenerator.create(
            StringFactory.class,
            () -> () -> value
        );

        Customizer actual = sut.toCustomizer();

        context.applyCustomizer(actual);
        StringFactory factory = context.generate(StringFactory.class);
        assertThat(factory.get()).isEqualTo(value);
    }
}
