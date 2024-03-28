package test.autoparams.customization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import autoparams.AutoSource;
import autoparams.customization.DelegatingCustomizer;
import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForDelegatingCustomizer {

    public static class Spy<T> implements Function<T, T> {

        private final T query;
        private final T answer;
        private final List<T> log = new ArrayList<>();
        private final List<T> logView = Collections.unmodifiableList(log);

        public Spy(T query, T answer) {
            this.query = query;
            this.answer = answer;
        }

        public T getQuery() {
            return query;
        }

        public T getAnswer() {
            return answer;
        }

        @Override
        public T apply(T t) {
            log.add(t);
            return answer;
        }

        public List<T> getLog() {
            return logView;
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_delegates_to_generator_customizer(
        Spy<ObjectGenerator> spy,
        Spy<ObjectProcessor> dummy
    ) {
        DelegatingCustomizer sut = new DelegatingCustomizer(spy, dummy);

        ObjectGenerator actual = sut.customize(spy.getQuery());

        assertThat(spy.getLog()).containsExactly(spy.getQuery());
        assertThat(actual).isSameAs(spy.getAnswer());
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_delegates_to_processor_customizer(
        Spy<ObjectGenerator> dummy,
        Spy<ObjectProcessor> spy
    ) {
        DelegatingCustomizer sut = new DelegatingCustomizer(dummy, spy);

        ObjectProcessor actual = sut.customize(spy.getQuery());

        assertThat(spy.getLog()).containsExactly(spy.getQuery());
        assertThat(actual).isSameAs(spy.getAnswer());
    }
}
