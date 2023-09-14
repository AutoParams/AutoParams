package autoparams.generator.test;

import autoparams.AutoParameterizedTest;
import autoparams.generator.ObjectGenerationContext;
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
}
