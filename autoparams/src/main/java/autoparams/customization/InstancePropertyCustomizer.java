package autoparams.customization;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.CompositeObjectProcessor;
import autoparams.processor.InstancePropertyWriter;
import autoparams.processor.ObjectProcessor;

@Deprecated
public class InstancePropertyCustomizer implements Customizer {

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return generator;
    }

    @Override
    public ObjectProcessor customize(ObjectProcessor processor) {
        return new CompositeObjectProcessor(
            processor,
            new InstancePropertyWriter()
        );
    }
}
