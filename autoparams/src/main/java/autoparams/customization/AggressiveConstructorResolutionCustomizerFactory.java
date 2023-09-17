package autoparams.customization;

import autoparams.generator.CompositeConstructorResolver;
import autoparams.generator.ConstructorResolver;
import autoparams.generator.ObjectContainer;
import java.util.Objects;
import java.util.Optional;

public class AggressiveConstructorResolutionCustomizerFactory implements
    AnnotationVisitor<ResolveConstructorAggressively>,
    CustomizerFactory {

    private Class<?> target;

    @Override
    public void visit(ResolveConstructorAggressively annotation) {
        this.target = annotation.value();
    }

    @Override
    public Customizer createCustomizer() {
        ConstructorResolver boundStrategy = bindType(target);
        return buildCustomizer(boundStrategy);
    }

    private static ConstructorResolver bindType(Class<?> target) {
        return t -> Objects.equals(t, target)
            ? ConstructorResolver.AGGRESSIVE_STRATEGY.resolve(t)
            : Optional.empty();
    }

    private Customizer buildCustomizer(ConstructorResolver boundStrategy) {
        return generator -> (query, context) ->
            query.getType().equals(ConstructorResolver.class)
                ? new ObjectContainer(
                    new CompositeConstructorResolver(
                        boundStrategy,
                        (ConstructorResolver) generator
                            .generate(query, context)
                            .unwrapOrElseThrow()))
                : generator.generate(query, context);
    }
}
