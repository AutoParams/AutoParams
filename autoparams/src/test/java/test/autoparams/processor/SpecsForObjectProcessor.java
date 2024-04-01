package test.autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.processor.InstancePropertyWriter;
import test.autoparams.AutoParameterizedTest;
import test.autoparams.HasSetter;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForObjectProcessor {

    @AutoParameterizedTest
    void toCustomizer_returns_customizer(InstancePropertyWriter sut) {
        Customizer actual = sut.toCustomizer();
        assertThat(actual).isNotNull();
    }

    @AutoParameterizedTest
    void toCustomizer_returns_customizer_that_works_correctly(
        InstancePropertyWriter sut,
        ResolutionContext context
    ) {
        Customizer actual = sut.toCustomizer();

        context.applyCustomizer(actual);
        HasSetter value = context.resolve(HasSetter.class);
        assertThat(value.getValue()).isNotNull();
    }
}
