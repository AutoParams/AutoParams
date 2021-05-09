package org.javaunit.autoparams.customization.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.CompositeCustomizer;
import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerationContext;
import org.javaunit.autoparams.generator.ObjectGenerator;
import org.javaunit.autoparams.generator.ObjectQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForCompositeCustomizer {

    public static final class EmptyGenerator implements ObjectGenerator {
        public ObjectContainer generate(ObjectQuery query, ObjectGenerationContext context) {
            return ObjectContainer.EMPTY;
        }
    }

    public static final class RelayCustomizer implements Customizer {
        private final ObjectGenerator prev;
        private final ObjectGenerator next;

        public RelayCustomizer(ObjectGenerator prev, ObjectGenerator next) {
            this.prev = prev;
            this.next = next;
        }

        public ObjectGenerator customize(ObjectGenerator generator) {
            return generator == prev ? next : generator;
        }
    }

    @Test
    void sut_implements_Customizer() {
        assertThat(CompositeCustomizer.class.getInterfaces()).contains(Customizer.class);
    }

    @ParameterizedTest
    @AutoSource
    void sut_sequentially_invokes_customizers(
        EmptyGenerator gen0,
        EmptyGenerator gen1,
        EmptyGenerator gen2,
        EmptyGenerator gen3
    ) {
        CompositeCustomizer sut = new CompositeCustomizer(
            new RelayCustomizer(gen0, gen1),
            new RelayCustomizer(gen1, gen2),
            new RelayCustomizer(gen2, gen3));

        ObjectGenerator actual = sut.customize(gen0);

        assertSame(gen3, actual);
    }

}
