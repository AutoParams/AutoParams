package autoparams.generator.test;

import autoparams.AutoParameterizedTest;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectQuery;
import autoparams.generator.TypeMatchingGenerator;
import autoparams.generator.UnwrapFailedException;

import static autoparams.generator.TypeMatchingGenerator.create;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForTypeMatchingGenerator {

    @AutoParameterizedTest
    void create_correctly_creates_generator_with_supplier_factory(
        String value,
        ObjectGenerationContext context
    ) {
        TypeMatchingGenerator actual = create(String.class, () -> value);

        assertThat(actual).isNotNull();
        assertThat(actual.generate(String.class, context).unwrapOrElseThrow())
            .isEqualTo(value);
        assertThatThrownBy(() -> actual.generate(int.class, context).unwrapOrElseThrow())
            .isInstanceOf(UnwrapFailedException.class);
    }

    @AutoParameterizedTest
    void create_correctly_creates_generator_with_function_factory(
        String value,
        ObjectGenerationContext context
    ) {
        TypeMatchingGenerator actual = create(
            String.class,
            x -> x == context ? value : null);

        assertThat(actual).isNotNull();
        assertThat(actual.generate(String.class, context).unwrapOrElseThrow())
            .isEqualTo(value);
        assertThatThrownBy(() -> actual.generate(int.class, context).unwrapOrElseThrow())
            .isInstanceOf(UnwrapFailedException.class);
    }

    @AutoParameterizedTest
    void constructor_with_predicate_and_bi_function_factory_correctly_creates_generator(
        String value,
        ObjectGenerationContext context
    ) {
        ObjectQuery query = ObjectQuery.fromType(String.class);

        TypeMatchingGenerator actual = new TypeMatchingGenerator(
            x -> x == String.class,
            (x, y) -> x == query && y == context ? value : null);

        assertThat(actual).isNotNull();
        assertThat(actual.generate(query, context).unwrapOrElseThrow())
            .isEqualTo(value);
        assertThatThrownBy(() -> actual.generate(int.class, context).unwrapOrElseThrow())
            .isInstanceOf(UnwrapFailedException.class);
    }

    @AutoParameterizedTest
    void constructor_with_bi_function_factory_and_candidate_types_correctly_creates_generator(
        String value,
        ObjectGenerationContext context
    ) {
        ObjectQuery query = ObjectQuery.fromType(String.class);

        TypeMatchingGenerator actual = new TypeMatchingGenerator(
            (x, y) -> x == query && y == context ? value : null,
            String.class);

        assertThat(actual).isNotNull();
        assertThat(actual.generate(query, context).unwrapOrElseThrow())
            .isEqualTo(value);
        assertThatThrownBy(() -> actual.generate(int.class, context).unwrapOrElseThrow())
            .isInstanceOf(UnwrapFailedException.class);
    }
}
