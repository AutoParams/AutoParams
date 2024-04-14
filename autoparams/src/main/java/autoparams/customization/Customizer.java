package autoparams.customization;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;

public interface Customizer {

    default ObjectGenerator customize(ObjectGenerator generator) {
        if (generator == null) {
            throw new IllegalArgumentException("The argument 'generator' is null.");
        }

        return generator;
    }

    default ObjectProcessor customize(ObjectProcessor processor) {
        if (processor == null) {
            throw new IllegalArgumentException("The argument 'processor' is null.");
        }

        return processor;
    }
}
