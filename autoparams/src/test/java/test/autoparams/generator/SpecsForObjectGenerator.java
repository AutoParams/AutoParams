package test.autoparams.generator;

import java.util.UUID;

import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectQuery;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForObjectGenerator {

    @AutoParameterizedTest
    void generate_with_type_correctly_generates_value_of_the_type(
        ResolutionContext context
    ) {
        ObjectGenerator sut = ObjectGenerator.DEFAULT;

        ObjectContainer actual = sut.generate(String.class, context);

        assertThat(actual).isNotNull();
        assertThat(actual.unwrapOrElseThrow()).isInstanceOf(String.class);
    }

    @FunctionalInterface
    public interface StringFactory {

        String get();

        static ObjectGenerator createGeneratorWith(String value) {
            return (query, context) ->
                query.getType().equals(StringFactory.class)
                    ? new ObjectContainer((StringFactory) () -> value)
                    : ObjectContainer.EMPTY;
        }
    }

    @AutoParameterizedTest
    void toCustomizer_returns_customizer(String value) {
        ObjectGenerator sut = StringFactory.createGeneratorWith(value);
        Customizer actual = sut.toCustomizer();
        assertThat(actual).isNotNull();
    }

    @AutoParameterizedTest
    void toCustomizer_returns_customizer_that_works_correctly(
        ResolutionContext context,
        String value
    ) {
        ObjectGenerator sut = StringFactory.createGeneratorWith(value);

        Customizer actual = sut.toCustomizer();

        context.applyCustomizer(actual);
        StringFactory factory = context.resolve(StringFactory.class);
        assertThat(factory.get()).isEqualTo(value);
    }

    public static class StringGenerator implements ObjectGenerator {

        @Override
        public ObjectContainer generate(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return query.getType().equals(String.class)
                ? new ObjectContainer("hello world")
                : ObjectContainer.EMPTY;
        }
    }

    @AutoParameterizedTest
    void customize_correctly_composes_generators(
        ResolutionContext context,
        StringGenerator sut
    ) {
        context.applyCustomizer(sut);

        assertThat(context.resolve(String.class)).isEqualTo("hello world");
        assertThat(context.resolve(UUID.class)).isNotNull();
    }
}
