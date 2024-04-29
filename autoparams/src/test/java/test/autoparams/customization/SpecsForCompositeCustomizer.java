package test.autoparams.customization;

import autoparams.AutoSource;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.CompositeCustomizer;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

class SpecsForCompositeCustomizer {

    public static final class EmptyGenerator implements ObjectGenerator {

        public ObjectContainer generate(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return ObjectContainer.EMPTY;
        }
    }

    public static final class RelayGeneratorCustomizer implements Customizer {

        private final ObjectGenerator prev;
        private final ObjectGenerator next;

        public RelayGeneratorCustomizer(
            ObjectGenerator prev,
            ObjectGenerator next
        ) {
            this.prev = prev;
            this.next = next;
        }

        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return generator == prev ? next : generator;
        }
    }

    public static final class EmptyProcessor implements ObjectProcessor {

        @Override
        public void process(
            ObjectQuery query,
            Object value,
            ResolutionContext context
        ) {
        }
    }

    public static final class RelayProcessorCustomizer implements Customizer {

        private final ObjectProcessor prev;
        private final ObjectProcessor next;

        public RelayProcessorCustomizer(
            ObjectProcessor prev,
            ObjectProcessor next
        ) {
            this.prev = prev;
            this.next = next;
        }

        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return generator;
        }

        @Override
        public ObjectProcessor customize(ObjectProcessor processor) {
            return processor == prev ? next : processor;
        }
    }

    @Test
    void sut_implements_Customizer() {
        assertThat(CompositeCustomizer.class.getInterfaces())
            .contains(Customizer.class);
    }

    @ParameterizedTest
    @AutoSource
    void sut_sequentially_invokes_customizers_for_generator(
        EmptyGenerator gen0,
        EmptyGenerator gen1,
        EmptyGenerator gen2,
        EmptyGenerator gen3
    ) {
        CompositeCustomizer sut = new CompositeCustomizer(
            new RelayGeneratorCustomizer(gen0, gen1),
            new RelayGeneratorCustomizer(gen1, gen2),
            new RelayGeneratorCustomizer(gen2, gen3)
        );

        ObjectGenerator actual = sut.customize(gen0);

        assertSame(gen3, actual);
    }

    @ParameterizedTest
    @AutoSource
    void sut_sequentially_invokes_customizers_for_processor(
        EmptyProcessor proc0,
        EmptyProcessor proc1,
        EmptyProcessor proc2,
        EmptyProcessor proc3
    ) {
        CompositeCustomizer sut = new CompositeCustomizer(
            new RelayProcessorCustomizer(proc0, proc1),
            new RelayProcessorCustomizer(proc1, proc2),
            new RelayProcessorCustomizer(proc2, proc3)
        );

        ObjectProcessor actual = sut.customize(proc0);

        assertSame(proc3, actual);
    }
}
