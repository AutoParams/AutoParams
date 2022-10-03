package autoparams;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

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
