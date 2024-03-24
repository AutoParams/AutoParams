package autoparams.customization;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.InstancePropertyWriter;
import autoparams.processor.ObjectProcessor;

public class InstancePropertyCustomizer implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return generator;
    }

    @Override
    public ObjectProcessor customize(ObjectProcessor processor) {
        // TODO: Refactor this to use composite pattern
        return (query, value, context) -> {
            processor.process(query, value, context);
            new InstancePropertyWriter().process(query, value, context);
        };
    }
}
