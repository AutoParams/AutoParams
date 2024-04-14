package test.autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectQuery;
import autoparams.processor.InstancePropertyWriter;
import autoparams.processor.ObjectProcessor;
import test.autoparams.AutoParameterizedTest;
import test.autoparams.HasSetter;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForObjectProcessor {

    @Deprecated
    @AutoParameterizedTest
    void toCustomizer_returns_customizer(InstancePropertyWriter sut) {
        Customizer actual = sut.toCustomizer();
        assertThat(actual).isNotNull();
    }

    @Deprecated
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

    public static class Prefixer implements ObjectProcessor {

        private final String prefix;

        public Prefixer(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public void process(
            ObjectQuery query,
            Object value,
            ResolutionContext context
        ) {
            if (query.getType().equals(HasSetter.class)) {
                HasSetter hasSetter = (HasSetter) value;
                hasSetter.setValue(prefix + hasSetter.getValue());
            }
        }
    }

    @AutoParameterizedTest
    void customize_correctly_composes_processors(ResolutionContext context) {
        context.applyCustomizer(new InstancePropertyWriter());
        context.applyCustomizer(new Prefixer("world "));
        context.applyCustomizer(new Prefixer("hello "));

        HasSetter actual = context.resolve(HasSetter.class);

        assertThat(actual.getValue()).startsWith("hello world ");
    }
}
