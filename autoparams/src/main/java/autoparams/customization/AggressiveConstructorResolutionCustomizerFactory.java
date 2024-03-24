package autoparams.customization;

import autoparams.ResolutionContext;
import autoparams.generator.CompositeConstructorResolver;
import autoparams.generator.ConstructorExtractor;
import autoparams.generator.ConstructorResolver;
import autoparams.generator.ObjectContainer;
import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.util.Comparator;
import java.util.Optional;

public class AggressiveConstructorResolutionCustomizerFactory implements
    AnnotationVisitor<ResolveConstructorAggressively>,
    CustomizerFactory {

    private static final Comparator<Constructor<?>> byParameters =
        Comparator.comparingInt(Constructor::getParameterCount);

    private Class<?> target;

    @Override
    public void visit(ResolveConstructorAggressively annotation) {
        this.target = annotation.value();
    }

    @Override
    public Customizer createCustomizer() {
        return generator -> (query, context) ->
            query.getType().equals(ConstructorResolver.class)
                ? new ObjectContainer(
                    new CompositeConstructorResolver(
                        createAggressiveResolver(context),
                        (ConstructorResolver) generator
                            .generate(query, context)
                            .unwrapOrElseThrow()))
                : generator.generate(query, context);
    }

    private ConstructorResolver createAggressiveResolver(ResolutionContext context) {
        return createAggressiveResolver(context.generate(ConstructorExtractor.class));
    }

    private ConstructorResolver createAggressiveResolver(ConstructorExtractor extractor) {
        ConstructorResolver resolver = new CompositeConstructorResolver(
            t -> extractor.extract(t).stream()
                .filter(c -> c.isAnnotationPresent(ConstructorProperties.class))
                .max(byParameters),
            t -> extractor.extract(t).stream().max(byParameters)
        );
        return bindResolver(resolver);
    }

    private ConstructorResolver bindResolver(ConstructorResolver resolver) {
        return t -> t.equals(target) ? resolver.resolve(t) : Optional.empty();
    }
}
