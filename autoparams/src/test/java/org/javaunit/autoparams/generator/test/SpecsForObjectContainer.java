package org.javaunit.autoparams.generator.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.UnwrapFailedException;
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

}
