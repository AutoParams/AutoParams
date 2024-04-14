package autoparams.generator;

import java.lang.reflect.Type;

import autoparams.ResolutionContext;
import autoparams.customization.Customizer;
import autoparams.customization.DelegatingCustomizer;

@FunctionalInterface
public interface ObjectGenerator extends Customizer {

    ObjectContainer generate(ObjectQuery query, ResolutionContext context);

    ObjectGenerator DEFAULT = new DefaultObjectGenerator();

    default ObjectContainer generate(Type type, ResolutionContext context) {
        return generate(new TypeQuery(type), context);
    }

    @Override
    default ObjectGenerator customize(ObjectGenerator generator) {
        return new CompositeObjectGenerator(this, generator);
    }

    @Deprecated
    default Customizer toCustomizer() {
        return new DelegatingCustomizer(
            generator -> new CompositeObjectGenerator(this, generator),
            processor -> processor
        );
    }
}
