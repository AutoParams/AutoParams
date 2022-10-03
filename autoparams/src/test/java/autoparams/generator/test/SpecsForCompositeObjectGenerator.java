package autoparams.generator.test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertSame;

import autoparams.AutoSource;
import autoparams.generator.CompositeObjectGenerator;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForCompositeObjectGenerator {

    @ParameterizedTest
    @AutoSource
    void if_first_generator_succeeds_to_generate_value_sut_returns_its_result(
        Class<?> type, ObjectGenerationContext context, Object value
    ) {
        ObjectQuery query = ObjectQuery.fromType(type);
        CompositeObjectGenerator sut = new CompositeObjectGenerator(
            (q, c) -> new ObjectContainer(value),
            (q, c) -> new ObjectContainer(null),
            (q, c) -> new ObjectContainer(null)
        );

        ObjectContainer actual = sut.generate(query, context);

        assertSame(value, actual.unwrapOrElseThrow());
    }

    @ParameterizedTest
    @AutoSource
    void if_first_generator_not_succeeds_to_generate_value_sut_returns_result_of_second_generator(
        Class<?> type, ObjectGenerationContext context, Object value
    ) {
        ObjectQuery query = ObjectQuery.fromType(type);
        CompositeObjectGenerator sut = new CompositeObjectGenerator(
            (q, c) -> ObjectContainer.EMPTY,
            (q, c) -> new ObjectContainer(value),
            (q, c) -> new ObjectContainer(null)
        );

        ObjectContainer actual = sut.generate(query, context);

        assertSame(value, actual.unwrapOrElseThrow());
    }

    @ParameterizedTest
    @AutoSource
    void if_only_last_generator_succeeds_to_generate_value_sut_returns_its_result(
        Class<?> type, ObjectGenerationContext context, Object value
    ) {
        ObjectQuery query = ObjectQuery.fromType(type);
        CompositeObjectGenerator sut = new CompositeObjectGenerator(
            (q, c) -> ObjectContainer.EMPTY,
            (q, c) -> ObjectContainer.EMPTY,
            (q, c) -> new ObjectContainer(value)
        );

        ObjectContainer actual = sut.generate(query, context);

        assertSame(value, actual.unwrapOrElseThrow());
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_relays_arguments(Class<?> type, ObjectGenerationContext context) {
        ObjectQuery query = ObjectQuery.fromType(type);
        CompositeObjectGenerator sut = new CompositeObjectGenerator(
            (q, c) -> {
                if (q == query && c == context) {
                    return ObjectContainer.EMPTY;
                } else {
                    throw new RuntimeException();
                }
            }
        );

        assertThatCode(() -> sut.generate(query, context)).doesNotThrowAnyException();
    }

}
