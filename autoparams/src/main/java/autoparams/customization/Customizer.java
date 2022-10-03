package autoparams.customization;

import autoparams.generator.ObjectGenerator;

@FunctionalInterface
public interface Customizer {

    ObjectGenerator customize(ObjectGenerator generator);

}
