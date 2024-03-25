package autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.generator.ObjectQuery;

public class CompositeObjectProcessor implements ObjectProcessor {

    private final ObjectProcessor[] processors;

    public CompositeObjectProcessor(ObjectProcessor... processors) {
        this.processors = processors;
    }

    @Override
    public void process(
        ObjectQuery query,
        Object value,
        ResolutionContext context
    ) {
        for (ObjectProcessor processor : processors) {
            processor.process(query, value, context);
        }
    }
}
