package autoparams.customization;

import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;
import autoparams.processor.InstanceFieldWriter;

@Deprecated
public final class FieldWriter implements Customizer {

    private final InstanceFieldWriter writer;

    private FieldWriter(InstanceFieldWriter writer) {
        this.writer = writer;
    }

    public FieldWriter(Class<?> targetType) {
        this(new InstanceFieldWriter(targetType));
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            ObjectContainer container = generator.generate(query, context);
            writer.process(query, container.unwrapOrElseThrow(), context);
            return container;
        };
    }

    public FieldWriter including(String... fieldNames) {
        return new FieldWriter(writer.including(fieldNames));
    }

    public FieldWriter excluding(String... fieldNames) {
        return new FieldWriter(writer.excluding(fieldNames));
    }
}
