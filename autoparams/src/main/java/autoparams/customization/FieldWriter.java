package autoparams.customization;

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
        return (query, context) -> generator
            .generate(query, context)
            .process(value -> {
                writer.process(query, value, context);
                return value;
            });
    }

    public FieldWriter including(String... fieldNames) {
        return new FieldWriter(writer.including(fieldNames));
    }

    public FieldWriter excluding(String... fieldNames) {
        return new FieldWriter(writer.excluding(fieldNames));
    }
}
