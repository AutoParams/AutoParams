package org.javaunit.autoparams;

import org.javaunit.autoparams.customization.Customizer;
import org.javaunit.autoparams.generator.ObjectContainer;
import org.javaunit.autoparams.generator.ObjectGenerator;

final class FixCustomization implements Customizer {

    private final Class<?> type;
    private final Object value;

    public FixCustomization(Class<?> type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> query.getType() == type
            ? new ObjectContainer(value)
            : generator.generate(query, context);
    }

}
