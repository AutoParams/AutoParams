package test.autoparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

import autoparams.AutoSource;
import autoparams.ValueAutoSource;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpecsForAutoSource {

    public static class DomainCustomizer implements Customizer {

        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return (query, context) -> query.getType() == String.class
                ? new ObjectContainer("Customized string value")
                : generator.generate(query, context);
        }
    }

    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    @AutoSource
    @Customization(DomainCustomizer.class)
    public @interface AutoDomainSource {
    }

    @ParameterizedTest
    @AutoDomainSource
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

    @SuppressWarnings("unused")
    @ParameterizedTest
    @AutoSource
    void sut_prevents_stack_overflow_for_recursive_structure(RecursiveObject value) {
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_works_with_TestInfo_tail_parameter(
        String x,
        TestInfo testInfo
    ) {
        assertThat(x).isNotNull();
        assertThat(testInfo).isNotNull();
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_works_with_TestReporter_tail_parameter(
        String x,
        TestReporter testReporter
    ) {
        assertThat(x).isNotNull();
        assertThat(testReporter).isNotNull();
    }

    public static class Prefixer implements ArgumentConverter {

        private final String prefix;

        public Prefixer(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Object convert(
            Object source,
            ParameterContext context
        ) throws ArgumentConversionException {
            if (source instanceof String) {
                return prefix + source;
            } else {
                return source;
            }
        }
    }

    public static class PrefixerCustomizer implements Customizer {

        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return (query, context) -> {
                Type type = query.getType();
                return type.equals(ArgumentConverter.class)
                    ? new ObjectContainer(new Prefixer("prefix"))
                    : generator.generate(query, context);
            };
        }
    }

    @ParameterizedTest
    @AutoSource
    @Customization(PrefixerCustomizer.class)
    void sut_applies_ArgumentConverter_to_arguments(String x, String y) {
        assertThat(x).startsWith("prefix");
        assertThat(y).startsWith("prefix");
    }

    @ParameterizedTest
    @ValueAutoSource(strings = { "a", "b", "c" })
    @Customization(PrefixerCustomizer.class)
    void sut_applies_ArgumentConverter_to_seed_arguments(String x, String y) {
        assertThat(x).startsWith("prefix");
    }
}
