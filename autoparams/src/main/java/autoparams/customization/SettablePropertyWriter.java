package autoparams.customization;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.processor.InstancePropertyWriter;

@Deprecated
public final class SettablePropertyWriter implements Customizer {

    private final InstancePropertyWriter writer = new InstancePropertyWriter();

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            ObjectContainer container = generator.generate(query, context);
            writer.process(query, container.unwrapOrElseThrow(), context);
            return container;
        };
    }
}
