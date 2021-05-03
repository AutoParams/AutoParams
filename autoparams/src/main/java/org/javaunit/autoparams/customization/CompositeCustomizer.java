package org.javaunit.autoparams.customization;

import org.javaunit.autoparams.generator.ObjectGenerator;

public class CompositeCustomizer implements Customizer {

    private Customizer[] customizers;

    public CompositeCustomizer(Customizer... customizers) {
        this.customizers = customizers;
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        for (Customizer c : customizers) {
            return c.customize(generator);
        }
        return ObjectGenerator.DEFAULT;
    }

}
