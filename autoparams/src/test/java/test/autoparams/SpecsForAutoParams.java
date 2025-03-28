package test.autoparams;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

import autoparams.AutoParams;
import autoparams.customization.Customization;
import autoparams.customization.Customizer;
import autoparams.customization.CustomizerFactory;
import autoparams.customization.CustomizerSource;
import autoparams.customization.Freeze;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpecsForAutoParams {

    @Test
    @AutoParams
    void sut_resolves_arguments(String args1, UUID args2) {
    }

    public static class IntCustomizer implements Customizer {

        @Override
        public ObjectGenerator customize(ObjectGenerator generator) {
            return (query, context) -> query.getType() == int.class
                ? new ObjectContainer(1024)
                : generator.generate(query, context);
        }
    }

    @Test
    @AutoParams
    @Customization(IntCustomizer.class)
    void sut_consumes_Customization(int arg1, String arg2) {
        assertEquals(1024, arg1);
        assertNotNull(arg2);
    }

    @Test
    @AutoParams
    void sut_consumes_Customization_on_parameters(
        @Customization(IntCustomizer.class) int arg1,
        String arg2
    ) {
        assertEquals(1024, arg1);
        assertNotNull(arg2);
    }

    public static class IntCustomizerFactory implements CustomizerFactory {

        @Override
        public Customizer createCustomizer() {
            return new IntCustomizer();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @CustomizerSource(IntCustomizerFactory.class)
    public @interface UseIntCustomizer {
    }

    @Test
    @AutoParams
    @UseIntCustomizer
    void sut_consumes_CustomizerSource(int arg1, String arg2) {
        assertEquals(1024, arg1);
        assertNotNull(arg2);
    }

    @Test
    @AutoParams
    void sut_consumes_ArgumentRecycler(@Freeze String arg1, String arg2) {
        assertEquals(arg1, arg2);
    }
}
