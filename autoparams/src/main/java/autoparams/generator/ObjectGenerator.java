package autoparams.generator;

import java.lang.reflect.Type;

import autoparams.DefaultObjectQuery;
import autoparams.ObjectQuery;
import autoparams.ResolutionContext;
import autoparams.customization.Customizer;

@FunctionalInterface
public interface ObjectGenerator extends Customizer {

    ObjectContainer generate(ObjectQuery query, ResolutionContext context);

    ObjectGenerator DEFAULT = new DefaultObjectGenerator();

    default ObjectContainer generate(Type type, ResolutionContext context) {
        return generate(new DefaultObjectQuery(type), context);
    }

    @Override
    default ObjectGenerator customize(ObjectGenerator generator) {
        return new CompositeObjectGenerator(this, generator);
    }
}
