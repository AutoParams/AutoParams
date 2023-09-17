package test.autoparams;

import autoparams.AutoSource;
import autoparams.generator.ObjectGenerationContext;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpecsForComplexObjects {

    @ParameterizedTest
    @AutoSource
    void sut_creates_complex_object(ComplexObject actual) {
        assertNotNull(actual);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_complex_objects(ComplexObject value1, ComplexObject value2) {
        assertThat(value1).isNotEqualTo(value2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_object_having_complex_object(MoreComplexObject actual) {
        assertNotNull(actual);
    }

    @ParameterizedTest
    @AutoSource
    void sut_creates_arbitrary_objects_having_complex_object(
        MoreComplexObject value1,
        MoreComplexObject value2
    ) {
        assertThat(value1).isNotEqualTo(value2);
    }

    public static final class OnlyPrivateConstructor {
        private OnlyPrivateConstructor() {
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_fails_for_type_with_no_public_constructor(ObjectGenerationContext context) {
        assertThat(
            assertThrows(
                RuntimeException.class,
                () -> context.generate(OnlyPrivateConstructor.class)
            )
        ).hasMessageContaining(OnlyPrivateConstructor.class.getSimpleName());
    }
}
