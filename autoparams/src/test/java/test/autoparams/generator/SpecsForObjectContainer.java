package test.autoparams.generator;

import autoparams.AutoSource;
import autoparams.generator.ObjectContainer;
import autoparams.generator.UnwrapFailedException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class SpecsForObjectContainer {

    @ParameterizedTest
    @AutoSource
    void yieldIfEmpty_returns_self_if_sut_is_filled(Object value, ObjectContainer next) {
        ObjectContainer sut = new ObjectContainer(value);
        ObjectContainer actual = sut.yieldIfEmpty(() -> next);
        assertSame(sut, actual);
    }

    @ParameterizedTest
    @AutoSource
    void yieldIfEmpty_returns_next_if_sut_is_empty(ObjectContainer next) {
        ObjectContainer sut = ObjectContainer.EMPTY;
        ObjectContainer actual = sut.yieldIfEmpty(() -> next);
        assertSame(next, actual);
    }

    @ParameterizedTest
    @AutoSource
    void constructor_creates_filled_container_with_null_argument(ObjectContainer next) {
        ObjectContainer sut = new ObjectContainer(null);
        ObjectContainer actual = sut.yieldIfEmpty(() -> next);
        assertSame(sut, actual);
    }

    @ParameterizedTest
    @AutoSource
    void unwrapOrElseThrow_returns_value_if_sut_is_filled(Object value) {
        ObjectContainer sut = new ObjectContainer(value);
        Object actual = sut.unwrapOrElseThrow();
        assertSame(value, actual);
    }

    @Test
    void unwrapOrElseThrow_throws_UnwrapFailedException_if_sut_is_empty() {
        ObjectContainer sut = ObjectContainer.EMPTY;
        Throwable actual = catchThrowable(sut::unwrapOrElseThrow);
        assertThat(actual).isInstanceOf(UnwrapFailedException.class);
    }

    public static class ProcessorSpy implements Function<Object, Object> {

        private final List<Object> records = new ArrayList<>();

        @Override
        public Object apply(Object value) {
            records.add(value);
            return value;
        }

        public List<Object> getRecords() {
            return records;
        }
    }

    @ParameterizedTest
    @AutoSource
    void process_correctly_invokes_processor(String value, ProcessorSpy spy) {
        ObjectContainer sut = new ObjectContainer(value);
        sut.process(spy);
        assertThat(spy.getRecords()).containsExactly(value);
    }

    @ParameterizedTest
    @AutoSource
    void process_does_not_invoke_processor_if_sut_is_empty(ProcessorSpy spy) {
        ObjectContainer sut = ObjectContainer.EMPTY;
        sut.process(spy);
        assertThat(spy.getRecords()).isEmpty();
    }

    @Test
    void process_returns_self_if_sut_is_empty() {
        ObjectContainer sut = ObjectContainer.EMPTY;
        ObjectContainer actual = sut.process(x -> x);
        assertSame(sut, actual);
    }

    @Test
    void process_returns_container_with_correct_value() {
        ObjectContainer sut = new ObjectContainer("hello");
        Object actual = sut.process(x -> x.toString() + " world").unwrapOrElseThrow();
        assertEquals("hello world", actual);
    }
}
