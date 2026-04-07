package test.autoparams.generator;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import autoparams.AutoSource;
import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectGeneratorBase;
import autoparams.type.TypeReference;
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
        ObjectQuery query = new DefaultObjectQuery(String.class);
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
        ObjectQuery query = new DefaultObjectQuery(Integer.class);
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
        ObjectQuery query = new DefaultObjectQuery(String.class);
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
        ObjectQuery query = new DefaultObjectQuery(Integer.class);
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
        ObjectQuery query = new DefaultObjectQuery(String.class);
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
        ObjectQuery query = new DefaultObjectQuery(Temporal.class);
        ObjectGenerator sut = new LocalDateStubbingProxy(value);

        ObjectContainer actual = sut.generate(query, context);

        assert actual != null;
        assertThat(actual.unwrapOrElseThrow()).isSameAs(value);
    }

    public static class StringListGenerator
        extends ObjectGeneratorBase<List<String>> {

        private final List<String> value;

        public StringListGenerator(List<String> value) {
            this.value = value;
        }

        @Override
        protected List<String> generateObject(
            ObjectQuery query,
            ResolutionContext context
        ) {
            return value;
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_generates_object_for_parameterized_type_query(
        ResolutionContext context,
        List<String> values
    ) {
        Type type = new TypeReference<List<String>>() { }.getType();
        ObjectQuery query = new DefaultObjectQuery(type);
        ObjectGenerator sut = new StringListGenerator(values);

        ObjectContainer actual = sut.generate(query, context);

        assertThat(actual.unwrapOrElseThrow()).isSameAs(values);
    }

    @ParameterizedTest
    @AutoSource
    void sut_returns_empty_for_non_matching_parameterized_type_query(
        ResolutionContext context,
        List<String> values
    ) {
        Type type = new TypeReference<List<Integer>>() { }.getType();
        ObjectQuery query = new DefaultObjectQuery(type);
        ObjectGenerator sut = new StringListGenerator(values);

        ObjectContainer actual = sut.generate(query, context);

        assertThat(actual).isSameAs(ObjectContainer.EMPTY);
    }
}
