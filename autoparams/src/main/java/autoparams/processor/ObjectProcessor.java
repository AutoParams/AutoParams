package autoparams.processor;

import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.generator.ObjectQuery;

@FunctionalInterface
public interface ObjectProcessor extends Customizer {

    void process(ObjectQuery query, Object value, ResolutionContext context);

    ObjectProcessor DEFAULT = (query, value, context) -> { };

    @Override
    default ObjectProcessor customize(ObjectProcessor processor) {
        return new CompositeObjectProcessor(processor, this);
    }

    @Deprecated
    default Customizer toCustomizer() {
        return this;
    }
}
