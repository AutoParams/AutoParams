package test.autoparams.processor;

import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.processor.InstancePropertyWriter;
import autoparams.processor.ObjectProcessor;
import test.autoparams.AutoParameterizedTest;
import test.autoparams.HasSetter;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForObjectProcessor {

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
