package org.javaunit.autoparams.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForAutoSource {

    public static class DomainCustomizer implements Customizer {
        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return (query, context) -> query.getType() == String.class
                ? new ObjectContainer("Customized string value")
                : generator.generate(query, context);
        }
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Customization(DomainCustomizer.class)
    @AutoSource
    public @interface DomainAutoSource {
    }

    @ParameterizedTest
    @DomainAutoSource
    void extended_data_source_applies_customization(String actual) {
        assertNotNull(actual);
        assertEquals("Customized string value", actual);
    }

    @ParameterizedTest
    @AutoSource
    void sut_provides_extension_context(ExtensionContext context) {
        assertThat(context).isNotNull();
        assertThat(context.getRequiredTestMethod().getName())
            .isEqualTo("sut_provides_extension_context");
    }

    public static class RecursiveObject {
        private final RecursiveObject value;

        public RecursiveObject(RecursiveObject value) {
            this.value = value;
        }

        public RecursiveObject getValue() {
            return value;
        }
    }

    @ParameterizedTest
    @AutoSource
    void sut_prevents_stack_overflow_for_recursive_structure(RecursiveObject value) {
    }

}
