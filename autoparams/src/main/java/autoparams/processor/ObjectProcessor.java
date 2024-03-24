package autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.generator.ObjectQuery;

@FunctionalInterface
public interface ObjectProcessor {

    void process(ObjectQuery query, Object value, ResolutionContext context);
}
