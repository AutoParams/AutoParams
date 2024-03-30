package autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.customization.DelegatingCustomizer;
import autoparams.generator.ObjectQuery;

@FunctionalInterface
public interface ObjectProcessor {

    void process(ObjectQuery query, Object value, ResolutionContext context);

    ObjectProcessor DEFAULT = (query, value, context) -> { };

    default Customizer toCustomizer() {
        return new DelegatingCustomizer(
            generator -> generator,
            processor -> new CompositeObjectProcessor(this, processor)
        );
    }
}
