package autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.customization.DelegatingCustomizer;
import autoparams.generator.ObjectQuery;

@FunctionalInterface
public interface ObjectProcessor extends Customizer {

    void process(ObjectQuery query, Object value, ResolutionContext context);

    ObjectProcessor DEFAULT = (query, value, context) -> { };

    @Override
    default ObjectProcessor customize(ObjectProcessor processor) {
        return new CompositeObjectProcessor(processor, this);
    }

    default Customizer toCustomizer() {
        return new DelegatingCustomizer(
            generator -> generator,
            processor -> new CompositeObjectProcessor(processor, this)
        );
    }
}
