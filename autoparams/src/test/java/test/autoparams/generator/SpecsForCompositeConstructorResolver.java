package test.autoparams.generator;

import autoparams.generator.CompositeConstructorResolver;
import autoparams.generator.ConstructorResolver;
import java.lang.reflect.Constructor;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import test.autoparams.AutoParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForCompositeConstructorResolver {

    @Test
    void sut_implements_ConstructorResolver() {
        assertThat(ConstructorResolver.class)
            .isAssignableFrom(CompositeConstructorResolver.class);
    }

    @AutoParameterizedTest
    void sut_uses_internal_resolver(Class<?> type) {
        // Arrange
        Constructor<?> constructor = type.getConstructors()[0];
        Optional<Constructor<?>> container = Optional.of(constructor);
        CompositeConstructorResolver sut = new CompositeConstructorResolver(t -> container);

        // Act
        Optional<Constructor<?>> actual = sut.resolve(type);

        // Assert
        assertThat(actual).isSameAs(container);
    }

    @AutoParameterizedTest
    void sut_uses_next_internal_resolver_when_first_fails(Class<?> type) {
        // Arrange
        Constructor<?> constructor = type.getConstructors()[0];
        Optional<Constructor<?>> container = Optional.of(constructor);
        CompositeConstructorResolver sut = new CompositeConstructorResolver(
            t -> Optional.empty(),
            t -> container);

        // Act
        Optional<Constructor<?>> actual = sut.resolve(type);

        // Assert
        assertThat(actual).isSameAs(container);
    }
}
