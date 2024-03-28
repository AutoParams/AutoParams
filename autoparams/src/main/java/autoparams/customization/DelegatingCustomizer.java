package autoparams.customization;

import java.util.function.Function;

import autoparams.generator.ObjectGenerator;
import autoparams.processor.ObjectProcessor;

public class DelegatingCustomizer implements Customizer {

    private final Function<ObjectGenerator, ObjectGenerator> customizeGenerator;
    private final Function<ObjectProcessor, ObjectProcessor> customizeProcessor;

    public DelegatingCustomizer(
        Function<ObjectGenerator, ObjectGenerator> customizeGenerator,
        Function<ObjectProcessor, ObjectProcessor> customizeProcessor
    ) {
        this.customizeGenerator = customizeGenerator;
        this.customizeProcessor = customizeProcessor;
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return customizeGenerator.apply(generator);
    }

    @Override
    public ObjectProcessor customize(ObjectProcessor processor) {
        return customizeProcessor.apply(processor);
    }
}
