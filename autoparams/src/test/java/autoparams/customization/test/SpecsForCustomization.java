package autoparams.customization.test;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.params.ParameterizedTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpecsForCustomization {

    public static class IntCustomization implements Customizer {
        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return (query, context) -> query.getType() == int.class
                ? new ObjectContainer(1024)
                : generator.generate(query, context);
        }
    }

    @ParameterizedTest
    @AutoSource
    @Customization(IntCustomization.class)
    void sut_applies_customizer(int arg1, String arg2) {
        assertEquals(1024, arg1);
        assertNotNull(arg2);
    }

    public static class StringCustomization implements Customizer {
        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return (query, context) -> query.getType() == String.class
                ? new ObjectContainer("hello world")
                : generator.generate(query, context);
        }
    }

    @ParameterizedTest
    @AutoSource
    @Customization({IntCustomization.class, StringCustomization.class})
    void sut_applies_multiple_customizers(int arg1, String arg2) {
        assertEquals(1024, arg1);
        assertEquals("hello world", arg2);
    }

    @ParameterizedTest
    @AutoSource
    void sut_correctly_works_for_parameters(
        String arg1,
        @Customization(StringCustomization.class) String arg2
    ) {
        assertNotEquals("hello world", arg1);
        assertEquals("hello world", arg2);
    }

}
