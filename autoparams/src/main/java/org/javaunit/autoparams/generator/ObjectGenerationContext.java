package org.javaunit.autoparams.generator;

import org.javaunit.autoparams.customization.Customizer;

public final class ObjectGenerationContext {

    private ObjectGenerator generator;

    public ObjectGenerationContext(ObjectGenerator generator) {
        this.generator = generator;
    }

    ObjectGenerator getGenerator() {
        return generator;
    }

    public void customizeGenerator(Customizer customizer) {
        generator = customizer.customize(generator);
    }

}
