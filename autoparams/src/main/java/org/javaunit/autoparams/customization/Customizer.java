package org.javaunit.autoparams.customization;

import org.javaunit.autoparams.generator.ObjectGenerator;

@FunctionalInterface
public interface Customizer {

    ObjectGenerator customize(ObjectGenerator generator);

}
