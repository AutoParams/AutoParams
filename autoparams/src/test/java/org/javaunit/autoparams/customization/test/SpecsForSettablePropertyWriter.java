package org.javaunit.autoparams.customization.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.customization.SettablePropertyWriter;
import org.junit.jupiter.params.ParameterizedTest;

class SpecsForSettablePropertyWriter {

    @ParameterizedTest
    @AutoSource
    @Customization(SettablePropertyWriter.class)
    public void sut_sets_property(HasSetter value) {
        assertNotNull(value.getProp());
    }

}
