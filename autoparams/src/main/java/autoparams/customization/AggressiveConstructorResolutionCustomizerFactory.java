package autoparams.customization;

import org.junit.jupiter.params.support.AnnotationConsumer;

/**
 * A factory that creates {@link Customizer} instances for applying aggressive
 * constructor resolution.
 * <p>
 * This factory is used in conjunction with the
 * {@link ResolveConstructorAggressively} annotation. It consumes the annotation
 * to determine which types should have their constructors resolved aggressively
 * (typically meaning the constructor with the most parameters is chosen). It
 * then creates an {@link AggressiveConstructorResolutionCustomizer} configured
 * with these types.
 * </p>
 *
 * @see ResolveConstructorAggressively
 */
public class AggressiveConstructorResolutionCustomizerFactory implements
    AnnotationConsumer<ResolveConstructorAggressively>,
    CustomizerFactory {

    private Class<?>[] types;

    /**
     * Creates an instance of
     * {@link AggressiveConstructorResolutionCustomizerFactory}.
     */
    public AggressiveConstructorResolutionCustomizerFactory() {
    }

    /**
     * Creates a {@link Customizer} that applies aggressive constructor
     * resolution for the types specified in the
     * {@link ResolveConstructorAggressively} annotation.
     * <p>
     * This method is called by the AutoParams framework after the
     * {@link #accept(ResolveConstructorAggressively)} method has been invoked
     * with the annotation instance.
     * </p>
     *
     * @return a {@link Customizer} that enables aggressive constructor
     *         resolution for the specified types.
     */
    @Override
    public Customizer createCustomizer() {
        return new AggressiveConstructorResolutionCustomizer(types);
    }

    /**
     * Consumes the {@link ResolveConstructorAggressively} annotation to
     * configure the types for which aggressive constructor resolution will be
     * applied.
     * <p>
     * This method is called by the AutoParams framework before
     * {@link #createCustomizer()} is invoked.
     * </p>
     *
     * @param annotation the {@link ResolveConstructorAggressively} annotation
     *                   instance from which to extract the target types.
     * @see ResolveConstructorAggressively
     */
    @Override
    public void accept(ResolveConstructorAggressively annotation) {
        this.types = annotation.value();
    }
}
