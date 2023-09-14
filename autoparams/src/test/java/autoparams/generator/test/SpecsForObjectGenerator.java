package autoparams.generator.test;

import autoparams.AutoParameterizedTest;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForObjectGenerator {

    @AutoParameterizedTest
    void generate_with_type_correctly_generates_value_of_the_type(
        ObjectGenerationContext context
    ) {
        ObjectGenerator sut = ObjectGenerator.DEFAULT;

        ObjectContainer actual = sut.generate(String.class, context);

        assertThat(actual).isNotNull();
        assertThat(actual.unwrapOrElseThrow()).isInstanceOf(String.class);
    }
}
