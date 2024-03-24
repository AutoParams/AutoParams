package autoparams;

import autoparams.customization.Customizer;
import autoparams.generator.ObjectGenerationContext;
import autoparams.generator.ObjectGenerator;
import autoparams.generator.ObjectQuery;
import autoparams.processor.ObjectProcessor;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class ResolutionContext extends ObjectGenerationContext {

    private ObjectProcessor processor;

    public ResolutionContext(
        ExtensionContext extensionContext,
        ObjectGenerator generator,
        ObjectProcessor processor
    ) {
        super(extensionContext, generator);
        this.processor = processor;
    }

    @Override
    public Object generate(ObjectQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("The argument 'query' is null.");
        }

        Object value = ObjectProcessor.class.equals(query.getType())
            ? ObjectProcessor.DEFAULT
            : super.generate(query);
        processor.process(query, value, this);
        return value;
    }

    // TODO: Rename to reflect the purpose of the method
    @Override
    public void customizeGenerator(Customizer customizer) {
        super.customizeGenerator(customizer);
        processor = customizer.customize(processor);
    }
}
