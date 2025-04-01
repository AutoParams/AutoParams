package test.autoparams.processor;

import autoparams.AutoSource;
import autoparams.DefaultObjectQuery;
import autoparams.ResolutionContext;
import autoparams.processor.InstancePropertyWriter;
import org.junit.jupiter.params.ParameterizedTest;
import test.autoparams.HasSetter;

import static org.assertj.core.api.Assertions.assertThat;

class SpecsForInstancePropertyWriter {

    @ParameterizedTest
    @AutoSource
    void sut_sets_property(
        InstancePropertyWriter sut,
        HasSetter bag,
        ResolutionContext context
    ) {
        sut.process(new DefaultObjectQuery(HasSetter.class), bag, context);
        assertThat(bag.getValue()).isNotNull();
    }
}
