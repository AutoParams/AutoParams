package autoparams.customization;

import autoparams.generator.AggressiveConstructorResolver;
import autoparams.generator.CompositeConstructorResolver;
import autoparams.generator.ConstructorExtractor;
import autoparams.generator.ConstructorResolver;
import autoparams.generator.ObjectContainer;
import autoparams.generator.ObjectGenerationContext;
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
        return generator -> (query, context) -> {
            if (query.getType().equals(ConstructorResolver.class)) {
                ConstructorResolver resolver = generateResolver(context);
                return new ObjectContainer(
                    new CompositeConstructorResolver(
                        type -> Objects.equals(type, target)
                            ? resolver.resolve(type)
                            : Optional.empty(),
                        (ConstructorResolver) generator
                            .generate(query, context)
                            .unwrapOrElseThrow()));
            } else {
                return generator.generate(query, context);
            }
        };
    }

    private static ConstructorResolver generateResolver(ObjectGenerationContext context) {
        return new AggressiveConstructorResolver(context.generate(ConstructorExtractor.class));
    }
}
