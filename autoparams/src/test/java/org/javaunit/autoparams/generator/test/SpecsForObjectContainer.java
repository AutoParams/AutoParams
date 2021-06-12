package org.javaunit.autoparams.generator.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Function;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.UnwrapFailedException;
import org.javaunit.autoparams.mockito.MockitoCustomizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

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
        Throwable actual = catchThrowable(() -> sut.unwrapOrElseThrow());
        assertThat(actual).isInstanceOf(UnwrapFailedException.class);
    }

    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void process_correctly_invokes_processor(String value, Function<Object, Object> processor) {
        ObjectContainer sut = new ObjectContainer(value);
        sut.process(processor);
        verify(processor, times(1)).apply(value);
    }

    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void process_does_not_invoke_processor_if_sut_is_empty(Function<Object, Object> processor) {
        ObjectContainer sut = ObjectContainer.EMPTY;
        sut.process(processor);
        verify(processor, never()).apply(null);
    }

    @ParameterizedTest
    @AutoSource
    @Customization(MockitoCustomizer.class)
    void process_returns_self_if_sut_is_empty(Function<Object, Object> processor) {
        ObjectContainer sut = ObjectContainer.EMPTY;
        ObjectContainer actual = sut.process(processor);
        assertSame(sut, actual);
    }

    @Test
    void process_returns_container_with_correct_value() {
        ObjectContainer sut = new ObjectContainer("hello");
        Object actual = sut.process(x -> x.toString() + " world").unwrapOrElseThrow();
        assertEquals("hello world", actual);
    }

}
