package test.autoparams.generator;

import autoparams.generator.ConstructorExtractor;
import autoparams.generator.ConstructorResolver;
import autoparams.generator.DefensiveConstructorResolver;
import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import test.autoparams.DecoratedWithConstructorProperties;
import test.autoparams.HasMultipleConstructors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

public class SpecsForDefensiveConstructorResolver {

    @Test
    void sut_implements_ConstructorResolver() {
        assertThat(ConstructorResolver.class)
            .isAssignableFrom(DefensiveConstructorResolver.class);
    }

    @Test
    void sut_returns_constructor_with_fewest_parameters() {
        ConstructorExtractor extractor = t -> asList(t.getConstructors());
        DefensiveConstructorResolver sut = new DefensiveConstructorResolver(extractor);

        Optional<Constructor<?>> actual = sut.resolve(HasMultipleConstructors.class);

        assertThat(actual).isNotEmpty();
        actual.ifPresent(c -> assertThat(c.getParameterCount()).isEqualTo(0));
    }

    @Test
    void sut_prefers_constructor_annotated_with_ConstructorProperties() {
        ConstructorExtractor extractor = t -> asList(t.getConstructors());
        DefensiveConstructorResolver sut = new DefensiveConstructorResolver(extractor);

        Optional<Constructor<?>> actual = sut.resolve(DecoratedWithConstructorProperties.class);

        assertThat(actual).isNotEmpty();
        actual.ifPresent(c -> assertThat(c.getParameterCount()).isEqualTo(1));
    }

    @Test
    void sut_relies_on_extractor() {
        // Arrange
        ConstructorExtractor extractor = t -> stream(t.getConstructors())
            .filter(c -> c.getParameterCount() == 1)
            .collect(Collectors.toList());

        DefensiveConstructorResolver sut = new DefensiveConstructorResolver(extractor);

        // Act
        Optional<Constructor<?>> actual = sut.resolve(HasMultipleConstructors.class);

        // Assert
        assertThat(actual).isNotEmpty();
        actual.ifPresent(c -> assertThat(c.getParameterCount()).isEqualTo(1));
    }
}
