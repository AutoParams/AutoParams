package autoparams.customization;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;

@FunctionalInterface
public interface Customizer {

    ObjectGenerator customize(ObjectGenerator generator);

    default ObjectProcessor customize(ObjectProcessor processor) {
        return processor;
    }
}
