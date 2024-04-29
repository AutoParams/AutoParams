package test.autoparams.generator;

import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import autoparams.AutoSource;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.TypeQuery;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectGeneratorBase;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForObjectGeneratorBase {

    public static class DelegatingStringGenerator
        extends ObjectGeneratorBase<String> {

        private final BiFunction<ObjectQuery, ResolutionContext, String> func;

        public DelegatingStringGenerator(
            BiFunction<ObjectQuery, ResolutionContext, String> func
        ) {
            this.func = func;
        }

        @Override
        protected String generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return func.apply(query, context);
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_calls_implementation_method_if_types_match(
        AtomicInteger counter,
        ResolutionContext context,
        String value
    ) {
        ObjectQuery query = new TypeQuery(String.class);
        ObjectGenerator sut = new DelegatingStringGenerator(
            (q, c) -> {
                counter.incrementAndGet();
                return value;
            }
        );

        sut.generate(query, context);

        assertThat(counter.get()).isEqualTo(1);
    }

    @ParameterizedTest
    @AutoSource
    void sut_does_not_call_implementation_method_if_types_do_not_match(
        AtomicInteger counter,
        ResolutionContext context,
        String value
    ) {
        ObjectQuery query = new TypeQuery(Integer.class);
        ObjectGenerator sut = new DelegatingStringGenerator(
            (q, c) -> {
                counter.incrementAndGet();
                return value;
            }
        );

        sut.generate(query, context);

        assertThat(counter.get()).isEqualTo(0);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_returns_value_if_types_match(
        ResolutionContext context,
        String value
    ) {
        ObjectQuery query = new TypeQuery(String.class);
        ObjectGenerator sut = new DelegatingStringGenerator((q, c) -> value);

        ObjectContainer actual = sut.generate(query, context);

        assert actual != null;
        assertThat(actual.unwrapOrElseThrow()).isEqualTo(value);
    }

    @ParameterizedTest
    @AutoSource
    void sut_returns_empty_if_types_do_not_match(
        ResolutionContext context,
        String value
    ) {
        ObjectQuery query = new TypeQuery(Integer.class);
        ObjectGenerator sut = new DelegatingStringGenerator((q, c) -> value);

        ObjectContainer actual = sut.generate(query, context);

        assert actual != null;
        assertThatThrownBy(actual::unwrapOrElseThrow)
            .isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_passes_arguments(
        ResolutionContext context,
        String value
    ) {
        ObjectQuery query = new TypeQuery(String.class);
        new DelegatingStringGenerator(
            (q, c) -> {
                assertThat(q).isEqualTo(query);
                assertThat(c).isEqualTo(context);
                return value;
            }
        ).generate(query, context);
    }

    public static class LocalDateStubbingProxy
        extends ObjectGeneratorBase<LocalDate> {

        private final LocalDate value;

        public LocalDateStubbingProxy(LocalDate value) {
            this.value = value;
        }

        @Override
        protected LocalDate generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return value;
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_supports_implemented_interfaces(
        ResolutionContext context,
        LocalDate value
    ) {
        ObjectQuery query = new TypeQuery(Temporal.class);
        ObjectGenerator sut = new LocalDateStubbingProxy(value);

        ObjectContainer actual = sut.generate(query, context);

        assert actual != null;
        assertThat(actual.unwrapOrElseThrow()).isSameAs(value);
    }
}
