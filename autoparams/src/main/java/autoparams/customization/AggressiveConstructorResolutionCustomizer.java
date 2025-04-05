package autoparams.customization;

import java.util.List;
import java.util.Optional;

import autoparams.ResolutionContext;
import autoparams.generator.CompositeConstructorResolver;
import autoparams.generator.ConstructorExtractor;
import autoparams.generator.ConstructorResolver;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerator;

import static java.util.Arrays.asList;

final class AggressiveConstructorResolutionCustomizer implements Customizer {

    private final List<Class<?>> types;

    public AggressiveConstructorResolutionCustomizer(Class<?>[] types) {
        this.types = asList(types);
    }

    @Override
    public ObjectGenerator customize(ObjectGenerator generator) {
        return (query, context) -> {
            if (query.getType().equals(ConstructorResolver.class)) {
                ConstructorResolver head = getResolver(context);
                ConstructorResolver next = (ConstructorResolver) generator
                    .generate(query, context)
                    .unwrapOrElseThrow();
                return new ObjectContainer(
                    new CompositeConstructorResolver(head, next)
                );
            }

            return generator.generate(query, context);
        };
    }

    private ConstructorResolver getResolver(ResolutionContext context) {
        return getResolver(context.resolve(ConstructorExtractor.class));
    }

    private ConstructorResolver getResolver(ConstructorExtractor extractor) {
        ConstructorResolver resolver = new CompositeConstructorResolver(
            new AnnotatedRichConstructorResolver(extractor),
            new RichConstructorResolver(extractor)
        );
        return bindResolver(resolver);
    }

    private ConstructorResolver bindResolver(ConstructorResolver resolver) {
        return type -> types.contains(type)
            ? resolver.resolve(type)
            : Optional.empty();
    }
}
