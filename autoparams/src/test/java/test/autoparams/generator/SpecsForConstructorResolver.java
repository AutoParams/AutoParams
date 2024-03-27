package test.autoparams.generator;

import java.lang.reflect.Constructor;
import java.util.Optional;

import autoparams.generator.ConstructorResolver;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpecsForConstructorResolver {

    @Test
    void resolveOrElseThrow_returns_constructor_if_it_is_present() {
        // Arrange
        Class<String> type = String.class;
        Constructor<?> constructor = type.getConstructors()[0];
        ConstructorResolver sut = t -> t.equals(type)
            ? Optional.of(constructor)
            : Optional.empty();

        // Act
        Constructor<?> actual = sut.resolveOrElseThrow(type);

        // Assert
        assertThat(actual).isSameAs(constructor);
    }

    @Test
    void resolveOrElseThrow_throws_exception_if_constructor_is_not_present() {
        // Arrange
        ConstructorResolver sut = t -> Optional.empty();

        // Act & Assert
        assertThatThrownBy(() -> sut.resolveOrElseThrow(String.class))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("No constructor found for " + String.class);
    }
}
